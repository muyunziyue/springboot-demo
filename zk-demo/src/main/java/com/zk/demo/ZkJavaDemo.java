package com.zk.demo;

import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @author ldx
 * @date 2022/8/19
 */
public class ZkJavaDemo {
    private static final String zkConnection = "zk.goo.com";
    private static final int sessionTimeout = 4000;

    public static void main(String[] args) {
        try (ZooKeeper zooKeeper = new ZooKeeper(zkConnection, sessionTimeout, null)) {
            List<String> children = zooKeeper.getChildren("/", null);
            System.out.println(children);

            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
