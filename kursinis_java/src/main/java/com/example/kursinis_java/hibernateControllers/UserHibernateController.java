package com.example.kursinis_java.hibernateControllers;

import com.example.kursinis_java.ds.Company;
import com.example.kursinis_java.ds.Person;
import com.example.kursinis_java.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserHibernateController {
    private EntityManagerFactory emf;

    public UserHibernateController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public void createUser(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editUser(User user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public Person getPersonById(int id) {
        EntityManager em = getEntityManager();
        Person person = null;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such user by given ID");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return person;
    }

    public Company getCompanyById(int id) {
        EntityManager em = getEntityManager();
        Company company = null;
        try {
            em.getTransaction().begin();
            company = em.find(Company.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User match not found for this ID");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return company;
    }

    public void removeUser(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User user = null;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (Exception e) {
                System.out.println("No such user by given Id");
            }
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> getAllUsers() {
        return getAllUsers(false, -1, -1);
    }

    public List<User> getAllUsers(boolean all, int resMax, int resFirst) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(User.class));
            Query q = em.createQuery(query);

            if (!all) {
                q.setMaxResults(resMax);
                q.setFirstResult(resFirst);
            }

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public User getUserById(int id) {
        EntityManager em = getEntityManager();
        User user = null;
        try {
            em.getTransaction().begin();
            user = em.getReference(User.class, id);
            user.getId();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User match not found for this id");
        }
        return user;
    }
    public User getUserByLoginData(String login, String password) {
        EntityManager em = getEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.like(root.get("password"), password), cb.like(root.get("login"), login));
        Query q;
        try {
            q = em.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
