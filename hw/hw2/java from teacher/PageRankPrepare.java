package org.myorg;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class PageRankPrepare {
    
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> {
        
        public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            String[] data = value.toString().split(",");
            if (data.length == 2 && data[0].charAt(0) != '"') {
                output.collect(new IntWritable(Integer.parseInt(data[0])), new Text(data[1]));
            }
        }
    }
    
    public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text> {
        
        public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            String citeds = values.next().toString();
            while(values.hasNext()) {
                citeds = citeds.concat(String.format(",%s",values.next().toString()));
            }
            output.collect(key, new Text(String.format("%f\t%s", 0.15, citeds)));
        }
    }
}
