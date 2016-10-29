package mensaje;

import java.util.ArrayList;

import com.google.gson.Gson;

import objetos.Ente;

public class App {

	public static void main(String[] args) {
		ArrayList<Ente> argumentos = new ArrayList<>();
		Ente e1 = new Ente("pedro");
		argumentos.add(e1);
		//Comando c1 = new Comando(1, argumentos);
		Gson gson = new Gson();
		//System.out.println(gson.toJson(c1));
		
		//Comando c2 = gson.fromJson(gson.toJson(c1), Comando.class);
		
		//System.out.println(c2.getId());
		//System.out.println(((Ente)c2.getListaArgumentos().get(0)).getNombre());
	}

}
