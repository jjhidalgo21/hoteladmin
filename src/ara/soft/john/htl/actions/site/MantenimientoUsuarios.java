package ara.soft.john.htl.actions.site;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public String execute() throws Exception {
		super.execute();
		String action = request.getParameter("action");
		
		if(action == null){//carga solamente la lista
			request.setAttribute("listaUsuarios", cargaListaUsuarios());
		}else if(action.equals("form")){
			cargaSelectRoles();
		}
		
		return "CORRECTO";
	}
	
	
	private void cargaSelectRoles(){
		if(selectRoles == null){
			DBCollection coll = BDMongo.getCollection(BDMongo.COLLECTION_ROL);
			DBCursor cursor = coll.find();
			cursor.sort(new BasicDBObject("nombre", -1));
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
