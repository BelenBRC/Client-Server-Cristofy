package com.cristofy.servidor;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JTextArea;

import com.cristofy.modelo.Conector;

/**
 * @brief   Clase que maneja el servidor
 * @author belen
 */
public class CristofyServer {
    
    private Integer portNumber;
    private boolean listening = true;
    private ServerSocket serverSocket;
    private EscuchaThread escucha;
    private JTextArea textArea;
    private DesconectarClientesThread desconectar;
    private ArrayClientes clientes;
    private Conector conector;

    /**
     * @throws Exception 
     * @brief   Constructor por defecto de la clase CristofyServer
     */
    public CristofyServer() throws Exception {
        setPortNumber(6969);
        setListening(true);
        setServerSocket(null);
        setEscucha(null);
        setTextArea(null);

        setDesconectar(null);
        setClientes(new ArrayClientes());
        System.out.println("\tConectando a la base de datos");
        setConector(Conector.newInstance());
        System.out.println("\tConexión establecida. Iniciando servidor");
        getConector().conectar();
        System.out.println("\tServidor iniciado");
    }

    // Getters y Setters

    /**
     * @brief   Método que devuelve el número de puerto del servidor
     * @return  Número de puerto del servidor
     */
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * @brief   Método que establece el número de puerto del servidor
     * @param   portNumber Número de puerto del servidor
     */
    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @brief   Método que devuelve si el servidor está escuchando
     * @return  true si el servidor está escuchando, false en caso contrario
     */
    public boolean isListening() {
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
     * @brief   Método que devuelve el hilo de escucha del servidor
     * @return  Hilo de escucha del servidor
     */
    public EscuchaThread getEscucha() {
        return escucha;
    }

    /**
     * @brief   Método que establece el hilo de escucha del servidor
     * @param   escucha Hilo de escucha del servidor
     */
    public void setEscucha(EscuchaThread escucha) {
        this.escucha = escucha;
    }

    /**
     * @brief   Método que devuelve el textArea en el que se muestra la información del servidor
     * @return  TextArea en el que se muestra la información del servidor
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * @brief   Método que establece el textArea en el que se muestra la información del servidor
     * @param   textArea TextArea en el que se muestra la información del servidor
     */
    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * @brief   Método que devuelve el hilo de desconexión de clientes
     * @return  Hilo de desconexión de clientes
     */
    public DesconectarClientesThread getDesconectar() {
        return desconectar;
    }

    /**
     * @brief   Método que establece el hilo de desconexión de clientes
     * @param   desconectar Hilo de desconexión de clientes
     */
    public void setDesconectar(DesconectarClientesThread desconectar) {
        this.desconectar = desconectar;
    }

    /**
     * @brief   Método que devuelve el array de clientes
     * @return  Array de clientes
     */
    public ArrayClientes getClientes() {
        return clientes;
    }

    /**
     * @brief   Método que establece el array de clientes
     * @param   arrayClientes Array de clientes
     */
    public void setClientes(ArrayClientes arrayClientes) {
        this.clientes = arrayClientes;
    }

    /**
     * @brief   Método que devuelve el conector a la base de datos
     * @return  Conector a la base de datos
     */
    public Conector getConector() {
        return conector;
    }

    /**
     * @brief   Método que establece el conector a la base de datos
     * @param   conector Conector a la base de datos
     */
    public void setConector(Conector conector) {
        this.conector = conector;
    }

    
    // Métodos

    /**
     * @brief   Método que ejecuta el servidor
     * @throws IOException  Si hay un error al abrir el socket
     */
    public void ejecutarServer() throws IOException {
        //Abrir socket
        if(getServerSocket() != null){
            getServerSocket().close();
            setServerSocket(null);
        }
        setServerSocket(new ServerSocket(getPortNumber()));
        setListening(true);
        setEscucha(new EscuchaThread(serverSocket, listening, textArea, getClientes()));
        setDesconectar(new DesconectarClientesThread(serverSocket, getClientes()));
        getEscucha().getThr().start();
        getDesconectar().getThr().start();
    }

    /**
     * @brief   Método que cierra el servidor
     * @throws IOException  Si hay un error al cerrar el socket
     */
    public void cerrarServer() throws IOException {
        //Dejar de escuchar
        setListening(false);

        //Parar el hilo de escucha
        if(getEscucha() != null){
            getEscucha().getThr().interrupt();
            setEscucha(null);
        }

        //Parar el hilo de desconexión de clientes
        if(getDesconectar() != null){
            getDesconectar().getThr().interrupt();
            setDesconectar(null);
        }

        //Desconectar clientes
        getClientes().desconectarClientes();

        //Cerrar el socket
        if(getServerSocket() != null){
            if(!getServerSocket().isClosed())
                getServerSocket().close();
            setServerSocket(null);
        }
    }
     

}
