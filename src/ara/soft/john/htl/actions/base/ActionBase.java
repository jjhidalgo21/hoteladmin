package ara.soft.john.htl.actions.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.hibernate.bytecode.buildtime.spi.ExecutionException;

import ara.soft.john.htl.security.Session;

import com.mongodb.BasicDBObject;
import com.opensymphony.xwork2.ActionSupport;

public class ActionBase extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1652268947841766356L;
	protected HttpServletRequest request = null;//el request web
    protected HttpServletResponse response = null;//el response web
    public String errorMessage = null;//mensaje de error
	
	protected boolean validateExistUser = true;//validar si hay un usuario logueado
	private Session session = null;//la session
	
	private ArrayList<String[]> listaErroresParametros = new ArrayList<String[]>();//se guardara un array por cada error
	//DEBE UTILIZAR EL SIGUIENTE FORMATO = {"nombreParametro","mensaje"}
	
	@Override
	public String execute() throws Exception {
		HttpSession httpSession = request.getSession();
		
		if(session == null && httpSession.getAttribute("session") != null)//si hay una session
			session = (Session) httpSession.getAttribute("session");//la obtiene
		
		saveSession();//guarda la session
		
		if(validateExistUser && (session == null || session._userNameLogIn == null)){//valida si hay usuario logueado
			response.sendRedirect("login.htl");//si no lo redirige al login
			return null;
		}
		
		return "CORRECTO";
	}
	
	
	protected void addParamError(String paramName, String message){
		listaErroresParametros.add(new String[]{paramName,message});
	}
	
	protected void poneListaErroresParametrosEnJsp(){
		request.setAttribute("listaErroresParametros", listaErroresParametros);
	}
	

	/**
	 * GUARDA LOS CAMBIOS EN LA SESION
	 */
	protected void saveSession(){
		HttpSession httpSession = request.getSession();
		if(session != null)
			httpSession.setAttribute("session", session);
	}
	
	/**
	 * CREA LA SESION SI NO EXISTE
	 */
	protected void createSession(){
		HttpSession httpSession = request.getSession();
		if(session == null)
			session = new Session();
		saveSession();
	}

	@Override
	public void setServletResponse(HttpServletResponse _response) {
		this.response = _response;
	}

	@Override
	public void setServletRequest(HttpServletRequest _request) {
		this.request = _request;
	}

	/**
	 * 
	 * @return la sesion
	 */
	protected Session getSession() {
		return session;
	}
	
	/**METODOS PARA EL MANEJO DE FORMULARIOS
	 
	
	/**
	 * *******DOCUMENTACION SUMAMENTE IMPORTANTE PARA EL MANEJO DE FORMULARIOS
	 * @return
	 * @throws Exception 
	 */
	protected BasicDBObject getBasicDBObjectFromParamMap(Map<String, String[]> paramsMap) throws Exception{
		BasicDBObject doc = new BasicDBObject();
		
		Map<String, String[]> params = request.getParameterMap();
		Iterator iterator =  params.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String[]> param = (Entry<String, String[]>) iterator.next();
			if(startWithValidation(param.getKey()) && equalValidation(param.getKey())){
				for(String subValue : param.getValue()){
					String paramRealName = param.getKey();
					if(paramRealName.contains(PARAM_TYPE_REQUERIDO)){//si es un campo requerido
						paramRealName = paramRealName.replace(PARAM_TYPE_REQUERIDO, "");//quita el prefijo _req
						if(subValue == null || (subValue = subValue.trim()).isEmpty())//valida si vienen datos
							addParamError(paramRealName, "El campo es requerido");//poncha el error en la lista
					}
					doc.append(paramRealName, subValue);
					System.out.println(param.getKey() +" ->> "+subValue);
				}
			}
		}
		
		if(listaErroresParametros.size() > 0){
			throw new Exception("error");
		}
		
		return doc;
	}
	
	/**
	 * VALIDAR SI EMPIEZA CON: algun valor de la lista de ignore
	 * @param value
	 * @return
	 */
	private boolean startWithValidation(String _value){
		if(ignoreParamsStartWith == null)
			ignoreParamsStartWith = llenaListaAuxiliar(_value, PARAM_TYPE_IGNORE_START_WITH);
		for(String[] igParam : ignoreParamsStartWith){//valida la operacion
			if(igParam[1] == null ||_value.startsWith(igParam[1]))
				return false;
		}
		return true;
	}
	
	/**
	 * VALIDAR SI ES IGUAL AL VALOR A IGNORAR
	 * @param value
	 * @return
	 */
	private boolean equalValidation(String _value){
		if(ignoreParamsEquals == null)
			ignoreParamsEquals = llenaListaAuxiliar(_value, PARAM_TYPE_IGNORE_EQUALS);
		for(String[] igParam : ignoreParamsEquals){//valida la operacion
			if(igParam[1] == null || _value.equalsIgnoreCase(igParam[1]))
				return false;
		}
		return true;
	}
	
	/*LLENA LA LISTA AUXILIAR PARA REALIZAR OPERACIONES*/
	private List<String[]> llenaListaAuxiliar(String _value,String typeList){
		List<String[]> _listaAuxiliar = null;
		if(paramsOperations == null){
			paramsOperations = new ArrayList<String[]>();
			//SETEA LAS OPERACIONES A REALIZAR
			
			paramsOperations.add(new String[]{PARAM_TYPE_IGNORE_START_WITH,"__"});//ignora parametros con "__" en el inicio(solo sirve para struts)
			paramsOperations.add(new String[]{PARAM_TYPE_IGNORE_START_WITH,"conf_"});//se usara el prefijo conf_ para definir variables se confirmacion
			paramsOperations.add(new String[]{PARAM_TYPE_IGNORE_EQUALS,"action"});//ignora el action
			paramsOperations.add(new String[]{PARAM_TYPE_IGNORE_EQUALS,"do"});//ignora el do	
		}
		_listaAuxiliar = new ArrayList<String[]>();
		for(String[] paramOperation : paramsOperations){//llena la lista con los objeto del tipo correcto
			if(paramOperation[0].equals(typeList)){
				_listaAuxiliar.add(paramOperation);
			}
		}
		return _listaAuxiliar;
	}
	
	//que tipo de manejo se debe realizar con el valor a ignorar
	private final String PARAM_TYPE_IGNORE_START_WITH = "startWith";
	private final String PARAM_TYPE_IGNORE_EQUALS = "equals";
	private final String PARAM_TYPE_REQUERIDO = "req_";
	private List<String[]> ignoreParamsStartWith = null;//es solo un auxiliar
	private List<String[]> ignoreParamsEquals = null;//es solo un auxiliar
	
	
	private List<String[]> paramsOperations = null;//aqui se guardan los tipos de operaciones a realizar
	
	/**METODOS PARA EL MANEJO DE FORMULARIOS**/
}
