package com.edc.common.bo;


public class EventMessage<T extends EventContent> {
	private String eventType;
	private T eventContent;

	public EventMessage(String eventType, T eventContent) {
		super();
		this.eventType = eventType;
		this.eventContent = eventContent;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the eventContent
	 */
	public T getEventContent() {
		return eventContent;
	}

	/**
	 * @param eventContent
	 *            the eventContent to set
	 */
	public void setEventContent(T eventContent) {
		this.eventContent = eventContent;
	}

}
