package com.cristofy.modelo;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa la entidad Estadistica
 * @author BelenBRC
 */
@Entity
@Table(name = "estadistica")
public class Estadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_estadistica;
    @Column(name = "num_reproducciones")
    @ColumnDefault("0")
    private Long num_reproducciones;
    @Column(name = "veces_incluida_en_playlists")
    @ColumnDefault("0")
    private Long veces_incluida_en_playlists;
    @OneToOne(mappedBy = "estadistica", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cancion cancion;

    /**
     * @brief Constructor por defecto de la clase Estadistica
     */
    public Estadistica() {
        setNum_reproducciones((long) 0);
        setVeces_incluida_en_playlists((long) 0);
    }

    /**
     * @brief Constructor de la clase Estadistica con parámetros
     * @param num_reproducciones             Número de reproducciones
     * @param veces_incluida_en_playlists    Veces incluida en playlists
     * @param cancion                        Canción a la que pertenece la estadística
     */
    public Estadistica(Long num_reproducciones, Long veces_incluida_en_playlists, Cancion cancion) {
        setNum_reproducciones(num_reproducciones);
        setVeces_incluida_en_playlists(veces_incluida_en_playlists);
        setCancion(cancion);
    }

    // Getters y Setters
    /**
     * @brief Método que devuelve el id de la estadística
     * @return  id_estadistica  (Long)  Id de la estadística
     */
    public Long getId_estadistica() {
        return id_estadistica;
    }

    /**
     * @brief Método que establece el id de la estadística
     * @param id_estadistica (Long)  Id de la estadística
     */
    public void setId_estadistica(Long id_estadistica) {
        this.id_estadistica = id_estadistica;
    }

    /**
     * @brief Método que devuelve el número de reproducciones
     * @return  num_reproducciones  (Long)  Número de reproducciones
     */
    public Long getNum_reproducciones() {
        return num_reproducciones;
    }

    /**
     * @brief Método que establece el número de reproducciones
     * @param num_reproducciones (Long)  Número de reproducciones
     */
    public void setNum_reproducciones(Long num_reproducciones) {
        this.num_reproducciones = num_reproducciones;
    }

    /**
     * @brief Método que devuelve las veces incluida en playlists
     * @return  veces_incluida_en_playlists (Long)  Veces incluida en playlists
     */
    public Long getVeces_incluida_en_playlists() {
        return veces_incluida_en_playlists;
    }

    /**
     * @brief Método que establece las veces incluida en playlists
     * @param veces_incluida_en_playlists (Long)  Veces incluida en playlists
     */
    public void setVeces_incluida_en_playlists(Long veces_incluida_en_playlists) {
        this.veces_incluida_en_playlists = veces_incluida_en_playlists;
    }

    /**
     * @brief Método que devuelve la canción a la que pertenece la estadística
     * @return  cancion (Cancion)    Canción a la que pertenece la estadística
     */
    public Cancion getCancion() {
        return cancion;
    }

    /**
     * @brief Método que establece la canción a la que pertenece la estadística
     * @param cancion (Cancion)    Canción a la que pertenece la estadística
     */
    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

}
