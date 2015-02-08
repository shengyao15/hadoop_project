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
import org.apache.hadoop.hbase.client.Get;
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

public class HBaseDemo {
	public static final byte[] TABLE_NAME = Bytes.toBytes("students");
	
public static void main(String[] args) throws  Exception {
	   Configuration conf = HBaseConfiguration.create();
	    conf.set("hbase.zookeeper.quorum", "hadoop1");
	    HBaseAdmin admin = new HBaseAdmin(conf);
	    
//	    dropTable(admin);
	    checkTableExist(admin);
		
		HTablePool pool = new HTablePool(conf,10);
		HTableInterface students = pool.getTable("students");
				
		prepareData(students,"hbase");
//	    delete(students);

//	    listAll(students);
//		searchByName(students,"hbase19");
		
//		searchByCountry(students,"USA");
}



private static void searchByCountry(HTableInterface students, String country) throws IOException {
	Scan scan = new Scan();
    Filter filter = new SingleColumnValueFilter(
            Bytes.toBytes("info"), Bytes.toBytes("country"), CompareOp.EQUAL, Bytes.toBytes(country)); 
    scan.setFilter(filter);
	scan.addFamily(Bytes.toBytes("info"));
	ResultScanner results = students.getScanner(scan);
	int i = 0;
	for(Result r : results) {
		p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("country")));
	    p(r.getValue(Bytes.toBytes("info"), Bytes.toBytes("english")));
	    System.out.println();
	}
}



private static void searchByName(HTableInterface students, String name) throws IOException {
	Get get = new Get(Bytes.toBytes(name));
	Scan s = new Scan(get);
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
	    
		Put p = new Put(Bytes.toBytes(username));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(username));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(String.valueOf(20+r.nextInt(10))));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("country"), Bytes.toBytes(country));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("english"), Bytes.toBytes(english));
		students.put(p);
		
		
	}
	
}

private static void p(byte[] v){
	System.out.print(Bytes.toString(v)+ "   ");
}

}