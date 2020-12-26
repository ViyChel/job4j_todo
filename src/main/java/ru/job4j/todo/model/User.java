package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;

/**
 * Class User.
 *
 * @author Vitaly Yagufarov (for.viy@gmail.com)
 * @version 1.0
 * @since 22.12.2020
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"name", "email", "password"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private Integer role;
}
