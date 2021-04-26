# -*- coding: utf-8 -*-
"""
Created on Thu Jan 18 13:58:54 2018

@author: Vanessa
"""

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
        self.eta = 0.01
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
            print self.MSE
            
    def Print(self):
        print('hweights:\n',self.hweights)
        print('hbiases:\n',self.hbiases)
        print('outweights:\n', self.outweights)
        print('outbiases:\n',self.outbiases)        
        if self.houtputs is not None:
            print('houtputs:\n',self.houtputs)
        if self.netoutputs is not None:
            print('netoutputs:\n',self.netoutputs)
            

#train data
print('reading train data...')
X = np.loadtxt('mnist_train.csv', delimiter=',')
labels = X[:,0].astype('int32') #the first column contains class labels
X = X[:,1:] #the rest of the columns are pixel values
X = X/255.0 #simple cormalization
print(X.shape)
print(labels.shape)
print()

#test data
print('reading test data...')
Xte = np.loadtxt('mnist_test.csv', delimiter=',')
labelste = Xte[:,0].astype('int32') #the first column contains class labels
Xte = Xte[:,1:] #the rest of the columns are pixel values
Xte = Xte/255.0 #simple cormalization
print(Xte.shape)
print(labelste.shape)
print()

net = MLPNN(784, 4, 10)
net.Learn(X, labels)
net.Print()

# mlp testing
nr_correct = 0
net.Forward(labelste)
for i in range(np.shape(Xte)[0]):
  if (labelste[i] == net.netoutputs[i]):
          nr_correct += 1
          
print nr_correct
print '\n%d %% correct' % ((nr_correct * 100)/np.shape(Xte)[0])

file.close()

