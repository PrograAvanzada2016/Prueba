package formularios;

import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {
  
	public Properties getProperties(){

			Properties propiedades = new Properties();
			try {
				propiedades.load(getClass().getResourceAsStream("config.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!propiedades.isEmpty()){
				return propiedades;
			} else
				return null;
			}
		}