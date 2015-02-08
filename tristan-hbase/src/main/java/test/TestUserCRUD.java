package test;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;



public class TestUserCRUD {
	  public static final byte[] TABLE_NAME = Bytes.toBytes("users");
	  public static final byte[] INFO_FAM   = Bytes.toBytes("info");

	  public static final byte[] USER_COL   = Bytes.toBytes("user");
	  public static final byte[] NAME_COL   = Bytes.toBytes("name");
	  public static final byte[] EMAIL_COL  = Bytes.toBytes("email");
	  public static final byte[] PASS_COL   = Bytes.toBytes("password");
	  public static final byte[] TWEETS_COL = Bytes.toBytes("tweet_count");
	  
	public static void main(String[] args) throws Exception {
		  Configuration conf = HBaseConfiguration.create();
		  conf.set("hbase.zookeeper.quorum", "hbaseserver");
		    HTablePool pool = new HTablePool(conf,10);
		    
		    HTableInterface users = pool.getTable("users");
		    
		    create(users);
		    search(users);
		    listAll(users);
		    //delete(users);
		    
		    users.close();
		    pool.close();
	}

	private static void delete(HTableInterface users) throws IOException {
		Delete d = new Delete(Bytes.toBytes("tristan"));
		users.delete(d);
	}

	private static void search(HTableInterface users) throws IOException {
		Get g = new Get(Bytes.toBytes("tristan"));
		g.addFamily(INFO_FAM);
		Result result = users.get(g);
		p(result.getRow());
		p(result.getValue(INFO_FAM, USER_COL));
		p(result.getValue(INFO_FAM, NAME_COL));
		p(result.getValue(INFO_FAM, EMAIL_COL));
		p(result.getValue(INFO_FAM, PASS_COL));
		byte[] tweetCount = result.getValue(INFO_FAM, "tweet_count".getBytes());
		if(tweetCount != null){
			 p(Bytes.toLong(tweetCount));
		}
	}

	private static void listAll(HTableInterface users) throws IOException {
		Scan s = new Scan();
		s.addFamily(INFO_FAM);
		ResultScanner results = users.getScanner(s);
		ArrayList<HBaseIA.TwitBase.model.User> ret
		  = new ArrayList<HBaseIA.TwitBase.model.User>();
		int i = 0;
		for(Result r : results) {
			if(i>5){
				break;
			}
			i++;
			p(r.getValue(INFO_FAM, USER_COL));
		    p(r.getValue(INFO_FAM, NAME_COL));
		    p(r.getValue(INFO_FAM, EMAIL_COL));
		    p(r.getValue(INFO_FAM, PASS_COL));
		    byte[] tweetCount2 = r.getValue(INFO_FAM, "tweet_count".getBytes());
		    if(tweetCount2 != null){
		    	 p(Bytes.toLong(tweetCount2));
		    }
		    p("-------------------");
		}
	}

	private static void create(HTableInterface users) throws IOException {
		Put p = new Put(Bytes.toBytes("tristan"));
		p.add(INFO_FAM, USER_COL, Bytes.toBytes("tristan"));
		p.add(INFO_FAM, NAME_COL, Bytes.toBytes("sheng yao"));
		p.add(INFO_FAM, EMAIL_COL, Bytes.toBytes("rao.sheng@hp.com2"));
		p.add(INFO_FAM, PASS_COL, Bytes.toBytes("asdf654321"));
		users.put(p);
	}
	
	private static void p(String v){
		System.out.println(v);
	}
	
	private static void p(long v){
		System.out.println(v);
	}
	
	private static void p(byte[] v){
		System.out.println(Bytes.toString(v));
	}
}
