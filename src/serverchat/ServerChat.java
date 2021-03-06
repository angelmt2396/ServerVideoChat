/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Angel
 */
public class ServerChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int puerto = 1234;
        int maxConexion = 10;
        ServerSocket servidor = null;
        Socket socket = null;
        Datos datos = new Datos();
        
        try{
            // Se crea el serverSocket
            servidor = new ServerSocket(puerto, maxConexion);
            
            // Bucle infinito para esperar conexiones
            
            while(true){
                System.out.println("Servidor en la espera de conexión...");
                socket = servidor.accept();
              
                System.out.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");
                ConexionCliente cc = new ConexionCliente();
                cc.setSocket(socket);
                cc.setInOut();
                cc.setDatos(datos);
                cc.start();
                
            }
        }catch(IOException ex){
            System.out.println("Error: " + ex.getMessage());
        }finally{
            try {
                socket.close();
                servidor.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar el servidor: " + ex.getMessage());
            }
        }
    }
    
}
