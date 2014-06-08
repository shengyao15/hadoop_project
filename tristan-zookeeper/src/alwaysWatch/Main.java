package alwaysWatch;

/**
 * A simple example program to use DataMonitor to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. It also starts the specified program
 * with the specified arguments when the znode exists and kills
 * the program if the znode goes away.
 */
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Main implements Watcher, StatCallback {
	String znode = "/e";
	ZooKeeper zk;

	public Main() throws Exception {
		zk = new ZooKeeper("192.168.1.107:2181", 30000, this);
		zk.exists(znode, true);
		Thread.sleep(Long.MAX_VALUE);
	}
	public static void main(String[] args) throws Exception {
		new Main();
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