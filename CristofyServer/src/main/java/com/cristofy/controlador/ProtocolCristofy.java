package com.cristofy.controlador;

import java.util.Random;

import com.cristofy.modelo.Perfil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * @brief   Clase que maneja el protocolo de comunicación con el cliente
 */
public class ProtocolCristofy {
    private static final String VERSION_PROTOCOLO = "PROTOCOLCRISTOFY1.0";
    private static final String WITH_TOKEN = "WITH_TOKEN";
    private static final String SIZE = "SIZE";
    private static final String CLIENT_DISC = "IS_DISCONNECTED";

    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LONGITUD_PALABRA_SECRETA = 20;
    private static final String[] ACCIONES = {  "LOGIN",                //0
                                                "BYEBYE",               //1
                                                "GET_USERS_CONNECTED",  //2
                                                "GET_MUSIC",            //3
                                                "GET_SONG",             //4
                                                "READY_TO_RECEIVE",     //5
                                                "FINISHED",             //6
                                                "GET_MUSIC_BY_STRING",  //7
                                                "GET_ARTIST_SONG"};     //8

    private static final String[] RESPUESTAS = {"WELLCOME",             //0
                                                "SEEYOU",               //1
                                                "OK_CONNECTED_USERS",   //2
                                                "OK_MUSIC_FOR",         //3
                                                "OK_SONG_FOR",          //4
                                                "OK_SONG",              //5
                                                "OK_MUSIC_BY_STRING_FOR",//6
                                                "OK_ARTIST_FOR"};       //7

    private static final String[] ERRORES = {   "ERROR",                //0
                                                "BAD_CREDENTIALS",      //1
                                                "CANT_GET_USERS",       //2
                                                "CANT_GET_MUSIC",       //3
                                                "CANT_GET_SONG",        //4
                                                "CANT_GET_ARTIST"};     //5      

    // Partes del mensaje
    private String usuario;
    private String contrasena;
    private String protocolo;
    private String hora;
    private String accion;
    private String withToken;
    private String tokenUsuario;
    private String id_cancion;
    private String tamanio_paquete;
    private String modificadorAccion;
    private String cadenaBusqueda;

    // Almacenamiento de datos
    private String palabraSecreta;
    private boolean valido = true;

    private ControladorUsuarios controladorUsuarios;
    public Perfil perfil;
    private ControladorCanciones controladorCanciones;

    public ProtocolCristofy() {
        controladorUsuarios = new ControladorUsuarios();
        perfil = null;
        controladorCanciones = new ControladorCanciones();

        usuario = "";
        contrasena = "";
        protocolo = "";
        hora = "";
        accion = "";
        withToken = "";
        tokenUsuario = "";
        id_cancion = "";
        tamanio_paquete = "";
        palabraSecreta = "";
        modificadorAccion = "";
        cadenaBusqueda = "";
        valido = true;
    }

    public String procesarEntrada(String cadenaEntrada) {
        valido = true;
        String cadenaSalida = "";

        if(cadenaEntrada == null){
            cadenaSalida = "Identificate";
        }
        else{
            dividirCadenaEntrada(cadenaEntrada);

            if(valido){
                if(accion.equals(ACCIONES[0])){ 
                    if(cadenaLoginValida()){
                        perfil.setConectado(true);
                        cadenaSalida = generarCadenaSalidaLoginCorrecto();
                    }
                    else{
                        cadenaSalida = generarCadenaError();
                    }
                }
                else{
                    if(cadenaConTokenValida()){
                        if(accion.equals(ACCIONES[1])){
                            perfil.setConectado(false);
                            cadenaSalida = generarCadenaSalidaLogout();
                        }
                        else if(accion.equals(ACCIONES[2])){
                            cadenaSalida = generarCadenaSalidaUsuariosConectados();
                        }
                        else if(accion.equals(ACCIONES[3])){
                            cadenaSalida = generarCadenaSalidaCancionesSistema();
                        }
                        else if(accion.equals(ACCIONES[4]) && modificadorAccion.equals("")){
                            cadenaSalida = generarCadenaSalidaGetSong();
                        }
                        else if(accion.equals(ACCIONES[4]) && modificadorAccion.equals(ACCIONES[6])){
                            modificadorAccion = "";
                            cadenaSalida = generarCadenaEnvioCancionFinalizado();
                            controladorCanciones.actualizarReproducciones(id_cancion);
                        }
                        else if(accion.equals(ACCIONES[5])){
                            try{
                                cadenaSalida = generarCadenaEnviarCancion();
                            }
                            catch(Exception e){
                                cadenaSalida = generarCadenaSalidaErrorCantGetSong();
                            }
                        }
                        else if(accion.equals(ACCIONES[7])){
                            cadenaSalida = generarCadenaSalidaBuscarCanciones();
                        }
                        else if(accion.equals(ACCIONES[8])){
                            cadenaSalida = generarCadenaSalidaDetallesArtista();
                        }
                    }
                }
            }
            else{
                cadenaSalida = generarCadenaError();
            }
        }

        return cadenaSalida;
    }

