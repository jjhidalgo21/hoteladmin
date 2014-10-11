package ara.soft.john.htl.actions.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ara.soft.john.htl.actions.base.ActionBase;
import ara.soft.john.htl.actions.bd.BDMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MantenimientoUsuarios extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7117644541272159843L;

	public List<String> selectRoles = null;
	
	
	/*FORMULARIO*/
	/*SI NECESITA UN PARAMETRO QUE NO SE GUARDE COMO UNA CONTRASEÑA NO PONERLO AQUI,
	 * DEBE LLAMARSE IGUAL QUE EL NAME DEL JSP, ES DECIR SI TIENE PREFIJOS DEBEN INCLUIRSE*/
	public String req_user;
	public String req_nombre;
	public String req_identificacion;
	public String req_correo;
	public String puesto;
	
	/*FORMULARIO*/
	
	@Override
	public String execute() throws Exception {
		super.execute();
		String action = request.getParameter("action");
		String doAction = request.getParameter("do");
		
		if(action == null){//carga solamente la lista
			request.setAttribute("listaUsuarios", cargaListaUsuarios());//pone la lista en el jsp
		}else if(action.equals("form") && doAction == null){//si es el formulario
			cargaSelectRoles();//carga la lista de roles
		}else if(action.equals("form") && doAction.equals("save")){
			Map<String, String[]> paramsMap = request.getParameterMap();//obtiene el mapa de parametros devuelto por el form
			try{
				BasicDBObject docUser = getBasicDBObjectFromParamMap(paramsMap);//obtiene un documento desde el mapa de parametros
				DBCollection coll = BDMongo.getCollection(BDMongo.COLLECTION_USUARIO);//obtiene la coleccion de la base de datos
				coll.save(docUser);//guarda la coleccion
				response.sendRedirect("mantUsuario.htl");//se redirige a la lista
				return null;
			}catch(Exception e){
				cargaSelectRoles();//carga la lista de roles
				poneListaErroresParametrosEnJsp();//esta lista se setea para validar el formulario y poder mostrar mensajes de error individuales
			}
		}
		
		return "CORRECTO";
	}
	
	
	private void cargaSelectRoles(){
		if(selectRoles == null){
			DBCollection coll = BDMongo.getCollection(BDMongo.COLLECTION_ROL);
			DBCursor cursor = coll.find();
			cursor.sort(new BasicDBObject("nombre", 1));
			selectRoles = new ArrayList<String>();
			while(cursor.hasNext()){
				selectRoles.add((String)cursor.next().get("nombre"));//monta el nombre del rol en la lista
			}
		}
	}
	
	public List<DBObject> cargaListaUsuarios(){
		List<DBObject> listaUsuarios = null;
		DBCollection coll = BDMongo.getCollection(BDMongo.COLLECTION_USUARIO);//obtiene la coleccion(tabla) de usuarios
		DBCursor cursor = coll.find();
		try {
		   listaUsuarios = cursor.toArray();
		} finally {
		   cursor.close();
		}
		return listaUsuarios;
	}
}
