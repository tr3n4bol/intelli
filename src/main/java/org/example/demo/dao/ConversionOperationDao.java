// ConversionOperationDao.java
package org.example.demo.dao;

import org.example.demo.entities.ConversionOperation;
import org.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ConversionOperationDao {
    public void save(ConversionOperation operation) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(operation);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<ConversionOperation> getHistory(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM ConversionOperation o WHERE o.username = :username ORDER BY o.timestamp DESC",
                            ConversionOperation.class)
                    .setParameter("username", username)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<ConversionOperation> findByUsername(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM ConversionOperation o WHERE o.username = :username ORDER BY o.timestamp DESC",
                    ConversionOperation.class)
                    .setParameter("username", username)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}