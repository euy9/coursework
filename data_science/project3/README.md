# Project 3: Association Rule Mining

### Goal

Implement A-Priori Association Rule Mining algorithm

---

### Descriptions

#### Association Rule Mining Algorithm `arma.py`
This program should be called as follows:

`python arma.py input_filename output_filename min_support_percentage min_confidence`

where 
* `input_filename` is the name of the file that contains market basket data
* `output_filename` is the name of the file that will store thre required output. This file should contain frequent item sets and the association rules that you discover after processing the submitted input data
> **Output Format**
>
> - `S, support_percentage, item_1, item_2, item_3, ...` to denote frequent itemsets
> - `R, support_percentage, confidence, item_4, item_5, ..., ’=>’, item_6, item_7, ...` to denote association rules
>   - `support_percentage` should be the support percentage for the frequent itemset or for the association rule
>   - `confidence` should be the confidence percentage for the association rule

* `min_support_percentage` is the minimum support percentage for an itemset/association rule to be considered frequent
* `min_confidence` is the minimum confidence for an association rule to be significant



---
### To Run
`python arma.py input_filename output_filename min_support_percentage min_confidence`
