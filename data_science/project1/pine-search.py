# Sol Yun
# euy9@pitt.edu
# pine-search.py reads inverted-index.json and keywords.txt then
# produce an ordered list of document filenames, along with their
# relevance scores and breakdown of weights for all keywords

import json
import os
from math import log2
import nltk
from collections import defaultdict
import pandas as pd


# constants
TOTAL_DOC_PER_WORD = 'Total Number of Documents per Word'
TOTAL_DOC = 'Total Number of Documents'
SCORE = 'Score'
RANK = 'Rank'
INDEX ='index'

def find_weight(freq, n_doc, N):
	""" calculate weight of pair (keyword, doc)
		freq: number of frequency of the keyword in a doc
		n_doc: number of docs where the keyword appears
		N: total number of documents
	"""
	return (1 + log2(freq)) * log2(N/n_doc)

def pretty_print(x):
	"""	pretty print to output
		x: a row of dataframe
	"""
	print('[%d] file=%s score=%f' %(x[RANK], x[INDEX], x[SCORE]))
	for idx, val in x.iteritems():
		if idx not in [INDEX, SCORE, RANK]:
			print('    weight(%s)=%f' %(idx, val))
	print('\n')



# load json file
with open('inverted-index.json', 'r') as fp:
	inv_ind = json.load(fp)

# for stemming
porter = nltk.PorterStemmer()

# output
print('Information Retrieval Engine - Eunsol Yun (euy9@pitt.edu)\n')

# open keywords.txt file
keyword_file = os.path.join(os.getcwd(), 'keywords.txt' )
with open(keyword_file, 'r') as f:
	for line in f:
		keywords = line.split()
		output =  defaultdict(lambda: [0]*len(keywords)) # for output
		for i, keyword in enumerate(keywords):
			keyword = porter.stem(keyword)
			N = inv_ind[TOTAL_DOC]	# total number of documents
			info = inv_ind[keyword]
			n_doc = info[TOTAL_DOC_PER_WORD]	# num of docs per word
			for key, val in info.items():
				if key != TOTAL_DOC_PER_WORD:
					weight = find_weight(val, n_doc, N)
					output[key][i] = weight
		
		# calculate relevance score & sort by score, then filename
		df = pd.DataFrame(output)
		df = df.transpose()
		df.columns = keywords
		df[SCORE] = df.sum(axis=1)
		df = df.reset_index()
		df = df.sort_values([SCORE, INDEX], ascending = [0, 1])
		df[RANK] = df[SCORE].rank(method = 'dense', ascending = False).astype(int)
		
		# print output
		print('------------------------------------------------------------')
		print('keywords = %s\n' %(' '.join(keywords)))
		df.apply(pretty_print, axis=1)




