import numpy as np
import cPickle as pickle
from math import sqrt
from pybrain.datasets.supervised import SupervisedDataSet as SDS
from sklearn.metrics import mean_squared_error as MSE
#model_file = 'model-data.pkl'
model_file = '/Users/luhonghai/Desktop/audio/model-data.bak.pkl'
#output_predictions_file = 'predictions.txt'
net = pickle.load( open( model_file, 'rb' ))
p = net.activate([%DATA%])
print p
