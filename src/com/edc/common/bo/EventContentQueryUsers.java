package com.edc.common.bo;

import java.util.List;

public class EventContentQueryUsers extends EventContent {

	public EventContentQueryUsers(List<User> onlineUsers,
			List<User> offlineUsers) {
		super();
		this.onlineUsers = onlineUsers;
		this.offlineUsers = offlineUsers;
	}

	private List<User> onlineUsers;

	private List<User> offlineUsers;

	public List<User> getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(List<User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

	public List<User> getOfflineUsers() {
		return offlineUsers;
	}

	public void setOfflineUsers(List<User> offlineUsers) {
		this.offlineUsers = offlineUsers;
	}

}
