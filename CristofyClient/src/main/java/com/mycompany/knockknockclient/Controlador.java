package com.mycompany.knockknockclient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Controlador {
    private final String PROTOCOLO = "PROTOCOLCRISTOFY1.0";
    private final String ACCION_LOGIN = "LOGIN";
    private final String ACCION_LOGOUT = "BYEBYE";
    private final String TOKEN = "WITH_TOKEN";

    public String getHora() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    public String obtenerCadenaEncriptada(String cadena) {
        String cadenaEncriptada ="";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(cadena.getBytes());
            cadenaEncriptada = Base64.getEncoder().encodeToString(md.digest());
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return cadenaEncriptada;
    }

    public String generarCadenaLogin(String usuario, String contrasena) {        
        return PROTOCOLO + "#" + getHora() + "#" + ACCION_LOGIN + "#" + usuario + "#" + obtenerCadenaEncriptada(contrasena);
        //return PROTOCOLO + "#" + getHora() + "#" + ACCION_LOGIN + "#" + usuario + "#" + contrasena;
    }

    public String generarCadenaLogout(CristofyClient cliente) {
        return PROTOCOLO + "#" + getHora() + "#" + ACCION_LOGOUT + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaGetUsuariosConectados(CristofyClient cliente) {
        return PROTOCOLO + "#" + getHora() + "#" + "GET_USERS_CONNECTED" + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaGetCancionesSistema(CristofyClient cliente){
        return PROTOCOLO + "#" + getHora() + "#" + "GET_MUSIC" + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaEscucharCancion(CristofyClient cliente, String idCancion) {
        return PROTOCOLO + "#" + getHora() + "#" + "GET_SONG" + "#" + idCancion + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaReadyToReceive(String idCancion, int tamPaquete, CristofyClient cliente) {
        return PROTOCOLO + "#" + getHora() + "#" + "READY_TO_RECEIVE" + "#" + idCancion + "#" + "SIZE_PACKAGE" + "#" + tamPaquete + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaGetSongFinished(String idCancion, CristofyClient cliente) {
        return PROTOCOLO + "#" + getHora() + "#" + "GET_SONG" + "#" + idCancion + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken() + "#" + "FINISHED";
    }

    public String generarCadenaBuscarCancionPorTexto(CristofyClient cliente, String cadenaBusqueda) {
        return PROTOCOLO + "#" + getHora() + "#" + "GET_MUSIC_BY_STRING" + "#" + cadenaBusqueda + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }

    public String generarCadenaVerDetallesArtistaCancion(CristofyClient cliente, String idCancion) {
        return PROTOCOLO + "#" + getHora() + "#" + "GET_ARTIST_SONG" + "#" + idCancion + "#" + cliente.getLogin() + "#" + TOKEN + "#" + cliente.getToken();
    }
}
