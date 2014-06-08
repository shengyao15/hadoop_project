package basic;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Main {
public static void main(String[] args) throws Exception {
	ZooKeeper zk = new ZooKeeper("192.168.1.107:2181", 30000, new Watcher() { 
	            // ������б��������¼�
	            public void process(WatchedEvent event) { 
	                System.out.println("�Ѿ�������" + event.getType() + "�¼���"); 
	            } 
	        }); 
	
	Thread.sleep(Long.MAX_VALUE);
	 
	 /*// ����һ��Ŀ¼�ڵ�
	 zk.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,
	   CreateMode.PERSISTENT); 
	 
	 
	 //�޸�
	 zk.setData("/testRootPath", "testRootData2".getBytes(),-1); 
	 
	 // ����һ����Ŀ¼�ڵ�
	 zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
	   Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
	 System.out.println(new String(zk.getData("/testRootPath",false,null))); 
	 // ȡ����Ŀ¼�ڵ��б�
	 System.out.println(zk.getChildren("/testRootPath",true)); 
	 // �޸���Ŀ¼�ڵ�����
	 zk.setData("/testRootPath/testChildPathOne","modifyChildDataOne".getBytes(),-1); 
	 System.out.println("Ŀ¼�ڵ�״̬��["+zk.exists("/testRootPath",true)+"]"); 
	 // ��������һ����Ŀ¼�ڵ�
	 zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), 
	   Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
	 System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null))); 
	 // ɾ����Ŀ¼�ڵ�
	 zk.delete("/testRootPath/testChildPathTwo",-1); 
	 zk.delete("/testRootPath/testChildPathOne",-1); 
	 // ɾ����Ŀ¼�ڵ�
	 zk.delete("/testRootPath",-1); 
	 // �ر�����
	 zk.close(); 
*/
}
}