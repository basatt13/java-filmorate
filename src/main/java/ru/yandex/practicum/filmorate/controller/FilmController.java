package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundIdException;
import ru.yandex.practicum.filmorate.filmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@Slf4j
@Getter
@RequestMapping
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> allFilms() {
        return inMemoryFilmStorage.allFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getUser(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }

    @GetMapping("/films/popular")
    public List<Film> rateFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer params
    ) {
        return filmService.rateOfFilms(params);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        if (id <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        if (userId <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        filmService.removeLike(id, userId);
    }

    @PutMapping("films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        if (id <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        if (userId <= 0) {
            throw new NotFoundIdException(id + " не может быть отрицательным числом");
        }
        filmService.addLike(id, userId);
    }
}
