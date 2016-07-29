package com.edc.utils;

import javax.websocket.Session;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.edc.common.Const;
import com.edc.common.bo.EventContentChating;
import com.edc.common.bo.EventContentClassroomInfo;
import com.edc.common.bo.EventContentFileShare;
import com.edc.common.bo.EventContentQueryUsers;
import com.edc.common.bo.EventContentStudentLogin;
import com.edc.common.bo.EventContentTeacherLogin;
import com.edc.common.bo.EventContentUserChangeInfo;
import com.edc.common.bo.EventMessage;
import com.edc.service.EdcBaseService;

public class WebSocketHelper {
	public static void userConnected(String userTag, Session session)
			throws Exception {
		session.getUserProperties().put(Const.UserPropKey_UserTag, userTag);
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		edcBaseService.userLogin(userTag, "", false);
		EdcCommonUtils.setUserUIPhase(Const.UserPropKey_UserUIPhase_Login,
				session);
	}

	public static void userDisconnected(Session session) throws Exception {
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		String userTag = (String) session.getUserProperties().get(
				Const.UserPropKey_UserTag);
		edcBaseService.userLogout(userTag);

		// broadcast user disconnected to queryUser phase
		EventMessage<EventContentQueryUsers> broadcastEventMsg = new EventMessage<EventContentQueryUsers>(
				Const.EVTP_QUERYUSERS, new EventContentQueryUsers(
						edcBaseService.queryUsers(true),
						edcBaseService.queryUsers(false)));
		EdcCommonUtils.broadcastEventMsgByUserUIPhase(broadcastEventMsg,
				Const.UserPropKey_UserUIPhase_QueryUers, session);
	}

	public static void loginCheckStudent(
			EventMessage<EventContentStudentLogin> enventMessageStudentLogin,
			Session session) throws Exception {
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		EventContentStudentLogin eventContentStudentLogin = (EventContentStudentLogin) enventMessageStudentLogin
				.getEventContent();
		String userName = eventContentStudentLogin.getUserName();
		String userTag = (String) session.getUserProperties().get(
				Const.UserPropKey_UserTag);

		edcBaseService.userLogin(userTag, userName, true);
		session.getUserProperties().put(Const.UserPropKey_UserName, userName);
		session.getUserProperties().put(Const.UserPropKey_UserRole,
				Const.ROLE_STUDENT);

		// broadcast new user connected to queryUser phase
		EventMessage<EventContentQueryUsers> broadcastEventMsg = new EventMessage<EventContentQueryUsers>(
				Const.EVTP_QUERYUSERS, new EventContentQueryUsers(
						edcBaseService.queryUsers(true),
						edcBaseService.queryUsers(false)));
		EdcCommonUtils.broadcastEventMsgByUserUIPhase(broadcastEventMsg,
				Const.UserPropKey_UserUIPhase_QueryUers, session);

		EdcCommonUtils.setUserUIPhase(Const.UserPropKey_UserUIPhase_HomePage,
				session);
		session.getBasicRemote().sendText(
				EdcCommonUtils.toJsonString(enventMessageStudentLogin));
	}

	public static void loginCheckTeacher(
			EventMessage<EventContentTeacherLogin> enventMessageTeacherLogin,
			Session session) throws Exception {

		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		boolean loginSuccess = true;
		EventContentTeacherLogin eventContentTeacherLogin = (EventContentTeacherLogin) enventMessageTeacherLogin
				.getEventContent();

		String userName = eventContentTeacherLogin.getUserName();
		String password = eventContentTeacherLogin.getPassword();
		String classrommName = eventContentTeacherLogin.getClassroomName();
		// 验证过程。。。
		if ("a".equals(userName) || "a".equals(password)) {
			loginSuccess = false;
		}
		eventContentTeacherLogin.setLoginSuccess(loginSuccess);

		String userTag = (String) session.getUserProperties().get(
				Const.UserPropKey_UserTag);

		if (loginSuccess) {
			edcBaseService.userLogin(userTag, userName, true);
			session.getUserProperties().put(Const.UserPropKey_UserName,
					userName);
			session.getUserProperties().put(Const.UserPropKey_UserRole,
					Const.ROLE_TEACHER);
			// broadcast new user connected to queryUser phase
			EventMessage<EventContentQueryUsers> broadcastEventMsg = new EventMessage<EventContentQueryUsers>(
					Const.EVTP_QUERYUSERS, new EventContentQueryUsers(
							edcBaseService.queryUsers(true),
							edcBaseService.queryUsers(false)));
			EdcCommonUtils.broadcastEventMsgByUserUIPhase(broadcastEventMsg,
					Const.UserPropKey_UserUIPhase_QueryUers, session);

			EdcCommonUtils.setUserUIPhase(
					Const.UserPropKey_UserUIPhase_HomePage, session);
			
			// change class info
			EventMessage<EventContentClassroomInfo> classroomEventMsg = new EventMessage<EventContentClassroomInfo>(
					Const.EVTP_CLASSROOM_INFO, new EventContentClassroomInfo(
							classrommName));
			EdcCommonUtils.broadcastEventMsgExceptUserUIPhase(
					classroomEventMsg, Const.UserPropKey_UserUIPhase_Login,
					session);
		}
		session.getBasicRemote().sendText(
				EdcCommonUtils.toJsonString(enventMessageTeacherLogin));
	}

