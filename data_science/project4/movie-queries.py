"""
	Sol Yun
	University of Pittsburgh - CS1656

	Assignmetn #4: Neo4j / Cypher
	Use Neo4j and Cypher to provide queries on graph database

	To Run: python movie-queries.py
		- download movie data base 'https://neo4j.com/developer/movie-database/'
		- have neo4j server locally running
"""


from neo4j import GraphDatabase, basic_auth
import sys

#connection with authentication
#driver = GraphDatabase.driver("bolt://localhost", auth=basic_auth("neo4j", "cs1656"), encrypted=False)

#connection without authentication
driver = GraphDatabase.driver("bolt://localhost", encrypted=False)
# driver = GraphDatabase.driver("bolt://localhost:7687", auth=("neo4j", "password"))

session = driver.session()
transaction = session.begin_transaction()

# redirect stdout to file
std = sys.stdout
sys.stdout = open('output.txt', 'w', encoding='UTF-8')

################################## QUERIES ####################################
### [Q1] List the first 20 actors in descending order of the number of films 
### they acted in.
### OUTPUT: actor_name, number_of_films_acted_in
print('### Q1 ###')
result = transaction.run("""
	MATCH (a:Actor)-[:ACTS_IN]->(m:Movie) 
	WITH a, COUNT(m) AS acted 
	ORDER BY acted DESC 
	RETURN a.name, acted 
	LIMIT 20
""")
for record in result:
   print ('%s, %d'%(record['a.name'], record['acted']))


### [Q2] List the titles of all movies with a review with at most 3 stars. 
### OUTPUT: movie title
print('\n### Q2 ###')
result = transaction.run("""
	MATCH (u:User)-[r:RATED]->(m:Movie)
	WHERE r.stars <= 3
	RETURN m.title
""")
for record in result:
   print ('%s'%(record['m.title']))


### [Q3] Find the movie with the largest cast, out of the list of movies that 
### have a review.
### OUTPUT: movie_title, number_of_cast_members
print('\n### Q3 ###')
result = transaction.run("""
	MATCH ()-[r:RATED]->(m:Movie)<-[:ACTS_IN]-(a:Actor)
	WITH m, COUNT(a) AS cast_size
	ORDER BY cast_size DESC
	LIMIT 1
	RETURN m.title, cast_size
""")
for record in result:
   print ('%s, %d'%(record['m.title'], record['cast_size']))


### [Q4] Find all the actors who have worked with at least 3 different directors 
### (regardless of how many movies they acted in). For example, 3 movies with 
### one director each would satisfy this (provided the directors where different), 
### but also a single movie with 3 directors would satisfy it as well. 
### OUTPUT: actor_name, number_of_directors_he/she_has_worked_with
print('\n### Q4 ###')
result = transaction.run("""
	MATCH (d:Director)-[:DIRECTED]->(m:Movie)<-[:ACTS_IN]-(a:Actor)
	WITH a, COUNT(DISTINCT d) AS num_d
	WHERE num_d >=3
	RETURN a.name, num_d
""")
for record in result:
   print ('%s, %d'%(record['a.name'], record['num_d']))


### [Q5] The Bacon number of an actor is the length of the shortest path between 
### the actor and Kevin Bacon in the "co-acting" graph. That is, Kevin Bacon has 
### Bacon number 0; all actors who acted in the same movie as him have Bacon 
### number 1; all actors who acted in the same film as some actor with Bacon 
### number 1 have Bacon number 2, etc. List all actors whose Bacon number is 
### exactly 2 (first name, last name).
### OUTPUT: actor_name
print('\n### Q5 ###')
result = transaction.run("""
	MATCH (kb:Actor {name: 'Kevin Bacon'})-[:ACTS_IN*4]-(a:Actor)
	WHERE NOT (kb)-[:ACTS_IN]->()<-[:ACTS_IN]-(a) AND kb <> a
	RETURN DISTINCT a.name
""")
for record in result:
   print ('%s'%(record['a.name']))


### [Q6] List which genres have movies where Tom Hanks starred in.
### OUTPUT: genre
print('\n### Q6 ###')
result = transaction.run("""
	MATCH (tom:Actor)-[:ACTS_IN]->(m:Movie)
	RETURN DISTINCT m.genre
""")
for record in result:
   print ('%s'%(record['m.genre']))


### [Q7] Show which directors have directed movies in at least 2 different genres.
### OUTPUT: director name, number of genres
print('\n### Q7 ###')
result = transaction.run("""
	MATCH (d:Director)-[:DIRECTED]->(m:Movie)
	WITH d, COUNT(DISTINCT m.genre) AS num_genre
	WHERE num_genre >= 2
	RETURN d.name, num_genre
""")
for record in result:
   print ('%s, %d'%(record['d.name'], record['num_genre']))


### [Q8] Show the top 5 pairs of actor, director combinations, in descending 
### order of frequency of occurrence.
### OUTPUT: director's name, actors' name, number of times director directed 
### said actor in a movie
print('\n### Q8 ###')
result = transaction.run("""
	MATCH (d:Director)-[:DIRECTED]->(m:Movie)<-[:ACTS_IN]-(a:Actor)
	WITH d, a, COUNT(m) AS num
	ORDER BY num DESC
	LIMIT 5
	RETURN d.name, a.name, num
""")
for record in result:
   print ('%s, %s, %d'%(record['d.name'], record['a.name'], record['num']))



sys.stdout = std
transaction.close()
session.close()