package com.zk.demo.配置管理;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 这个程序模拟配置的改变：
 * 1、增加一个配置
 * 2、修改一个配置
 * 3、删除一个配置
 *
 * 这个程序是管理员程序！
 */
public class ClusterConfigClient {

	// 获取zookeeper连接时所需要的服务器连接信息，格式为主机名：端口号
	private static final String ConnectString = "bigdata02:2181,bigdata03:2181,bigdata04:2181";

	// 请求链接的会话超时时长
	private static final int SessionTimeout = 5000;

	private static ZooKeeper zk = null;
	private static final String PARENT_NODE = "/config";

	public static void main(String[] args) throws Exception {

		zk = new ZooKeeper(ConnectString, SessionTimeout, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println(event.getPath() + "\t-----" + event.getType());
			}
		});

		// 创建父节点
		if (null == zk.exists(PARENT_NODE, false)) {
			zk.create(PARENT_NODE, "cluster-config".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		// 预先建立几个配置信息
//		zk.create(PARENT_NODE + "/hadoop", "hadoop".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create(PARENT_NODE+"/hive", "hive".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create(PARENT_NODE+"/mysql", "mysql".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		zk.create(PARENT_NODE+"/redis", "redis".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

//		zk.delete(PARENT_NODE+"/hadoop", -1);
//		zk.delete(PARENT_NODE+"/hive", -1);
		zk.delete(PARENT_NODE+"/mysql", -1);
//		zk.delete(PARENT_NODE+"/redis", -1);

//		zk.setData(PARENT_NODE+"/hadoop", "bbbbbbb".getBytes(), -1);
//		zk.setData(PARENT_NODE+"/hive", "3456".getBytes(), -1);
//		zk.setData(PARENT_NODE+"/mysql", "2423234234234".getBytes(), -1);
//		zk.setData(PARENT_NODE+"/redis", "3456333".getBytes(), -1);

		zk.close();
	}
}
