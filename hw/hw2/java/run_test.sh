#!/bin/sh
rm -r classes
mkdir classes
javac -d classes/ HomeWork2.java
jar -cvf hw2.jar -C classes/ ./

hadoop fs -rm -r hw2_java
hadoop jar hw2.jar org.myorg.HomeWork2 PageRank/part* hw2_java