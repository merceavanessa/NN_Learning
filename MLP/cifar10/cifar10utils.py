import numpy as np
import matplotlib.pyplot as plt
import pickle

def load_batch(path):
    with open(path, 'rb') as f:
        d = pickle.load(f, encoding='bytes')
    return d

def get_cifar10_train(path):
    temp = load_batch(path+'\\'+'data_batch_1')
    data = temp[b'data']
    labels = np.array(temp[b'labels'])
    temp = load_batch(path+'\\'+'data_batch_2')
    data = np.vstack((data,temp[b'data']))
    labels = np.append(labels, temp[b'labels'])
    temp = load_batch(path+'\\'+'data_batch_3')
    data = np.vstack((data,temp[b'data']))
    labels = np.append(labels, temp[b'labels'])
    temp = load_batch(path+'\\'+'data_batch_4')
    data = np.vstack((data,temp[b'data']))
    labels = np.append(labels, temp[b'labels'])
    temp = load_batch(path+'\\'+'data_batch_5')
    data = np.vstack((data,temp[b'data']))
    labels = np.append(labels, temp[b'labels'])    
    return data, labels

def get_cifar10_test(path):
    temp = load_batch(path+'\\'+'test_batch')
    data = temp[b'data']
    labels = np.array(temp[b'labels'])
    return data, labels

