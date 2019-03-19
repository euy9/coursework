# Project 1: Information Retrieval Engine

### Goal
Understand information retrieval by building a simple information retrieval system that:
* processes a set of documents,
* creates an inverted index, and
* returns a raked list of document filenames (along with relevance scores and a breakdown of weights for all keywords) that match the given keywords

---
### Descriptions

#### Pitt INformation retrieval Engine Indexer Program `pine-index.py`
This program 
* reads all files that are in subdirectory `input/` (assuming all files in this directory are text files),
* convert all characters to lower case,
* eliminate punctuation,
* eliminante numbers,
* perform stemming using `nltk` stemmer, and
* constructs an inverted index.

The inverted index (a single data structure) contains:
* the total number of documents.
* For every word,
  * a list of documents the word appears in,
  * how many times the word appeared per document, and
  * how many documents the word appears in
  
The program stores the inverted index as a JSON object in a file named `inverted_index.json`.


#### Pitt INformation retrieval Engine Search Program `pine-search.py`
This program
* reads the JSON object from `inverted_index.json`,
* reads a set of keywords from file `keywords.txt`, and
  * `keywords.txt` contains one set of keywords per line
* produce an ordered list of document file names, along with their relevance scores and a breakdown of weights for all keywords
  * __relevance score__ computed as: w(key, doc) = (1 + log2 freq(key,doc)) * log2 (N / n(doc)) 
  * in cases of documents with the same relevance score, order by filename order and use the same rank number

---
### To Run
`python pine-index.py`

`python pine-search.py`
