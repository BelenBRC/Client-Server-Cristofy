package com.mycompany.knockknockclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class ReproducirCancionThread implements Runnable{
    public Thread thr;
    private Player player;
    private ByteArrayInputStream bais;

    public ReproducirCancionThread(byte[] cancion){
        this.thr= new Thread(this);
        bais = new ByteArrayInputStream(cancion);
        try {
            player = new Player(bais);
        } catch (JavaLayerException e) {
            System.out.println("Error al crear el reproductor de la canción: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        reproducirCancion();
        try {
            detenerCancion();
        } catch (IOException e) {
            System.out.println("Error al liberar recursos tras reproducir la canción: " + e.getMessage());
        }
    }

    public void reproducirCancion(){
        try {
            player.play();
        } catch (Exception e) {
            System.out.println("Error al reproducir la canción: " + e.getMessage());
        }
    }

    public void detenerCancion() throws IOException{
        if (player != null) {
            player.close();
            player = null;
        }
        if (player != null) {
            player.close();
            player = null;
        }
        if (thr != null) {
            thr.interrupt();
            thr = null;
        }
    }
    
}
