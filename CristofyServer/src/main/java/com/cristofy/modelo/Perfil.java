package com.cristofy.modelo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * Clase que representa la entidad Perfil
 * @author BelenBRC
 */
@Entity
@Table(name = "perfil")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id_perfil;

    @Column(nullable = false)
    @Check(constraints = "LENGTH(nombre_usuario) > 3 AND LENGTH(nombre_usuario) < 30")
    private String  nombre_usuario;

    private String apellidos;

    @Column(unique = true, nullable = false)
    @Check(constraints = "LENGTH(login) > 3 AND LENGTH(login) < 20")
    private String  login;

    @Column(nullable = false)
    @Check(constraints = "LENGTH(contrasenia) > 3")
    private String  contrasenia;

    @Column(unique = true, nullable = false)
    @Check(constraints = "LENGTH(email) > 4 AND LENGTH(email) < 40")
    private String  email;

    @ManyToMany(mappedBy = "listaPerfiles")
    private List<Playlist> listaPlaylists;

    @Transient
    private Integer total_playlists_guardadas;
    @Transient
    private Integer total_canciones_en_playlists_guardadas;
    @Transient
    private boolean conectado;
    @Transient
    private String token_sesion;


    /**
     * @brief Constructor por defecto de la clase Perfil
     */
    public Perfil() {
        setListaPlaylists(new ArrayList<Playlist>());
    }

    /**
     * @brief Constructor de la clase Perfil con parámetros
     * @param nombre_usuario    Nombre del usuario
     * @param login             Nombre de usuario para iniciar sesión
     * @param contrasenia       Contraseña del usuario
     * @param email             Correo electrónico del usuario
     */
    public Perfil(String nombre_usuario, String login, String contrasenia, String email, Integer anio_creacion) {
        setNombre_usuario(nombre_usuario);
        setLogin(login);
        setContrasenia(contrasenia);
        setEmail(email);
        setListaPlaylists(new ArrayList<Playlist>());
    }

    // Getters y Setters
    /**
     * @brief Método que devuelve el id del perfil
     * @return  id_perfil   (Long)  Id del perfil
     */
    public Long getId_perfil() {
        return id_perfil;
    }

    /**
     * @brief Método que establece el id del perfil
     * @param id_perfil (Long)  Id del perfil
     */
    public void setId_perfil(Long id_perfil) {
        this.id_perfil = id_perfil;
    }

    /**
     * @brief Método que devuelve el nombre del usuario
     * @return  nombre_usuario  (String)    Nombre del usuario
     */
    public String getNombre_usuario() {
        return nombre_usuario;
    }

    /**
     * @brief Método que establece el nombre del usuario
     * @param nombre_usuario    (String)    Nombre del usuario
     */
    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    /**
     * @brief Método que devuelve los apellidos del usuario
     * @return  apellidos   (String)    Apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @brief Método que establece los apellidos del usuario
     * @param apellidos (String)    Apellidos del usuario
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * @brief Método que devuelve el nombre de usuario para iniciar sesión
     * @return  login   (String)    Nombre de usuario para iniciar sesión
     */
    public String getLogin() {
        return login;
    }

    /**
     * @brief Método que establece el nombre de usuario para iniciar sesión
     * @param login (String)    Nombre de usuario para iniciar sesión
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @brief Método que devuelve la contraseña del usuario
     * @return  contrasenia (String)    Contraseña del usuario
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @brief Método que establece la contraseña del usuario
     * @param contrasenia   (String)    Contraseña del usuario
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * @brief Método que devuelve el correo electrónico del usuario
     * @return  email   (String)    Correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * @brief Método que establece el correo electrónico del usuario
     * @param email (String)    Correo electrónico del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @brief Método que devuelve la lista de playlists del perfil
     * @return  listaPlaylists  (List<Playlist>) Lista de playlists del perfil
     */
    public List<Playlist> getListaPlaylists() {
        return listaPlaylists;
    }

    /**
     * @brief Método que establece la lista de playlists del perfil
     * @param listaPlaylists    (List<Playlist>) Lista de playlists del perfil
     */
    public void setListaPlaylists(List<Playlist> listaPlaylists) {
        this.listaPlaylists = listaPlaylists;
    }

    /**
     * @brief Método que devuelve el total de playlists guardadas por el perfil
     * @return  total_playlists_guardadas   (Integer)   Total de playlists guardadas por el perfil
     */
    public Integer getTotal_playlists_guardadas() {
        setTotal_playlists_guardadas(getListaPlaylists().size());
        return total_playlists_guardadas;
    }

    /**
     * @brief Método que establece el total de playlists guardadas por el perfil
     * @param total_playlists_guardadas (Integer)   Total de playlists guardadas por el perfil
     */
    public void setTotal_playlists_guardadas(Integer total_playlists_guardadas) {
        this.total_playlists_guardadas = total_playlists_guardadas;
    }

    /**
     * @brief Método que devuelve el total de canciones guardadas en playlists por el perfil
     * @return  total_canciones_guardadas_en_playlists   (Integer)   Total de canciones guardadas en playlists por el perfil
     */
    public Integer getTotal_canciones_en_playlists_guardadas() {
        Integer totalCanciones = 0;
        for (Playlist p : getListaPlaylists()){
            totalCanciones += p.getListaCanciones().size();
        }
        setTotal_canciones_en_playlists_guardadas(totalCanciones);
        return total_canciones_en_playlists_guardadas;
    }

    /**
     * @brief Método que establece el total de canciones guardadas en playlists por el perfil
     * @param total_canciones   (Integer)   Total de canciones guardadas en playlists por el perfil
     */
    public void setTotal_canciones_en_playlists_guardadas(Integer total_canciones) {
        this.total_canciones_en_playlists_guardadas = total_canciones;
    }

    /**
     * @brief Método que devuelve si el usuario está conectado
     * @return  conectado   (boolean)   Si el usuario está conectado
     */
    public boolean isConectado() {
        return conectado;
    }

    /**
     * @brief Método que establece si el usuario está conectado
     * @param conectado (boolean)   Si el usuario está conectado
     */
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    /**
     * @brief Método que devuelve el token de sesión del usuario
     * @return  token_sesion    (String)    Token de sesión del usuario
     */
    public String getToken_sesion() {
        return token_sesion;
    }

    /**
     * @brief Método que establece el token de sesión del usuario
     * @param token_sesion  (String)    Token de sesión del usuario
     */
    public void setToken_sesion(String token_sesion) {
        this.token_sesion = token_sesion;
    }

    // Métodos

    /**
     * @brief Método que añade una playlist a la lista de playlists del perfil
     * @param playlist   (Playlist)  Playlist a añadir
     */
    public void addPlaylist(Playlist playlist){
        getListaPlaylists().add(playlist);
        playlist.getListaPerfiles().add(this);
        
        actualizarTotales();
    }

    /**
     * @brief Método que elimina una playlist de la lista de playlists del perfil
     * @param playlist   (Playlist)  Playlist a eliminar
     */
    public void removePlaylist(Playlist playlist){
        getListaPlaylists().remove(playlist);
        playlist.getListaPerfiles().remove(this);

        actualizarTotales();
    }

    /**
     * @brief Método que actualiza los totales de playlists y canciones guardadas en playlists
     */
    private void actualizarTotales(){
        setTotal_playlists_guardadas(getListaPlaylists().size());
        int totalCanciones = 0;
        for (Playlist p : getListaPlaylists()){
            totalCanciones += p.getListaCanciones().size();
        }
        setTotal_canciones_en_playlists_guardadas(totalCanciones);
    }

}
