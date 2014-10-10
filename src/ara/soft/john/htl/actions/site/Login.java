package ara.soft.john.htl.actions.site;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import ara.soft.john.htl.actions.base.PublicActionBase;
import ara.soft.john.htl.actions.bd.BDMongo;

public class Login extends PublicActionBase{

	public String user = null;
	public String pass = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -9160262948341453832L;
	private String result = "CORRECTO";
	
	@Override
	public String execute() throws Exception {
		super.execute();
        
        this.createSession();//se crea una session en chipinpet
        
        login();
		
        return result;
	}
	
	
	public void login(){
		if(user != null && pass != null){
			DBCollection coll = BDMongo.getCollection(BDMongo.COLLECTION_USUARIO);//obtiene la coleccion(tabla) de usuarios
			
			BasicDBObject query = new BasicDBObject("user", user).append("pass", pass);//genera un query basico
			
			DBCursor cursor = coll.find(query);//hace el query
			
			try{
				if(cursor.hasNext()){//mueve el cursor para ver si encontro un elemento
					DBObject obj = cursor.next();
					getSession()._userNameLogIn = (String) obj.get("user");
					result = "DO_LOGIN";
				}
			}finally{
				cursor.close();
			}
		}
	}
	

}
