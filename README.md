# java-filmorate
Template repository for Filmorate project.


![Диаграмма](https://github.com/basatt13/java-filmorate/blob/add-friends-likes/My%20First%20Board.jpg)

Получить все фильмы:

SELECT *
FROM Film

Получить список всех пользователей:cd

SELECT *
FROM User

Получить топ N популярных фильмов:

SELECT f.name
FROM Film AS f
INNER JOIN Likes AS l ON f.film_id=l.film_id
GROUP BY film_id
ORDER BY COUNT(user_id)
LIMIT N;

Получить список общих друзей:

SELECT f.friends_id
FROM(SELECT f.friends_id
FROM User AS u
INNER JOIN Friends AS f ON u.user_id=f.user.id
WHERE u.user_id = 1
UNION ALL
SELECT f.friends_id
FROM User AS u
INNER JOIN Friends AS f ON u.user_id=f.user.id
WHERE u.user_id = 2)
GROUP BY f.friends_id
WHERE COUNT(f.friends_id) > 1;



