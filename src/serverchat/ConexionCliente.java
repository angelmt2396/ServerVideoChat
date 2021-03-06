/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Angel
 */
public class ConexionCliente extends Thread implements Observer{

    private Socket socket;
    private Datos datos;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    public ConexionCliente(){
        
    }
    
    public void setSocket(Socket socket){
        this.socket = socket;
    }
    
    public void setInOut(){
        try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ConexionCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void setDatos(Datos datos){
        this.datos = datos;
    }
    @Override
    public void run(){
        String mensajeRecibido;
        boolean conectado = true;
        // Se apunta a la lista de observadores de mensajes
        datos.addObserver(this);
         while (conectado) {
            try {
                // Lee un mensaje enviado por el cliente
                mensajeRecibido = dis.readUTF();
                // Pone el mensaje recibido en mensajes para que se notifique 
                // a sus observadores que hay un nuevo mensaje.
                datos.setMensaje(mensajeRecibido);
            } catch (IOException ex) {
                System.out.println("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                conectado = false; 
                // Si se ha producido un error al recibir datos del cliente se cierra la conexion con el.
                try {
                    dis.close();
                    dos.close();
                } catch (IOException ex2) {
                    System.out.println("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                }
            }
        }   
    }
    
    @Override
    public void update(Observable o, Object arg) {
        try {
            // Envia el mensaje al cliente
            dos.writeUTF(arg.toString());
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje al cliente (" + ex.getMessage() + ").");
        }
    }
    
}
