import sqlite3 as lite
import csv
import re
import pandas as pd

def get_schema_name(file_name):
	""" returns the corresponding schema name, given file name """
	if file_name == 'actors.csv':
		return 'Actors'
	elif file_name == 'cast.csv':
		return 'Cast'
	elif file_name == 'directors.csv':
		return 'Directors'
	elif file_name == 'movie_dir.csv':
		return 'Movie_Director'
	elif file_name == 'movies.csv':
		return 'Movies'

def get_attr_name(file_name):
	""" returns the corresponding attribute names, given file name """
	if file_name == 'actors.csv':
		return ['aid', 'fname', 'lname', 'gender']
	elif file_name == 'cast.csv':
		return ['aid', 'mid', 'role']
	elif file_name == 'directors.csv':
		return ['did', 'fname', 'lname']
	elif file_name == 'movie_dir.csv':
		return ['did', 'mid']
	elif file_name == 'movies.csv':
		return ['mid', 'title', 'year', 'rank']

con = lite.connect('cs1656.sqlite')

with con:
	cur = con.cursor() 

	########################################################################		
	### CREATE TABLES ######################################################
	########################################################################		
	# DO NOT MODIFY - START 
	cur.execute('DROP TABLE IF EXISTS Actors')
	cur.execute("CREATE TABLE Actors(aid INT, fname TEXT, lname TEXT, gender CHAR(6), PRIMARY KEY(aid))")

	cur.execute('DROP TABLE IF EXISTS Movies')
	cur.execute("CREATE TABLE Movies(mid INT, title TEXT, year INT, rank REAL, PRIMARY KEY(mid))")

	cur.execute('DROP TABLE IF EXISTS Directors')
	cur.execute("CREATE TABLE Directors(did INT, fname TEXT, lname TEXT, PRIMARY KEY(did))")

	cur.execute('DROP TABLE IF EXISTS Cast')
	cur.execute("CREATE TABLE Cast(aid INT, mid INT, role TEXT)")

	cur.execute('DROP TABLE IF EXISTS Movie_Director')
	cur.execute("CREATE TABLE Movie_Director(did INT, mid INT)")
	# DO NOT MODIFY - END

	########################################################################		
	### READ DATA FROM FILES ###############################################
	########################################################################		
	# actors.csv, cast.csv, directors.csv, movie_dir.csv, movies.csv
	# UPDATE THIS

	for f in ['movies.csv', 'actors.csv', 'cast.csv', 'directors.csv', 'movie_dir.csv']:
		df = pd.read_csv(f, names = get_attr_name(f))
		df.to_sql(get_schema_name(f), con, if_exists='append', index=False)
		



	########################################################################		
	### INSERT DATA INTO DATABASE ##########################################
	########################################################################		
	# UPDATE THIS TO WORK WITH DATA READ IN FROM CSV FILES
	#cur.execute("INSERT INTO Actors VALUES(1001, 'Harrison', 'Ford', 'Male')") 
	#cur.execute("INSERT INTO Actors VALUES(1002, 'Daisy', 'Ridley', 'Female')")   

	#cur.execute("INSERT INTO Movies VALUES(101, 'Star Wars VII: The Force Awakens', 2015, 8.2)") 
	#cur.execute("INSERT INTO Movies VALUES(102, 'Rogue One: A Star Wars Story', 2016, 8.0)")
	
	#cur.execute("INSERT INTO Cast VALUES(1001, 101, 'Han Solo')")  
	#cur.execute("INSERT INTO Cast VALUES(1002, 101, 'Rey')")  

	#cur.execute("INSERT INTO Directors VALUES(5000, 'J.J.', 'Abrams')")  
	
	#cur.execute("INSERT INTO Movie_Director VALUES(5000, 101)")  

	#con.commit()
    
    	

	########################################################################		
	### QUERY SECTION ######################################################
	########################################################################		
	queries = {}

	# DO NOT MODIFY - START 	
	# DEBUG: all_movies ########################
	queries['all_movies'] = '''
		SELECT * FROM Movies
		'''	
	# DEBUG: all_actors ########################
	queries['all_actors'] = '''
		SELECT * FROM Actors
		'''	
	# DEBUG: all_cast ########################
	queries['all_cast'] = '''
		SELECT * FROM Cast
		'''	
	# DEBUG: all_directors ########################
	queries['all_directors'] = '''
		SELECT * FROM Directors
		'''	
	# DEBUG: all_movie_dir ########################
	queries['all_movie_dir'] = '''
		SELECT * FROM Movie_Director
		'''	
	# DO NOT MODIFY - END

	########################################################################		
	### INSERT YOUR QUERIES HERE ###########################################
	########################################################################		
	# NOTE: You are allowed to also include other queries here (e.g., 
	# for creating views), that will be executed in alphabetical order.
	# We will grade your program based on the output files q01.csv, 
	# q02.csv, ..., q12.csv

	# Q01 ########################
	# List all the actors (first and last name) who acted in at least one film 
	# in the 80s (1980-1990, both ends inclusive) and in at least one film in 
	# the 21st century (>=2000). Sort alphabetically, by the actor's last and 
	# first name.		
	queries['q01'] = ''' 
		SELECT a.fname, a.lname
		FROM Cast
			NATURAL JOIN Movies AS m
			NATURAL JOIN Actors AS a
		GROUP BY a.fname, a.lname
		HAVING SUM(m.year >= 2000) > 0 AND SUM(m.year BETWEEN 1980 AND 1990) > 0
		ORDER BY a.lname, a.fname
		'''	
	


	# Q02 ########################	
	# List all the movies (title, year) that were released in the same year as
	# the movie entitled "Rogue One: A Star Wars Story", but had a better rank
	# (Note: the higher the value in the rank attribute, the better the rank 
	# of the movie). Sort alphabetically, by movie title.	
	queries['q02'] = '''
		SELECT m.title, m.year
		FROM Movies AS m 
		INNER JOIN (SELECT year, rank 
					FROM Movies 
					WHERE title = "Rogue One: A Star Wars Story") AS s
					ON m.year = s.year
		WHERE m.rank > s.rank
		ORDER BY m.title ASC
		'''	

	# Q03 ########################	
	# List all the actors (first and last name) who played in a Star Wars movie
	# (i.e., title like '%Star Wars%') in decreasing order of how many Star 
	# Wars movies they appeared in. If an actor plays multiple roles in the 
	# same movie, count that still as one movie. If there is a tie, use the 
	# actor's last and first name to generate a full sorted order.	
	queries['q03'] = '''
		SELECT a.fname, a.lname
		FROM Actors AS a 
		NATURAL JOIN (SELECT aid, COUNT(DISTINCT mid) AS appear
					FROM Movies NATURAL JOIN Cast
					WHERE title LIKE '%Star Wars%'
					GROUP BY aid) AS s
		ORDER BY s.appear DESC, a.lname ASC, a.fname ASC
		'''	

	# Q04 ########################		
	# Find the actor(s) (first and last name) who only acted in films released 
	# before 1985. Sort alphabetically, by the actor's last and first name.
	queries['q04'] = '''
		SELECT fname, lname
		FROM Actors NATURAL JOIN Movies NATURAL JOIN Cast
		GROUP BY fname, lname
		HAVING SUM(year < 1985) > 0 AND SUM(year >= 1985) < 1
		ORDER BY lname, fname ASC
		'''	

	# Q05 ########################	
	# List the top 20 directors in descending order of the number of films 
	# they directed (first name, last name, number of films directed). 
	# For simplicity, feel free to ignore ties at the number 20 spot 
	# (i.e., always show up to 20 only).	
	queries['q05'] = '''
		SELECT fname, lname, num_film
		FROM Directors NATURAL JOIN 
			(SELECT did, COUNT(*) AS num_film
			FROM Movie_Director
			GROUP BY did)
		ORDER BY num_film DESC
		LIMIT 20
		'''	

	# Q06 ########################	
	# Find the top 10 movies with the largest cast (title, number of cast 
	# members) in decreasing order. Note: show all movies in case of a tie.	
	queries['q06a'] = '''DROP VIEW IF EXISTS mc'''	
	queries['q06b'] = '''
		CREATE VIEW mc AS
			SELECT mid, COUNT(DISTINCT aid) AS num_cast
			FROM Cast
			GROUP BY mid
		'''	
	queries['q06c'] = '''	
		SELECT title, num_cast
		FROM Movies NATURAL JOIN mc
		WHERE num_cast >= (SELECT num_cast 
							FROM mc 
							ORDER BY num_cast DESC
							LIMIT 10,1)
		ORDER BY num_cast DESC
		'''	


	# Q07 ########################	
	# Find the movie(s) whose cast has more actresses than actors (i.e., 
	# gender=female vs gender=male). Show the title, the number of 
	# actresses, and the number of actors in the results. Sort 
	# alphabetically, by movie title.	
	queries['q07'] = '''
		SELECT title, num_female, num_male
		FROM Movies NATURAL JOIN
			(SELECT mid, 
				SUM(CASE WHEN gender = 'Female' THEN 1 ELSE 0 END) num_female,
				SUM(CASE WHEN gender = 'Male' THEN 1 ELSE 0 END) num_male
			FROM Actors NATURAL JOIN Cast
			GROUP BY mid
			HAVING num_female > num_male)
		ORDER BY title 
		'''	

	# Q08 ########################	
	# Find all the actors who have worked with at least 7 different directors. 
	# Do not consider cases of self-directing (i.e., when the director is also 
	# an actor in a movie), but count all directors in a movie towards the 
	# threshold of 7 directors. Show the actor's first, last name, and the 
	# number of directors he/she has worked with. Sort in decreasing order 
	# of number of directors.	
	queries['q08a'] = ''' DROP VIEW IF EXISTS ad '''
	queries['q08b'] = ''' 
		CREATE VIEW ad AS
			SELECT a.aid, a.fname, a.lname, d.did, d.fname dfname, d.lname dlname
			FROM(SELECT aid, did
				FROM Cast NATURAL JOIN Movie_Director) c,
				Actors a, Directors d
			WHERE a.aid = c.aid AND d.did = c.did
		'''	
	queries['q08c'] = '''
		SELECT fname, lname, num_dir
		FROM(
			SELECT aid, fname, lname, COUNT(DISTINCT did) num_dir
			FROM ad
			WHERE fname||lname <> dfname||dlname
			GROUP BY aid
			HAVING num_dir > 7)
		ORDER BY num_dir DESC
		'''	

	# Q09 ########################		
	# For all actors whose first name starts with an S, count the movies that 
	# he/she appeared in his/her debut year (i.e., year of their first movie). 
	# Show the actor's first and last name, plus the count. Sort by decreasing 
	# order of the count.
	queries['q09a'] = ''' DROP VIEW IF EXISTS debut '''
	queries['q09b'] = '''
		CREATE VIEW debut AS
			SELECT aid, MIN(year) debut_year
			FROM Movies NATURAL JOIN Cast NATURAL JOIN Actors
			WHERE fname LIKE 'S%'
			GROUP BY aid	
		'''	
	queries['q09c'] = '''
		SELECT fname, lname, c
		FROM Actors NATURAL JOIN
			(SELECT a.aid, COUNT(*) c
			FROM Cast a NATURAL JOIN Movies m
				JOIN debut d ON d.aid = a.aid AND m.year = d.debut_year
			GROUP BY a.aid)
		ORDER BY c DESC
		'''

	# Q10 ########################	
	# Find instances of nepotism between actors and directors, i.e., an actor 
	# in a movie and the director having the same last name, but a different 
	# first name. Show the last name and the title of the movie, sorted 
	# alphabetically by last name.	
	queries['q10'] = '''
		SELECT a.lname, m.title
		FROM Cast c
			JOIN Movie_Director md ON c.mid = md.mid
			JOIN Actors a ON a.aid = c.aid
			JOIN Directors d ON d.did = md.did
			JOIN Movies m ON m.mid = c.mid
		WHERE a.lname = d.lname AND a.fname <> d.fname
		ORDER BY a.lname
		'''

	# Q11 ########################
	# The Bacon number of an actor is the length of the shortest path between 
	# the actor and Kevin Bacon in the "co-acting" graph. That is, Kevin Bacon 
	# has Bacon number 0; all actors who acted in the same movie as him have 
	# Bacon number 1; all actors who acted in the same film as some actor with 
	# Bacon number 1 have Bacon number 2, etc. List all actors whose Bacon 
	# number is 2 (first name, last name). You can familiarize yourself with 
	# the concept, by visiting The Oracle of Bacon.		
	queries['q11a'] = ''' DROP VIEW IF EXISTS kb '''
	queries['q11b'] = '''
		CREATE VIEW kb AS
			SELECT aid, mid
			FROM Actors NATURAL JOIN Cast
			WHERE fname = 'Kevin' AND lname = 'Bacon'
		'''	
	queries['q11c'] = ''' DROP VIEW IF EXISTS kb1 '''
	queries['q11d'] = '''
		CREATE VIEW kb1 AS
			SELECT aid, mid
			FROM Cast
			WHERE aid IN(SELECT aid
						FROM Actors NATURAL JOIN Cast
						WHERE mid IN (SELECT mid FROM kb))
		'''
	queries['q11e'] = '''
		SELECT fname, lname
		FROM Actors NATURAL JOIN Cast
		WHERE mid IN (SELECT mid FROM kb1)
			AND aid NOT IN (SELECT aid FROM kb1)
		'''

	# Q12 ########################		
	# Assume that the popularity of an actor is reflected by the average rank 
	# of all the movies he/she has acted in. Find the top 20 most popular 
	# actors (in descreasing order of popularity) -- list the actor's 
	# first/last name, the total number of movies he/she has acted, and his/her 
	# popularity score. For simplicity, feel free to ignore ties at the number 
	# 20 spot (i.e., always show up to 20 only).
	queries['q12'] = '''
		SELECT fname, lname, c, avgrank
		FROM Actors NATURAL JOIN
			(SELECT aid, COUNT(mid) c, AVG(rank) avgrank
			FROM(SELECT aid, mid, rank
				FROM Cast NATURAL JOIN Movies
				GROUP BY aid, mid)
			GROUP BY aid)
		ORDER BY avgrank DESC
		LIMIT 20
		'''


	########################################################################		
	### SAVE RESULTS TO FILES ##############################################
	########################################################################		
	# DO NOT MODIFY - START 	
	for (qkey, qstring) in sorted(queries.items()):
		try:
			cur.execute(qstring)
			all_rows = cur.fetchall()
			
			print ("=========== ",qkey," QUERY ======================")
			print (qstring)
			print ("----------- ",qkey," RESULTS --------------------")
			for row in all_rows:
				print (row)
			print (" ")

			save_to_file = (re.search(r'q0\d', qkey) or re.search(r'q1[012]', qkey))
			if (save_to_file):
				with open(qkey+'.csv', 'w') as f:
					writer = csv.writer(f)
					writer.writerows(all_rows)
					f.close()
				print ("----------- ",qkey+".csv"," *SAVED* ----------------\n")
		
		except lite.Error as e:
			print ("An error occurred:", e.args[0])
	# DO NOT MODIFY - END
	
