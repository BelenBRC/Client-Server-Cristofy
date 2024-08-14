package com.cristofy.controlador;

import java.util.ArrayList;

import com.cristofy.modelo.Conector;
import com.cristofy.modelo.Perfil;

public class ControladorUsuarios {
    private ArrayList<Perfil> perfiles;
    private Conector conector;

    public ControladorUsuarios() {
        try{
            conector = Conector.newInstance();
            conector.conectar();
        }
        catch(Exception e){
            e.printStackTrace();
        }   

        perfiles = new ArrayList<Perfil>();
        perfiles = conector.getPerfiles();
        //TODO importante no volver a pedir los perfiles a la BD para poder controlar conectados 
    }

    public Perfil comprobarCredenciales(String usuario, String contrasena) {
        Perfil perfil = null;

        for (int i = 0; i < perfiles.size() && perfil==null; i++){
            if (perfiles.get(i).getLogin().equals(usuario) && perfiles.get(i).getContrasenia().equals(contrasena) && !perfiles.get(i).isConectado()) {
                perfil = perfiles.get(i);
            }
        }
        return perfil;
    }

    public void desconectar(Perfil perfil) {
        perfil.setConectado(false);
        perfil.setToken_sesion("");
    }

    public String getUsuariosConectados() {
        StringBuilder usuariosConectados;
        usuariosConectados = new StringBuilder();
        String cadena = "";

        ArrayList<Perfil> perfilesConectados = new ArrayList<Perfil>();
        for (int i = 0; i < perfiles.size(); i++){
            if (perfiles.get(i).isConectado()) {
                perfilesConectados.add(perfiles.get(i));
            }
        }

        try{
            usuariosConectados.append(perfilesConectados.size());
            usuariosConectados.append("#");
            for (int i = 0; i < perfilesConectados.size(); i++){
                usuariosConectados.append(perfilesConectados.get(i).getLogin());
                usuariosConectados.append("|");
                usuariosConectados.append(perfilesConectados.get(i).getNombre_usuario());
                usuariosConectados.append("|");
                usuariosConectados.append(perfilesConectados.get(i).getApellidos());
                usuariosConectados.append("|");
                usuariosConectados.append(perfilesConectados.get(i).getTotal_playlists_guardadas());
                usuariosConectados.append("|");
                usuariosConectados.append(perfilesConectados.get(i).getTotal_canciones_en_playlists_guardadas());

                if(i < perfiles.size()-1)
                    usuariosConectados.append("@");
            }
            cadena = usuariosConectados.toString();
        }
        catch(Exception e){
            cadena = "ERROR";
        }

        return cadena;
    }
}
