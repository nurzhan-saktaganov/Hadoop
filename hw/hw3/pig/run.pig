cite = LOAD '/data/patents/cite75_99.txt'
	USING PigStorage(',')
	AS (citing: long, cited: long);   

citingList = GROUP cite BY cited;

citingCount = FOREACH citingList 
	GENERATE COUNT(cite) AS linkCount,
	group AS patent;

apat = LOAD '/data/patents/apat63_99.txt'
	USING PigStorage(',')
	AS (patent: long, gyear: int, gdate: int, appyear: chararray
	, country: chararray, postate: chararray, assignee: chararray
	, asscode: chararray, claims: chararray, nclass: chararray
	, cat: chararray, subcat: chararray, cmade: chararray
	, creceive: chararray, ratiocit: chararray, general: chararray
	, original: chararray, fwdaplag: chararray, bckgtlag: chararray
	, selfctub: chararray, selfctlb: chararray, secdupbd: chararray
	, secdlwbd: chararray);

apatAndCiteCount = JOIN citingCount
	BY patent LEFT OUTER,
	apat BY patent;
	
orderedApatAndCiteCount = ORDER apatAndCiteCount
	BY linkCount DESC;

toPrint = LIMIT orderedApatAndCiteCount 10;

DUMP toPrint;
