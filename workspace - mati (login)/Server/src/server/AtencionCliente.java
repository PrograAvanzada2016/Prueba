package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.google.gson.Gson;

import dao.DAO;
import dao.DAOException;
import model.Usuario;

public class AtencionCliente extends Thread{

    private final Socket socket;
    private final ObjectOutputStream salida;
    private final ObjectInputStream entrada;
    private final DAO<Usuario> usuarioDAO;
    private Gson gson;

public AtencionCliente(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream, DAO usuarioDAO) {
 this.socket = socket;
 salida = outputStream;
 entrada = inputStream;
 this.usuarioDAO = usuarioDAO;
 gson = new Gson();
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
                       /* try{
                        Alumno alumno = (Alumno) entrada.readObject();
                        alumnoDAO.insertar(alumno);
                        salida.writeObject("Alumno Insertado Correctamente");
                        }catch (DAOExeption ex){
                            salida.writeObject("Duplicado");
                            }*/
                        break;
                    case "listar":
                      /* List<Alumno> lista = alumnoDAO.listarTodos();
                       System.out.println("listarTodos OK");
                       salida.writeObject(lista);
                       System.out.println("Se envio la lista");*/
                        break;
                     case "buscar":
                         Usuario usuario = gson.fromJson((String)entrada.readObject(), Usuario.class);
                         long pointer = usuarioDAO.buscar(usuario);
                         if(pointer!=-1){
                           salida.reset();
                           salida.writeObject();
                           salida.reset();
                           salida.writeBoolean(true);
                         }
                         else{
                           salida.writeBoolean(false);
                         }
                         break;    
                    case "borrar":
                         /*alumno = (Alumno) entrada.readObject();
                         alumnoDAO.borrar(alumno);
                         salida.writeObject("Alumno Borrado Correctamente");*/
                         break;
                    case "modificar":
                        /* alumno = (Alumno) entrada.readObject();
                         alumnoDAO.modificar(alumno);
                         salida.writeObject("Alumno Modificado Correctamente");*/
                         break;
                }
                comando="fin";
                //comando = (String) entrada.readObject();
             }
          }
       }catch (IOException | ClassNotFoundException ex) {
    	   ex.printStackTrace();
       } catch (DAOException e) {
		e.printStackTrace();
	}
}
}
