package watcher;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class SelfWatcher1 implements Watcher{
    
    ZooKeeper zk=null;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("x:  "+event.toString());
    }
    
    SelfWatcher1(String address){
        try{
            zk=new ZooKeeper(address,30000,this);     //在创建ZooKeeper时第三个参数负责设置该类的默认构造函数
            zk.create("/root", new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }catch(IOException e){
            e.printStackTrace();
            zk=null;
        }catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    void setWatcher(){
        try {
            Stat s=zk.exists("/root", false);
            if(s!=null){
                zk.getData("/root", false, s);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    void trigeWatcher(){
        try {
            Stat s=zk.exists("/root", false);        //此处不设置watcher
            zk.setData("/root", "a".getBytes(), s.getVersion()); //修改数据时需要提供version，version设为-1表示强制修改
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void disconnect(){
        if(zk!=null)
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    
    public static void main(String[] args){
        SelfWatcher1 inst=new SelfWatcher1("192.168.1.107:2181");
        inst.setWatcher();
        inst.trigeWatcher();
        inst.disconnect();
    }

}