package com.cristofy.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JTextArea;

/**
 * @brief   Clase EscuchaThread que maneja el hilo de escucha del servidor
 * @author belen
 */
public class EscuchaThread implements Runnable{
    private Thread thr;
    private ServerSocket serverSocket;
    private boolean listening = true;
    private JTextArea textArea;
    private ArrayClientes clientes;

    /**
     * @brief   Constructor de la clase EscuchaThread con parámetros
     * @param serverSocket  Socket del servidor
     * @param listening     true si el servidor está escuchando, false en caso contrario
     * @param tArea         JTextArea donde se mostrarán los mensajes
     * @param clientes      Lista de clientes conectados al servidor
     */
    public EscuchaThread(ServerSocket serverSocket, boolean listening, JTextArea tArea, ArrayClientes clientes) {
        setThr(new Thread(this));
        setServerSocket(serverSocket);
        setListening(listening);
        setTextArea(tArea);
        setClientes(clientes);
    }

    // Getters y Setters

    /**
     * @brief   Método que devuelve el hilo de escucha del servidor
     * @return  Hilo de escucha del servidor
     */
    public Thread getThr() {
        return thr;
    }

    /**
     * @brief   Método que establece el hilo de escucha del servidor
     * @param   thr Hilo de escucha del servidor
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
     * @brief   Método que devuelve si el servidor está escuchando
     * @return  true si el servidor está escuchando, false en caso contrario
     */
    public boolean getListening() {
        return listening;
    }

    /**
     * @brief   Método que establece si el servidor está escuchando
     * @param   listening true si el servidor está escuchando, false en caso contrario
     */
    public void setListening(boolean listening) {
        this.listening = listening;
    }

    /**
     * @brief   Método que devuelve el JTextArea donde se mostrarán los mensajes
     * @return  JTextArea donde se mostrarán los mensajes
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * @brief   Método que establece el JTextArea donde se mostrarán los mensajes
     * @param   textArea JTextArea donde se mostrarán los mensajes
     */
    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
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

    // Métodos

    /**
     * @brief   Método que inicia el hilo de escucha del servidor
     */
    @Override
    public void run() {
        boolean salir = false;
        while (!salir) {
            if (getListening() == false || getServerSocket().isClosed() || getServerSocket() == null){
                salir = true;
            }
            else{
                try {
                    MultiServerThread nuevoCliente = new MultiServerThread(getServerSocket().accept(), getTextArea(), getListening());
                    boolean repetido = false;
                    //Comprobar que no se repite la IP del cliente
                    for(int i=0; i<getClientes().clientes.size(); i++){
                        if(getClientes().clientes.get(i).getSocket().getInetAddress().equals(nuevoCliente.getSocket().getInetAddress())){
                            repetido = true;
                            nuevoCliente.getSocket().close();
                            nuevoCliente = null;
                        }
                    }
                    if(!repetido){
                        nuevoCliente.getThr().start();
                        getClientes().agregarCliente(nuevoCliente);
                    }
                } catch (IOException e) {
                    salir = true;
                } catch (NullPointerException e){
                    salir = true;
                } catch (Exception e){
                    salir = true;
                }
            }
        }
        System.out.println("Hilo de escucha finalizando...");
    }
    
}
