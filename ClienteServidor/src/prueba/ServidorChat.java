package prueba;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServidorChat implements Runnable{
	Socket socket;
	private Scanner input;
	private String message = "";
	private ArrayList<Socket> salaDeChat;
	
	public ServidorChat(Socket socket, ArrayList<Socket> salaDeChat){
		this.socket=socket;
		this.salaDeChat=salaDeChat;
	}
	
	public void checkConnection()throws Exception{
		if(!this.socket.isConnected()){
			
			for(int x=0; x<this.salaDeChat.size(); x++){
				if(this.salaDeChat.get(x).equals(this.socket)){
					this.salaDeChat.remove(x);
				}
			}
			
		}
	}
	
	
	@Override
	public void run() {
		
		try{
			try{
				input = new Scanner(socket.getInputStream());
				new PrintWriter(socket.getOutputStream());
				
				while(true){
					this.checkConnection();
					
					if(!input.hasNext()){
						return;
					}
					
					message = input.nextLine();
					System.out.println("El cliente dice: "+message);
					
					for(int x=0; x<this.salaDeChat.size(); x++){
						Socket tempSocket =this.salaDeChat.get(x);
						if(tempSocket!=null && !tempSocket.isClosed()){
							PrintWriter tempOut = new PrintWriter(tempSocket.getOutputStream());
							tempOut.println(message);
							tempOut.flush();
							System.out.println("mensaje enviado a: "+tempSocket.getLocalAddress().getHostName());
						}
					}
				}
			}finally{
				socket.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
