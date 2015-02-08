package tristan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import utils.Md5Utils;

public class HBaseDemo2 {
	public static final byte[] TABLE_NAME = Bytes.toBytes("students");
	
public static void main(String[] args) throws  Exception {
	   Configuration conf = HBaseConfiguration.create();
	    conf.set("hbase.zookeeper.quorum", "hbaseserver");
	    HBaseAdmin admin = new HBaseAdmin(conf);
	    
//	    dropTable(admin);
	    checkTableExist(admin);
		
		HTablePool pool = new HTablePool(conf,10);
		HTableInterface students = pool.getTable("students");
		
//		prepareData(students,"hbase");
//	    delete(students);

//	    listAll(students);
		search(students,"China");
//		search2(students,"China");
}

private static final int longLength = 8; // bytes

private static Scan mkScan(String user) {
    byte[] userHash = Md5Utils.md5sum(user);
    byte[] startRow = Bytes.padTail(userHash, longLength); // 212d...866f00...
    byte[] stopRow = Bytes.padTail(userHash, longLength);
    stopRow[Md5Utils.MD5_LENGTH-1]++;                      // 212d...867000...

    p("Scan starting at: '" + to_str(startRow) + "'");
    p("Scan stopping at: '" + to_str(stopRow) + "'");

    Scan s = new Scan(startRow, stopRow);
    s.addFamily(Bytes.toBytes("info"));
    return s;
	
  }

private static Scan mkScan2(String country) {
    byte[] countryHash = Md5Utils.md5sum(country);
    byte[] startRow = Bytes.padHead(countryHash, Md5Utils.MD5_LENGTH); // 212d...866f00...
    byte[] stopRow = Bytes.padHead(countryHash, Md5Utils.MD5_LENGTH);
    
    
    
    
    stopRow[Md5Utils.MD5_LENGTH-1]++;                      // 212d...867000...

    p("Scan starting at: '" + to_str(startRow) + "'");
    p("Scan stopping at: '" + to_str(stopRow) + "'");

    Scan s = new Scan(startRow, stopRow);
    s.addFamily(Bytes.toBytes("info"));
    return s;
	
  }

private static void search2(HTableInterface students, String country) throws IOException {
	Scan s = mkScan2(country);//new Scan();
	s.addFamily(Bytes.toBytes("info"));
	ResultScanner results = students.getScanner(s);
	int i = 0;
	for(Result r : results) {
		p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("country")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("english")));
	    System.out.println();
	}
	
}


// Filter filter = new SingleColumnValueFilter(
//         Bytes.toBytes("info"), Bytes.toBytes("id"), CompareOp.EQUAL, Bytes.toBytes("1")); 
// scan.setFilter(filter);

private static void search(HTableInterface students, String name) throws IOException {
	Scan s = mkScan3(name);//new Scan();
	s.addFamily(Bytes.toBytes("info"));
	
	
	
	ResultScanner results = students.getScanner(s);
	int i = 0;
	for(Result r : results) {
		p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("country")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("english")));
	    System.out.println();
	}
	
}


private static Scan mkScan3(String country) {
	Scan scan = new Scan();
     Filter filter = new SingleColumnValueFilter(
             Bytes.toBytes("info"), Bytes.toBytes("country"), CompareOp.EQUAL, Bytes.toBytes(country)); 
     scan.setFilter(filter);
     
     return scan;
}

private static void listAll(HTableInterface students) throws IOException {
	Scan s = new Scan();
	s.addFamily(Bytes.toBytes("info"));
	ResultScanner results = students.getScanner(s);
	int i = 0;
	for(Result r : results) {
		p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("country")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("english")));
	    System.out.println();
	}
	
}




private static void checkTableExist(HBaseAdmin admin) throws IOException {
	if (!admin.tableExists(Bytes.toBytes("students"))) {
		HTableDescriptor desc = new HTableDescriptor(
				Bytes.toBytes("students"));
		HColumnDescriptor c = new HColumnDescriptor(Bytes.toBytes("info"));
		desc.addFamily(c);
		admin.createTable(desc);
	}
}


private static void dropTable(HBaseAdmin admin) throws IOException {
    admin.disableTable("students");  
    admin.deleteTable("students"); 
	
}


private static void delete(HTableInterface students) throws IOException {
	Delete d = new Delete(Bytes.toBytes("tristan"));
	students.delete(d);
}


private static void prepareData(HTableInterface students, String name ) throws IOException {
	
		Random r = new Random();
		String[] countrys = { "USA", "China", "Japan" };
		for (int i = 0; i < 20; i++) {
			Map<String,String> map = new HashMap<String, String>();
			
			String country = countrys[r.nextInt(3)];
			String english = String.valueOf(30+r.nextInt(70));
			String username = name+i;
			
			//-----------
			byte[] userHash = Md5Utils.md5sum(username);
		    byte[] countryHash = Md5Utils.md5sum(country);
		    byte[] rowKey = new byte[Md5Utils.MD5_LENGTH + Md5Utils.MD5_LENGTH];
		    int offset = 0;
		    offset = Bytes.putBytes(rowKey, offset, userHash, 0, userHash.length);
		    Bytes.putBytes(rowKey, offset, countryHash, 0, countryHash.length);
		    //-----------
		    
			Put p = new Put(rowKey);
			p.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(username));
			p.add(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(String.valueOf(20+r.nextInt(10))));
			p.add(Bytes.toBytes("info"), Bytes.toBytes("country"), Bytes.toBytes(country));
			p.add(Bytes.toBytes("info"), Bytes.toBytes("english"), Bytes.toBytes(english));
			students.put(p);
			
			
		}
	
}
private static void p(String v){
	System.out.println(v);
}

private static void p(long v){
	System.out.println(v);
}

private static void p(byte[] v){
	System.out.print(Bytes.toString(v)+ "   ");
}

private static String to_str(byte[] xs) {
    StringBuilder sb = new StringBuilder(xs.length *2);
    for(byte b : xs) {
      sb.append(b).append(" ");
    }
    sb.deleteCharAt(sb.length() -1);
    return sb.toString();
  }
}
