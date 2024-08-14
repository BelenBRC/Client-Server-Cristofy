package com.cristofy.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hibernate.Session;

import java.util.List;
import java.util.ArrayList;

public class Conector {
    private static Conector instancia = null;

    //****************************//
    //Hibernate
    private HibernateUtil               hibernateUtil;
    private Session                     session;

    //****************************//
    //SQL
    private String                      url;
    private Connection                  conexion;

    /**
     * @brief Constructor de la clase Conector con la url por defecto
     * @post  Conexión con url por defecto 
     * @throws SQLException    Excepción de SQL en la conexión con la base de datos
     */
    private Conector() throws SQLException{    
        //SQL
        setUrl("jdbc:mysql://localhost/cristofy?user=user&password=1234");
        setConexion(null);

        //Hibernate
        setHibernateUtil(new HibernateUtil());
        getHibernateUtil();
        setSession(HibernateUtil.getCurrentSession());
        getSession();
    }



    //Sets y gets
    /**
     * @brief Establece el HibernateUtil
     * @param hibernateUtil (HibernateUtil1) HibernateUtil
     */
    private void setHibernateUtil(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    /**
     * @brief Establece la sesión de Hibernate
     * @param session   (Session)   Sesión de Hibernate
     */
    private void setSession(Session session) {
        this.session = session;
    }

    /**
     * @brief Establece la url de la base de datos
     * @param url   (String)    Url de la base de datos
     */
    private void setUrl(String url) {
        this.url = url;
    }

    /**
     * @brief Establece la conexión con la base de datos
     * @param conexion  (Connection)    Conexión con la base de datos
     */
    private void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * @brief Devuelve el HibernateUtil
     * @return  (HibernateUtil1) HibernateUtil
     */
    private HibernateUtil getHibernateUtil() {
        return hibernateUtil;
    }

    /**
     * @brief Devuelve la sesión de Hibernate
     * @return  (Session)   Sesión de Hibernate
     */
    private Session getSession() {
        return session;
    }

    /**
     * @brief Devuelve la url de la base de datos
     * @return  (String)    Url de la base de datos
     */
    private String getUrl() {
        return url;
    }

    /**
     * @brief Devuelve la conexión con la base de datos
     * @return  (Connection)    Conexión con la base de datos
     */
    private Connection getConexion() {
        return conexion;
    }

 
    //*********************************************************//
    //*********************************************************//

    /**
     * @brief   Método que devuelve la instancia de la clase Conector con la url por defecto
     * @return  (Conector)  Instancia de la clase Conector
     * @throws SQLException   Excepción de SQL en la conexión con la base de datos
     */
    public static Conector newInstance() throws SQLException{
        if(instancia == null){
            instancia = new Conector();
        }
        return instancia;
    }

    //Métodos
    /**
     * @brief   Método que establece la conexión con la base de datos
     * @pre     La base de datos debe existir
     * @post    Conexión con la base de datos
     * @post    Si la conexión falla, se asegura de cerrar la conexión si se ha abierto
     * @throws Exception        Excepción general
     */
    public void conectar() throws Exception{
        try{
            HibernateUtil.getCurrentSession();
            if(getConexion() ==null){
                setConexion(DriverManager.getConnection(getUrl()));
            }
        }
        catch(Exception e){
            getSession().close();
            HibernateUtil.closeSession();
            throw e;
        }
    }

    /**
     * @brief   Método que cierra la conexión con la base de datos
     * @pre     La conexión debe estar abierta
     * @post    Conexión cerrada
     * @throws Exception        Excepción general
     */
    public void desconectar() throws Exception{
        getConexion().close();
        setConexion(null);
        HibernateUtil.closeSession();
    }

    //*********************************************************//
    //********************  Canciones  ************************//
    //*********************************************************//

    public ArrayList<Cancion> getCanciones(){
        List<Cancion> canciones = new ArrayList<Cancion>();
        getSession().beginTransaction();
 
        canciones = getSession().createQuery("from Cancion", Cancion.class).getResultList();
        getSession().getTransaction().commit();

        return (ArrayList<Cancion>) canciones;
    }

    public void actualizarReproducciones(Cancion cancion) {
        getSession().beginTransaction();
        getSession().persist(cancion);
        getSession().persist(cancion.getEstadistica());
        getSession().getTransaction().commit();
    }

    //*********************************************************//
    //********************  Perfiles  *************************//
    //*********************************************************//

    public ArrayList<Perfil> getPerfiles(){
        List<Perfil> perfiles = new ArrayList<Perfil>();
        getSession().beginTransaction();
 
        perfiles = getSession().createQuery("from Perfil", Perfil.class).getResultList();
        getSession().getTransaction().commit();

        return (ArrayList<Perfil>) perfiles;
    }

    //*********************************************************//
    //********************  Artistas  *************************//
    //*********************************************************//

    public ArrayList<Cancion> getCancionesArtista(Long idArtista) {
        List<Cancion> canciones = new ArrayList<Cancion>();
        getSession().beginTransaction();

        canciones = getSession().createQuery("from Cancion where artista.id_artista = " + idArtista, Cancion.class).getResultList();
        getSession().getTransaction().commit();
        System.out.println("Canciones del artista: " + canciones.size());

        return (ArrayList<Cancion>) canciones;
    }

}
