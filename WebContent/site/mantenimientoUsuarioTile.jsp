<%@page import="java.util.ArrayList"%>
<%@page import="ara.soft.john.htl.utils.FormValidation"%>
<%@page import="ara.soft.john.htl.actions.site.MantenimientoUsuarios"%>
<%@page import="com.mongodb.DBObject"%>
<%@page import="com.mongodb.BasicDBObject"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- DATA TABLES -->
<link href="css/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />

<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>

<!-- DATA TABES SCRIPT -->
<script src="js/plugins/datatables/jquery.dataTables.js" type="text/javascript"></script>
<script src="js/plugins/datatables/dataTables.bootstrap.js" type="text/javascript"></script>


<%
String action = request.getParameter("action");
%>

<%if(action == null) {//solo muestra la lista%>

<!-- page script -->
<script type="text/javascript">
    $(function() {
        $('#tablaUsuarios').dataTable({
        	"order": [ 2, 'asc' ]
        });
    });
</script>

<%} %>

<div class="row">

	<%if(action == null) {//solo muestra la lista%>
	
    <div class="col-xs-12">
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">Lista de Usuarios</h3>
            </div><!-- /.box-header -->
            <br>
            <a class="btn btn-block btn-social btn-bitbucket" href="mantUsuario.htl?action=form">
                <i class="fa fa-plus"></i> Crear Usuario
            </a>
            <br>
            <div class="box-body table-responsive">
                <table id="tablaUsuarios" class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Identificación</th>
                            <th>Nombre Completo</th>
                            <th>Correo</th>
                            <th>Puesto</th>
                            <th>Télefono(s)</th>
                            <th>Rol(es)</th>
                            <th>Activo</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<%
                    		List<DBObject> listaUsuarios = (List<DBObject>) request.getAttribute("listaUsuarios");
                    		
                    		for(DBObject ob : listaUsuarios){
                    	%>
                    	<tr>
                    		<td><%=ob.get("user")%></td>
                            <td><%=ob.get("identificacion")%></td>
                            <td><%=ob.get("nombre")%></td>
                            <td><%=ob.get("correo")%></td>
                            <td><%=ob.get("puesto")%></td>
                            <td>
                            	<%if(ob.get("telefonos") != null){
                            		List<BasicDBObject> listaTel = (List<BasicDBObject>)ob.get("telefonos");
                            		for(BasicDBObject obTel : listaTel){%>
                            			<%=obTel.get("numero")%>
                            			<br>
                            		<%}
                            	}else{%>
                            	-
                            	<%}%>
                            </td>
                            <td>
                            	<%if(ob.get("roles") != null){
                            		List<BasicDBObject> listaRol = (List<BasicDBObject>)ob.get("roles");
                            		for(BasicDBObject obRol : listaRol){%>
                            			<%=obRol.get("nombre")%>
                            			<br>
                            		<%}
                            	}else{%>
                            	-
                            	<%}%>
                            </td>
                            <td>
                            	 <div class="checkbox">
                                     <label>
                                         <input type="checkbox" <%=(Boolean.valueOf(((String)ob.get("activo")))? "checked=\"checked\"" : "")%> disabled>
                                     </label>
                                 </div>
                            </td>
                          </tr>
                    	<%	
                    		}
                    	%>
                    </tbody>
                    <tfoot>
                        <tr>
                            <th>Usuario</th>
                            <th>Identificación</th>
                            <th>Nombre Completo</th>
                            <th>Correo</th>
                            <th>Puesto</th>
                            <th>Télefono(s)</th>
                            <th>Rol(es)</th>
                            <th>Activo</th>
                        </tr>
                    </tfoot>
                </table>
            </div><!-- /.box-body -->
        </div><!-- /.box -->
    </div>
    
    <%} %>
    
    
    <%if(action != null && action.equals("form")) {//solo muestra formulario%>
    <div class="col-xs-12">
	    <!-- general form elements disabled -->
	    <div class="box box-warning">
	        <div class="box-header">
	            <h3 class="box-title">Insertar/Editar Usuario</h3>
	        </div><!-- /.box-header -->
	        <div class="box-body">
	        	<%
	        	ArrayList<String[]> listaErroresParam = (ArrayList<String[]>) request.getAttribute("listaErroresParametros");
	        	boolean auxHayError = false;//por defecto false
	        	%>
	        	
	        	<%auxHayError = FormValidation.hayErrorParam("user", listaErroresParam);%>
	        	<s:form action="?action=form&do=save" method="POST">
	                <!-- text input -->
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                	<%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Usuario<%= FormValidation.getErrorMessage("user", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Usuario</label>
	                	<%}%>
	                    <s></s>
	                    <s:textfield name="req_user" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <%auxHayError = FormValidation.hayErrorParam("nombre", listaErroresParam);%>
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                    <%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Nombre Completo<%= FormValidation.getErrorMessage("nombre", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Nombre Completo</label>
	                	<%}%>
	                    <s></s>
	                    <s:textfield name="req_nombre" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <%auxHayError = FormValidation.hayErrorParam("identificacion", listaErroresParam);%>
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                	<%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Identificación<%= FormValidation.getErrorMessage("identificacion", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Identificación</label>
	                	<%}%>
	                    <s></s>
	                    <s:textfield name="req_identificacion" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <%auxHayError = FormValidation.hayErrorParam("correo", listaErroresParam);%>
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                	<%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Correo<%= FormValidation.getErrorMessage("correo", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Correo</label>
	                	<%}%>
	                    <s></s>
	                    <s:textfield name="req_correo" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <div class="form-group">
	                    <label>Puesto</label>
	                    <s></s>
	                    <s:textfield name="puesto" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <div class="form-group">
	                    <label>Rol(es)</label>
	                    <s:select multiple="true" list='selectRoles' name="roles"/>
	                </div>
	                <div class="form-group">
	                    <div class="checkbox">
	                        <label>
	                        	<s:checkbox name="activo" fieldValue="true" value="true"/>
	                            Activo
	                        </label>
	                    </div>
	                </div>
	                <br>
	                <%auxHayError = FormValidation.hayErrorParam("pass", listaErroresParam);%>
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                	<%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Contraseña<%= FormValidation.getErrorMessage("pass", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Contraseña</label>
	                	<%}%>
	                    <s></s>
	                    <s:password name="req_pass" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                <%auxHayError = FormValidation.hayErrorParam("conf_pass", listaErroresParam);%>
	                <div class="form-group <%= auxHayError? "has-error" : "" %>">
	                	<%if(auxHayError){%>
	                		<label class="control-label" ><i class="fa fa-times-circle-o"></i>Confirmar Contraseña<%= FormValidation.getErrorMessage("conf_pass", listaErroresParam)%></label>
	                	<%}else {%>
	                		<label>Confirmar Contraseña</label>
	                	<%}%>
	                    <s></s>
	                    <s:password name="conf_pass" cssClass="form-control" placeholder="Escriba aquí ..." />
	                </div>
	                
	                
	                <div class="box-footer">
	                	<s:submit cssClass="btn btn-primary" value="Guardar..."/>
                    </div>
	
	            </s:form>
	        </div><!-- /.box-body -->
	    </div><!-- /.box -->
	</div><!--/.col (right) -->
    <%} %>
    
    
</div>