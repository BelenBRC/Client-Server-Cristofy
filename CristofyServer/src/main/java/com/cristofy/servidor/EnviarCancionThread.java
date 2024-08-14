package com.cristofy.servidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class EnviarCancionThread implements Runnable{
    public Thread thr;
    public String id_cancion;
    private String inicioCadena;
    private Integer tamanioPaquete;
    private Long fileSizeBytes;
    private Integer numeroPaquetes;
    private String rutaAlmacenamiento;

    private PrintWriter out;

    private byte[] cancionBytes;
    private String[] paquetesCancionCodificados;
    private String[] cadenasSalida;

    private Integer paquetesEnviados;

    public EnviarCancionThread(PrintWriter out, String datos) {
        this.thr = new Thread(this);
        this.out = out;
        this.paquetesEnviados = 0;

        desglosarCadena(datos);
        calcularPaquetes(fileSizeBytes);

        cancionBytes = new byte[(int) (long) fileSizeBytes];

        leerCancion();
        prepararPaquetes();
        prepararCadenasSalida();
    }

    @Override
    public void run() {
        while(paquetesEnviados < numeroPaquetes && out != null && !thr.isInterrupted()){
            out.println(cadenasSalida[paquetesEnviados]);
            paquetesEnviados++;
        }
        eliminarHebra();
    }

    private void desglosarCadena(String datos) {
        String[] datosDesglosados = datos.split("@");
        inicioCadena = datosDesglosados[1];
        String datosCancion = datosDesglosados[2];
        
        
        String[] datosCancionDesglosados = datosCancion.split("#");
        tamanioPaquete = Integer.parseInt(datosCancionDesglosados[0]);
        fileSizeBytes = Long.parseLong(datosCancionDesglosados[1]);
        rutaAlmacenamiento = datosCancionDesglosados[2];

        //Extraer el id de la canción
        String[] datosIniciales = inicioCadena.split("#");
        id_cancion = datosIniciales[3];
    }

    private void calcularPaquetes(Long fileSizeBytes) {
        if(fileSizeBytes%tamanioPaquete == 0){
            numeroPaquetes = (int) (fileSizeBytes / tamanioPaquete);
        } else {
            numeroPaquetes = (int) (fileSizeBytes / tamanioPaquete) + 1;
        }
    }

    private void leerCancion(){
        try {
            cancionBytes = Files.readAllBytes(Paths.get(rutaAlmacenamiento));
        } catch (IOException e) {
            System.out.println("Error al leer la canción de la ruta.");
        }
    }

    private void prepararPaquetes(){
        byte[] paquete = new byte[tamanioPaquete];
        paquetesCancionCodificados = new String[numeroPaquetes];
        for(int i = 0; i < numeroPaquetes; i++){
            if(i == numeroPaquetes - 1){
                paquete = new byte[(int) (fileSizeBytes - (tamanioPaquete * i))];
                System.arraycopy(cancionBytes, tamanioPaquete * i, paquete, 0, (int) (fileSizeBytes - (tamanioPaquete * i)));
            } else {
                System.arraycopy(cancionBytes, tamanioPaquete * i, paquete, 0, tamanioPaquete);
            }
            paquetesCancionCodificados[i] = Base64.getEncoder().encodeToString(paquete);
        }
    }

    private void prepararCadenasSalida(){
        cadenasSalida = new String[numeroPaquetes];
        for(int i = 0; i < numeroPaquetes; i++){
            cadenasSalida[i] = inicioCadena + "#ID_PACKAGE#" + i + "#BYTES#" + paquetesCancionCodificados[i];
        }
    }
    
    private void eliminarHebra(){
        //Liberar recursos
        if(thr != null){
            thr.interrupt();
            thr = null;
        }
    }
}
