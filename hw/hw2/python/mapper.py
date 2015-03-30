#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys

__author__ = 'Nurzhan Saktaganov'

def main():
    for line in sys.stdin:
        # send structure
        print line[:-1]

        nid, page_rank, adjacency_list = line[:-1].split('\t')

        if adjacency_list == '[]':
            continue
        
        adjacency_list = adjacency_list[1:-1].split(',')

        p = float(page_rank) / len(adjacency_list)

        for nodeid in adjacency_list:
            print '%s\t%f' % (nodeid, p)

if __name__ == '__main__':
    main()
