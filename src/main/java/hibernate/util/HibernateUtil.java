package hibernate.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateUtil {

  private static HibernateUtil instance;

  private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDatabase");
  private final EntityManager entityManager = entityManagerFactory.createEntityManager();

  public static HibernateUtil getInstance() {
    if (null == instance) {
      instance = new HibernateUtil();
    }
    return instance;
  }

  public void saveByHibernateSession(Object o) {
    try {
      Session session = entityManager.unwrap(Session.class);
      Transaction transaction = session.beginTransaction();
      session.save(o);
      transaction.commit();
      session.close();
    } catch (PersistenceException e) {
      System.out.println("Problem with unwraping seesion occurred : " + e);
    }
  }

  public void save(Object o) {
    entityManager.getTransaction().begin();
    if(!entityManager.contains(o)) {
      entityManager.persist(o);
      entityManager.flush();
    }
    entityManager.getTransaction().commit();
  }

  public void delete(Class clazz, Long objectId) {
    entityManager.getTransaction().begin();
    Object toRemove  = entityManager.find(clazz, objectId);
    entityManager.remove(toRemove);
    entityManager.getTransaction().commit();
  }
}
