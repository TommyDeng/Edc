function wsConnect(wsUrl) {
	if (wsocket != null
			&& (wsocket.readyState == wsocket.OPEN || wsocket.readyState == wsocket.CONNECTING)) {
		wsocket.close();
	} else {
		if (!localStorage.userTag) {
			var guid = newGuid();
			localStorage.userTag = guid;
		}
		// var url = "ws://10.141.139.141:9080/Edc/websocket/guid"
		var url = wsUrl + localStorage.userTag;
		GuserTag = localStorage.userTag;
		wsocket = new WebSocket(url);
		wsocket.onopen = function(evt) {
			onOpen(evt, localStorage.userTag);
		};
		wsocket.onclose = function(evt) {
			onClose(evt, localStorage.userTag);
		};
		wsocket.onmessage = function(evt) {
			onMessage(evt, localStorage.userTag);
		};
		wsocket.onerror = function(evt) {
			onError(evt, localStorage.userTag);
		};
	}
}

function onOpen(evt, userTag) {
	console.info('wsocket Connected...' + userTag);
}
function onClose(evt, userTag) {
	console.info('wsocket Disconnected...' + userTag);
	console.info(evt.reason);
}
function onMessage(evt, userTag) {
	if (typeof (evt.data) == "string") {
		handlEvent(evt.data);
	} else {
		console.info('client recv binary<==:' + evt.data);
		receiveFileShare(evt.data);
		// var reader = new FileReader();
		// reader.onload = function(evt) {
		// if (evt.target.readyState == FileReader.DONE) {
		// var data = new Uint8Array(evt.target.result);
		// handler(data);
		// }
		// };
		// reader.readAsArrayBuffer(evt.data);
	}
}

function onError(evt, userTag) {
	console.error('wsocket Error occured: ' + evt.message);
}

/**
 * 发送消息
 * 
 * @param eventContent
 * @param eventType
 * @returns {Boolean}
 */
function sendEvent(eventContent, eventType) {
	if (wsocket.readyState == wsocket.OPEN) {
		var outJsonObj = new EventMessage(eventType);
		EventMessage.prototype.eventContent;
		outJsonObj.eventContent = eventContent;

		var outJsonStr = JSON.stringify(outJsonObj);

		console.info('client send msg==>:' + outJsonStr);
		wsocket.send(outJsonStr);

	} else {
		console.info('WebSocket Not Valiad...');
	}
}

/**
 * 发送File
 * 
 * @param eventContent
 * @param eventType
 * @returns {Boolean}
 */
function sendFile(fileOut) {
	if (wsocket.readyState == wsocket.OPEN) {
		// restore file info bag
		GtempFileName = fileOut.name;// : "smallPng.png"
		GtempFileSize = fileOut.size;// : 14151
		GtempFileType = fileOut.type;// : "image/png";image/jpeg

		// 1 send File(Blob)
		wsocket.send(fileOut);
		// 2 send ArrayBuffer
		// var oReader = new FileReader();
		// oReader.onload = function(evt) {
		// if (evt.target.readyState == FileReader.DONE) {
		// var binaryString = evt.target.result;
		// console.info(binaryString);
		// wsocket.binaryType = "ArrayBuffer";
		// console.info(wsocket.binaryType);
		// wsocket.send(binaryString);
		// }
		// };
		// oReader.readAsArrayBuffer(uploadFile);
	} else {
		console.info('WebSocket Not Valiad...');
	}
}


/**
 * 处理消息
 * 
 * @param data
 */
function handlEvent(data) {
	console.info('client recv msg<==:' + data);
	var returnMsg = JSON.parse(data);
	var enventType = returnMsg.eventType;
	var enventContent = returnMsg.eventContent;
	switch (enventType) {
	case EVTP_LOGIN_TC:
		loginCheckTeacher(enventContent);
		break;

	case EVTP_LOGIN_SD:
		loginCheckStudent(enventContent);
		break;

	case EVTP_CHAT:
		chatMessageRecv(enventContent);
		break;

	case EVTP_QUERYUSERS:
		queryUser(enventContent);
		break;
	case EVTP_USER_CHANGE_INFO:
		updateTargetUserInfo(enventContent);
		break;
	case EVTP_RAISE_HAND:
		updateTargetUserRaiseHand(enventContent);
		break;
	case EVTP_FILE_SHARE_UPLOAD:
		sendFileInfoBag(enventContent);
		break;
	case EVTP_FILE_SHARE_DISPATCH:
		receiveFileShare(enventContent);
		break;
	case EVTP_CLASSROOM_INFO:
		updateClassroomInfo(enventContent);
		break;
	default:
		console.error("handlEvent error eventType: " + enventType);
		break;
	}
}

function EventMessage(eventType) {
	this.eventType = eventType;
}
function EventContent() {
}