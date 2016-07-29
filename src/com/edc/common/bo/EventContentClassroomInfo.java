package com.edc.common.bo;

public class EventContentClassroomInfo extends EventContent {
	private String classroomName;

	
	public EventContentClassroomInfo(String classroomName) {
		super();
		this.classroomName = classroomName;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

}
