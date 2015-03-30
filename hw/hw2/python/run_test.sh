#!/bin/sh

hadoop fs -rm -r hw2_python

hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
	-file mapper.py reducer.py \
	-mapper mapper.py \
	-reducer reducer.py \
	-input PageRank/part* \
	-output hw2_python
