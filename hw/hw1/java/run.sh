#!/bin/sh
rm -r classes
mkdir classes
javac -d classes/ HomeWork1.java
jar -cvf hw1.jar -C classes/ ./

hadoop fs -rm -r hw1_java
hadoop jar hw1.jar org.myorg.HomeWork1 /data/patents/apat63_99.txt hw1_java
