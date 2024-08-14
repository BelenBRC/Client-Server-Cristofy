package com.cristofy.modelo;

import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Clase que representa la entidad Artista
 * @author BelenBRC
 */
@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id_artista;
    @Column(unique = true, nullable = false)
    private String  nombre_artista;
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Cancion> canciones;

    /**
     * @brief Constructor por defecto de la clase Artista
     */
    public Artista() {
        setCanciones(new ArrayList<Cancion>());
    }

    /**
     * @brief Constructor de la clase Artista con parámetros
     * @param nombre_artista  Nombre del artista
     */
    public Artista(String nombre_artista) {
        setNombre_artista(nombre_artista);
        if(getCanciones() == null) {
            setCanciones(new ArrayList<Cancion>());
        }
    }

    // Getters y Setters

    /**
     * @brief Método que devuelve el id del artista
     * @return  id_artista  (Long)  Id del artista
     */
    public Long getId_artista() {
        return id_artista;
    }

    /**
     * @brief Método que establece el id del artista
     * @param id_artista    (Long)  Id del artista
     */
    public void setId_artista(Long id_artista) {
        this.id_artista = id_artista;
    }

    /**
     * @brief Método que devuelve el nombre del artista
     * @return  nombre_artista  (String)  Nombre del artista
     */
    public String getNombre_artista() {
        return nombre_artista;
    }

    /**
     * @brief Método que establece el nombre del artista
     * @param nombre_artista    (String)  Nombre del artista
     */
    public void setNombre_artista(String nombre_artista) {
        this.nombre_artista = nombre_artista;
    }

    /**
     * @brief Método que devuelve la lista de canciones del artista
     * @return  canciones   (List<Cancion>) Lista de canciones del artista
     */
    public List<Cancion> getCanciones() {
        return canciones;
    }

    /**
     * @brief Método que establece la lista de canciones del artista
     * @param canciones (List<Cancion>) Lista de canciones del artista
     */
    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    /**************************************************************************/

    /**
     * @brief Método que añade una canción a la lista de canciones del artista
     * @param cancion   (Cancion)   Canción a añadir
     * @post La canción se añade a la lista de canciones del artista
     *          y se establece el artista en la canción
     */
    public void addCancion(Cancion cancion) {
        getCanciones().add(cancion);
        cancion.setArtista(this);
    }

}
