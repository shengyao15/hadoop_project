package basic;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class Tristan {
    private static final int TIMEOUT = 30000;

    private static Watcher getWatcher(final String msg){
        return new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                System.out.println(msg+"\t"+event.toString());
                
            }
        };
    }
    
    public static void main(String[] args) throws Exception {
    	ZooKeeper zk = new ZooKeeper("192.168.1.107:2181", TIMEOUT, null);
    	Stat s=zk.exists("/tristan", getWatcher("EXISTS"));
        if(s!=null){
            zk.getData("/tristan", getWatcher("GETDATA"), s);
        }
        zk.getChildren("/tristan", getWatcher("Children"));
        
       Thread.sleep(Long.MAX_VALUE);
    }
}