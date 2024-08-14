package com.cristofy.servidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayClientes {
    public ArrayList<MultiServerThread> clientes;

    private ReentrantLock cerrojo;
    private Condition puedeEliminar;
    
    public ArrayClientes() {
        clientes = new ArrayList<MultiServerThread>();
        cerrojo = new ReentrantLock(true);
        puedeEliminar = cerrojo.newCondition();
    }

    public void agregarCliente(MultiServerThread cliente) {
        cerrojo.lock();
        try {
            clientes.add(cliente);
            System.out.println("Cliente agregado. Quedan " + clientes.size() + " clientes conectados.");
            puedeEliminar.signalAll();
        } finally {
            cerrojo.unlock();
        }
    }

    public void eliminarClientesMuertos() throws InterruptedException {
        cerrojo.lock();
        try {
            while (clientes.isEmpty()) {
                puedeEliminar.await();
            }
            for(int i = 0; i < clientes.size(); i++){
                if(clientes.get(i) != null){
                    if(clientes.get(i).getThr() == null || clientes.get(i).getThr().isInterrupted() || !clientes.get(i).getThr().isAlive()){
                        String login = clientes.get(i).getProtocolo().getLoginClienteDesconectado();
                        clientes.get(i).getProtocolo().desconectar();
                        clientes.remove(i);
                        System.out.println("Cliente eliminado. Quedan " + clientes.size() + " clientes conectados.");
                        broadcastDesconexion(login);
                    }
                }
                else{
                    clientes.remove(i);
                    System.out.println("Cliente eliminado. Quedan " + clientes.size() + " clientes conectados.");
                }
            }
        } finally {
            cerrojo.unlock();
        }
    }

    public void desconectarClientes() throws IOException {
        cerrojo.lock();
        try {
            for(int i = 0; i < clientes.size(); i++){
                String login = clientes.get(i).getProtocolo().getLoginClienteDesconectado();
                clientes.get(i).getProtocolo().desconectar();
                clientes.get(i).cerrarHebra();
                clientes.remove(i);
                System.out.println("Cliente desconectado.");
                broadcastDesconexion(login);
            }
            clientes.clear();
            System.out.println("Clientes desconectados. Quedan " + clientes.size() + " clientes conectados.");
        } finally {
            cerrojo.unlock();
        }
    }

    private void broadcastDesconexion(String login) {
        if (login != "not-logged-in"){
            for(int i = 0; i < clientes.size(); i++){
                clientes.get(i).getOut().println(clientes.get(i).getProtocolo().mensajeClienteDesconectado(login));
            }
        }
    }
}
