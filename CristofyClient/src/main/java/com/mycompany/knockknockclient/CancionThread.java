package com.mycompany.knockknockclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class CancionThread implements Runnable{
    private Thread thr;
    private byte[] cancion;
    private Integer tamPaquete;
    private Integer tamCancion;
    public String idCancion;
    public Integer bytesRecibidos;

    private Integer paquetesEsperados;
    private Integer paquetesRecibidos;

    private LeerServidorThread leerServidorThread;

    private JPanel progresoDescarga;
    private JLabel porcentajeProgreso;
    private JProgressBar barraProgreso;

    public CancionThread(Integer tamPaqueteP, Integer tamCancionP, String idCancionP, LeerServidorThread hilo, JPanel progresoDescarga, JLabel porcentajeProgreso, JProgressBar barraProgreso){
        setThr(new Thread(this));
        this.tamPaquete = tamPaqueteP;
        this.tamCancion = tamCancionP;
        this.idCancion = idCancionP;
        this.bytesRecibidos = 0;
        this.paquetesRecibidos = 0;
        this.leerServidorThread = hilo;
        this.progresoDescarga = progresoDescarga;
        this.porcentajeProgreso = porcentajeProgreso;
        this.barraProgreso = barraProgreso;

        if(tamCancion % tamPaquete == 0){
            paquetesEsperados = tamCancion / tamPaquete;
        }
        else{
            paquetesEsperados = (tamCancion / tamPaquete) + 1;
        }

        cancion = new byte[tamCancion+600]; //+600 para compatibilidad con alvarito

        progresoDescarga.setVisible(true);
    }

    public void setThr(Thread thr) {
        this.thr = thr;
    }

    public Thread getThr() {
        return thr;
    }

    @Override
    public void run() {

        //Guardar cancion
        try {
            FileOutputStream fos = new FileOutputStream("cancion" + idCancion + ".mp3");
            fos.write(cancion);
            System.out.println("Canción guardada: " + idCancion + ".mp3" + " de " + tamCancion + " bytes.");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }   


        //Enviar mensaje de que se ha recibido la canción
        leerServidorThread.finalizarRecibirCancion(idCancion);

        //Ocultar barra de progreso
        progresoDescarga.setVisible(false);
        //Limpiar barra de progreso
        barraProgreso.setValue(0);
        porcentajeProgreso.setText("0");

        //Reproducir canción
        try {
            leerServidorThread.getCliente().escucharCancion(cancion);
        } catch (IOException e) {
            System.out.println("Error al reproducir la canción: " + idCancion + "\n" + e.getMessage());
        }
    }

    public void leerBytesEntrada(String lectorSocket){
        String[] mensaje = lectorSocket.split("#");
        boolean leido = false;
        for(int i = 0; !leido && i < mensaje.length; i++){
            if(mensaje[i].equals("ID_PACKAGE")){
                paquetesRecibidos++;

                byte[] paquete = Base64.getDecoder().decode(mensaje[i+3].trim());
                for(int j = 0; j < paquete.length; j++){
                    cancion[bytesRecibidos] = paquete[j];
                    bytesRecibidos++;
                    actualizarProgreso();
                }
                leido = true;
            }
        }

        if(paquetesRecibidos.equals(paquetesEsperados)){
            getThr().run();
        }
    }

    private void actualizarProgreso(){
        Integer porcentaje = (bytesRecibidos * 100) / tamCancion;
        barraProgreso.setValue(porcentaje);
        porcentajeProgreso.setText(porcentaje+"");
    }

    public void detenerDescargaCancion(){
        if (thr != null) {
            thr.interrupt();
            thr = null;
        }
        //Ocultar barra de progreso
        progresoDescarga.setVisible(false);
        //Limpiar barra de progreso
        barraProgreso.setValue(0);
        porcentajeProgreso.setText("0");
    }
}
