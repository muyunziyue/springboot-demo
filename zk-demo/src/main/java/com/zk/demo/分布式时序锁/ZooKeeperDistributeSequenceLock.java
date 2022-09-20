package com.zk.demo.分布式时序锁;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Author： 马中华 奈学教育 https://blog.csdn.net/zhongqi2513
 * DateTime： 2017/4/25 15:29
 * Description：时序锁实现
 *
 * 需求描述：多个客户端，需要同时访问同一个资源，但同时只允许一个客户端进行访问。
 * 设计思路：多个客户端都去父 znode 下写入一个子 znode ，能写入成功的去执行等待，当上一个任务完成的时候，
 * 等待队列中的最小ID 的任务可以继续执行!
 **/
public class ZooKeeperDistributeSequenceLock {

    private static final String connectStr = "bigdata02:2181,bigdata03:2181,bigdata04:2181";
    private static final int sessionTimeout = 4000;
    private static final String PARENT_NODE = "/parent_locks";
    private static final String SUB_NODE = "/sub_sequence_lock";
    private static String currentPath = "";

    static ZooKeeper zookeeper = null;

    public static void main(String[] args) throws Exception {

        ZooKeeperDistributeSequenceLock mdc = new ZooKeeperDistributeSequenceLock();

        /**
         * 1、拿到zookeeper链接
         */
        mdc.getZookeeperConnect();

        /**
         * 2、查看父节点是否存在，不存在则创建
         */
        Stat exists = zookeeper.exists(PARENT_NODE, false);
        if (exists == null) {
            zookeeper.create(PARENT_NODE, PARENT_NODE.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        /**
         * 3、监听父节点
         */
        zookeeper.getChildren(PARENT_NODE, true);

        /**
         * 4、往父节点下注册节点，注册临时节点，好处就是，当宕机或者断开链接时该节点自动删除
         */
        currentPath = zookeeper.create(PARENT_NODE + SUB_NODE, SUB_NODE
                .getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        Thread.sleep(Long.MAX_VALUE);

        /**
         * 5、关闭zk链接
         */
        zookeeper.close();
    }

    /**
     * 拿到zookeeper集群的链接
     */
    public void getZookeeperConnect() throws Exception {
        zookeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "\t" + event.getPath());

                // 匹配看是不是子节点变化，并且监听的路径也要对
                if (event.getType() == EventType.NodeChildrenChanged && event.getPath().equals(PARENT_NODE)) {
                    try {
                        // 获取父节点的所有子节点, 并继续监听
                        List<String> childrenNodes = zookeeper.getChildren(PARENT_NODE, true);

                        // 匹配当前创建的znode是不是最小的znode
                        Collections.sort(childrenNodes);
                        if ((PARENT_NODE + "/" + childrenNodes.get(0)).equals(currentPath)) {
                            // 处理业务
                            handleBusiness(zookeeper, currentPath);
                        } else {
                            System.out.println("not me");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void handleBusiness(ZooKeeper zk, String create) throws Exception {
        Random random = new Random();
        int sleepTime = 4000;
        System.out.println(create + " is working .......... ");
        // 线程睡眠0-4秒钟，是模拟业务代码处理所消耗的时间
        Thread.sleep(random.nextInt(sleepTime));
        // 模拟业务处理完成
        zk.delete(currentPath, -1);
        System.out.println(create + " is done --------");
        // 线程睡眠0-4秒， 是为了模拟客户端每次处理完了之后再次处理业务的一个时间间隔，
        // 最终的目的就是用来打乱你运行的多台服务器抢注该子节点的顺序
        Thread.sleep(random.nextInt(sleepTime));
        // 模拟去抢资源锁
        currentPath = zk.create(PARENT_NODE + SUB_NODE, SUB_NODE
                .getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }
}
