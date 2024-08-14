package com.mycompany.knockknockclient;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/**
 * @brief Clase LeerServidorThread que representa un hilo que lee los mensajes del servidor
 * @author belen
 */
public class LeerServidorThread implements Runnable {
    private static final Integer TAM_PAQUETE = 1024;
    private Thread thr;
    private CristofyClient cliente;
    private JTextArea textArea;
    private JTable tablaUsuarios;
    private JTable tablaCanciones;
    private JPanel progresoDescarga;
    private JLabel porcentajeDescarga;
    private JProgressBar barraProgreso;
    private JLabel nombreArtista;
    private Controlador controlador;

    public ArrayList<CancionThread> hilosCanciones;
    
    /**
     * @brief   Constructor por parámetros de la clase LeerServidorThread
     * @param clienteP                  Cliente al que pertenece el hilo
     * @param tArea                     Área de texto en la que se muestran los mensajes del servidor
     * @param tablaUsers                Tabla en la que se muestran los usuarios conectados
     * @param tablaCanciones            Tabla en la que se muestran las canciones del sistema
     * @param jPanelProgresoDescarga    Panel en el que se muestra el progreso de la descarga de una canción
     * @param porcentajeDescarga        Etiqueta en la que se muestra el porcentaje de descarga de una canción
     * @param barraProgreso             Barra de progreso que muestra el progreso de la descarga de una canción
     * @param nombreArtista             Etiqueta en la que se muestra el nombre del artista de una canción
     */
    public LeerServidorThread(CristofyClient clienteP, JTextArea tArea, JTable tablaUsers, JTable tablaCanciones, JPanel jPanelProgresoDescarga, JLabel porcentajeDescarga, JProgressBar barraProgreso, JLabel nombreArtista){
        setThr(new Thread(this));
        setCliente(clienteP);
        setTextArea(tArea);
        setTablaUsuarios(tablaUsers);
        setTablaCanciones(tablaCanciones);
        controlador = new Controlador();
        hilosCanciones = new ArrayList<CancionThread>();
        this.progresoDescarga = jPanelProgresoDescarga;
        this.porcentajeDescarga = porcentajeDescarga;
        this.barraProgreso = barraProgreso;
        this.nombreArtista = nombreArtista;
    }

    //Setters y Getters

    /**
     * @brief   Setter del atributo thr. 
     *          Establece el hilo que lee los mensajes del servidor
     * @param thr   Hilo que lee los mensajes del servidor
     */
    public void setThr(Thread thr) {
        this.thr = thr;
    }

    /**
     * @brief   Getter del atributo thr. 
     *          Devuelve el hilo que lee los mensajes del servidor
     * @return  Hilo que lee los mensajes del servidor
     */
    public Thread getThr() {
        return thr;
    }

    /**
     * @brief   Setter del atributo cliente. 
     *          Establece el cliente al que pertenece el hilo
     * @param cliente   Cliente al que pertenece el hilo
     */
    public void setCliente(CristofyClient cliente) {
        this.cliente = cliente;
    }

    /**
     * @brief   Getter del atributo cliente. 
     *          Devuelve el cliente al que pertenece el hilo
     * @return  Cliente al que pertenece el hilo
     */
    public CristofyClient getCliente() {
        return cliente;
    }

    /**
     * @brief   Setter del atributo textArea. 
     *          Establece el área de texto en la que se muestran los mensajes del servidor
     * @param textArea  Área de texto en la que se muestran los mensajes del servidor
     */
    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * @brief   Getter del atributo textArea. 
     *          Devuelve el área de texto en la que se muestran los mensajes del servidor
     * @return  Área de texto en la que se muestran los mensajes del servidor
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * @brief   Setter del atributo tablaUsuarios. 
     *          Establece la tabla en la que se muestran los usuarios conectados
     * @param tablaUsuarios Tabla en la que se muestran los usuarios conectados
     */
    public void setTablaUsuarios(JTable tablaUsuarios) {
        this.tablaUsuarios = tablaUsuarios;
    }

    /**
     * @brief   Getter del atributo tablaUsuarios. 
     *          Devuelve la tabla en la que se muestran los usuarios conectados
     * @return  Tabla en la que se muestran los usuarios conectados
     */
    public JTable getTablaUsuarios() {
        return tablaUsuarios;
    }

    /**
     * @brief   Setter del atributo tablaCanciones. 
     *          Establece la tabla en la que se muestran las canciones del sistema
     * @param tablaCanciones Tabla en la que se muestran las canciones del sistema
     */
    public void setTablaCanciones(JTable tablaCanciones) {
        this.tablaCanciones = tablaCanciones;
    }

