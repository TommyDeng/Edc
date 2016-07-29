package com.edc.common.bo;

public class EventContentStudentLogin extends EventContent {
	private String userName;
	private boolean loginSuccess;

	public EventContentStudentLogin(String userName, String password,
			boolean loginSuccess) {
		super();
		this.userName = userName;
		this.loginSuccess = loginSuccess;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the loginSuccess
	 */
	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	/**
	 * @param loginSuccess
	 *            the loginSuccess to set
	 */
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

}
