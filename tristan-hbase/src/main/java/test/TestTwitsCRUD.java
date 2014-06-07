package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;


import utils.Md5Utils;


public class TestTwitsCRUD {
	  public static final byte[] TABLE_NAME = Bytes.toBytes("twits");
	  public static final byte[] TWITS_FAM   = Bytes.toBytes("twits");

	  public static final byte[] USER_COL   = Bytes.toBytes("user");
	  public static final byte[] TWIT_COL   = Bytes.toBytes("twit");
	  private static final int longLength = 8; // bytes
	  
	public static void main(String[] args) throws Exception {
		  Configuration conf = HBaseConfiguration.create();
		    conf.set("hbase.zookeeper.quorum", "16.165.93.5");
		    HTablePool pool = new HTablePool(conf,10);
		    
		    HTableInterface twits = pool.getTable("twits");
		    
		    //listAll(twits);
		    
		    //search(twits);
		    
		    //create(twits);
		    
		    
		    
	}


	private static void create(HTableInterface twits) throws IOException {
		DateTime dt = new DateTime();
		byte[] userHash = Md5Utils.md5sum("ayant68");
		byte[] timestamp = Bytes.toBytes(-1 * dt.getMillis());
		byte[] rowKey = new byte[Md5Utils.MD5_LENGTH + longLength];

		int offset = 0;
		offset = Bytes.putBytes(rowKey, offset, userHash, 0, userHash.length);
		Bytes.putBytes(rowKey, offset, timestamp, 0, timestamp.length);
		
		
		Put p = new Put(rowKey);
		p.add(TWITS_FAM, USER_COL, Bytes.toBytes("ayant68"));
		p.add(TWITS_FAM, TWIT_COL, Bytes.toBytes("the song of ice and fire"));
		twits.put(p);
	}


	private static void search(HTableInterface twits) throws IOException {
		byte[] userHash = Md5Utils.md5sum("ayant68");
		byte[] startRow = Bytes.padTail(userHash, longLength); // 212d...866f00...
		byte[] stopRow = Bytes.padTail(userHash, longLength);
		stopRow[Md5Utils.MD5_LENGTH-1]++;                      // 212d...867000...

		Scan s = new Scan(startRow, stopRow);
		s.addColumn(TWITS_FAM, USER_COL);
		s.addColumn(TWITS_FAM, TWIT_COL);
		
		ResultScanner results = twits.getScanner(s);
		List<HBaseIA.TwitBase.model.Twit> ret = new ArrayList<HBaseIA.TwitBase.model.Twit>();
		for(Result r : results) {
			List<KeyValue> column = r.getColumn(TWITS_FAM, USER_COL);
			for (KeyValue keyValue : column) {
				p(keyValue.getValue());
			}
			p(r.getValue(TWITS_FAM, TWIT_COL));
		}
	}
	
	 
	private static void listAll(HTableInterface twits) throws IOException {
		Scan s = new Scan();
		s.addFamily(TWITS_FAM);
		ResultScanner results = twits.getScanner(s);
		ArrayList<HBaseIA.TwitBase.model.User> ret
		  = new ArrayList<HBaseIA.TwitBase.model.User>();
		int i = 0;
		for(Result r : results) {
			if(i>2){
				break;
			}
			i++;
			
			p(r.getRow());
			List<KeyValue> column = r.getColumn(TWITS_FAM, USER_COL);
			for (KeyValue keyValue : column) {
				p(keyValue.getValue());
				p(keyValue.getTimestamp());
			}
			p(r.getValue(TWITS_FAM, TWIT_COL));
			
		    p("-------------------");
		}
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
