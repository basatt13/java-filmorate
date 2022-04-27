package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
@Getter
@RequestMapping(value = "/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(films.size() + 1);
        films.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " добавлен");

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с ID " + film.getId() + " отсутствует");
        }else {
            films.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " обновлен");
        }
        return film;
    }

    void validateFilm(Film film) {
        if (film.getName().isEmpty()) {
            log.error("Имя фильма не должно быть пустым");
            throw new ValidationException("Ошибка данных запроса");
        }

        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания - 200 символов");
            throw new ValidationException("Ошибка данных запроса");
        }
        LocalDate valiDate = LocalDate.of(1895, 12, 28);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(film.getReleaseDate(), formatter);
        if (date.isBefore(valiDate)) {
            log.error("Дата релиза раньше 28 декабря 1985 года");
            throw new ValidationException("Ошибка данных запроса");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Ошибка данных запроса");
        }
    }
}
