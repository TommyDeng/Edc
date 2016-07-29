package com.edc.utils;

import javax.websocket.Session;

import org.springframework.web.context.ContextLoader;

import com.edc.common.Const;
import com.edc.common.bo.EventMessage;
import com.edc.service.EdcBaseService;
import com.google.gson.Gson;

public class EdcCommonUtils {
	public static String toJsonString(EventMessage<?> outContent) {
		Gson gson = new Gson();
		return gson.toJson(outContent);
	}

	public static EdcBaseService getContextBaseService() {
		return (EdcBaseService) ContextLoader.getCurrentWebApplicationContext()
				.getBean("edcBaseService");
	}

	public static void setUserUIPhase(String phase, Session session) {
		session.getUserProperties().put(Const.UserPropKey_UserUIPhase, phase);
	}

	public static void broadcastEventMsgByUserUIPhase(EventMessage<?> eventMsg,
			String phase, Session session) throws Exception {
		for (Session targetSession : session.getOpenSessions()) {
			String userUIPhase = (String) targetSession.getUserProperties()
					.get(Const.UserPropKey_UserUIPhase);
			String userRole = (String) targetSession.getUserProperties().get(
					Const.UserPropKey_UserRole);
			if (phase.equals(userUIPhase)
					&& Const.ROLE_TEACHER.equals(userRole)) {
				targetSession.getBasicRemote().sendText(toJsonString(eventMsg));
			}
		}
	}

	public static void broadcastEventMsgExceptUserUIPhase(
			EventMessage<?> eventMsg, String exceptPhase, Session session) throws Exception {
		for (Session targetSession : session.getOpenSessions()) {
			String userUIPhase = (String) targetSession.getUserProperties()
					.get(Const.UserPropKey_UserUIPhase);
			if (!exceptPhase.equals(userUIPhase)) {
				targetSession.getBasicRemote().sendText(toJsonString(eventMsg));
			}
		}
	}
	
	public static void broadcastEventMsgByUserTag(
			EventMessage<?> eventMsg, String targetUserTag, Session session) throws Exception {
		for (Session targetSession : session.getOpenSessions()) {
			String userTag = (String) targetSession.getUserProperties()
					.get(Const.UserPropKey_UserTag);
			if (targetUserTag.equals(userTag)) {
				targetSession.getBasicRemote().sendText(toJsonString(eventMsg));
			}
		}
	}
	
}
