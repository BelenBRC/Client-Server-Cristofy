package com.cristofy.controlador;

import java.util.ArrayList;

import com.cristofy.modelo.Cancion;
import com.cristofy.modelo.Conector;

public class ControladorCanciones {
    private ArrayList<Cancion> canciones;
    private Conector conector;

    public ControladorCanciones(){
        try{
            conector = Conector.newInstance();
            conector.conectar();
        }
        catch(Exception e){
            e.printStackTrace();
        }        

        canciones = new ArrayList<Cancion>();
    }

    private String generarCadenaResultadoCanciones(ArrayList<Cancion> canciones){
        StringBuilder cadena = new StringBuilder();
        cadena.append(canciones.size());
        cadena.append("#");
        for(int i=0; i < canciones.size(); i++){
            cadena.append(canciones.get(i).getId_cancion());
            cadena.append("|");
            cadena.append(canciones.get(i).getTitulo());
            cadena.append("|");
            cadena.append(canciones.get(i).getArtista().getNombre_artista());
            cadena.append("|");
            cadena.append(canciones.get(i).getEstadistica().getNum_reproducciones());
            cadena.append("|");
            cadena.append(canciones.get(i).getEstadistica().getVeces_incluida_en_playlists());
            cadena.append("|");
            if(i < canciones.size()-1){
                cadena.append("@");
            }
        }
        return cadena.toString();
    }

    public String getDetallesArtista(String id_cancion) {
        StringBuilder cadena = new StringBuilder();
        String nombreArtista="";
        Long idArtista=0l;
        //Buscar el artista de la canción
        for(int i=0; i < canciones.size() && nombreArtista.equals(""); i++){
            if(canciones.get(i).getId_cancion().toString().equals(id_cancion)){
                nombreArtista = canciones.get(i).getArtista().getNombre_artista();
                idArtista = canciones.get(i).getArtista().getId_artista();
            }
        }
        try{
            ArrayList<Cancion> cancionesArtista = conector.getCancionesArtista(idArtista);
            cadena.append(nombreArtista);
            cadena.append("#");
            cadena.append(generarCadenaResultadoCanciones(cancionesArtista));
        }
        catch(Exception e){
            e.printStackTrace();
            cadena = new StringBuilder("ERROR");
        }
        return cadena.toString();
    }

    public String getCancionesSistema(){
        String cadena = "";
        try{
            canciones = conector.getCanciones();
            cadena = generarCadenaResultadoCanciones(canciones);
        }
        catch(Exception e){
            e.printStackTrace();
            cadena = "ERROR";
        }
        return cadena;
    }

    public String getCancionesPorString(String cadenaBusqueda) {
        ArrayList<Cancion> cancionesBusqueda = new ArrayList<Cancion>();
        String cadena = "";
        try{
            canciones = conector.getCanciones();
            for(int i=0; i < canciones.size(); i++){
                if(canciones.get(i).getTitulo().toLowerCase().contains(cadenaBusqueda.toLowerCase()) || canciones.get(i).getArtista().getNombre_artista().toLowerCase().contains(cadenaBusqueda.toLowerCase())){
                    cancionesBusqueda.add(canciones.get(i));
                }
            }
            cadena = generarCadenaResultadoCanciones(cancionesBusqueda);
        }
        catch(Exception e){
            e.printStackTrace();
            cadena = "ERROR";
        }
        return cadena;
    }

    public String getTamanioCancion(String id_cancion) {
        String tamanio = "0";
        for(int i=0; i < canciones.size() && tamanio=="0"; i++){
            if(canciones.get(i).getId_cancion().toString().equals(id_cancion)){
                tamanio = canciones.get(i).getFile_size_bytes().toString();
            }
        }
        return tamanio;
    }

    public String getDatosEnvioCancion(String id_cancion) {
        String datos = "";
        for(int i=0; i < canciones.size() && datos==""; i++){
            if(canciones.get(i).getId_cancion().toString().equals(id_cancion)){
                datos = canciones.get(i).getFile_size_bytes().toString() + "#" + canciones.get(i).getRuta();
            }
        }
        if(datos == "")
            datos = "ERROR";
        return datos;
    }

    public void actualizarReproducciones(String id_cancion) {
        for(int i=0; i < canciones.size(); i++){
            if(canciones.get(i).getId_cancion().toString().equals(id_cancion)){
                canciones.get(i).getEstadistica().setNum_reproducciones(canciones.get(i).getEstadistica().getNum_reproducciones()+1);
                conector.actualizarReproducciones(canciones.get(i));
            }
        }
    }
}
