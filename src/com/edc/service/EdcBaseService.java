package com.edc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edc.common.bo.EventContentUserChangeInfo;
import com.edc.common.bo.User;
import com.edc.dao.EdcBaseDao;

@Service
public class EdcBaseService {

	
	@Autowired
	private EdcBaseDao edcBaseDao;

	private Map<String, User> userTagAndUserNameMap;

	public void userLogin(String userTag, String userName, boolean online) {
		User targetUser = getUserTagAndUserNameMap().get(userTag);
		if (targetUser == null) {
			targetUser = new User(userTag);
			getUserTagAndUserNameMap().put(userTag, targetUser);
		}
		targetUser.setOnline(online);
		targetUser.setUserName(userName);
	}

	public void userLogout(String userTag) {
		User targetUser = getUserTagAndUserNameMap().get(userTag);
		if (targetUser != null) {
			targetUser.setOnline(false);
			targetUser.setRaiseHand(false);
		}
	}

	public List<User> queryUsers(Boolean... isOnline) {
		List<User> usersList = new ArrayList<User>();

		for (Entry<String, User> targetUserEntry : getUserTagAndUserNameMap()
				.entrySet()) {
			boolean userIsOnline = targetUserEntry.getValue().isOnline();

			if ((Boolean.TRUE.equals(isOnline[0]) && userIsOnline)
					|| (Boolean.FALSE.equals(isOnline[0]) && !userIsOnline)
					|| isOnline == null) {
				usersList.add(targetUserEntry.getValue());
			}
		}

		return usersList;
	}

	public void updateUser(EventContentUserChangeInfo userInfo) {
		String userTag = userInfo.getUserTag();
		User targetUser = getUserTagAndUserNameMap().get(userTag);
		targetUser.setUserName(userInfo.getUserName());
	}

	public void updateUserRaiseHand(EventContentUserChangeInfo userInfo) {
		String userTag = userInfo.getUserTag();
		User targetUser = getUserTagAndUserNameMap().get(userTag);
		targetUser.setRaiseHand(userInfo.isRaiseHand());
	}

	private Map<String, User> getUserTagAndUserNameMap() {
		if (userTagAndUserNameMap == null) {
			userTagAndUserNameMap = new HashMap<String, User>();
//			userTagAndUserNameMap.put("dummy1", new User("dummy1", "dummy1",
//					true));
//			userTagAndUserNameMap.put("dummy2", new User("dummy2", "dummy2",
//					true));
//			userTagAndUserNameMap.put("dummy3", new User("dummy3", "dummy3",
//					false));
//			userTagAndUserNameMap.put("dummy4", new User("dummy4", "dummy4",
//					false));
		}
		return userTagAndUserNameMap;
	}
}
