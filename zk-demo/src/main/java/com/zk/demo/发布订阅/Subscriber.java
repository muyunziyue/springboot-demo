package com.zk.demo.发布订阅;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Author： 马中华 奈学教育 https://blog.csdn.net/zhongqi2513
 * DateTime： 2017/4/25 15:29
 * Description： 订阅者
 *
 * 设计思路： Subscriber 定于某个频道，然后如果有发布者在该频道下发布消息，则订阅者必然会收到
 **/
public class Subscriber {

    // zookeeper服务器地址
    private static final String CONNECT_INFO = "bigdata02:2181,bigdata03:2181,bigdata04:2181";
    private static final int TIME_OUT = 4000;

    // 备用的父子节点
    private static final String PARENT_NODE = "/publish_parent";

    // 会话对象
    private static ZooKeeper zookeeper = null;

    // latch就相当于一个对象锁，当latch.await()方法执行时，方法所在的线程会等待
    // 当latch的count减为0时，将会唤醒等待的线程
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static List<String> oldNews = null;

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        // 请开始你的表演！！！

        /**
         * 第一步：获取会话
         */
        zookeeper = new ZooKeeper(CONNECT_INFO, TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 确保链接建立
                if (countDownLatch.getCount() > 0 && event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("创建会话链接成功");
                    try {
                        // 获取旧的服务列表
                        oldNews = zookeeper.getChildren(PARENT_NODE, false);
                        System.out.println("oldNews.size() = " + oldNews.size());
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                }

                // 处理监听
                String listen_path = event.getPath();
                Event.EventType eventType = event.getType();

                // 如果是 TEST_NODE 发生 NodeChildrenChanged
                if (listen_path.equals(PARENT_NODE) && eventType == Event.EventType.NodeChildrenChanged) {
                    System.out.println(PARENT_NODE + " 发生了 " + eventType + " 事件");

                    // 逻辑处理
                    try {
                        // 最新的所有已发布的消息。
                        List<String> newNews = zookeeper.getChildren(PARENT_NODE, false);
                        System.out.println("newNews.size() = " + newNews.size());
                        // 找出最新发布的那条消息
                        // 如果发布者删除了某条消息，对用户来说，意义不大，但是发布了一条消息。那么需要通知所有订阅者用户
                        for (String node : newNews) {
                            if (!oldNews.contains(node)) {
                                byte[] data = zookeeper.getData(PARENT_NODE + "/" + node, false, null);
                                System.out.println("发布了新服务：" + new String(data));
                            }
                        }
                        oldNews = newNews;
                        zookeeper.getChildren(PARENT_NODE, true);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        /**
         * 第二步：待zookeeper会话建立成功，主线程恢复执行
         */
        countDownLatch.await();

        /**
         * 第三步：确保父服务节点存在
         */
        ArrayList<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        CreateMode mode = CreateMode.PERSISTENT;
        // 判断父节点是否存在
        Stat exists_parent = zookeeper.exists(PARENT_NODE, false);
        if (exists_parent == null) {
            zookeeper.create(PARENT_NODE, PARENT_NODE.getBytes(), acls, mode);
        }

        /**
         * 第三步：注册监听
         */
        zookeeper.getChildren(PARENT_NODE, true);

        Thread.sleep(Integer.MAX_VALUE);
        /**
         * 第四步：关闭连接
         */
        zookeeper.close();
    }
}
