package com.cristofy.servidor;

import com.cristofy.controlador.ProtocolCristofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

/**
 * @brief   Clase que maneja el hilo del servidor
 * @author belen
 */
public class MultiServerThread implements Runnable{
    private Thread thr;
    private Socket socket = null;
    private PrintWriter out;
    private BufferedReader in;
    private ProtocolCristofy protocolo;
    private boolean listening;
    private String inputLine;
    private String outputLine; 
    private JTextArea textArea;

    private ArrayList<EnviarCancionThread> hebrasCanciones;

    //Constructor
    /**
     * @brief   Constructor de la clase MultiServerThread con parámetros
     * @param socket    Socket del servidor
     * @param tArea     JTextArea donde se mostrarán los mensajes
     * @param listening true si el servidor está escuchando, false en caso contrario
     */
    public MultiServerThread(Socket socket, JTextArea tArea, boolean listening) {
        setThr(new Thread(this));
        setSocket(socket);
        setTextArea(tArea);
        setProtocolo(new ProtocolCristofy());
        setListening(listening);
        hebrasCanciones = new ArrayList<EnviarCancionThread>();
    }

    // Getters y Setters

    /**
     * @brief   Método que devuelve el hilo del servidor
     * @return  Hilo del servidor
     */
    public Thread getThr() {
        return thr;
    }

    /**
     * @brief   Método que establece el hilo del servidor
     * @param   thr Hilo del servidor
     */
    public void setThr(Thread thr) {
        this.thr = thr;
    }

    /**
     * @brief   Método que devuelve el socket del servidor
     * @return  Socket del servidor
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @brief   Método que establece el socket del servidor
     * @param   socket Socket del servidor
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @brief   Método que devuelve el PrintWriter del servidor
     * @return  PrintWriter del servidor
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * @brief   Método que establece el PrintWriter del servidor
     * @param   out PrintWriter del servidor
     */
    public void setOut(PrintWriter out) {
        this.out = out;
    }

    /**
     * @brief   Método que devuelve el BufferedReader del servidor
     * @return  BufferedReader del servidor
     */
    public BufferedReader getIn() {
        return in;
    }

    /**
     * @brief   Método que establece el BufferedReader del servidor
     * @param   in BufferedReader del servidor
     */
    public void setIn(BufferedReader in) {
        this.in = in;
    }

    /**
     * @brief   Método que devuelve el protocolo del servidor
     * @return  Protocolo del servidor
     */
    public ProtocolCristofy getProtocolo() {
        return protocolo;
    }

    /**
     * @brief   Método que establece el protocolo del servidor
     * @param   protocolo Protocolo del servidor
     */
    public void setProtocolo(ProtocolCristofy protocolo) {
        this.protocolo = protocolo;
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
     * @brief   Método que devuelve el mensaje de entrada
     * @return  Mensaje de entrada
     */
    public String getInputLine() {
        return inputLine;
    }

    /**
     * @brief   Método que establece el mensaje de entrada
     * @param   inputLine Mensaje de entrada
     */
    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    /**
     * @brief   Método que devuelve el mensaje de salida
     * @return  Mensaje de salida
     */
    public String getOutputLine() {
        return outputLine;
    }

    /**
     * @brief   Método que establece el mensaje de salida
     * @param   outputLine Mensaje de salida
     */
    public void setOutputLine(String outputLine) {
        this.outputLine = outputLine;
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
    

    //Métodos

    /**
     * @brief   Método que inicia el hilo del servidor
     */
    @Override
    public void run() {
            
        try{
            setOut(new PrintWriter(getSocket().getOutputStream(), true));
            setIn(new BufferedReader( 
                    new InputStreamReader(getSocket().getInputStream())));
            setOutputLine(getProtocolo().procesarEntrada(null));
            getOut().println(getOutputLine());

            while (!getSocket().isClosed() && (inputLine = getIn().readLine()) != null){
                
                setOutputLine(getProtocolo().procesarEntrada(getInputLine()));

                if(getOutputLine().contains("SEND_CANCION")){
                    try{
                        String[] datos = getOutputLine().split("@");
                        String idCancion = datos[1].split("#")[3];
                        //Comprobar si ya se está enviando la canción
                        boolean enviando = false;
                        for(int i=0; !enviando && i<hebrasCanciones.size(); i++){
                            if(hebrasCanciones.get(i).id_cancion.equals(idCancion)){
                                enviando = true;
                            }
                        }
                        if(!enviando){
                            EnviarCancionThread hebra = new EnviarCancionThread(getOut(), getOutputLine());
                            hebrasCanciones.add(hebra);
                            hebra.thr.start();
                        }
                    }
                    catch(Exception e){
                        System.out.println("Error al iniciar la hebra para enviar canción");
                    }
                }
                else if(getOutputLine().contains("FINISH_SENDING_CANCION")){
                    //Finalizar la hebra de envío de canción
                    String idCancion = getOutputLine().split("#")[1];
                    boolean encontrado = false;
                    for(int i=0; !encontrado && i<hebrasCanciones.size(); i++){
                        if(hebrasCanciones.get(i).id_cancion.equals(idCancion)){
                            if(hebrasCanciones.get(i).thr != null){
                                hebrasCanciones.get(i).thr.interrupt();
                                hebrasCanciones.get(i).thr = null;
                            }
                            hebrasCanciones.remove(i);
                            encontrado = true;
                            System.out.println("Hebra de envío de canción " + idCancion + " finalizada");
                        }
                    }
                }
                else{
                    getTextArea().append(getSocket().getInetAddress().getHostAddress() + ": " + getInputLine() + "\n");
                    getOut().println(getOutputLine());
                    getTextArea().append("\tYo: " + getOutputLine() + "\n");
                }

                if(getOutputLine().contains("SEEYOU")){
                    cerrarHebra();
                }
            }

            cerrarHebra();

        } catch (IOException e) {
        }
    
    }

    /**
     * @brief   Método que cierra la hebra
     * @throws IOException  Si hay un error al cerrar el socket
     */
    public void cerrarHebra() throws IOException {
        //Cerrar todas las hebras de envío de canciones
        for(EnviarCancionThread hebra : hebrasCanciones){
            hebra.thr.interrupt();
            hebra = null;
        }
        hebrasCanciones.clear();
        //Mandar al cliente que se desconecte
        if(getOut() != null){
            getOut().println(getProtocolo().generarCadenaSalidaLogout());
            getOut().close();
            setOut(null);
        }
        if(getIn() != null){
            getIn().close();
            setIn(null);
        }
        if(getThr() != null){
            getThr().interrupt();
            setThr(null);
        }
    }
}
