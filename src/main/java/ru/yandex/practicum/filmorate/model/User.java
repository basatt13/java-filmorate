package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private String email;
    private String login;
    private String birthday;
    private String name;

    @Builder.Default
    private int idU = 0;
}
