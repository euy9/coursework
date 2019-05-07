# Project 4: Neo4j / Cypher

### Goal

Use Neo4j and its query language, Cypher, in graph databases

---

### Descriptions

#### Database Model
Use the Movies database https://neo4j.com/developer/movie-database/

#### Queries

* **[Q1]** List the first 20 actors in descending order of the number of films they acted in.  
*OUTPUT*: actor_name, number_of_films_acted_in

* **[Q2]** List the titles of all movies with a review with at most 3 stars.
*OUTPUT*: movie title

* **[Q3]** Find the movie with the largest cast, out of the list of movies that have a review.   
*OUTPUT*: movie_title, number_of_cast_members

* **[Q4]** Find all the actors who have worked with at least 3 different directors (regardless of how many movies they acted in). For example, 3 movies with one director each would satisfy this (provided the directors where different), but also a single movie with 3 directors would satisfy it as well.
*OUTPUT*: actor_name, number_of_directors_he/she_has_worked_with

* **[Q5]** The Bacon number of an actor is the length of the shortest path between the actor and Kevin Bacon in the *"co-acting"* graph. That is, Kevin Bacon has Bacon number 0; all actors who acted in the same movie as him have Bacon number 1; all actors who acted in the same film as some actor with Bacon number 1 have Bacon number 2, etc. *List all actors whose Bacon number is exactly 2* (first name, last name). You can familiarize yourself with the concept, by visiting [The Oracle of Bacon](https://oracleofbacon.org).  
*OUTPUT*: actor_name

* **[Q6]** List which genres have movies where Tom Hanks starred in.  
*OUTPUT*: genre

* **[Q7]** Show which directors have directed movies in at least 2 different genres.  
*OUTPUT*: director name, number of genres

* **[Q8]** Show the top 5 pairs of actor, director combinations, in descending order of frequency of occurrence.   
*OUTPUT*: director's name, actors' name, number of times director directed said actor in a movie



---
### To Run
`python movie-queries.py` 
