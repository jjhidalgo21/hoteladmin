package ara.soft.john.htl.actions.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import ara.soft.john.htl.security.Session;

import com.opensymphony.xwork2.ActionSupport;

public class ActionBase extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1652268947841766356L;
	protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    public String errorMessage = null;
	
	protected boolean validateExistUser = true;
	private Session session = null;
	
	@Override
	public String execute() throws Exception {
		HttpSession httpSession = request.getSession();
		
		if(session == null && httpSession.getAttribute("session") != null)
			session = (Session) httpSession.getAttribute("session");
		
		saveSession();
		
		if(validateExistUser && (session == null || session._userNameLogIn == null)){
			response.sendRedirect("login.htl");
			return null;
		}
		
		return "CORRECTO";
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
}
