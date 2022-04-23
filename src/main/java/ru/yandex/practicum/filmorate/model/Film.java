package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Film {
    private String name;
    private String description;
    private String releaseDate;
    private int duration;

    @Builder.Default
    private int id = 0;
}
