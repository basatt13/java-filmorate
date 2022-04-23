package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Getter
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public Collection<Film> allFilms() {
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        boolean check = true;
        if (film.getName().isEmpty()) {
            System.out.println("Название фильма не может быть пустым");
            check = false;
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            System.out.println("Описание не должно быть пустым. Максимальная длина описания - 200 символов");
            check = false;
        }
        LocalDate valiDate = LocalDate.of(1895, 12, 28);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(film.getReleaseDate(), formatter);
        if (date.isBefore(valiDate) || film.getReleaseDate().isEmpty()) {
            check = false;
        }
        if (film.getDuration() <= 0) {
            System.out.println("Продолжительность фильма должна быть положительной");
            check = false;
        }
        if (!check) {
            throw new ValidationException("Проверьте данные запроса");
        } else {
            film.setId(films.size() + 1);
            films.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " добавлен");
            return film;
        }

    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        try {
            boolean check = true;
            if (film.getName().isEmpty()) {
                System.out.println("Название фильма не может быть пустым");
                check = false;
            }
            if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
                System.out.println("Описание не должно быть пустым. Максимальная длина описания - 200 символов");
                check = false;
            }
            LocalDate valiDate = LocalDate.of(1895, 12, 28);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(film.getReleaseDate(), formatter);
            if (date.isBefore(valiDate)) {
                System.out.println("Дата релиза раньше 28 декабря 1985 года");
                check = false;
            }
            if (film.getDuration() < 0) {
                System.out.println("Продолжительность фильма должна быть положительной");
                check = false;
            }
            if (!films.containsKey(film.getId())) {
                System.out.println("Фильм с ID " + film.getId() + " отсутствует");
                check = false;
            }
            if (!check) {
                throw new ValidationException("Проверьте данные запроса");
            }
            films.put(film.getId(), film);
            log.info("Фильм " + film.getName() + " обновлен");
        } catch (ValidationException ex) {
            ex.getMessage();
        }
        return film;
    }
}
