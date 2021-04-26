import numpy as np
import matplotlib.pyplot as plt

data = np.loadtxt('mnist_test.csv', delimiter=',')

print(data.shape)
print(type(data))
print(data.dtype)

#display the first of the images
im = data[0,1:] #the first column contains class label
print(im.shape)
im.shape = (28,28)
print(im.shape)
#print(im)
plt.imshow(im)



#generate a display of several first images
rows = 8
cols = 10
counter = 0

images = None

for i in range(rows):
    current_row = None
    for j in range(cols):
        im = data[counter,1:].reshape(28,28)
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