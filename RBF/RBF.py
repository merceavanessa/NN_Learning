#!/usr/bin/env python
import numpy as np
import random

class RBFNN:
    def __init__(self, radial_num, output_num):
        
        self.weights = np.random.rand(output_num, radial_num)
        self.biases = np.ones(output_num)
        self.netoutputs = None
        self.MSE = 0
        self.eta = 0.1
    
    def Forward(self, inputs):
        self.netoutputs = np.empty((inputs.shape[0], 3))
        
        for f in range(inputs.shape[0]):
            x = inputs[f] 
            for i in range(3):
                w = self.weights[i]
                y = np.dot(w, x) + self.biases[i]
                self.netoutputs[f][i] = y

    def GetOutputs(self):
        return self.netoutputs
    
    def Learn(self, inputs, outputs):
        SE = 0 # squared error for each set of training examples
        self.MSE = 0 # mean squared error for each set of training examples
        prevMSE = 0 # previous mean squared error
        first_time = True

        while (first_time or (np.abs(self.MSE - prevMSE) > 0.0001)):
            SE = 0
            prevMSE = self.MSE
            self.MSE = 0
            for f in range(inputs.shape[0]):
                x = inputs[f]
#               set the expected class value as 1 for the proper output neuron
                class_value = np.full((3), 0)
                class_value[int(outputs[f])] = 1
#               compute se
                for i in range(3): 
                    d = class_value[i]
                    w = self.weights[i]
                    y = np.dot(w, x) + self.biases[i]
                    for j in range(len(x)):
                        self.weights[i][j] = self.weights[i][j] + self.eta * (d - y) * x[j]
                    self.biases[i] = self.biases[i] + self.eta * (d - y) * 1 
                    SE += (y - d)**2         
            self.MSE =  SE / inputs.shape[0]
            first_time = False
        print self.MSE
            
    def Print(self):
        print('weights:\n', self.weights)    
        if self.netoutputs is not None:
            print('netoutputs:\n',self.netoutputs)
            

dataset = []
file = open("iris.csv", "r+");
for line in file:
   row = line.split()
   row[4] = ["Iris-setosa", "Iris-versicolor", "Iris-virginica"].index(row[4])
   row[:4] = [float(row[j]) for j in xrange(len(row))]
   dataset.append(row[:5])
file.close()
random.shuffle(dataset)
datasetSize = len(dataset)

class_values = np.empty((datasetSize))
for i in range(datasetSize):
    class_values[i] = dataset[i][4]
    
# PLACING THE RADIAL POINTS
radials = np.empty((datasetSize / 10, datasetSize))
radialNumber = 0
for f in range(datasetSize):
      if f % 10 == 0:  # radial here
          R = []
          for i in range(datasetSize):
                u = np.array(dataset[i][:4]) # current example
                sigma = 1; # the radius
                x = np.array(dataset[f][:4]) # the data at the position for placing the radial points
                v = np.exp(-np.sum((u - x) * (u - x)) / sigma**2) 
                R.append(v)
          radials[radialNumber] = R
          radialNumber += 1
radials = radials.T

training_x = np.empty((int(datasetSize * 0.7), radialNumber))
training_y = np.empty((int(datasetSize * 0.7)))
# 70% of the input for training; last column of the input is the bias vector
for i in range (0, training_x.shape[0]):
    for j in range (radialNumber):
        training_x[i][j] = radials[i][j]
    training_y[i] = class_values[i]

testing_x = np.empty ((int(datasetSize * 0.3), radialNumber))
testing_y = np.empty ((int(datasetSize * 0.3)))
# 30% of the input for testing
k = 0
for i in range (training_x.shape[0], datasetSize):
    for j in range(radialNumber):
        testing_x[k][j] = radials[i][j]
    testing_y[k] = class_values[k]
    k += 1

net = RBFNN(radialNumber, 3)
net.Learn(training_x, training_y)
net.Forward(testing_x)
#net.Print()

# testing
nr_correct = 0
index = 0
for j in range(len(testing_x)):
    max = -1
    for i in range(3):
        if (net.netoutputs[j][i] > max):
           max = net.netoutputs[j][i] 
           index = i
    if ((index == testing_y[j])): 
          nr_correct += 1
         
print '\n%d %% correct' % ((nr_correct * 100)/(testing_x.shape[0] - 1))

file.close()

