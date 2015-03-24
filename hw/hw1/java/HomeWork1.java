package org.myorg;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class HomeWork1 {
    
    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> {

        private static final int GYEAR   = 1;
        private static final int COUNTRY = 4;

        public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            
            String line = value.toString();
            
            if (line.startsWith("\"")){
                return;
            }

            String[] row = line.split(",");
            row[COUNTRY] = row[COUNTRY].substring(1, row[COUNTRY].length() - 1);

            output.collect(new IntWritable(Integer.parseInt(row[GYEAR])), new Text(row[COUNTRY]));
        }
    }
    
    public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, IntWritable, Text> {
        public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            String country;

            while (values.hasNext()){
                country = values.next().toString();
                if(map.containsKey(country)){
                    map.put(country, map.get(country) + 1);
                } else {
                    map.put(country, 1);
                }
            }

            Integer[] patents_count = map.values().toArray(new Integer[map.size()]);
            Arrays.sort(patents_count);

            int uniq_countries = patents_count.length;
            int minimum = patents_count[0];
            int maximum = patents_count[uniq_countries - 1];
            double mean = 0.0;
            double mediana;
            double devitation = 0.0;

            if (uniq_countries % 2 == 0){
                mediana = 1.0 * (patents_count[uniq_countries / 2 - 1] + patents_count[uniq_countries / 2]) / 2;
            } else {
                mediana = patents_count[uniq_countries / 2];
            }
            
            for(int i = 0; i < patents_count.length; i++){
                mean += patents_count[i];
            }

            mean /= patents_count.length;

            for(int i = 0; i < patents_count.length; i++){
                devitation += (patents_count[i] - mean) * (patents_count[i] - mean);
            }

            devitation = Math.sqrt(devitation / patents_count.length);

            String result = String.valueOf(uniq_countries) + "\t" + String.valueOf(minimum) + "\t" + String.valueOf(mediana)
                    + "\t" + String.valueOf(maximum) + "\t" + String.valueOf(mean) + "\t" + String.valueOf(devitation);

            output.collect(key, new Text(result));
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(HomeWork1.class);
        conf.setJobName("hw1_java");
        
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
