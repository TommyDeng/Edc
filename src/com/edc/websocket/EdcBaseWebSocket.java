package com.edc.websocket;

import java.util.UUID;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.edc.common.Const;
import com.edc.common.bo.EventContent;
import com.edc.common.bo.EventContentChating;
import com.edc.common.bo.EventContentFileShare;
import com.edc.common.bo.EventContentQueryUsers;
import com.edc.common.bo.EventContentStudentLogin;
import com.edc.common.bo.EventContentTeacherLogin;
import com.edc.common.bo.EventContentUserChangeInfo;
import com.edc.common.bo.EventMessage;
import com.edc.utils.EdcCommonUtils;
import com.edc.utils.EdcFileUtils;
import com.edc.utils.ProjectConfigUtils;
import com.edc.utils.WebSocketHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ServerEndpoint(value = "/websocket/{userTag}")
public class EdcBaseWebSocket {
	private Gson gson = new Gson();

	@OnOpen
	public void open(Session session,
			@PathParam(value = "userTag") String userTag) throws Exception {
		WebSocketHelper.userConnected(userTag, session);
	}

	@OnMessage
	public void onMessage(Session session, String inMsgStr) throws Exception {
		EventMessage<EventContent> msg = gson.fromJson(inMsgStr,
				new TypeToken<EventMessage<EventContent>>() {
				}.getType());
		String enventType = msg.getEventType();
		switch (enventType) {
		case Const.EVTP_LOGIN_TC:
			EventMessage<EventContentTeacherLogin> enventMessageTeacherLogin = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentTeacherLogin>>() {
							}.getType());

			WebSocketHelper.loginCheckTeacher(enventMessageTeacherLogin, session);
			break;
		case Const.EVTP_LOGIN_SD:
			EventMessage<EventContentStudentLogin> enventMessageStudentLogin = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentTeacherLogin>>() {
							}.getType());

			WebSocketHelper.loginCheckStudent(enventMessageStudentLogin, session);
			break;
		case Const.EVTP_LOGOUT_TC:
			WebSocketHelper.userDisconnected(session);
			break;
		case Const.EVTP_CHAT:
			EventMessage<EventContentChating> enventMessageChating = gson
					.fromJson(inMsgStr,
							new TypeToken<EventMessage<EventContentChating>>() {
							}.getType());
			WebSocketHelper.chatingMessageBroadcast(enventMessageChating,
					session);
			break;

		case Const.EVTP_QUERYUSERS:
			EventMessage<EventContentQueryUsers> enventMessageQueryUsers = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentQueryUsers>>() {
							}.getType());
			WebSocketHelper.queryUser(enventMessageQueryUsers, session);
			break;

		case Const.EVTP_USER_CHANGE_INFO:
			EventMessage<EventContentUserChangeInfo> enventMessageUserChangeInfo = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentUserChangeInfo>>() {
							}.getType());
			WebSocketHelper
					.userChangeInfo(enventMessageUserChangeInfo, session);
			break;

		case Const.EVTP_RAISE_HAND:
			EventMessage<EventContentUserChangeInfo> enventMessageUserRaiseHand = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentUserChangeInfo>>() {
							}.getType());
			WebSocketHelper.userRaiseHand(enventMessageUserRaiseHand, session);
			break;

		case Const.EVTP_FILE_SHARE_DISPATCH:
			EventMessage<EventContentFileShare> enventMessageFileShare = gson
					.fromJson(
							inMsgStr,
							new TypeToken<EventMessage<EventContentFileShare>>() {
							}.getType());
			WebSocketHelper.fileShare(enventMessageFileShare, session);
			break;

		default:
			break;
		}
	}

	@OnMessage
	public void onMessage(Session session, byte[] inMsg) throws Exception {
		String fileTag = UUID.randomUUID().toString();
		EdcFileUtils.saveAsFile(inMsg,
				ProjectConfigUtils.getDefaultFileShareRestorePath(), fileTag);
		String userName = (String) session.getUserProperties().get(
				Const.UserPropKey_UserName);
		EventContentFileShare eventContentFileShare = new EventContentFileShare(
				userName, fileTag);
		EventMessage<EventContentFileShare> eventMsg = new EventMessage<EventContentFileShare>(
				Const.EVTP_FILE_SHARE_UPLOAD, eventContentFileShare);
		session.getBasicRemote()
				.sendText(EdcCommonUtils.toJsonString(eventMsg));
	}

	@OnError
	public void onError(Session session, Throwable e) {
		e.printStackTrace();
	}

	@OnClose
	public void onClose(Session session, CloseReason reson) throws Exception {
		WebSocketHelper.userDisconnected(session);
		System.err
				.println(reson.getCloseCode() + ":" + reson.getReasonPhrase());
	}
}
