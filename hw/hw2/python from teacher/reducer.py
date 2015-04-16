#!/usr/bin/env python
# -*- coding: utf-8 -*-
 
import sys
from itertools import groupby

splt = lambda line: line.strip().split('\t')

D = 0.85

for citing, group in groupby((splt(line) for line in sys.stdin), lambda line: line[0]):
    citeds = ''
    pf = 0.0
    for values in group:
        flag = values[1]
        if flag == 'G' and len(values) == 3:
            citeds = values[2]
        elif flag == 'P':
            pf += float(values[2])
    print '%s\t%s\t%s' % (citing,1-D+D*(pf),citeds)
    
