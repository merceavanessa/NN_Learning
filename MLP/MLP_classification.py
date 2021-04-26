#!/usr/bin/env python
import numpy as np
import matplotlib.cm as cm
import matplotlib.mlab as mlab
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import random

def sigmoid(x):
    beta = 1.0
    return 1.0/(1.0 + np.exp(-beta*x))

class MLPNN:
    def __init__(self, inputs_num, hidden_num, output_num):
        
        self.outweights = np.random.rand(output_num, hidden_num)
        self.outbiases =  np.full((output_num), 1, dtype = float)
        self.hweights = np.random.rand(hidden_num, inputs_num)
        self.hbiases = np.full((hidden_num), 1, dtype = float)
        self.houtputs = None
        self.netoutputs = None
        self.MSE = 0
        self.epoch = 10000
        self.eta = 0.05
        self.hidden_num = hidden_num
        self.inputs_num = inputs_num
        self.output_num = output_num
        
    def Forward(self, inputs):
        
        self.houtputs = np.empty((inputs.shape[0],self.hweights.shape[0]), dtype = float)
        self.netoutputs = np.empty((inputs.shape[0],self.outweights.shape[0]), dtype = float)
       
        for f in range(len(inputs)):
            x = inputs[f]
            u = np.dot(self.hweights, x) + self.hbiases
            h = sigmoid(u)
            self.houtputs[f] = h;
            self.netoutputs[f] = np.dot(self.outweights, h) + self.outbiases
          
    def GetOutputs(self):
        return self.netoutputs
    
    def Learn(self, inputs, outputs):
        
        for e in range(self.epoch):
            self.MSE = 0
            for f in range(len(inputs)):
                
                x = inputs[f]
                u = np.dot(self.hweights, x) + self.hbiases
                h = sigmoid(u)
                y = np.dot(self.outweights, h) + self.outbiases
                
        #       set the expected class value as 1 for the proper output neuron
                class_value = np.full((y.shape[0]), 0)
                class_value[int(outputs[f])] = 1
                
        #       compute se
                SE = 0
                for i in range(self.output_num):
                      SE += np.square(y[i] - class_value[i])
                self.MSE += SE
                
        #       update layer 2
                for i in range (self.output_num):
                    for j in range (self.hidden_num):
                        self.outweights[i][j] += self.eta * (class_value[i] - y[i]) * h[j]
                    self.outbiases[i] += self.eta * (class_value[i] - y[i]) * 1
                
        #       update layer 1
                for t in range (self.hidden_num):
                    for g in range(self.output_num):
                        gradient = 0
                        for i in range(self.output_num):
                            gradient += (class_value[i] - y[i]) * self.outweights[i][t] 
                        self.hweights[t][g] += self.eta * gradient * (1 * h[t] * (1 - h[t])) * x[g]
                    self.hbiases[t] += self.eta * gradient * (1 * h[t] * (1 - h[t])) * 1
#           update MSE
            self.MSE /= len(inputs)
        
    def Print(self):
        print('hweights:\n',self.hweights)
        print('hbiases:\n',self.hbiases)
        print('outweights:\n', self.outweights)
        print('outbiases:\n',self.outbiases)        
        if self.houtputs is not None:
            print('houtputs:\n',self.houtputs)
        if self.netoutputs is not None:
            print('netoutputs:\n',self.netoutputs)
            

dataset = []
file = open("iris.csv", "r+");
for line in file:
   row = line.split()
   row[4] = ["Iris-setosa", "Iris-versicolor", "Iris-virginica"].index(row[4])
   row[:4] = [float(row[j]) for j in xrange(len(row))]
   dataset.append(row)
file.close()

random.shuffle(dataset)
# 70% of the input for training
training_data = dataset[:int(len(dataset) * 0.7)] 
# 30% of the input for testing
testing_data = dataset[int(len(dataset) * 0.7):] 

training_x = np.array([data[:4] for data in training_data])
training_y = np.array([data[4] for data in training_data])

testing_x = np.array([data[:4] for data in testing_data])
testing_y = np.array([data[4] for data in testing_data])     


net = MLPNN(4, 4, 3)
net.Learn(training_x, training_y)
net.Print()

# mlp testing
nr_correct = 0
net.Forward(testing_x)
for j in range(len(testing_x)):
    max = -1
    for i in range(3):
        if (net.netoutputs[j][i] > max):
           max = net.netoutputs[j][i] 
           index = i
    if ((index == testing_y[j]) and (round(max) == 1)):
          nr_correct += 1
         
print '\n%d %% correct' % ((nr_correct * 100)/len(testing_data))

file.close()

