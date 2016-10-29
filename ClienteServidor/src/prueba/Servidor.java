package prueba;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor
{
	public static ArrayList<Socket> connectionArray = new ArrayList<>();
	public static ArrayList<String> users = new ArrayList<>();
	public ArrayList<Socket> salaDeChat1;
	public ArrayList<Socket> salaDeChat2;
	public ArrayList<Socket> salaDeChat3;
	
	public Servidor(){
		salaDeChat1 = new ArrayList<>();
		salaDeChat2 = new ArrayList<>();
		salaDeChat3 = new ArrayList<>();
	}
	public static void main(String[] args) throws Exception
	{
		try
		{
			Servidor servidor = new Servidor();
			//Propiedades props = new Propiedades();
			final int puerto = 444;//props.getPuerto();
			ServerSocket serverSocket = new ServerSocket(puerto);
			ServidorChat chat = null;
			int nroSala;
			while(true){
				System.out.println("Esperando un cliente...");
				Socket socket = serverSocket.accept();
				System.out.println("Seleccione la sala por favor");
				DataInputStream in = new DataInputStream(socket.getInputStream());
				nroSala = in.readInt();
				
				switch(nroSala){
					case 1:
						servidor.salaDeChat1.add(socket);
						chat = new ServidorChat(socket,servidor.salaDeChat1);
						break;
					case 2:
						servidor.salaDeChat2.add(socket);
						chat = new ServidorChat(socket,servidor.salaDeChat2);
						break;
					case 3:
						servidor.salaDeChat3.add(socket);
						chat = new ServidorChat(socket,servidor.salaDeChat3);
						break;
					default: 
						break;	
				}
				Thread instancia = new Thread(chat);
				instancia.start();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
