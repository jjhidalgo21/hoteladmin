package ara.soft.john.htl.actions.base;

public class PublicActionBase extends ActionBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7617989838541582725L;
	
	@Override
	public String execute() throws Exception {
		this.validateExistUser = false;//no valida si hay un usuario logueado
		return super.execute();
	}

}
