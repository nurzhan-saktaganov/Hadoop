#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys

__author__ = 'Nurzhan Saktaganov'

D = 0.85

def main():
    previous_nid, adjacency_list, page_rank = None, [], 0.0

    for line in sys.stdin:
        line = line[:-1].split('\t')
        nid = line[0]

        if nid != previous_nid and not previous_nid == None:
            print_result(previous_nid, (1-D) + D * page_rank, adjacency_list)
            adjacency_list = []
            page_rank = 0.0

        if len(line) == 3:
            adjacency_list = line[2][1:-1].split(',')
        elif len(line) == 2:
            page_rank += float(line[1])

        previous_nid = nid

    print_result(previous_nid,  (1-D) + D * page_rank, adjacency_list)

def print_result(nid, page_rank, adjacency_list):
    print '%s\t%s\t[%s]' % (nid, page_rank, ','.join(adjacency_list))

if __name__ == '__main__':
    main()
