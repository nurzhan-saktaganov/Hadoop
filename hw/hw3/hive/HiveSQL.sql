CREATE EXTERNAL TABLE cite (citing BIGINT, cited BIGINT)
	ROW FORMAT DELIMITED
	FIELDS TERMINATED BY ','
	STORED AS TEXTFILE;

LOAD DATA INPATH '/user/n.saktaganov/patents/cite75_99.txt'
	OVERWRITE INTO TABLE cite;

CREATE EXTERNAL TABLE apat(patent BIGINT, gyear INT
	, gdate INT, appyear STRING, country STRING)
	ROW FORMAT DELIMITED
	FIELDS TERMINATED BY ','
	STORED AS TEXTFILE;

LOAD DATA INPATH '/user/n.saktaganov/patents/apat63_99.txt'
	OVERWRITE INTO TABLE apat;

SELECT cited, COUNT(1) AS rate
	FROM apat JOIN cite ON (apat.patent = cite.citing)
	WHERE gyear=1990 AND country='"US"'
	GROUP BY cited
	ORDER BY rate DESC
	LIMIT 10;

