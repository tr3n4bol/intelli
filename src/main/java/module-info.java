module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.postgresql.jdbc;
    requires bcrypt;

    opens org.example.demo to javafx.fxml;
    opens org.example.demo.controllers to javafx.fxml;
    opens org.example.demo.entities to org.hibernate.orm.core;

    exports org.example.demo;
    exports org.example.demo.controllers;
    exports org.example.demo.entities;
    exports org.example.demo.services;
    exports org.example.demo.dao;
    exports org.example.demo.util;
}