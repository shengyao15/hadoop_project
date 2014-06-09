package watcher;
import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class SelfWatcher3 implements Watcher, StatCallback{
    
    static ZooKeeper zk=null;
    static String znode = "/root";
    
    private static Watcher getWatcher(final String msg){
        return new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                System.out.println(msg+"\t"+event.toString());
            }
        };
    }
    
    
    public static void main(String[] args) throws Exception, InterruptedException{
    	new SelfWatcher3();
    }


	public SelfWatcher3() throws Exception {
		zk=new ZooKeeper("hbaseserver:2181",30000, this); 
        
        zk.getData(znode, true, null);
        zk.setData(znode, "a".getBytes(), -1);
        zk.setData(znode, "b".getBytes(), -1);
        zk.setData(znode, "c".getBytes(), -1);
        zk.setData(znode, "d".getBytes(), -1);
        zk.setData(znode, "e".getBytes(), -1);

       Thread.sleep(Long.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		zk.exists(znode, true, this, null);
	}


	@Override
	public void processResult(int rc, String path, Object ctx, Stat stat) {
		try {
			byte b[] = zk.getData(znode, false, null);
			System.out.println(new String(b, "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}