# Sol Yun
# euy9@pitt.edu
# pine-index.py reads all text files in the subdirectory input/
# and construct an inverted index

import os 
import string
import nltk
import json
from collections import defaultdict

# constants
TOTAL_DOC_PER_WORD = 'Total Number of Documents per Word'
TOTAL_DOC = 'Total Number of Documents'

### read all files in subdirectly input/
input_folder = os.path.join(os.getcwd(), 'input' )
docs = os.listdir(input_folder)

### preprocess words & construct an inverted index
inv_ind = defaultdict(lambda: defaultdict(int))		# inverted index data structure

# loop through docs and loop through each line
for i, doc in enumerate(docs):
	with open(os.path.join(input_folder, doc)) as f:
		for line in f:
            # for each line, eliminate punctuation and convert to lower case
			line = line.translate({ord(c): None for c in \
						  string.punctuation + string.digits})
			tokens = nltk.word_tokenize(line.lower())
			porter = nltk.PorterStemmer()	# stemming
			for token in tokens:
				word = porter.stem(token)
				word_dict = inv_ind[word]	# dict inside dict
				word_dict[doc] += 1		# count num of words per doc

				# count total num of doc the word appears in
				if TOTAL_DOC_PER_WORD not in word_dict:
					word_dict[TOTAL_DOC_PER_WORD] = len(word_dict)
				else:
					word_dict[TOTAL_DOC_PER_WORD] = len(word_dict) -1
				

inv_ind[TOTAL_DOC] = len(docs)	# store tot num of documents
with open('inverted-index.json', 'w') as fp:
	json.dump(inv_ind, fp, indent = 4)


