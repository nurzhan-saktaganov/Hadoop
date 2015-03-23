#!/usr/bin/env python
import sys

__author__ = 'Nurzhan Saktaganov'

def main():
    previous_year = None
    dic = {}

    for line in sys.stdin:
        current_year, country = line[:-1].split('\t')
        if current_year == previous_year or previous_year == None:
            if country in dic:
                dic[country] += 1
            else:
                dic[country] = 1
        else:
            print_result(previous_year, dic)
            dic = {}
            dic[country] = 1

        previous_year = current_year

    print_result(previous_year, dic)

def print_result(year, dic):
    patents_count = map(int, dic.values())
    patents_count.sort()

    uniq_countries = len(patents_count)
    minimum = patents_count[0]
    maximum = patents_count[uniq_countries - 1]
    mean = 1.0 * sum(patents_count) / uniq_countries

    if uniq_countries % 2 == 0:
        mediana = 1.0 * (patents_count[uniq_countries / 2 - 1] + patents_count[uniq_countries / 2]) / 2
    else:
        mediana = patents_count[uniq_countries / 2]

    deviation = (sum([(x - mean) * (x - mean) for x in patents_count]) / uniq_countries) ** (0.5)

    print '%d\t%d\t%d\t%.2lf\t%d\t%.2lf\t%d' % (int(year), uniq_countries, \
                minimum, mediana, maximum, mean, deviation)

if __name__ == '__main__':
    main()