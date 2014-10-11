package ara.soft.john.htl.utils;

import java.util.ArrayList;

public class FormValidation {
	public static boolean hayErrorParam(String param, ArrayList<String[]> listaErrores){
		if(listaErrores != null){
			for(String[] item : listaErrores){//recorre la lista
				if(item[0].equals(param)){//busca el parametro
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * DEVUELVE EL MENSAJE DE ERROR REFERENTE AL PARAMETRO
	 * @param param
	 * @param listaErrores
	 * @return
	 */
	public static String getErrorMessage(String param, ArrayList<String[]> listaErrores){
		String res = null;
		if(listaErrores != null){
			for(String[] item : listaErrores){//recorre la lista
				if(item[0].equals(param)){//busca el parametro
					res = " / Mensaje: "+item[1];
					return res;
				}
			}
		}
		return res;
	}
}
