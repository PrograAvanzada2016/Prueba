package prueba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Propiedades {
	private String ip;
	private int puerto;
	
	public Propiedades() throws IOException{
		String archivoPropiedades = "config.properties";
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(archivoPropiedades);
		
		try 
		{
			if (inputStream != null)
				prop.load(inputStream);
			else
				throw new FileNotFoundException("Archivo de propiedades '" + archivoPropiedades + "' no encontrado.");
 
			this.ip = prop.getProperty("ip");
			this.puerto = Integer.parseInt(prop.getProperty("puerto"));
		} 
		catch (Exception e)
		{
			System.out.println("Exception: " + e);
		} 
		finally 
		{
			inputStream.close();
		}
	}

	public String getIp() 
	{
		return this.ip;
	}

	public int getPuerto() 
	{
		return this.puerto;
	}
}
