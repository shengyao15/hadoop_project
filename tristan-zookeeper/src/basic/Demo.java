package basic;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class Demo {
    private static final int TIMEOUT = 30000;

    public static void main(String[] args) throws IOException {
    	ZooKeeper zkp = new ZooKeeper("192.168.1.107:2181", TIMEOUT, null);
    	
    	
        try {
            // ����һ��EPHEMERAL���͵Ľڵ㣬�Ự�رպ������Զ���ɾ��
            zkp.create("/node1", "data1".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            if (zkp.exists("/node1", false) != null) {
                System.out.println("node1 exists now.");
            }
            try {
                // ���ڵ����Ѵ���ʱ��ȥ���������׳�KeeperException(��ʹ���ε�ACL��CreateMode���ϴεĲ�һ��)
                zkp.create("/node1", "data1".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            } catch (KeeperException e) {
                System.out.println("KeeperException caught:" + e.getMessage());
            }

            // �رջỰ
            zkp.close();
            
            zkp = new ZooKeeper("192.168.1.107:2181", TIMEOUT, null);
            //���½����Ự��node1�Ѿ���������
            if (zkp.exists("/node1", false) == null) {
                System.out.println("node1 dosn't exists now.");
            }
            //����SEQUENTIAL�ڵ�
            zkp.create("/node-", "same data".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
            zkp.create("/node-", "same data".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
            zkp.create("/node-", "same data".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
            List<String> children = zkp.getChildren("/", null);
            System.out.println("Children of root node:");
            for (String child : children) {
                System.out.println(child);
            }

            zkp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}