    /**
     * @brief   Getter del atributo tablaCanciones. 
     *          Devuelve la tabla en la que se muestran las canciones del sistema
     * @return  Tabla en la que se muestran las canciones del sistema
     */
    public JTable getTablaCanciones() {
        return tablaCanciones;
    }

    /**
     * @brief   Método que inicia el hilo que lee los mensajes del servidor
     */
    @Override
    public void run(){
         try {
            String line;
            while ((line = getCliente().getIn().readLine()) != null) {
                textArea.append(line + "\n");
                if(line.contains("WELCOME") || line.contains("WELLCOME")){
                    buscarToken(line);
                }
                else if(line.contains("SEEYOU")){
                    getCliente().cerrarCliente();
                    break;
                }
                else if(line.contains("OK_CONNECTED_USERS")){
                    cargarUsuariosConectados(line);
                }
                else if(line.contains("OK_MUSIC_FOR")){
                    cargarCancionesSistema(line);
                }
                else if(line.contains("IS_DISCONNECTED")){
                    if(tablaUsuarios.getModel().getRowCount() != 0){
                        String[] mensaje = line.split("#");
                        String usuarioDesconectado = mensaje[2];
                        DefaultTableModel model = (DefaultTableModel) getTablaUsuarios().getModel();
                        for(int i = 0; i < model.getRowCount(); i++){
                            if(model.getValueAt(i, 0).equals(usuarioDesconectado)){
                                model.removeRow(i);
                            }
                        }
                    }
                }
                else if(line.contains("OK_SONG_FOR")){
                    String idCancion = "";
                    String tamCancion = "";
                    String[] mensaje = line.split("#");
                    for(int i = 0; i < mensaje.length; i++){
                        if(mensaje[i].equals(getCliente().getToken())){
                            tamCancion = mensaje[i+2];
                        }
                        if(mensaje[i].equals("OK_SONG_FOR")){
                            idCancion = mensaje[i+1];
                        }
                    }
                    try{
                        //Crear hilo para recibir la canción
                        CancionThread hiloCancion = new CancionThread(TAM_PAQUETE, Integer.parseInt(tamCancion), idCancion, this, progresoDescarga, porcentajeDescarga, barraProgreso);
                        hilosCanciones.add(hiloCancion);
                        //Mandar mensaje al servidor para recibir la canción
                        getCliente().getOut().println(controlador.generarCadenaReadyToReceive(idCancion, TAM_PAQUETE, cliente));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if(line.contains("ID_PACKAGE")){
                    String idCancion = "";
                    String[] mensaje = line.split("#");
                    for(int i = 0; i < mensaje.length; i++){
                        if(mensaje[i].equals("OK_SONG")){
                            idCancion = mensaje[i+1];
                        }
                    }
                    for(int i = 0; i < hilosCanciones.size(); i++){
                        if(hilosCanciones.get(i).idCancion.equals(idCancion)){
                            hilosCanciones.get(i).leerBytesEntrada(line);
                        }
                    }
                }
                else if(line.contains("OK_MUSIC_BY_STRING_FOR")){
                    cargarCancionesSistema(line);
                }
                else if(line.contains("OK_ARTIST_FOR")){
                    cargarCancionesArtista(line);
                }
            }
            //Conexión con el servidor cerrada
            cerrarHebra();
        } catch (IOException e) {
        }
    }

    /**
     * @brief   Método que busca el token en el mensaje recibido del servidor
     */
    public void buscarToken(String line){
        String[] mensaje = line.split("#");
        for(int i = 0; i < mensaje.length; i++){
            if(mensaje[i].equals("WITH_TOKEN")){
                getCliente().setToken(mensaje[i+1]);
            }
        }
    }

    /**
     * @brief   Método que carga los usuarios conectados en la tabla de usuarios
     * @param line  Mensaje recibido del servidor
     */
    public void cargarUsuariosConectados(String line){
        String[] mensaje = line.split("#");
        Integer numUsuarios;
        String[] usuarios = null;
        for(int i = 0; i < mensaje.length; i++){
            if(mensaje[i].equals(getCliente().getToken())){
                numUsuarios = Integer.parseInt(mensaje[i+1]);
                if(numUsuarios > 0){
                    usuarios = mensaje[i+2].split("@");
                }
            }
        }
        introducirDatosTabla(usuarios);
    }

    /**
     * @brief   Método que introduce los datos de los usuarios en la tabla de usuarios
     * @param usuarios  Array de usuarios conectados
     */
    public void introducirDatosTabla(String[] usuarios){
        DefaultTableModel model = (DefaultTableModel) getTablaUsuarios().getModel();
        //Limpiar datos de la tabla
        model.setRowCount(0);

        //Insertar usuarios en la tabla
        for(int i = 0; i < usuarios.length; i++){
            String[] datosUsuario = usuarios[i].split("\\|");
            Object[] fila = {datosUsuario[0], datosUsuario[1], datosUsuario[2], datosUsuario[3], datosUsuario[4]};

            model.addRow(fila);
        }
    }

    /**
     * @brief   Método que carga las canciones del sistema en la tabla de canciones
     * @param line  Mensaje recibido del servidor
     */
    public void cargarCancionesSistema(String line){
        String[] mensaje = line.split("#");
        Integer numCanciones;
        String[] canciones = null;
        for(int i = 0; i < mensaje.length; i++){
            if(mensaje[i].equals(getCliente().getToken())){
                numCanciones = Integer.parseInt(mensaje[i+1]);
                if(numCanciones > 0){
                    canciones = mensaje[i+2].split("@");
                }
            }
        }
        DefaultTableModel model = (DefaultTableModel) getTablaCanciones().getModel();
        model.setRowCount(0);
        if(canciones != null){
            introducirDatosTablaCanciones(canciones);
        }
    }

    /**
     * @brief   Método que carga las canciones de un artista en la tabla de canciones
     * @param line  Mensaje recibido del servidor
     */
    public void cargarCancionesArtista(String line){
        String[] mensaje = line.split("#");
        Integer numCanciones;
        String[] canciones = null;
        for(int i = 0; i < mensaje.length; i++){
            if(mensaje[i].equals(getCliente().getToken())){
                nombreArtista.setText(mensaje[i+1]);
                numCanciones = Integer.parseInt(mensaje[i+2]);
                if(numCanciones > 0){
                    canciones = mensaje[i+3].split("@");
                }
            }
        }
        if(canciones != null){
            introducirDatosTablaCanciones(canciones);
        }
    }

    /**
     * @brief   Método que introduce los datos de las canciones en la tabla de canciones
     * @param canciones  Array de canciones del sistema
     */
    public void introducirDatosTablaCanciones(String[] canciones){
        DefaultTableModel model = (DefaultTableModel) getTablaCanciones().getModel();
        //Limpiar datos de la tabla
        model.setRowCount(0);

        //Insertar canciones en la tabla
        for(int i = 0; i < canciones.length; i++){
            String[] datosCancion = canciones[i].split("\\|");
            Object[] fila = {datosCancion[0], datosCancion[1], datosCancion[2], datosCancion[3], datosCancion[4]};

            model.addRow(fila);
        }
    }

    public void finalizarRecibirCancion(String idCancion){
        getCliente().getOut().println(controlador.generarCadenaGetSongFinished(idCancion, cliente));
        getCliente().getOut().flush();

        boolean encontrado = false;
        System.out.println("Tamaño del array de hilos de canciones: " + hilosCanciones.size());
        for(int i = 0; !encontrado && i < hilosCanciones.size() ; i++){
            if(hilosCanciones.get(i).idCancion.equals(idCancion)){
                hilosCanciones.get(i).detenerDescargaCancion();
                hilosCanciones.remove(i);
                encontrado = true;
                System.out.println("Hilo de canción con id: " + idCancion + " cerrado.");
            }
        }
        //Ocultar barra de progreso
        progresoDescarga.setVisible(false);
        //Limpiar barra de progreso
        barraProgreso.setValue(0);
        porcentajeDescarga.setText("0");
    }

    /**
     * @brief   Método que cierra el hilo que lee los mensajes del servidor y los hilos de las canciones
     */
    public void cerrarHebra(){
        //Limpiar hebras de canciones
        for(int i = 0; i < hilosCanciones.size(); i++){
            hilosCanciones.get(i).getThr().interrupt();
            System.out.println("Hilo de canción con id: " + hilosCanciones.get(i).idCancion + " cerrado.");
        }
        //Ocultar barra de progreso
        progresoDescarga.setVisible(false);
        //Limpiar barra de progreso
        barraProgreso.setValue(0);
        porcentajeDescarga.setText("0");
        
        hilosCanciones.clear();
        this.thr.interrupt();
    }
}
