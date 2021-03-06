#!/bin/sh

STEP0="PageRank_Step_0"
hadoop fs -rm -r ${STEP0}

hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
	 -numReduceTasks 3 \
	 -file mapper0.py reducer0.py \
	 -mapper mapper0.py \
	 -reducer reducer0.py \
	 -input /data/patents/cite75_99.txt \
	 -output ${STEP0}

hadoop jar PageRank.jar org.myorg.PageRank 3