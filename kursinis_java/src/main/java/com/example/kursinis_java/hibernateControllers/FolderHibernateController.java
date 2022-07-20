package com.example.kursinis_java.hibernateControllers;

import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class FolderHibernateController {
    private EntityManagerFactory emf;

    public FolderHibernateController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void remove(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Folder folder = null;
            try {
                folder = em.getReference(Folder.class, id);
            } catch (Exception e) {
                System.out.println("User match not found for this ID");
            }

            assert folder != null;
            Course project = folder.getParentCourse();
            if (project != null) {
                project.getCourseFolders().remove(folder);
                em.merge(project);
            }

            for (Folder t : folder.getSubFolders()) {
                remove(t.getId());
            }
            folder.getSubFolders().clear();
            em.merge(folder);

            User responsible = folder.getResponsible();
            responsible.getMyFolders().remove(folder);
            em.merge(responsible);

            User creator = folder.getCreator();
            creator.getMyFolders().remove(folder);
            em.merge(creator);

            em.remove(folder);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public Folder getFolderById(int id) {
        EntityManager em;
        Folder folder = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            folder = em.find(Folder.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("User match not found for this ID");
        }
        return folder;
    }
    public void edit(Folder folder) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(folder);
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
