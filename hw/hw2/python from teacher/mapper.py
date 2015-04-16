#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

for line in sys.stdin:
    citing, pr, citeds = line.split('\t')
    citeds = citeds.strip()
    print '%s\tG\t%s' % (citing, citeds)
    if len(citeds) > 0:
        cs = citeds.split(',')
        count = len(cs)
        for cited in cs:
            print '%s\tP\t%s' % (cited, float(pr)/count)
