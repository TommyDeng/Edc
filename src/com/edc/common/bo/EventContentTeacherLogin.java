package com.edc.common.bo;

public class EventContentTeacherLogin extends  EventContent{
	private String userName;
	private String password;
	private String classroomName;
	private boolean loginSuccess;
	
	
	public EventContentTeacherLogin(String userName, String password,
			boolean loginSuccess) {
		super();
		this.userName = userName;
		this.password = password;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

}