	public static void chatingMessageBroadcast(
			EventMessage<EventContentChating> eventMsg, Session session)
			throws Exception {
		EdcCommonUtils.setUserUIPhase(Const.UserPropKey_UserUIPhase_Chat,
				session);

		// broadcast message except user in login phase
		EdcCommonUtils.broadcastEventMsgExceptUserUIPhase(eventMsg,
				Const.UserPropKey_UserUIPhase_Login, session);
	}

	public static void queryUser(EventMessage<EventContentQueryUsers> eventMsg,
			Session session) throws Exception {
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		eventMsg.setEventContent(new EventContentQueryUsers(edcBaseService
				.queryUsers(true), edcBaseService.queryUsers(false)));
		session.getBasicRemote()
				.sendText(EdcCommonUtils.toJsonString(eventMsg));
		EdcCommonUtils.setUserUIPhase(Const.UserPropKey_UserUIPhase_QueryUers,
				session);
	}

	public static void userChangeInfo(
			EventMessage<EventContentUserChangeInfo> eventMsg, Session session)
			throws Exception {
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		EventContentUserChangeInfo eventContentUserChangeInfo = eventMsg
				.getEventContent();
		edcBaseService.updateUser(eventContentUserChangeInfo);
		session.getUserProperties().put(Const.UserPropKey_UserName,
				eventContentUserChangeInfo.getUserName());
		EdcCommonUtils.broadcastEventMsgByUserUIPhase(eventMsg,
				Const.UserPropKey_UserUIPhase_QueryUers, session);

	}

	public static void userRaiseHand(
			EventMessage<EventContentUserChangeInfo> eventMsg, Session session)
			throws Exception {
		EdcBaseService edcBaseService = EdcCommonUtils.getContextBaseService();
		EventContentUserChangeInfo eventContentUserChangeInfo = eventMsg
				.getEventContent();
		// 更新后台数据状态
		edcBaseService.updateUserRaiseHand(eventContentUserChangeInfo);
		// 发送给老师端
		EdcCommonUtils.broadcastEventMsgByUserUIPhase(eventMsg,
				Const.UserPropKey_UserUIPhase_QueryUers, session);
		// 发送给学生端

		EdcCommonUtils.broadcastEventMsgByUserTag(eventMsg,
				eventContentUserChangeInfo.getUserTag(), session);
	}

	public static void fileShare(EventMessage<EventContentFileShare> eventMsg,
			Session session) throws Exception {
		EventContentFileShare eventContentFileShare = eventMsg
				.getEventContent();
		String fileName = eventContentFileShare.getFileName();
		String targetFile = ProjectConfigUtils.getDefaultFileShareRestorePath()
				+ eventContentFileShare.getFileTag();
		// change file extension
		if (StringUtils.isBlank(FilenameUtils.getExtension(targetFile))) {
			EdcFileUtils.appendFileExtension(targetFile,
					FilenameUtils.getExtension(fileName));
		}

		// broadcast message except user in login phase
		EdcCommonUtils.broadcastEventMsgExceptUserUIPhase(eventMsg,
				Const.UserPropKey_UserUIPhase_Login, session);

	}

}
