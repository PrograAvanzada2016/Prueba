/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import dao.AlumnoDAO;
import dao.DAO;
import dao.DAOExeption;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persona.Alumno;

/**
 *
 * @author lab3
 */
public class AtencionCliente extends Thread{

           private final Socket socket;
           private final ObjectOutputStream salida;
           private final ObjectInputStream entrada;
           private final DAO alumnoDAO;

    public AtencionCliente(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream, DAO alumnoDAO) {
        this.socket = socket;
        salida = outputStream;
        entrada = inputStream;
        this.alumnoDAO = alumnoDAO;
    }
    
     
    @Override
    public void run() {
              try {
                  String comando = (String) entrada.readObject();
                  while(true){
                    if("fin".equals(comando)){
                        salida.close();
                        entrada.close();
                        socket.close();
                    }
                    else{
                       switch (comando){
                           case "insertar":
                               try{
                               Alumno alumno = (Alumno) entrada.readObject();
                               alumnoDAO.insertar(alumno);
                               salida.writeObject("Alumno Insertado Correctamente");
                               }catch (DAOExeption ex){
                                   salida.writeObject("Duplicado");
                                   }
                               break;
                           case "listar":
                              List<Alumno> lista = alumnoDAO.listarTodos();
                              System.out.println("listarTodos OK");
                              salida.writeObject(lista);
                              System.out.println("Se envio la lista");
                               break;
                            case "buscar":
                                Alumno alumno = (Alumno) entrada.readObject();
                                long pointer = alumnoDAO.buscar(alumno);
                                if(pointer!=-1){
                                  salida.reset();
                                  salida.writeObject(alumno.getApellido().trim());
                                  salida.reset();
                                  salida.writeObject(alumno.getNombre().trim());
                                  salida.reset();
                                  salida.writeObject(alumno.getSexo()=='M'?"0":"1");
                                  salida.reset();
                                  salida.writeObject(String.valueOf(alumno.getFechaIngreso()).trim());
                                  salida.reset();
                                  salida.writeObject(String.valueOf(alumno.getFechaNac()).trim());
                                  salida.reset();
                                  salida.writeObject(String.valueOf(alumno.getPromedio()).trim());
                                  salida.reset();
                                  salida.writeObject(String.valueOf(alumno.getCantMatAprob()).trim());
                                  salida.writeObject("Alumno Encontrado");
                                }
                                else{
                                  salida.writeObject("");
                                  salida.writeObject("");
                                  salida.writeObject("0");
                                  salida.writeObject("");
                                  salida.writeObject("");
                                  salida.writeObject("");
                                  salida.writeObject("");
                                  salida.writeObject("Alumno NO Encontrado");
                                }
                                break;    
                           case "borrar":
                                alumno = (Alumno) entrada.readObject();
                                alumnoDAO.borrar(alumno);
                                salida.writeObject("Alumno Borrado Correctamente");
                                break;
                           case "modificar":
                                alumno = (Alumno) entrada.readObject();
                                alumnoDAO.modificar(alumno);
                                salida.writeObject("Alumno Modificado Correctamente");
                                break;
                       }
                    comando = (String) entrada.readObject();
                    }
                 }
              }catch (IOException | ClassNotFoundException | DAOExeption ex) {
                  Logger.getLogger(AtencionCliente.class.getName()).log(Level.SEVERE, null, ex);
              }
    }
    
}
