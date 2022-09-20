package com.zk.demo.发布订阅;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Author： 马中华 奈学教育 https://blog.csdn.net/zhongqi2513
 * DateTime： 2017/4/25 15:29
 * Description：发布者程序
 *
 * 实现思路： Publisher程序只负责发布消息
 **/
public class Publisher {

    // zookeeper服务器地址
    private static final String CONNECT_INFO = "bigdata02:2181,bigdata03:2181,bigdata04:2181";
    private static final int TIME_OUT = 4000;

    // 备用的父子节点
    private static final String PARENT_NODE = "/publish_parent";
    private static final String SUB_NODE = PARENT_NODE + "/publish_info14";
//    private static final String PUBLISH_INFO = "bigdata01,7899,com.mazh.nx.Service01,getSum,[1,2,3]";
//    private static final String PUBLISH_INFO = "bigdata02,6655,com.mazh.nx.Service02,hello,huangbo";
    private static final String PUBLISH_INFO = "bigdata03,8457,com.mazh.nx.Service03,getName,xuzheng";

    // 会话对象
    private static ZooKeeper zookeeper = null;

    // latch就相当于一个对象锁，当latch.await()方法执行时，方法所在的线程会等待
    // 当latch的count减为0时，将会唤醒等待的线程
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
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
                    countDownLatch.countDown();
                }

                // 发布者不用干点啥
            }
        });

        /**
         * 第二步：先确保父节点存在
         */
        ArrayList<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        CreateMode mode = CreateMode.PERSISTENT;
        // 判断父节点是否存在
        Stat exists_parent = zookeeper.exists(PARENT_NODE, false);
        if (exists_parent == null) {
            zookeeper.create(PARENT_NODE, PARENT_NODE.getBytes(), acls, mode);
        }

        /**
         * 第三步：发布消息
         */
        zookeeper.create(SUB_NODE, PUBLISH_INFO.getBytes(), acls, mode);

        /**
         * 第四步：关闭会话链接
         */
        zookeeper.close();
    }
}
