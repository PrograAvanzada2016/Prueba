package formularios;

public class Personaje {
	
	private String nombrePersonaje;
	private String razaPersonaje;
	private String castaPersonaje;
	
	public Personaje (String nombre, String raza, String casta){
		
		this.nombrePersonaje = nombre;
		this.razaPersonaje = raza;
		this.castaPersonaje = casta;
	}

	public String getNombrePersonaje() {
		return nombrePersonaje;
	}

	public void setNombrePersonaje(String nombrePersonaje) {
		this.nombrePersonaje = nombrePersonaje;
	}

	public String getRazaPersonaje() {
		return razaPersonaje;
	}

	public void setRazaPersonaje(String razaPersonaje) {
		this.razaPersonaje = razaPersonaje;
	}

	public String getCastaPersonaje() {
		return castaPersonaje;
	}

	public void setCastaPersonaje(String castaPersonaje) {
		this.castaPersonaje = castaPersonaje;
	}
	
	

}
