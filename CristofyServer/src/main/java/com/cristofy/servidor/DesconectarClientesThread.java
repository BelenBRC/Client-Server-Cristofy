package com.cristofy.servidor;

import java.net.ServerSocket;

public class DesconectarClientesThread implements Runnable {
    private Thread thr;
    private ServerSocket serverSocket;
    private ArrayClientes clientes;

    /**
     * @brief   Método que devuelve el hilo de desconexión de clientes
     * @param serverSocket  Socket del servidor
     * @param clientes      Lista de clientes conectados al servidor
     */
    public DesconectarClientesThread(ServerSocket serverSocket, ArrayClientes clientes) {
        setThr(new Thread(this));
        setServerSocket(serverSocket);
        setClientes(clientes);
    }

    // Getters y Setters

    /**
     * @brief   Método que devuelve el hilo de desconexión de clientes
     * @return  Hilo de desconexión de clientes
     */
    public Thread getThr() {
        return thr;
    }

    /**
     * @brief   Método que establece el hilo de desconexión de clientes
     * @param   thr Hilo de desconexión de clientes
     */
    public void setThr(Thread thr) {
        this.thr = thr;
    }

    /**
     * @brief   Método que devuelve el socket del servidor
     * @return  Socket del servidor
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * @brief   Método que establece el socket del servidor
     * @param   serverSocket Socket del servidor
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * @brief   Método que devuelve la lista de clientes conectados al servidor
     * @return  Lista de clientes conectados al servidor
     */
    public ArrayClientes getClientes() {
        return clientes;
    }

    /**
     * @brief   Método que establece la lista de clientes conectados al servidor
     * @param   clientes Lista de clientes conectados al servidor
     */
    public void setClientes(ArrayClientes clientes) {
        this.clientes = clientes;
    }

    /**
     * @brief   Método que ejecuta el hilo de desconexión de clientes
     */
    @Override
    public void run() {
        boolean salir = false;
        while (!salir) {
            if(getServerSocket().isClosed() || getServerSocket() == null){
                salir = true;
            }
            if(!salir){
                try {
                    getClientes().eliminarClientesMuertos();
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println("Cerrando la hebra de desconexión de clientes...");
    }

}