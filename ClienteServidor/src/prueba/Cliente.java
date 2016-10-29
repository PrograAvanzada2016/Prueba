package prueba;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) throws Exception{
		String server="localhost";
		String nombre="";
		Scanner entrada = new Scanner(System.in);
		DataOutputStream out = null;
		boolean flag = true;
		int nroSala;
		try{
			final int puerto = 444;
			Socket socket = new Socket(server, puerto);
			System.out.println("Te conectaste a: "+server);
			
			
			System.out.println("Por favor, ingrese su nombre: ");
			nombre= entrada.nextLine();
			
			System.out.println("Por favor, ingrese el numero de sala: ");
			nroSala = entrada.nextInt();
			
			
			
			System.out.println("Te conectaste a: "+server+" sala numero: "+nroSala);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeInt(nroSala);
			
			ClienteChat chat = new ClienteChat(socket,nombre);
			Thread thread = new Thread(chat);
			thread.start();
			thread.sleep(1000);
			while(flag){
				String message = entrada.nextLine();
				if(message.equals("adios")){
					flag=false;
				}else{ 
					chat.send(message);
				}
			}
			chat.disconnected();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
