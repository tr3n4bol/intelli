// OhmCalculationDao.java
package org.example.demo.dao;

import org.example.demo.entities.OhmCalculation;
import org.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class OhmCalculationDao {
    public void save(OhmCalculation calculation) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(calculation);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<OhmCalculation> getHistory(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM OhmCalculation o WHERE o.username = :username ORDER BY o.timestamp DESC",
                            OhmCalculation.class)
                    .setParameter("username", username)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<OhmCalculation> findByUsername(String username) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM OhmCalculation o WHERE o.username = :username ORDER BY o.timestamp DESC", 
                    OhmCalculation.class)
                    .setParameter("username", username)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}