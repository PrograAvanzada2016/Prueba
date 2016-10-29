package mensaje;

import java.util.ArrayList;

import com.google.gson.Gson;

public abstract class Comando {
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
