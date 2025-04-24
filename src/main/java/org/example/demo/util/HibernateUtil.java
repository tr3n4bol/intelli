package org.example.demo.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static EntityManagerFactory entityManagerFactory;

    public static void init() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("calculator-pu");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            init();
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}