/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import dao.AlumnoDAO;
import dao.DAO;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lab3
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try {
            ServerSocket server = new ServerSocket(4444);
            DAO alumnoDAO = new AlumnoDAO(new File("Alum.txt"));
            
            while(true){
            
                Socket socket = server.accept();
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); 
                final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                new AtencionCliente(socket, objectOutputStream, objectInputStream, alumnoDAO).start();
            }
            
                    
           /* ObjectOutputStream salida;
            ObjectOutputStream entrada;*/
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
}
