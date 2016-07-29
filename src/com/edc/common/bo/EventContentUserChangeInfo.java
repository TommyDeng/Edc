package com.edc.common.bo;

public class EventContentUserChangeInfo extends EventContent {
	private String userTag;
	private String userName;
	private boolean raiseHand;

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
	 * @return the userTag
	 */
	public String getUserTag() {
		return userTag;
	}

	/**
	 * @param userTag
	 *            the userTag to set
	 */
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	/**
	 * @return the raiseHand
	 */
	public boolean isRaiseHand() {
		return raiseHand;
	}

	/**
	 * @param raiseHand
	 *            the raiseHand to set
	 */
	public void setRaiseHand(boolean raiseHand) {
		this.raiseHand = raiseHand;
	}

}
