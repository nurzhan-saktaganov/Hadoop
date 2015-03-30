package org.myorg;

import java.io.IOException;
import java.util.*;
import java.lang.Double;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class HomeWork2 {
    
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> {

        private static final int NID = 0;
        private static final int PAGE_RANK = 1;
        private static final int ADJACENCY_LIST = 2;

        private Text text = new Text();
        private IntWritable patentID = new IntWritable();

        String line;
        String[] row;
        String[] adjacencyList;
        float pageRank;

        public void map(LongWritable key, Text value, OutputCollector <IntWritable, Text> output, Reporter reporter) throws IOException {

            line = value.toString();
            row = line.split("\t");

            //write structure: <patent ID>\t[<structure>]
            text.set(row[ADJACENCY_LIST]);
            patentID.set(Integer.parseInt(row[NID]));
            output.collect(patentID, text);

            if (row[ADJACENCY_LIST].compareTo("[]") == 0){
                return;
            }

            adjacencyList = row[ADJACENCY_LIST].substring(1, row[ADJACENCY_LIST].length() - 1).split(",");
            pageRank = Float.parseFloat(row[PAGE_RANK]) / adjacencyList.length;

            text.set(Float.toString(pageRank));

            for(int i = 0; i < adjacencyList.length; i++){
                patentID.set(Integer.parseInt(adjacencyList[i]));
                output.collect(patentID, text);
            }
        }
    }
    
    public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text> {

        private static final float D = 0.85f;
        private Text text = new Text();
        
        private float pageRank;
        private String value;
        private String adjacencyList;

        public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {

            pageRank = 0.0f;
            adjacencyList = "[]";

            while(values.hasNext()){
                value = values.next().toString();
                if (value.startsWith("[")) {
                    adjacencyList = value;
                } else {
                    pageRank += Float.parseFloat(value);
                }
            }

            pageRank = (1.0f - D) + D * pageRank;
            text.set(Float.toString(pageRank) + "\t" + adjacencyList);
            output.collect(key, text);
        }
    }


    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(HomeWork2.class);
        conf.setJobName("hw2_java");
        
        conf.setOutputKeyClass(IntWritable.class);
        conf.setOutputValueClass(Text.class);
        
        conf.setMapperClass(Map.class);
        //conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);
        
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        
        JobClient.runJob(conf);
    }
}
