package ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.*;

import entities.User;
import utils.AuthenticationUtils;

@Stateless
public class UserBean {

    public User createUser(User user) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jdbc/showcase");
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            user.setPassword(user.getPassword());
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        em.persist(user);
        em.close();
        return user;
    }

    public User findUserById(String id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jdbc/showcase");
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createNamedQuery("findUserById", User.class);
        query.setParameter("name", id);
        User user = null;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignore exception and return NULL for user instead
        }
        em.close();
        return user;
    }

    public boolean checkPassword(User user) {
        User existingUser = findUserById(user.getName());
        return (existingUser != null &&
                user.getPassword().equals(existingUser.getPassword()));
    }
}