    public void dividirCadenaEntrada(String cadenaEntrada) {
        String[] partes = cadenaEntrada.split("#");
        if (partes.length == 5) {
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            usuario = partes[3];
            contrasena = partes[4].trim();       
        }
        else if (partes.length == 6){
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            usuario = partes[3];
            withToken = partes[4];
            tokenUsuario = partes[5].trim();
        }
        else if (partes.length == 7 && (partes[2].equals(ACCIONES[4]) || partes[2].equals(ACCIONES[8]))){
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            id_cancion = partes[3];
            usuario = partes[4];
            withToken = partes[5];
            tokenUsuario = partes[6].trim();
        }
        else if (partes.length == 7 && partes[2].equals(ACCIONES[7])){
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            cadenaBusqueda = partes[3].trim();
            usuario = partes[4];
            withToken = partes[5];
            tokenUsuario = partes[6].trim();
        }
        else if (partes.length == 8 && partes[2].equals(ACCIONES[4]) && partes[7].equals(ACCIONES[6])){
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            id_cancion = partes[3];
            usuario = partes[4];
            withToken = partes[5];
            tokenUsuario = partes[6];
            modificadorAccion = partes[7].trim();
        }
        else if(partes.length == 9 && partes[2].equals(ACCIONES[5])){
            protocolo = partes[0];
            hora = partes[1];
            accion = partes[2];
            id_cancion = partes[3];
            tamanio_paquete = partes[5];
            usuario = partes[6];
            withToken = partes[7];
            tokenUsuario = partes[8].trim();
        }
        else {
            usuario = "";
            contrasena = "";
            protocolo = "";
            accion = "";
            valido = false;
        }

        if(valido){
            if(!protocoloValido() || !accionValida()){
                valido = false;
            }
        }
    }

    private boolean protocoloValido() {
        boolean esValido = false;
        if (protocolo.equals(VERSION_PROTOCOLO)) {
            esValido = true;
        }
        return esValido;
    }

    private boolean accionValida() {
        boolean esValida = false;
        for (int i = 0; i < ACCIONES.length && !esValida; i++) {
            if (accion.equals(ACCIONES[i])) {
                esValida = true;
            }
        }
        return esValida;
    }

    private boolean usuarioContraseniaValidos() {
        boolean esValido = false;
        
        if (controladorUsuarios.comprobarCredenciales(usuario, contrasena) != null) {
            perfil = controladorUsuarios.comprobarCredenciales(usuario, contrasena);
            esValido = true;
        }

        return esValido;
    }

    private boolean cadenaLoginValida() {
        if (!usuarioContraseniaValidos()) {
            valido = false;
        }
        else{
            genererPalabraSecretaAleatoria();
            perfil.setToken_sesion(palabraSecreta);
        }
        return valido;
    }

