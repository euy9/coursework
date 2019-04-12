"""
	Sol Yun
	University of Pittsburgh - CS1656

	Project 3: Association Rule Mining
	- Implement a simplified version of the A-Priori association rule mining 

	To Run:
		`python arma.py input_filename output_filename min_supp min_conf`
"""
import sys
import csv
import pandas as pd
import itertools

######################### Functions ##########################
def transform_to_binary(unique_items, item_list):
	""" transform transactions into binary encoding 
		unique_items: list of unique items
		item_list: list of transactions that include items

		return: list of binary encoding
	"""
	ret = []
	for line in item_list:
		ret.append([1 if item in line else 0 for item in unique_items])
	return ret

def generate_new_itemsets(old, size):
	""" generate candidate frequent itemsets of size, given verified frequent 
		itemsets of size-1
		old: set of verified frequent items of size -1
		size: (int) number of items in each itemset

		return: list of candidate frequent itemsets
	"""
	if size == 2:
		new = [frozenset.union(*x)  for x in itertools.combinations(old,size)]
	elif size > 2:
		new = []
		for a in old:
			for b in old:
				if a != b:
					c = a.intersection(b)
					if len(c) == size-2:
						new.append(a.union(b))
	return new


#################### Read arguments #####################

[name, input_file_name, output_file_name, min_supp, min_conf] = sys.argv
min_supp, min_conf = float(min_supp), float(min_conf)

## read input_file & convert to pandas dataframe with binary flag
## input_file format: `transaction_id, item_1, item_2, ...`
unique_items = set()
item_list = []
with open(input_file_name) as input:
	for line in input:
		line = line.rstrip().split(',')[1:]
		item_list.append(line)
		unique_items = unique_items.union(set(line))

unique_items = sorted(unique_items)
df = pd.DataFrame(transform_to_binary(unique_items, item_list), 
				  columns = unique_items)


#################### A-Priori Algorithm #####################
## generate VFI[i], the verified frequent itemsets of size i
# for size 1
tot_tran = df.values.shape[0]	# total number of transactions
max_len = df.values.shape[1]	# maximum num of items in an itemset
size = 1
itemset = [{x} for x in range(df.values.shape[1])]
supp = df.sum(axis = 0).values / tot_tran

# only include if greater than min support percentage
verified = set()
supp_dict = {}
for p, i in zip(supp, itemset):
	if p >= min_supp:
		supp_dict[frozenset(i)] = p
		verified.add((frozenset(i)))
VFI = {size: verified}	# verified frequent itemsets

# generate plausible candidate itemsets of size i + 1
while size <= max_len:
	size += 1
	itemset = generate_new_itemsets(VFI[size-1], size)
	if len(itemset) == 0:	# stop when there's no candidate itemset
		break
	verified = set()

	# calculate support percentage for the new itemset
	for s in itemset:
		supp = df.values[:, list(s)].all(axis=1).sum() / tot_tran
		if supp >= min_supp:
			supp_dict[frozenset(s)] = supp
			verified.add(frozenset(s))
	VFI[size] = verified


#################### Association Rules #####################
## generate all possible associate rules from VFI
VAR = []	# verified association rules
for size, vfi in VFI.items():
	if size == 1: continue		# skip frequent items of size 1
	for itemset in vfi:
		# find all subsets of each itemset
		subsets = itertools.chain(*map(lambda x: 
						itertools.combinations(itemset, x), range(size)))
		itemset_supp = supp_dict[itemset]
		for ss in subsets:
			if len(ss) != 0:	# skip all empty subsets
				conf = itemset_supp / supp_dict[frozenset(ss)]
				if conf >= min_conf:	# if greater or equal to min conf
					VAR.append([itemset_supp, conf, ss, itemset.difference(ss)])


#################### Print Output #####################
## format for frequent itemsets: S, supp, item1, item2, ...
## format for association rule: R, supp, conf, item1, ..., '=>', item3, ...
out = open(output_file_name, 'w')

# print all frequent itemsets
for itemsets_by_size in VFI.values():
	for itemset in itemsets_by_size:
		# get item names
		items = ','.join(map(unique_items.__getitem__, sorted(list(itemset))))
		out.write("S,%.4f,%s\n" %(supp_dict[itemset], items))

# print all association rules
for i in VAR:
	# get item names of antecedents & consequents of the associatoin rule
	ante = ','.join(map(unique_items.__getitem__, sorted(list(i[2]))))
	conse = ','.join(map(unique_items.__getitem__, sorted(list(i[3]))))
	out.write("R,%.4f,%.4f,%s,'=>',%s\n" %(i[0], i[1], ante, conse))

# close output file
out.close()

	