package com.edc.common.bo;

public class EventContentFileShare extends EventContent {
	private String userName;

	private String fileTag;
	private String fileName;

	private String fileSize;
	private String fileType;

	public EventContentFileShare() {
		super();
	}

	public EventContentFileShare(String userName, String fileTag) {
		super();
		this.userName = userName;
		this.fileTag = fileTag;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileTag() {
		return fileTag;
	}

	public void setFileTag(String fileTag) {
		this.fileTag = fileTag;
	}

}