    private boolean cadenaConTokenValida() {
        if (!usuario.equals(perfil.getLogin())) {
            valido = false;
        }
        else if (!withToken.equals(WITH_TOKEN)) {
            valido = false;
        }
        else if (!tokenUsuario.equals(perfil.getToken_sesion().trim())) {
            valido = false;
        } 

        return valido;
    }

    private String generarCadenaSalidaLoginCorrecto(){
        StringBuilder cadenaSalida = new StringBuilder();

        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(this.hora);
        cadenaSalida.append("#");
        cadenaSalida.append(RESPUESTAS[0]);
        cadenaSalida.append("#");
        cadenaSalida.append(this.usuario);
        cadenaSalida.append("#");
        cadenaSalida.append(WITH_TOKEN);
        cadenaSalida.append("#");
        cadenaSalida.append(this.palabraSecreta);

        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaLogout(){
        StringBuilder cadenaSalida = new StringBuilder();

        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(this.hora);
        cadenaSalida.append("#");
        cadenaSalida.append(RESPUESTAS[1]);
        cadenaSalida.append("#");
        cadenaSalida.append(this.usuario);
        cadenaSalida.append("#");
        cadenaSalida.append(WITH_TOKEN);
        cadenaSalida.append("#");
        cadenaSalida.append(this.palabraSecreta);

        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaUsuariosConectados(){
        StringBuilder cadenaSalida = new StringBuilder();
        String usuariosConectados = controladorUsuarios.getUsuariosConectados();
        if(usuariosConectados.equals("ERROR")){
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(ERRORES[0]);
            cadenaSalida.append("#");
            cadenaSalida.append(ERRORES[2]);
        }
        else{
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[2]);
            cadenaSalida.append("#");
            cadenaSalida.append(this.usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(this.palabraSecreta);
            cadenaSalida.append("#");
            cadenaSalida.append(usuariosConectados);
        }

        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaErrorCantGetMusic(){
        StringBuilder cadenaSalida = new StringBuilder();

        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(hora);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[0]);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[3]);

        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaCancionesSistema(){
        StringBuilder cadenaSalida = new StringBuilder();
        String cancionesSistema = controladorCanciones.getCancionesSistema();
        //String cancionesSistema = "2#Tu calorro|Estopa|23402|254@La raja de tu falda|Estopa|132232|368";
        if(cancionesSistema.equals("ERROR")){
            cadenaSalida.append(generarCadenaSalidaErrorCantGetMusic());
        }
        else{
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[3]);
            cadenaSalida.append("#");
            cadenaSalida.append(this.usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(this.palabraSecreta);
            cadenaSalida.append("#");
            cadenaSalida.append(cancionesSistema);
        }

        return cadenaSalida.toString();
    }

    private String generarCadenaSalidaErrorCantGetArtist(){
        StringBuilder cadenaSalida = new StringBuilder();
        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(hora);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[0]);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[5]);
        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaDetallesArtista(){
        StringBuilder cadenaSalida = new StringBuilder();
        String detallesArtista = controladorCanciones.getDetallesArtista(id_cancion);
        if(detallesArtista.equals("ERROR")){
            cadenaSalida.append(generarCadenaSalidaErrorCantGetArtist());
        }
        else{
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[7]);
            cadenaSalida.append("#");
            cadenaSalida.append(this.usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(this.palabraSecreta);
            cadenaSalida.append("#");
            cadenaSalida.append(detallesArtista);
        }

        return cadenaSalida.toString();
    }

    private String generarCadenaSalidaErrorCantGetSong() {
        StringBuilder cadenaSalida = new StringBuilder();
        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(hora);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[0]);
        cadenaSalida.append("#");
        cadenaSalida.append(ERRORES[4]);
        return cadenaSalida.toString();
    }

