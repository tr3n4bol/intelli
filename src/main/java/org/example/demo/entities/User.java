package org.example.demo.entities;

import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "USER";


    // JavaFX Properties для работы с TableView
    private transient StringProperty usernameProperty;
    private transient StringProperty roleProperty;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры для обычных полей
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
        if (usernameProperty != null) {
            usernameProperty.set(username);
        }
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) {
        this.role = role;
        if (roleProperty != null) {
            roleProperty.set(role);
        }
    }

    // Методы для JavaFX Properties
    public StringProperty usernameProperty() {
        if (usernameProperty == null) {
            usernameProperty = new SimpleStringProperty(this, "username", username);
        }
        return usernameProperty;
    }

    public StringProperty roleProperty() {
        if (roleProperty == null) {
            roleProperty = new SimpleStringProperty(this, "role", role);
        }
        return roleProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}