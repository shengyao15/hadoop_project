package test;

import org.apache.hadoop.hbase.util.Bytes;

public class P {
	 static void p(String v){
		System.out.println(v);
	}
	
	 static void p(long v){
		System.out.println(v);
	}
	
	static void p(byte[] v){
		System.out.println(Bytes.toString(v));
	}
}