    public String generarCadenaSalidaGetSong(){
        StringBuilder cadenaSalida = new StringBuilder();
        String cancion = controladorCanciones.getTamanioCancion(id_cancion);
        if(cancion.equals("ERROR")){
            cadenaSalida.append(generarCadenaSalidaErrorCantGetSong());
        }
        else{
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[4]);
            cadenaSalida.append("#");
            cadenaSalida.append(id_cancion);
            cadenaSalida.append("#");
            cadenaSalida.append(this.usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(this.palabraSecreta);
            cadenaSalida.append("#");
            cadenaSalida.append(SIZE);
            cadenaSalida.append("#");
            cadenaSalida.append(cancion); //file_size_bytes
        }
        return cadenaSalida.toString();
    }

    public String mensajeClienteDesconectado(String login){
        StringBuilder cadenaSalida = new StringBuilder();

        cadenaSalida.append(VERSION_PROTOCOLO);
        cadenaSalida.append("#");
        cadenaSalida.append(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        cadenaSalida.append("#");
        cadenaSalida.append(login);
        cadenaSalida.append("#");
        cadenaSalida.append(CLIENT_DISC);

        return cadenaSalida.toString();
    }

    private void genererPalabraSecretaAleatoria(){
        Random aleatorio = new Random();
        StringBuilder palabraSec = new StringBuilder();

        for (int i = 0; i < LONGITUD_PALABRA_SECRETA; i++) {
            palabraSec.append(ALFABETO.charAt(aleatorio.nextInt(ALFABETO.length())));
        }
        
        
        palabraSecreta = obtenerCadenaEncriptada(palabraSec.toString());
    }

    private String generarCadenaError(){
        StringBuilder cadenaError = new StringBuilder();

        cadenaError.append(VERSION_PROTOCOLO);
        cadenaError.append("#");
        cadenaError.append(this.hora);
        cadenaError.append("#");
        cadenaError.append(ERRORES[0]);
        cadenaError.append("#");
        cadenaError.append(ERRORES[1]);

        return cadenaError.toString();
    }

    private String generarCadenaEnviarCancion(){
        String datosCancion = controladorCanciones.getDatosEnvioCancion(id_cancion);
        if(datosCancion.equals("ERROR")){
            return generarCadenaSalidaErrorCantGetSong();
        }
        else{
            StringBuilder cadenaSalida = new StringBuilder();

            cadenaSalida.append("SEND_CANCION");
            cadenaSalida.append("@");
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[5]);
            cadenaSalida.append("#");
            cadenaSalida.append(id_cancion);
            cadenaSalida.append("#");
            cadenaSalida.append("FOR");
            cadenaSalida.append("#");
            cadenaSalida.append(usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(palabraSecreta);
            cadenaSalida.append("@");
            cadenaSalida.append(tamanio_paquete);
            cadenaSalida.append("#");
            cadenaSalida.append(datosCancion);

            return cadenaSalida.toString();
        }
    }

    private String generarCadenaSalidaBuscarCanciones(){
        StringBuilder cadenaSalida = new StringBuilder();
        String cancionesSistema = controladorCanciones.getCancionesPorString(cadenaBusqueda);
        if(cancionesSistema.equals("ERROR")){
            cadenaSalida.append(generarCadenaSalidaErrorCantGetMusic());
        }
        else{
            cadenaSalida.append(VERSION_PROTOCOLO);
            cadenaSalida.append("#");
            cadenaSalida.append(hora);
            cadenaSalida.append("#");
            cadenaSalida.append(RESPUESTAS[6]);
            cadenaSalida.append("#");
            cadenaSalida.append(this.usuario);
            cadenaSalida.append("#");
            cadenaSalida.append(WITH_TOKEN);
            cadenaSalida.append("#");
            cadenaSalida.append(this.palabraSecreta);
            cadenaSalida.append("#");
            cadenaSalida.append(cancionesSistema);
        }

        return cadenaSalida.toString();
    }

    private String generarCadenaEnvioCancionFinalizado(){
        return "FINISH_SENDING_CANCION#" + id_cancion;
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

    public void desconectar() {
        if(perfil != null)
            controladorUsuarios.desconectar(perfil);
    }

    public String getLoginClienteDesconectado(){
        if(perfil != null)
            return perfil.getLogin();
        else
            return "not-logged-in";
    }
}