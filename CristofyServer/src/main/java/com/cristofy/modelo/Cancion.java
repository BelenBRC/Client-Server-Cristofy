package com.cristofy.modelo;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Clase que representa la entidad Cancion
 * @author BelenBRC
 */
@Entity
@Table(name = "cancion", uniqueConstraints = @UniqueConstraint(columnNames = {"artista_id_artista", "titulo"}))
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id_cancion;
    @Column(nullable = false)
    private String  titulo;
    @Check(constraints = "duracion_seg >= 1 AND duracion_seg < 2000") // 33 minutos máximo
    private Integer duracion_seg;
    @Check(constraints = "anio_publicacion > 1000 AND anio_publicacion <= 2024")
    private Integer anio_publicacion;
    private String  ruta;
    private Long file_size_bytes;
    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Estadistica estadistica;
    @ManyToOne
    private Artista artista;
    @ManyToMany(mappedBy = "listaCanciones")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Playlist> listaPlaylists;

    /**
     * @brief Constructor por defecto de la clase Cancion
     */
    public Cancion() {
        setEstadistica(new Estadistica());
        setListaPlaylists(new ArrayList<Playlist>());
    }

    /**
     * @brief Constructor de la clase Cancion con parámetros
     * @param titulo            Título de la canción
     * @param duracion_seg      Duración de la canción en segundos
     * @param anio_publicacion  Año de publicación de la canción
     * @param ruta              Ruta de la canción en el sistema de archivos
     * @param size              Tamaño del archivo de la canción en bytes
     * @param estadistica       Estadística de la canción
     * @param artista           Artista de la canción
     */
    public Cancion(String titulo, Integer duracion_seg, Integer anio_publicacion, String ruta, Long size, Estadistica estadistica, Artista artista) {
        setTitulo(titulo);
        setDuracion_seg(duracion_seg);
        setAnio_publicacion(anio_publicacion);
        setEstadistica(estadistica);
        setRuta(ruta);
        setFile_size_bytes(size);
        setArtista(artista);
        setListaPlaylists(new ArrayList<Playlist>());
    }

    // Getters y Setters

    /**
     * @brief Método que devuelve el id de la canción
     * @return  id_cancion  (Long)  Id de la canción
     */
    public Long getId_cancion() {
        return id_cancion;
    }

    /**
     * @brief Método que establece el id de la canción
     * @param id_cancion    (Long)  Id de la canción
     */
    public void setId_cancion(Long id_cancion) {
        this.id_cancion = id_cancion;
    }

    /**
     * @brief Método que devuelve el título de la canción
     * @return  titulo  (String)    Título de la canción
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @brief Método que establece el título de la canción
     * @param titulo    (String)    Título de la canción
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @brief Método que devuelve la duración de la canción en segundos
     * @return  duracion_seg    (Integer)   Duración de la canción en segundos
     */
    public Integer getDuracion_seg() {
        return duracion_seg;
    }

    /**
     * @brief Método que establece la duración de la canción en segundos
     * @param duracion_seg  (Integer)   Duración de la canción en segundos
     */
    public void setDuracion_seg(Integer duracion_seg) {
        this.duracion_seg = duracion_seg;
    }

    /**
     * @brief Método que devuelve el año de publicación de la canción
     * @return  anio_publicacion    (Integer)   Año de publicación de la canción
     */
    public Integer getAnio_publicacion() {
        return anio_publicacion;
    }

    /**
     * @brief Método que establece el año de publicación de la canción
     * @param anio_publicacion  (Integer)   Año de publicación de la canción
     */
    public void setAnio_publicacion(Integer anio_publicacion) {
        this.anio_publicacion = anio_publicacion;
    }

    /**
     * @brief Método que devuelve la ruta de la canción en el sistema de archivos
     * @return  ruta    (String)    Ruta de la canción en el sistema de archivos
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * @brief Método que establece la ruta de la canción en el sistema de archivos
     * @param ruta  (String)    Ruta de la canción en el sistema de archivos
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * @brief Método que devuelve el tamaño del archivo de la canción en bytes
     * @return  file_size_bytes (Long)   Tamaño del archivo de la canción en bytes
     */
    public Long getFile_size_bytes() {
        return file_size_bytes;
    }

    /**
     * @brief Método que establece el tamaño del archivo de la canción en bytes
     * @param file_size_bytes   (Long)  Tamaño del archivo de la canción en bytes
     */
    public void setFile_size_bytes(Long file_size_bytes) {
        this.file_size_bytes = file_size_bytes;
    }

    /**
     * @brief Método que devuelve la estadística de la canción
     * @return  estadistica (Estadistica)   Estadística de la canción
     */
    public Estadistica getEstadistica() {
        return estadistica;
    }

    /**
     * @brief Método que establece la estadística de la canción
     * @param estadistica   (Estadistica)   Estadística de la canción
     */
    public void setEstadistica(Estadistica estadistica) {
        this.estadistica = estadistica;
    }

    /**
     * @brief Método que devuelve el artista de la canción
     * @return  artista (Artista)   Artista de la canción
     */
    public Artista getArtista() {
        return artista;
    }

    /**
     * @brief Método que establece el artista de la canción
     * @param artista   (Artista)   Artista de la canción
     */
    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    /**
     * @brief Método que devuelve las playlists a las que pertenece una canción
     * @return  listaPlaylists  (List<Playlist>)  Lista de playlists a las que pertenece la canción
     */
    public List<Playlist> getListaPlaylists() {
        return listaPlaylists;
    }

    /**
     * @brief Método que establece la lista de playlists a las que pertenece la canción
     * @param playlists (List<Playlists>)   Lista de playlists a las que pertenece la canción
     */
    public void setListaPlaylists(List<Playlist> playlists) {
        this.listaPlaylists = playlists;
    }

}
