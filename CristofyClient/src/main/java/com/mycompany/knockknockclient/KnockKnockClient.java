package com.mycompany.knockknockclient;

import java.io.IOException;

/**
 * @brief Clase KnockKnockClient que representa un cliente que se conecta a un servidor
 * @author belen
 */
public class KnockKnockClient {
    
    public static void main(String[] args) throws IOException {
        
        VistaCliente vista;
        
        try{
            vista = new VistaCliente();
            vista.setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Cliente iniciado");
        
    }
}