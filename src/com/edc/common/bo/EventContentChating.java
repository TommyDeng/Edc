package com.edc.common.bo;

public class EventContentChating extends EventContent {
	private String userName;
	private String chatContent;

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
	 * @return the chatContent
	 */
	public String getChatContent() {
		return chatContent;
	}

	/**
	 * @param chatContent
	 *            the chatContent to set
	 */
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

}
