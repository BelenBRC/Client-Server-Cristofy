package com.mycompany.knockknockclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @brief Clase CristofyClient que representa un cliente que se conecta a un servidor
 * @author belen
 */
public class CristofyClient {
    
    private String ip;
    private Integer puerto;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String login;
    private String token;

    private ReproducirCancionThread escuchandoCancion;

    /**
     * @brief Constructor por defecto de la clase CristofyClient
     */
    public CristofyClient() {}

    //Setters y Getters

    /**
     * @brief   Setter del atributo ip. 
     *          Establece la IP del servidor al que se conecta el cliente
     * @param ip    IP del servidor al que se conecta el cliente
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @brief   Getter del atributo ip. 
     *          Devuelve la IP del servidor al que se conecta el cliente
     * @return  IP del servidor al que se conecta el cliente
     */
    public String getIp() {
        return ip;
    }

    /**
     * @brief   Setter del atributo puerto. 
     *          Establece el puerto del servidor al que se conecta el cliente
     * @param puerto    Puerto del servidor al que se conecta el cliente
     */
    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    /**
     * @brief   Getter del atributo puerto. 
     *          Devuelve el puerto del servidor al que se conecta el cliente
     * @return  Puerto del servidor al que se conecta el cliente
     */
    public Integer getPuerto() {
        return puerto;
    }

    /**
     * @brief   Setter del atributo socket. 
     *          Establece el socket del cliente
     * @param socket    Socket del cliente
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @brief   Getter del atributo socket
     *          Devuelve el socket del cliente
     * @return  Socket del cliente
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @brief   Setter del atributo out
     *          Establece el PrintWriter del cliente
     * @param out   PrintWriter del cliente
     */
    public void setOut(PrintWriter out) {
        this.out = out;
    }

    /**
     * @brief   Getter del atributo out
     *          Devuelve el PrintWriter del cliente
     * @return  PrintWriter del cliente
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * @brief   Setter del atributo in
     *          Establece el BufferedReader del cliente
     * @param in    BufferedReader del cliente
     */
    public void setIn(BufferedReader in) {
        this.in = in;
    }

    /**
     * @brief   Getter del atributo in
     *          Devuelve el BufferedReader del cliente
     * @return  BufferedReader del cliente
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * @brief   Setter del atributo login
     *          Establece el login del cliente
     * @param login Login del cliente
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @brief   Getter del atributo login
     *          Devuelve el login del cliente
     * @return  Login del cliente
     */
    public String getLogin() {
        return login;
    }

    /**
     * @brief   Setter del atributo token
     *          Establece el token del cliente
     * @param token Token del cliente
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @brief   Getter del atributo token
     *          Devuelve el token del cliente
     * @return  Token del cliente
     */
    public String getToken() {
        return token;
    }

    /**
     * @brief   Setter del atributo escuchandoCancion
     *          Establece el hilo que está reproduciendo una canción
     * @param escuchandoCancion Hilo que está reproduciendo una canción
     */
    public void setEscuchandoCancion(ReproducirCancionThread escuchandoCancion) {
        this.escuchandoCancion = escuchandoCancion;
    }

    /**
     * @brief   Getter del atributo escuchandoCancion
     *          Devuelve el hilo que está reproduciendo una canción
     * @return  Hilo que está reproduciendo una canción
     */
    public ReproducirCancionThread getEscuchandoCancion() {
        return escuchandoCancion;
    }

    //Métodos  

    /**
     * @brief   Método que establece la conexión del cliente con el servidor
     * @param IP        IP del servidor al que se conecta el cliente    
     * @param puerto    Puerto del servidor al que se conecta el cliente
     * @throws UnknownHostException En caso de que la IP no sea válida
     * @throws IOException          En caso de que haya un error en la conexión
     */
    public void conectar(String IP, Integer puerto) throws UnknownHostException, IOException{
        setIp(IP);
        setPuerto(puerto);

        ejecutarCliente();
    }
    
    /**
     * @brief   Método que ejecuta el cliente
     * @throws UnknownHostException En caso de que la IP no sea válida
     * @throws IOException          En caso de que haya un error en la conexión
     */
    private void ejecutarCliente() throws UnknownHostException, IOException{
        //Abrir socket
        setSocket(new Socket(ip, puerto));

        //Abrir canales de lectura y escritura
        setOut(new PrintWriter(getSocket().getOutputStream(), true));
        setIn(new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
    }

    /**
     * @brief   Método que cierra la conexión del cliente con el servidor
     * @throws IOException  En caso de que haya un error al cerrar la conexión
     */
    public void cerrarCliente() throws IOException{
        System.out.println("Desconectando del servidor");
        //Detener hilo que está reproduciendo una canción
        if(getEscuchandoCancion() != null && getEscuchandoCancion().thr != null){
            getEscuchandoCancion().detenerCancion();
            setEscuchandoCancion(null);
        }
        //Cerrar canales
        getOut().close();
        getIn().close();
        //Cerrar socket
        getSocket().close();
        setLogin(null);
        setToken(null);
    }

    /**
     * @brief   Método que establece la cancion que está escuchando el cliente e inicia el hilo que la reproduce
     * @param cancion   Bytes de la canción que está escuchando el cliente
     * @throws IOException 
     */
    public void escucharCancion(byte[] cancion) throws IOException{
        stopCancion();
        setEscuchandoCancion(new ReproducirCancionThread(cancion));
        getEscuchandoCancion().thr.start();
    }

    /**
     * @brief   Método que detiene la canción que está escuchando el cliente
     * @throws IOException 
     */
    public void stopCancion() throws IOException{
        if(getEscuchandoCancion() != null && getEscuchandoCancion().thr != null){
            getEscuchandoCancion().detenerCancion();
            setEscuchandoCancion(null);
        }
    }
    
}
