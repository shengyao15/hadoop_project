package tristan;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyWordCount {
	public static class MyMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);// 类似于int类型
		private Text word = new Text(); // 可以理解成String类型

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			// 默认情况下即根据空格分隔字符串
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		};
	}

	// Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
	public static class MyReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			
			context.write(key, result);// 这是最后结果
		};
	}

	public static void main(String[] args) throws Exception {
		// 声明配置信息
		Configuration conf = new Configuration();
		// 声明Job
		Job job = new Job(conf, "Word Count");
		// 设置工作类
		job.setJarByClass(MyWordCount.class);
		// 设置mapper类
		job.setMapperClass(MyMapper.class);
		// 可选
		job.setCombinerClass(MyReducer.class);
		// 设置合并计算类
		job.setReducerClass(MyReducer.class);
		// 设置key为String类型
		job.setOutputKeyClass(Text.class);
		// 设置value为int类型
		job.setOutputValueClass(IntWritable.class);
		// 设置或是接收输入输出
//		FileInputFormat.setInputPaths(job, new Path("hdfs://localhost:9000/sample/wordcount.txt"));
//		FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/result/"+ day));
        FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
		// 执行
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
