package com.example.simple_insta_backend.entity;

import com.example.simple_insta_backend.entity.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { // имплементируем UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, updatable = false)
    private String username;

    @Column(name = "password", length = 3000) // будем кодировать пароль
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false) // валидация перед тем как сохранить в БД
    private String lastname;

    @Column(name = "bio", columnDefinition = "text") // сохранять объёмные тексты
    private String bio;

    // Отслеживаем когда был создан тот или иной объект
    @Column(name = "created_date", nullable = false, updatable = false)
//    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime createdDate;

    // Задаём значение этого атрибута
    // прямо перед созданием новой записи в БД
//    @PrePersist
//    protected void onCreate() {
//        this.createdDate = LocalDateTime.now();
//    }

    // **************************************************************

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    //    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Role> roles = new HashSet<>(); // в Set только уникальные элементы
    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> roles = new HashSet<>(); // в Set только уникальные элементы

    // ЧТО ЭТО?
    //
    @Transient
    private Collection<? extends GrantedAuthority> authorities; // "authorities" - права

    // ДОБАВИЛИ НОВЫЙ КОНСТРУКТОР
//    public User(Long id, String username, String password, String email, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.authorities = authorities;
//    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
