package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationUserException;
import ru.yandex.practicum.filmorate.filmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.filmStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Integer addLike(Long idFilm, Long idUser) {
        if (!inMemoryFilmStorage.getFilms().containsKey(idFilm)) {
            throw new NotFoundIdException("Некорректные данные:" + idFilm);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(idUser)) {
            throw new NotFoundIdException("Некорректные данные:" + idUser);
        }
        Film film = inMemoryFilmStorage.getFilms().get(idFilm);
        User user = inMemoryUserStorage.getUsers().get(idUser);
        Set<User> list = new HashSet<>();
        if (film.getLikes() == null) {
            list.add(user);
        } else {
            list = film.getLikes();
        }
        film.setLikes(list);
        System.out.println("Количество лайков");
        System.out.println("♡");
        return list.size();
    }

    public Integer removeLike(Long idFilm, Long idUser) {
        if (!inMemoryFilmStorage.getFilms().containsKey(idFilm)) {
            throw new NotFoundIdException("Некорректные данные:" + idFilm);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(idUser)) {
            throw new NotFoundIdException("Некорректные данные:" + idUser);
        }
        Film film = inMemoryFilmStorage.getFilms().get(idFilm);
        User user = inMemoryUserStorage.getUsers().get(idUser);
        Set<User> list = new HashSet<>();
        if (film.getLikes() == null) {
        } else {
            list = film.getLikes();
        }
        list.remove(user);
        film.setLikes(list);
        System.out.println("Количество лайков");
        System.out.println("♡");
        return film.getLikes().size();
    }

    public List<Film> rateOfFilms(Integer count) {
        if (count <= 0) {
            throw new ValidationUserException("Показатель: " + count + " должен быть положительным");
        }
        final Set<User>[] list = new Set[]{new HashSet<>()};
        Comparator<Film> comparator = Comparator.comparing(obj -> {
            if (obj.getLikes() == null) {
                return 1;
            } else {
                return obj.getLikes().size() * -1;
            }
        });
        return inMemoryFilmStorage.allFilms().stream().sorted(comparator).skip(0).limit(count).collect(Collectors.toList());
    }
}

