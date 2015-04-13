apat = LOAD '/data/patents/apat63_99.txt'
	USING PigStorage(',')
	AS (patent:long, gyear: int, gdate: int,
	appyear:chararray, country: chararray);  

apatUSA1990 = FILTER apat BY gyear==1990 AND country=='"US"';

cite = LOAD '/data/patents/cite75_99.txt'
	USING PigStorage(',')
	AS (citing: long, cited: long);

citeUSA1990 = JOIN
	apatUSA1990 BY patent,
	cite BY citing;

citingList = GROUP citeUSA1990 BY cited;

citingCount = FOREACH citingList
	GENERATE
	group AS patent,
	COUNT(citeUSA1990) AS rate; 

orderedCitingCount = ORDER citingCount
	BY rate DESC;

toPrint = LIMIT orderedCitingCount 10;

DUMP toPrint;
