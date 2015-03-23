#!/usr/bin/env python

import sys

__author__ = 'Nurzhan Saktaganov'

#"PATENT","GYEAR","GDATE","APPYEAR","COUNTRY","POSTATE","ASSIGNEE","ASSCODE","CLAIMS","NCLASS","CAT", "SUBCAT"\
# ,"CMADE","CRECEIVE","RATIOCIT","GENERAL","ORIGINAL","FWDAPLAG","BCKGTLAG","SELFCTUB","SELFCTLB","SECDUPBD","SECDLWBD"

def main():
	for line in sys.stdin:
		if line.startswith('"'):
			continue
		row = line[:-1].split(',')

		print '%s\t%s' % (row[GYEAR], row[COUNTRY][1:3])

#PATENT   = 0
GYEAR    = 1
#GDATE    = 2
#APPYEAR  = 3
COUNTRY  = 4
#POSTATE  = 5
#ASSIGNEE = 6
#ASSCODE  = 7
#CLAIMS   = 8
#NCLASS   = 9
#CAT      = 10
#SUBCAT   = 11
#CMADE    = 12
#CRECEIVE = 13
#RATIOCIT = 14
#GENERAL  = 15
#ORIGINAL = 16
#FWDAPLAG = 17
#BCKGTLAG = 18
#SELFCTUB = 19
#SELFCTLB = 20
#SECDUPBD = 21
#SECDLWBD = 22

if __name__ == '__main__':
	main()