package ara.soft.john.htl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ERXProperties {
	private static Properties propiedades = new Properties();
	private static InputStream entrada = null;
	
	static{
		try {
			entrada = ERXProperties.class.getClassLoader().getResourceAsStream("/hotels.properties");
			// cargamos el archivo de propiedades
	        propiedades.load(entrada);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        if (entrada != null) {
	            try {
	                entrada.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
        
	}
	
	public static String stringForKey(String _key){
		return propiedades.getProperty(_key);
	}
}
