package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import utils.Md5Utils;

public class TestFollowsCRUD {

	private static final String[] mans = { "r Dion24", "r Knute12", "r Lars68",
			"resan Gregg99", "ry Lynn60", "s Colin10" };
	public static final byte[] FOLLOWS_TABLE_NAME = Bytes.toBytes("follows");
	public static final byte[] FOLLOWED_TABLE_NAME = Bytes
			.toBytes("followedBy");
	public static final byte[] RELATION_FAM = Bytes.toBytes("f");
	public static final byte[] FROM = Bytes.toBytes("from");
	public static final byte[] TO = Bytes.toBytes("to");
	private static final int KEY_WIDTH = 32;

	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "16.165.93.5");
		HTablePool pool = new HTablePool(conf, 10);
		
		//create(pool);

		search(pool);
		
	}

	private static void search(HTablePool pool) throws IOException {
		HTableInterface t = pool.getTable("follows");

	    byte[] ahash = Md5Utils.md5sum("tristan");
	    byte[] rowkey = new byte[KEY_WIDTH];

	    Bytes.putBytes(rowkey, 0, ahash, 0, ahash.length);
	    
	    byte[] startKey = rowkey;
	    byte[] endKey = Arrays.copyOf(startKey, startKey.length);
	    endKey[Md5Utils.MD5_LENGTH-1]++;
	    Scan scan = new Scan(startKey, endKey);
	    scan.addColumn(RELATION_FAM, TO);
	    scan.setMaxVersions(1);

	    ResultScanner results = t.getScanner(scan);
	    List<HBaseIA.TwitBase.model.Relation> ret
	      = new ArrayList<HBaseIA.TwitBase.model.Relation>();
	    for (Result r : results) {
	      KeyValue kv = r.getColumnLatest(RELATION_FAM, TO);
	      P.p(kv.getValue());
	      
	    }
	}

	private static void create(HTablePool pool) throws IOException {
		for (int i = 0; i < mans.length; i++) {

			HTableInterface t = pool.getTable("follows");

			byte[] ahash = Md5Utils.md5sum("ayant68");
			byte[] bhash = Md5Utils.md5sum(mans[i]);
			byte[] rowkey = new byte[KEY_WIDTH];

			int offset = 0;
			offset = Bytes.putBytes(rowkey, offset, ahash, 0, ahash.length);
			Bytes.putBytes(rowkey, offset, bhash, 0, bhash.length);

			Put p = new Put(rowkey);
			p.add(RELATION_FAM, FROM, Bytes.toBytes("ayant68"));
			p.add(RELATION_FAM, TO, Bytes.toBytes(mans[i]));
			t.put(p);

		}
	}
}
