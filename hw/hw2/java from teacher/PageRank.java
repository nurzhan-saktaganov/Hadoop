package org.myorg;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import org.myorg.PageRankPrepare;

public class PageRank {
    
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> {
        
        public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            String[] data = value.toString().split("\t");
            if (data.length > 2) {
                IntWritable citing = new IntWritable(Integer.parseInt(data[0]));
                Double pr = Double.parseDouble(data[1]);
                String citeds = data[2].trim();
                // Вывод графа
                output.collect(citing, new Text("G\t" + citeds));
            
                if (citeds.length() > 0) {
                    String[] cs = citeds.split(",");
                    pr /= cs.length;
                    for (String cited: cs) {
                        output.collect(new IntWritable(Integer.parseInt(cited)), new Text("P\t" + pr.toString()));
                    }
                }
            }
        }
    }
    
    public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text> {
        
        private static final double D = 0.85;

        public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            String citeds = "";
            double pf = 0.0;
            while(values.hasNext()) {
                String[] data = values.next().toString().split("\t");
                if (data[0].equals("G")) {
                    // Приняли граф
                    citeds = data[1].trim();
                } else {
                    pf += Double.parseDouble(data[1]);
                }
            }
            output.collect(key, new Text(Double.toString(1 - D + D * pf) + "\t" + citeds));
        }
    }
    
    public static void main(String[] args) throws Exception {

        //  Prepare

        JobConf conf = new JobConf(PageRankPrepare.class);
        conf.setJobName("PageRank_Java_Prepare");

        conf.setMapOutputKeyClass(IntWritable.class);
        conf.setMapOutputValueClass(Text.class);
        conf.setOutputKeyClass(IntWritable.class);
        conf.setOutputValueClass(Text.class);

        conf.setMapperClass(PageRankPrepare.Map.class);
        conf.setReducerClass(PageRankPrepare.Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        conf.setNumReduceTasks(3);

        Path outputPath = new Path("PageRank_Step_0");
        FileInputFormat.setInputPaths(conf, new Path("/data/patents/cite75_99.txt"));
        FileOutputFormat.setOutputPath(conf, outputPath);

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        
        JobClient.runJob(conf);

        //  PageRank
        
        conf = new JobConf(PageRank.class);
        
        conf.setMapOutputKeyClass(IntWritable.class);
        conf.setMapOutputValueClass(Text.class);
        conf.setOutputKeyClass(IntWritable.class);
        conf.setOutputValueClass(Text.class);
        
        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);
        
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        conf.setNumReduceTasks(3);
        
        int numi = 30;
        if (args.length > 0) {
            numi = Integer.parseInt(args[0]);
        }
        fs = FileSystem.get(conf);
        for (int i = 1; i <= numi; i++) {
            conf.setJobName(String.format("PageRank_Java_Step_%1$d",i));
            
            Path inputPath = outputPath;
            outputPath = new Path(String.format("PageRank_Step_0%1$d",i));
            if (fs.exists(outputPath)) {
                fs.delete(outputPath, true);
            }
            
            FileInputFormat.setInputPaths(conf, inputPath);
            FileOutputFormat.setOutputPath(conf, outputPath);

            JobClient.runJob(conf);
            
            fs.delete(inputPath, true);
        }
    }
}
