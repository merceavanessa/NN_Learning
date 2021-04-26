# -*- coding: utf-8 -*-
"""
Created on Wed Nov 08 12:25:53 2017

@author: Vanessa
"""
import random
import math
import csv

MIN_TEMPERATURE_POINT = 2e-300
CORRECT_ANSWER = 12.521

left_range = 0
right_range = 40

def equal_3decimals(x, y):
    return round(x, 3) == y

def f(x):
    return (math.sin(0.15 * x) + math.cos(x))

def schedule(b, x):
    return b * x

def compute_range(alpha, value):
    global left_range, right_range
    if ((value - ((alpha / 2) * 40)) < 0):
        left_range = 0
    else:
        left_range = value - ((alpha / 2) * 40)
        
    if ((value + ((alpha / 2) * 40)) > 40):
        right_range = 40
    else:
        right_range = value + ((alpha / 2) * 40)
                
def simulated_annealing(alpha, beta, T):
    current_value = random.uniform(0, 40)
    while (T > MIN_TEMPERATURE_POINT):
        T = schedule(beta,T) 
        for i in range(15):
            compute_range(alpha, current_value)
            next_value = random.uniform(left_range, right_range)
            Df = f(next_value) - f(current_value)
            if (Df > 0):
                current_value = next_value
            else:
                if (math.exp(Df / T)  > random.random()):
                    current_value = next_value
    return current_value

def get_statistics():
    nr_correct = 0
    with open('result.csv', 'wb') as csvfile:
        writer = csv.writer(csvfile, delimiter=' ', quotechar='|', quoting=csv.QUOTE_MINIMAL)
        writer.writerow(['Nr.'] + ['x'] + ['f(x)'])
        for i in range(100):
            computed_answer = simulated_annealing(0.35, 0.95, 10)
            if (equal_3decimals(computed_answer, CORRECT_ANSWER)):
                nr_correct += 1
                writer.writerow([i] + ["%.3f" % computed_answer] + [ "%.3f" % f(computed_answer)] + ['correct'])
            else:
                writer.writerow([i] + ["%.3f" % computed_answer] + [ "%.3f" % f(computed_answer)] + ['incorrect'])
    return "%d %% correct" % (nr_correct)

print get_statistics()