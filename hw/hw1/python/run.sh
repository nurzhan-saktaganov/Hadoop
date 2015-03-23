#!/bin/sh
hadoop fs -rm -r hw1_python

hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
	 -file mapper.py reducer.py \
	 -mapper mapper.py \
	 -reducer reducer.py \
	 -input /data/patents/apat63_99.txt \
	 -output hw1_python
