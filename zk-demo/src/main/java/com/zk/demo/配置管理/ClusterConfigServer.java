package com.zk.demo.配置管理;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个  ClusterConfigServer 程序就是运行在 datanode 中的
 */
public class ClusterConfigServer {
	
	// 获取zookeeper连接时所需要的服务器连接信息，格式为主机名：端口号
	private static final String ConnectString = "bigdata02:2181,bigdata03:2181,bigdata04:2181";

	// 请求了解的会话超时时长
	private static final int SessionTimeout = 5000;

	private static ZooKeeper zk = null;
	private static final String PARENT_NODE = "/config";

	private static Map<String, String> configMap = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {

		// 第一步：获取zk链接
		zk = new ZooKeeper(ConnectString, SessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				// System.out.println(event.getPath() + "\t-----" + event.getType());

				// 增加或者删除的逻辑
				if (event.getPath().equals(PARENT_NODE) && event.getType() == EventType.NodeChildrenChanged) {
					try {
						// 当监听一收到通知，就表示有子节点变化事件，那么就先找出现在还剩下的配置
						Map<String, String> currentConfigMap = getConfigMap(PARENT_NODE);

						// 用当前还剩下的配置， 跟之前的配置做对比，找出是新增加的配置，还是删除的配置
						// 首先确定下来是新增加了一个配置还是删除了一个配置
						String doFlag = "add";
						Map<String, String> bigMap = currentConfigMap;
						Map<String, String> smallMap = configMap;

						if (currentConfigMap.size() < configMap.size()) {
							doFlag = "delete";
							bigMap = configMap;
							smallMap = currentConfigMap;
						}

						Set<String> smallMapKeySet = smallMap.keySet();
						// 不管是新增加一个配置还是删除一个配置，我都用大的map的元素去小的map里找，没找出结果的就是多出来的
						for (String key : bigMap.keySet()) {
							if (!smallMapKeySet.contains(key)) {
								System.out.println("当前 " + doFlag + " 了一项配置，该配置节点名称是：" + key);
								// 如果是新增， 那么还得为新增加的节点，添加NodeDataChanged监听
								if (doFlag.equals("add")) {
									zk.getData(key, true, null);
								}
								break;
							}
						}
						// 最后一定要注意，map的迭代
						configMap = currentConfigMap;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 假如是修改配置怎么办。？
				for (String key : configMap.keySet()) {
					if (event.getPath().equals(key) && event.getType() == EventType.NodeDataChanged) {
						try {
							String lastValue = configMap.get(key);
							// 获取该节点的数据，并且持续加监听， 没有被更改过数据的不用加监听，反正没有被触发
							byte[] data = zk.getData(key, true, null);
							System.out.println("节点 " + key + "  的配置信息   " + new String(lastValue) + "  被改成了：" + new String(data));
							configMap = getConfigMap(PARENT_NODE);
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		/**
		 * 第二步, 监听程序一上线， 先获取到所有的config信息， 
		 * 如果在方法中就加了各种监听， 那么下面两段话就可以注释掉了。
		 */
		configMap = getConfigMap(PARENT_NODE);

		Thread.sleep(Long.MAX_VALUE);

		zk.close();
	}

	/**
	 * 获取PARENT_NODE节点下所有的配置信息放到一个map中
	 */
	public static Map<String, String> getConfigMap(String znode) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 给父节点加NodeChildrenChanged事件
		List<String> children = zk.getChildren(znode, true);
		for (String child : children) {
			byte[] data = zk.getData(znode + "/" + child, false, null);
			map.put(znode + "/" + child, new String(data));
			// 给子节点加NodeDataChanged事件
			zk.getData(znode + "/" + child, true, null);
		}
		return map;
	}
}