# -*- coding: utf-8 -*-
import numpy as np
import itertools 

inf = np.inf
cities = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']

distances = [[inf, 5, 4, 7, 6, 5, 7, 4, 2, 9], 
            [6, inf, 5, 6, 4, 8, 5, 4, 3, 8],
            [3, 5, inf, 3, 5, 6, 9, 8, 7, 6],
            [7, 5, 4, inf, 3, 5, 7, 9, 8, 3],
            [5, 4, 5, 3, inf, 4, 6, 7, 8, 7],
            [5, 6, 5, 5, 4, inf, 5, 4, 3, 2], 
            [6, 7, 9, 7, 6, 6, inf, 5, 7, 9],
            [5, 4, 8, 7, 6, 4, 4, inf, 6, 5],
            [2, 3, 6, 9, 8, 3, 7, 5, inf, 7],
            [9, 7, 5, 4, 8, 3, 9, 5, 7, inf]]

def distance_of_path(path):
    d = 0
    for j in range(9):
        curr_city_index = cities.index(path[j])
        next_city_index = cities.index(path[j+1])
        d += distances[curr_city_index][next_city_index]
        
    index = cities.index(path[9])
    index2 = cities.index(path[0])
    d += distances[index][index2]
    return d

def get_min_path_by_permutations(cities):
    l = list(itertools.permutations(cities))
    min_distance = np.inf
    
    for i in range(len(l)):
      
        current_distance = distance_of_path(l[i])
        
        if (current_distance < min_distance):
            min_path = l[i]
            min_distance = current_distance
    return min_path, min_distance

print get_min_path_by_permutations(cities)