package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import java.util.HashMap;
import java.util.Map;

public class JpaUtil {

    private static final EntityManagerFactory emf;

    static {
        try {
            System.out.println("Initialisiere EMF...");

            Map<String, Object> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            props.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/ghostnet");
            props.put("jakarta.persistence.jdbc.user", "root");
            props.put("jakarta.persistence.jdbc.password", "PASSWORD");
            props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            props.put("hibernate.hbm2ddl.auto", "update");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.format_sql", "true");

            emf = Persistence.createEntityManagerFactory("Geisternetz");

            System.out.println("EMF erfolgreich!");
        } catch (Exception e) {
            System.err.println("Fehler beim EMF!");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
