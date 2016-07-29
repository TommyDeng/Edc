package com.edc.common.bo;

public class User {
	private String userName;
	private String userTag;
	private boolean isOnline;
	private boolean raiseHand;

	public User(String userTag) {
		super();
		this.userTag = userTag;
	}
	
	public User(String userTag, String userName, boolean isOnline) {
		super();
		this.userTag = userTag;
		this.userName = userName;
		this.isOnline = isOnline;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * @return the raiseHand
	 */
	public boolean isRaiseHand() {
		return raiseHand;
	}

	/**
	 * @param raiseHand the raiseHand to set
	 */
	public void setRaiseHand(boolean raiseHand) {
		this.raiseHand = raiseHand;
	}
	
	

}
