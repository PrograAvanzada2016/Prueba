package mensaje;

import java.util.ArrayList;

import objetos.Ente;

public class ComandoAtacar extends Comando{
	private ArrayList<Ente> personajes;
	public ComandoAtacar(int id, ArrayList<Ente> personajes){
		this.id=id;
		this.personajes=personajes;
	}
	public ArrayList<Ente> getPersonajes() {
		return personajes;
	}
	public void setPersonajes(ArrayList<Ente> personajes) {
		this.personajes = personajes;
	}
	
	
}
