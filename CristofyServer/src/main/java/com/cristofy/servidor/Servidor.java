package com.cristofy.servidor;

import com.cristofy.vista.VistaServer;

/**
 * @brief   Clase principal del servidor que inicia la vista del servidor
 * @author belen
 */
public class Servidor {

    public static void main(String[] args) {
        VistaServer vista;
        try{
            vista = new VistaServer();
            vista.setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
