import numpy as np
import matplotlib.pyplot as plt
import pickle

from cifar10utils import *

X, labels = get_cifar10_train('cifar-10-batches-py')
print(X.shape)
print(X.dtype)
print(labels.shape)
print(labels.dtype)

Xte, labelste = get_cifar10_test('cifar-10-batches-py')
print(Xte.shape)
print(Xte.dtype)
print(labelste.shape)
print(labelste.dtype)

##display the first of the train images
im = X[0,:] 
print(im.shape)
im = np.dstack((im[:1024].reshape((32,32)),im[1024:2048].reshape((32,32)),im[2048:].reshape((32,32))))
print(im.shape)
#print(im)
plt.imshow(im)



#generate a display of several first images
rows = 12
cols = 20
counter = 0

images = None

for i in range(rows):
    current_row = None
    for j in range(cols):
        im = X[counter,:]
        im = np.dstack((im[:1024].reshape((32,32)),im[1024:2048].reshape((32,32)),im[2048:].reshape((32,32))))
        counter = counter + 1
        if current_row is None:
            current_row = im
        else:
            current_row = np.hstack((current_row, im))
    if images is None:
        images = current_row
    else:
        images = np.vstack((images, current_row))
        
plt.figure()
plt.imshow(images)

plt.show()