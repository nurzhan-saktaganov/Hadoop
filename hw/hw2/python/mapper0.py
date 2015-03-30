#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

for line in sys.stdin:
    citing, cited = line.strip().split(',')
    if citing.isdigit():
        print '%s\t%s' % (citing, cited)
