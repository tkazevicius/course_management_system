package com.example.kursinis_java.hibernateControllers;

import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CourseHibernateController {
    private EntityManagerFactory emf;

    public CourseHibernateController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void editCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(course);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void createCourse(Course course) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Course> getAllCourses() {
        return getAllCourses(false, -1, -1);
    }

    public List<Course> getAllCourses(boolean all, int resMax, int resFirst) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Course.class));
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

    public Course getCourseById(int id) {
        EntityManager em = getEntityManager();;
        Course course = null;
        try {
            em.getTransaction().begin();
            course = em.getReference(Course.class, id);
            course.getId();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User match not found for this ID");
        }
        return course;
    }
    public List<Course> getCourseByUserId(int id) {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> query = cb.createQuery(Course.class);

        Root<Course> root = query.from(Course.class);

        CriteriaBuilder.In<Integer> inClause = cb.in(root.get("id"));
        User user = em.getReference(User.class, id);
        for (Course p : user.getMyModeratedCourses()) {
            inClause.value(p.getId());
        }
        query.select(root).where(inClause);
        Query q;
        try {
            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    public void removeCourse(int id) {
        FolderHibernateController folderHibernateController = new FolderHibernateController(emf);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Course course = null;
            try {
                course = em.getReference(Course.class, id);

                for (Folder f : course.getCourseFolders()) {
                    folderHibernateController.remove(f.getId());
                }

                course.getCourseFolders().clear();
                em.merge(course);

                for (User u : course.getCourseModerator()) {
                    u.getMyModeratedCourses().remove(course);
                    em.merge(u);
                }

            } catch (Exception e) {
                System.out.println("User match not found for this ID");
            }


            em.remove(course);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
