// ExchangeRateDao.java
package org.example.demo.dao;

import org.example.demo.entities.ExchangeRate;
import org.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {
    public Optional<ExchangeRate> findByFromAndToCurrency(String fromCurrency, String toCurrency) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            ExchangeRate rate = em.createQuery(
                            "SELECT r FROM ExchangeRate r WHERE r.fromCurrency = :from AND r.toCurrency = :to",
                            ExchangeRate.class)
                    .setParameter("from", fromCurrency)
                    .setParameter("to", toCurrency)
                    .getSingleResult();
            return Optional.ofNullable(rate);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public void save(ExchangeRate rate) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(rate);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<ExchangeRate> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT r FROM ExchangeRate r", ExchangeRate.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}