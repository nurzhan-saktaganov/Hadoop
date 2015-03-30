#!/bin/sh

rm -r classes
mkdir classes
javac -d classes/ HomeWork2.java
jar -cvf hw2.jar -C classes/ ./

INPUT='/data/patents/cite75_99.txt'
OUTPUT='java_PageRank_Step_0'

hadoop fs -rm -r ${OUTPUT}
hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar \
    -file mapper0.py reducer0.py \
    -mapper mapper0.py \
    -reducer reducer0.py \
    -input ${INPUT} \
    -output ${OUTPUT}

for ((i=1;i<=30;i++))
do
    INPUT=${OUTPUT}
    OUTPUT='java_PageRank_Step_'${i}

    hadoop fs -rm -r ${OUTPUT}
    hadoop jar hw2.jar org.myorg.HomeWork2 ${INPUT}  ${OUTPUT}
    hadoop fs -rm -r ${INPUT}

#    hadoop fs -text ${OUTPUT}/part* | sort -k2,2nr | head > ${OUTPUT}_top.txt
done
