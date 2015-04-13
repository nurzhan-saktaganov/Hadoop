CREATE EXTERNAL TABLE cite (citing BIGINT, cited BIGINT) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS TEXTFILE;
	
LOAD DATA LOCAL INPATH '/user/n.saktaganov/patents/cite75_99.txt' OVERWRITE INTO TABLE cite;

LOAD DATA INPATH '/user/n.saktaganov/patents/cite75_99.txt' OVERWRITE INTO TABLE cite;

LOAD DATA INPATH '/user/n.saktaganov/patents/cite75_99.txt' INTO TABLE cite;

