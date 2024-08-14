package com.cristofy.modelo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "playlist", uniqueConstraints = @UniqueConstraint(columnNames = {"nombre_playlist", "id_creador"}))
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id_playlist;
    @Column(nullable = false)
    private String  nombre_playlist;
    private Long    id_creador;
    @Check(constraints = "num_canciones >= 0")
    @ColumnDefault("0")
    private Integer num_canciones;
    @ManyToMany(cascade =  CascadeType.MERGE)
    @JoinTable(name="contiene", uniqueConstraints = @UniqueConstraint(columnNames={"id_playlist", "id_cancion"}), joinColumns = @JoinColumn(name="id_playlist"), inverseJoinColumns = @JoinColumn(name="id_cancion"))
    private List<Cancion> listaCanciones;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name="guarda", uniqueConstraints = @UniqueConstraint(columnNames={"id_playlist", "id_perfil"}), joinColumns = @JoinColumn(name="id_playlist"), inverseJoinColumns = @JoinColumn(name="id_perfil"))
    private List<Perfil> listaPerfiles;

    /**
     * @brief Constructor por defecto de la clase Playlist
     */
    public Playlist() {
        setListaCanciones(new ArrayList<Cancion>());
        setListaPerfiles(new ArrayList<Perfil>());
    }

    /**
     * @brief Constructor de la clase Playlist con parámetros
     * @param nombre_playlist   Nombre de la playlist
     * @param id_creador        Id del creador de la playlist
     * @param num_canciones     Número de canciones de la playlist
     */
    public Playlist(String nombre_playlist, Long id_creador, Integer num_canciones) {
        setNombre_playlist(nombre_playlist);
        setId_creador(id_creador);
        setNum_canciones(num_canciones);
        setListaCanciones(new ArrayList<Cancion>());
        setListaPerfiles(new ArrayList<Perfil>());
    }

    // Getters y Setters

    /**
     * @brief Método que devuelve el id de la playlist
     * @return  id_playlist (Long)  Id de la playlist
     */
    public Long getId_playlist() {
        return id_playlist;
    }

    /**
     * @brief Método que establece el id de la playlist
     * @param id_playlist   (Long)  Id de la playlist
     */
    public void setId_playlist(Long id_playlist) {
        this.id_playlist = id_playlist;
    }

    /**
     * @brief Método que devuelve el nombre de la playlist
     * @return  nombre_playlist (String)    Nombre de la playlist
     */
    public String getNombre_playlist() {
        return nombre_playlist;
    }

    /**
     * @brief Método que establece el nombre de la playlist
     * @param nombre_playlist   (String)    Nombre de la playlist
     */
    public void setNombre_playlist(String nombre_playlist) {
        this.nombre_playlist = nombre_playlist;
    }

    /**
     * @brief Método que devuelve el id del creador de la playlist
     * @return  id_creador  (Long)  Id del creador de la playlist
     */
    public Long getId_creador() {
        return id_creador;
    }

    /**
     * @brief Método que establece el id del creador de la playlist
     * @param id_creador    (Long)  Id del creador de la playlist
     */
    public void setId_creador(Long id_creador) {
        this.id_creador = id_creador;
    }

    /**
     * @brief Método que devuelve el número de canciones de la playlist
     * @return  num_canciones   (Integer)   Número de canciones de la playlist
     */
    public Integer getNum_canciones() {
        return num_canciones;
    }

    /**
     * @brief Método que establece el número de canciones de la playlist
     * @param num_canciones  (Integer)   Número de canciones de la playlist
     */
    public void setNum_canciones(Integer num_canciones) {
        this.num_canciones = num_canciones;
    }

    /**
     * @brief Método que devuelve la lista de canciones de la playlist
     * @return  listaCanciones  (List<Cancion>) Lista de canciones de la playlist
     */
    public List<Cancion> getListaCanciones() {
        return listaCanciones;
    }

    /**
     * @brief Método que establece la lista de canciones de la playlist
     * @param canciones (List<Cancion>) Lista de canciones de la playlist
     */
    public void setListaCanciones(List<Cancion> canciones) {
        this.listaCanciones = canciones;
    }

    /**
     * @brief Método que devuelve la lista de perfiles que guardan la playlist
     * @return  listaPerfiles   (List<Perfil>)  Lista de perfiles que guardan la playlist
     */
    public List<Perfil> getListaPerfiles() {
        return listaPerfiles;
    }

    /**
     * @brief Método que establece la lista de perfiles que guardan la playlist
     * @param perfiles  (List<Perfil>)  Lista de perfiles que guardan la playlist
     */
    public void setListaPerfiles(List<Perfil> perfiles) {
        this.listaPerfiles = perfiles;
    }

    // Métodos
    /**
     * @brief Método que añade una canción a la playlist
     * @param cancion   (Cancion)   Canción a añadir
     * @post La canción se añade a la lista de canciones de la playlist
     * @post El número de canciones de la playlist se incrementa en 1
     * @post La playlist se añade a la lista de playlists de la canción
     * @post El número de veces que la canción ha sido incluida en playlists se incrementa en 1
     */
    public void addCancion(Cancion cancion){
        getListaCanciones().add(cancion);
        //Actualizar el número de canciones
        setNum_canciones(getListaCanciones().size());
        //Guardar la playlist en la canción
        cancion.getListaPlaylists().add(this);
        cancion.getEstadistica().setVeces_incluida_en_playlists(cancion.getEstadistica().getVeces_incluida_en_playlists() + 1);
    }

    /**
     * @brief Método que elimina una canción de la playlist
     * @param cancion   (Cancion)   Canción a eliminar
     * @post La canción se elimina de la lista de canciones de la playlist
     * @post El número de canciones de la playlist se decrementa en 1
     * @post La playlist se elimina de la lista de playlists de la canción
     * @post El número de veces que la canción ha sido incluida en playlists se decrementa en 1
     */
    public void removeCancion(Cancion cancion){
        getListaCanciones().remove(cancion);
        //Actualizar el número de canciones
        setNum_canciones(getListaCanciones().size());
        //Eliminar la playlist de la canción
        cancion.getListaPlaylists().remove(this);
        cancion.getEstadistica().setVeces_incluida_en_playlists(cancion.getEstadistica().getVeces_incluida_en_playlists() - 1);
    }

    /**
     * @brief Método que actualiza el número de canciones de la playlist
     */
    public void actualizarNumCanciones(){
        setNum_canciones(getListaCanciones().size());
    }
    
}
