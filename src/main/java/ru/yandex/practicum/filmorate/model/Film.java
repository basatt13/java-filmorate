package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private String name;
    private String description;
    private String releaseDate;
    private int duration;

    @Builder.Default
    private int id = 0;
    @Builder.Default
    private Set<User> likes = new HashSet<>();
}
