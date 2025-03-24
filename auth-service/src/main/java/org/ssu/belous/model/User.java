package org.ssu.belous.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "gen_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @NonNull
    @Column(nullable = false, name = "uuid")
    private UUID uuid;

    @Setter
    @NonNull
    @Column(nullable = false, length = 20, name = "username")
    private String username;

    @Setter
    @NonNull
    @Column(nullable = false, length = 64, name = "password")
    private String password;

    @Setter
    @NonNull
    @Column(name = "role")
    private String role;

    @Setter
    @NonNull
    @Column(name = "name")
    private String name;

    @Setter
    @NonNull
    @Column(name = "second_name")
    private String secondName;

    @Setter
    @NonNull
    @Column(name = "last_name")
    private String lastName;

    @Setter
    @NonNull
    @Column(name = "city")
    private String city;

    @Setter
    @NonNull
    @Column(name = "street")
    private String street;

    @Setter
    @NonNull
    @Column(name = "number_home")
    private String number_home;

    @Setter
    @Column(name = "number_apartment")
    private String number_apartment;
}
