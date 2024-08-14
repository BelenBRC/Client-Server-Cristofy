package com.cristofy.modelo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @brief   Clase que maneja la factoría de sesiones de Hibernate
 * @autor   BelenBRC
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory = null;
    private static Session session = null;
    private static Configuration configuration = null;
    private static ServiceRegistry serviceRegistry = null;

    /**
     * @brief   Método que crea la factoría de sesiones
     * @throws  HibernateException   Error al crear la factoría de sesiones
     */
    public static void buildSessionFactory() throws HibernateException{
        configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        //Registramos las clases que hay que mapear con cada tabla de la base de datos
        configuration.addAnnotatedClass(Artista.class);
        configuration.addAnnotatedClass(Cancion.class);
        configuration.addAnnotatedClass(Estadistica.class);
        configuration.addAnnotatedClass(Perfil.class);
        configuration.addAnnotatedClass(Playlist.class);

        //Obtenemos el registro de servicios estándar
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        //Creamos la factoría de sesiones
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    /**
     * @brief   Método que abre una nueva sesión
     * @throws  HibernateException   Error al abrir la sesión
     */
    public static void openSession() throws HibernateException{
        if(sessionFactory == null){
            buildSessionFactory();
        }

        session = sessionFactory.openSession();
    }

    /**
     * @brief   Método que devuelve la sesión actual
     * @return  session     Sesión actual
     * @throws  HibernateException   Error al obtener la sesión actual
     */
    public static Session getCurrentSession() throws HibernateException{
        if((session == null) || (!session.isOpen())){
            openSession();
        }

        return session;
    }

    /**
     * @brief   Método que cierra la sesión actual y la factoría de sesiones si están abiertas
     * @throws  HibernateException   Error al cerrar la sesión actual
     */
    public static void closeSession() throws HibernateException{
        if(session != null){
            session.close();
        }

        if(sessionFactory != null){
            sessionFactory.close();
        }
    }
}
