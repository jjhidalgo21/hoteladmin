package ara.soft.john.htl.actions.bd;

import java.net.UnknownHostException;

import ara.soft.john.htl.security.Rol;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class BDMongo {
	/*COLECCIONES - TABLAS DE LA BASE DE DATOS*/
	public static final String COLLECTION_USUARIO = "USUARIO";
	public static final String COLLECTION_ROL = "ROL";
	public static final String COLLECTION_TELEFONO = "TELEFONO";
	
	private static DB db = null;//la conexion a la base de datos
	
	static{
		try {
			System.out.println("Iniciando conexión con el servidor MongoDB...");
			MongoClient mongoClient = new MongoClient("localhost");
			System.out.println("Conenexión establecida, conectando a la base de datos...");
			db = mongoClient.getDB( "hotel_admin" );
			db.dropDatabase();
			System.out.println("Base de datos lista para realizar operaciones!!");
			inicializarBaseDeDatosVacia();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return la conexion con la base de datos
	 */
	public static DB getDb() {
		return db;
	}
	
	
	/**
	 * OBTIENE UNA COLECCION DE LA BASE DE DATOS, ESTO EQUIVALE A UNA TABLA DE SQL
	 * @param collectionName
	 * @return
	 */
	public static DBCollection getCollection(String collectionName){
		return db.getCollection(collectionName);
	}
	
	
	/**
	 * REALIZA TODAS LAS OPERACIONES PARA INSERTAR EN BASE DE DATOS LOS ELEMENTOS POR DEFECTO
	 */
	public static void inicializarBaseDeDatosVacia(){
		String defaultMsg = "Creando colección por defecto: ";
		if(!db.collectionExists(COLLECTION_ROL)){
			System.out.println(defaultMsg+COLLECTION_ROL+" ...");
			
			DBCollection coll = getCollection(COLLECTION_ROL);//obtiene la coleccion que almacenara los roles
			
			BasicDBObject doc = new BasicDBObject("nombre", Rol.ROL_ADMINISTRADOR);//crea un rol
			coll.insert(doc);//lo inserta
			
			doc = new BasicDBObject("nombre", Rol.ROL_NORMAL);//--
			coll.insert(doc);//--
			
			doc = new BasicDBObject("nombre", Rol.ROL_REPORTES);//--
			coll.insert(doc);//--
			
			doc = new BasicDBObject("nombre", Rol.ROL_CONFIGURACION);//--
			coll.insert(doc);//--
			
		}
		
		if(!db.collectionExists(COLLECTION_USUARIO)){
			System.out.println(defaultMsg+COLLECTION_USUARIO+" ...");
			
			DBCollection collRol = BDMongo.getCollection(BDMongo.COLLECTION_ROL);//obtiene la coleccion de usuarios
	
			DBCollection coll = getCollection(COLLECTION_USUARIO);//coleccion de usuarios
			
			BasicDBObject doc = new BasicDBObject("user", "admin")
	        .append("pass", "admin").append("roles", collRol.find().toArray())//aqui inserta la lista de roles
	        .append("identificacion", "0000").append("nombre", "Administrador General")
	        .append("activo", "true").append("correo", "c.andreyagro@gmail.com").append("puesto", "Administrador del Sistema");
			
			coll.insert(doc);
		}
		
		
		
		
	}
}
