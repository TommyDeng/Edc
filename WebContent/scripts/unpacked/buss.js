//common var
var wsocket = null;
var GuserName = null;
var GuserTag = null;
var GuserRaiseHand = false;
var GuserRole = null;

var GtempFileName = null;// : "smallPng.png"
var GtempFileSize = null;// : 14151
var GtempFileType = null;// : "image/png";image/jpeg

var GtempFileoTimer = 0;

var SP = "_";
var EVTP_LOGIN_TC = "EVTPLOGINTC";
var EVTP_LOGIN_SD = "EVTPLOGINSD";
var EVTP_LOGOUT_TC = "EVTPLOGOUTTC";
var EVTP_CHAT = "EVTPCHAT";
var EVTP_QUERYUSERS = "EVTPQUERYUSERS";
var EVTP_USER_CHANGE_INFO = "EVTPUSERCHANGEINFO";
var EVTP_RAISE_HAND = "EVTPRAISEHAND";

var EVTP_FILE_SHARE_UPLOAD = "EVTPFILESHAREUPLOAD";
var EVTP_FILE_SHARE_DISPATCH = "EVTPFILESHAREDISPATCH";

var EVTP_SHARESCREEN = "EVTPSHARESCREEN";
var EVTP_CLASSROOM_INFO = "EVTPCLASSROOMINFO";
var EVTP_UNKONW = "EVTPUNKONW";

var ROLE_STUDENT = "STUDENT";
var ROLE_TEACHER = "TEACHER";
var ROLE_ADMIN = "ADMIN";

$(document).ready(function() {
	// $(document).bind("contextmenu", function(e) {
	// return false;
	// });
	// $(document).bind("keydown", function(e) {
	// if (event.keyCode == 116) {
	// event.keyCode = 0;
	// event.cancelBubble = true;
	// return false;
	// }
	// });

	checkHtml5Support();
	initPage();
	// wsConnect("ws://192.168.1.102:8080/Edc/websocket/");
	wsConnect("ws://10.141.138.112:9080/Edc/websocket/");
	// wsConnect("ws://10.141.139.141:9080/Edc/myHandler/");
});

function initPage() {
	GuserRole = getURLParameter('role');
	var versionTips = "";
	if (ROLE_TEACHER == GuserRole) {
		versionTips = " - Teacher Clinet";
		$("#login_btn").click(function() {
			teacherLogin();
		});

		$("#student_menu1").hide();

	} else if (ROLE_ADMIN == GuserRole) {
		versionTips = " - Admin";
		$("#login_btn").click(function() {
			teacherLogin();
		});

		$("#header_tips").html("Admin");

	} else {// 默认为学生端
		versionTips = " - Student Clinet";
		$("#password_tr").hide();

		$("#login_btn").click(function() {
			studentLogin();
		});

		$("#teacher_menu1").hide();
		$("#teacher_menu2").hide();
	}

	$("#version").append(versionTips);

}

/**
 * teacher login
 */
function teacherLogin() {
	if ($.trim($("#username").val()) == "") {
		$("#login_tips_div").html("Empty name...");
		twinkleHiddenTipsDiv("login_tips_div", 500);
		return false;
	}
	var tracherLoginContent = new EventContent();
	EventContent.prototype.userName;
	EventContent.prototype.password;
	EventContent.prototype.classroomName;
	tracherLoginContent.userName = $("#username").val();
	tracherLoginContent.password = $("#password").val();
	tracherLoginContent.classroomName = $("#classroomName").val();
	sendEvent(tracherLoginContent, EVTP_LOGIN_TC);
}

/**
 * student login
 */
function studentLogin() {
	if ($.trim($("#username").val()) == "") {
		$("#login_tips_div").html("Empty name...");
		twinkleHiddenTipsDiv("login_tips_div", 500);
		return false;
	}
	var studentLoginContent = new EventContent();
	EventContent.prototype.userName;
	studentLoginContent.userName = $("#username").val();
	sendEvent(studentLoginContent, EVTP_LOGIN_SD);
}

function teacherLoginSuccess() {
	pageRedirect("login-ui", "all_main_page", false);
	$("#teacher_menu1").click();
}

function studentLoginSuccess() {
	pageRedirect("login-ui", "all_main_page", false);
	$("#student_menu1").click();
}

function teacherLoginFailed() {
	$("#login_tips_div").html("Invalid name or password...");
	twinkleHiddenTipsDiv("login_tips_div", 500);
}

function userLogout() {
	sendEvent(null, EVTP_LOGOUT_TC);
	if (ROLE_TEACHER == GuserRole) {
		location.href = "index.xhtml?role=" + ROLE_TEACHER;
	} else if (ROLE_ADMIN == GuserRole) {
		location.href = "index.xhtml?role=" + ROLE_ADMIN;
	} else {
		location.href = "index.xhtml?role=" + ROLE_STUDENT;
	}
}

/**
 * click student_demonstration
 */
function onM_student_demonstration(bottonElement) {
	panelRedirect("teacher_menu_studentdemonstration");
	hightLightButton(bottonElement.id);
	sendEvent(null, EVTP_QUERYUSERS);
}

function onM_student_HomePage(bottonElement) {
	hightLightButton(bottonElement.id);
	$("#student_info_div").html(GuserName);
	panelRedirect("student_homepage");
}

/**
 * click screen_broadcast
 */
function onM_screen_broadcast(bottonElement) {
	hightLightButton(bottonElement.id);
	panelRedirect("teacher_menu_screenbroadcast");
}

/**
 * click chatingroom
 */
function onM_chatingroom(bottonElement) {
	hightLightButton(bottonElement.id);
	panelRedirect("teacher_menu_chatingroom");
}

/**
 * click file_sharing
 */
function onM_file_sharing(bottonElement) {
	hightLightButton(bottonElement.id);
	panelRedirect("teacher_menu_filesharing");
}

/**
 * click file_sharing over Http
 */
function onM_file_sharing2(bottonElement) {
	hightLightButton(bottonElement.id);
	panelRedirect("teacher_menu_filesharing2");
}

function loginCheckTeacher(enventContent) {
	if (enventContent.loginSuccess) {
		GuserName = enventContent.userName;
		teacherLoginSuccess();
	} else {
		teacherLoginFailed();
	}
}

function loginCheckStudent(enventContent) {
	if (enventContent.loginSuccess) {
		GuserName = enventContent.userName;
		studentLoginSuccess();
	} else {
		teacherLoginFailed();
	}
}

function queryUser(enventContent) {
	var innerHtml = "";
	var maxRowElementCount = 3;
	var result = 0;
	var totalOnline = $(enventContent.onlineUsers).size();
	$(enventContent.onlineUsers)
			.each(
					function(index, element) {
						result = index % maxRowElementCount;
						if (result == 0) {
							innerHtml += "<tr>";
						}

						var addStyleForRaiseHandUser = "";
						if (element.raiseHand) {
							addStyleForRaiseHandUser = " style='background-color: crimson;' ";
						}

						innerHtml += "<td>";
						innerHtml += "<div id='user_"
								+ element.userTag
								+ "' class='online-user-div' "
								+ addStyleForRaiseHandUser
								+ " onclick='canelRaiseHandByTeacher(this.id)' >"
								+ element.userName + "</div>";
						innerHtml += "</td>";
						if (result == maxRowElementCount - 1
								|| index == totalOnline - 1) {
							innerHtml += "</tr>";
						}
					});
	$("#online_users_table").html(innerHtml);

	innerHtml = "";
	var totalOffline = $(enventContent.offlineUsers).size();
	$(enventContent.offlineUsers).each(
			function(index, element) {
				result = index % maxRowElementCount;
				if (result == 0) {
					innerHtml += "<tr>";
				}
				innerHtml += "<td>";
				innerHtml += "<div id='user_" + element.userTag
						+ "' class='offline-user-div'>" + element.userName
						+ "</div>";
				innerHtml += "</td>";
				if (result == maxRowElementCount - 1
						|| index == totalOffline - 1) {
					innerHtml += "</tr>";
				}
			});
	$("#offline_users_table").html(innerHtml);
}

function chatMessageRecv(enventContent) {
	var chatShowMsg = "<p>" + enventContent.userName + " : "
			+ enventContent.chatContent + "</p>";
	// var chatShowMsg2 = enventContent.userName + " : "
	// + enventContent.chatContent + "<p>";

	$("#chat_output_div").append(chatShowMsg);
	var divHeight = $("#chat_output_div")[0].scrollHeight;
	$("#chat_output_div").scrollTop(divHeight);
}

function chatMessageSendOut() {
	var chatingContent = new EventContent();
	EventContent.prototype.userName;
	EventContent.prototype.chatContent;
	chatingContent.userName = GuserName;
	chatingContent.chatContent = $("#chat_input_textarea").val();
	sendEvent(chatingContent, EVTP_CHAT);
	$("#chat_input_textarea").val("");
}

function prepareStudentChangeInfo() {
	$("#student_changeinfo_div").toggle(500);
}

function sendStudentChangeInfo() {
	var newName = $("#student_changeinfo_div_name_input").val();

	if ($.trim(newName) == "") {
		$("#change_name_tips_div").html("Empty name...");
		twinkleHiddenTipsDiv("change_name_tips_div", 500);
		return;
	}

	if (GuserName !== newName) {
		GuserName = newName;

		var userChangeInfoContent = new EventContent();
		EventContent.prototype.userTag;
		EventContent.prototype.userName;
		userChangeInfoContent.userTag = localStorage.userTag;
		userChangeInfoContent.userName = GuserName;
		sendEvent(userChangeInfoContent, EVTP_USER_CHANGE_INFO);
	}
	$("#student_info_div").html(GuserName);
	$("#change_name_tips_div").animate({
		opacity : '0'
	});
	$("#student_changeinfo_div").toggle(500);
}

function updateTargetUserInfo(enventContent) {
	var targetUserTag = enventContent.userTag;
	var targetUserName = enventContent.userName;
	$("#user_" + targetUserTag).html(targetUserName);
}

function studentRaisehand() {
	var raiseHandContent = new EventContent();
	EventContent.prototype.userTag;
	EventContent.prototype.raiseHand;
	raiseHandContent.userTag = localStorage.userTag;

	GuserRaiseHand = !GuserRaiseHand;
	raiseHandContent.raiseHand = GuserRaiseHand;
	sendEvent(raiseHandContent, EVTP_RAISE_HAND);
}

function updateTargetUserRaiseHand(enventContent) {
	// 更新老师端
	var targetUserTag = enventContent.userTag;
	var targetUserRaiseHand = enventContent.raiseHand;
	if (targetUserRaiseHand) {
		twinkleUserIcon("user_" + targetUserTag);
	} else {
		cancelTwinkleUserIcon("user_" + targetUserTag);
	}

	// 更新指定的学生端
	if (targetUserTag == GuserTag) {
		GuserRaiseHand = targetUserRaiseHand;
		if (GuserRaiseHand) {
			$("#student_raisehand_div").attr("src", "images/edc/handsdown.png");
			// $("#student_raisehand_div").css("background-color", "cadetblue");
		} else {
			$("#student_raisehand_div").attr("src", "images/edc/handsup.png");
			// $("#student_raisehand_div").removeAttr("style");
		}
	}

}

function canelRaiseHandByTeacher(pageElementId) {
	var raiseHandContent = new EventContent();
	EventContent.prototype.userTag;
	EventContent.prototype.raiseHand;
	raiseHandContent.userTag = pageElementId.substring(5);
	raiseHandContent.raiseHand = false;
	sendEvent(raiseHandContent, EVTP_RAISE_HAND);
}

/**
 * 文件共享
 */
function fileShareSelected() {
	// get selected file element
	// var uploadFile = document.getElementById('upload_share_file').files[0];
	// console.info("file size:" + uploadFile.size);

	// var oReader = new FileReader();
	// 图片预览
	// var oImage = document.getElementById('preview');
	// oReader.onload = function(e) {
	// // e.target.result contains the DataURL which we will use as a source of
	// // the image
	// // printObjectAllProperties(e.target);
	//
	// oImage.src = e.target.result;
	// oImage.onload = function() { // binding onload event
	// // we are going to display some custom image information here
	// // console.info("file type:" + bytesToSize(uploadFile.size));
	// };
	// };

	// read selected file as DataURL
	// oReader.readAsDataURL(uploadFile);
}

function startFileShareUploading() {
	var uploadFile = document.getElementById('upload_share_file').files[0];
	if (uploadFile) {
		sendFile(uploadFile);
		$("#upload_share_file").val("");
	}
}

function sendFileInfoBag(fileShareContent) {
	fileShareContent.fileName = GtempFileName;
	fileShareContent.fileSize = GtempFileSize;
	fileShareContent.fileType = GtempFileType;

	sendEvent(fileShareContent, EVTP_FILE_SHARE_DISPATCH);

	GtempFileTag = null;
	GtempFileName = null;
	GtempFileSize = null;
	GtempFileType = null;

}

function startFileShareUploading2() {
	var uploadFile = document.getElementById('upload_share_file2').files[0];

	GtempFileName = uploadFile.name;// : "smallPng.png"
	GtempFileSize = uploadFile.size;// : 14151
	GtempFileType = uploadFile.type;// : "image/png";image/jpeg

	var tempFileTag = newGuid();//
	GtempFileTag = tempFileTag;

	var vFD = new FormData();
	vFD.append("fileTag", tempFileTag);
	vFD.append("fileBlob", uploadFile);
	// create XMLHttpRequest object, adding few event listeners, and POSTing
	// our data
	var oXHR = new XMLHttpRequest();
	oXHR.upload.addEventListener('progress', uploadProgress, false);
	oXHR.addEventListener('load', uploadFinish, false);
	oXHR.addEventListener('error', uploadError, false);
	oXHR.addEventListener('abort', uploadAbort, false);
	oXHR.open('POST', 'storeFile');
	console.info("file send :" + tempFileTag);
	oXHR.send(vFD);

	$("#file_share2_progress").fadeIn("slow");
	// GtempFileoTimer = setInterval(doInnerUpdates, 300);

}

// function doInnerUpdates() { // we will use this function to display upload
// speed
// var iCB = iBytesUploaded;
// var iDiff = iCB - iPreviousBytesLoaded;
//
// // if nothing new loaded - exit
// if (iDiff == 0)
// return;
//
// iPreviousBytesLoaded = iCB;
// iDiff = iDiff * 2;
// var iBytesRem = iBytesTotal - iPreviousBytesLoaded;
// var secondsRemaining = iBytesRem / iDiff;
//
// // update speed info
// var iSpeed = iDiff.toString() + 'B/s';
// if (iDiff > 1024 * 1024) {
// iSpeed = (Math.round(iDiff * 100 / (1024 * 1024)) / 100).toString()
// + 'MB/s';
// } else if (iDiff > 1024) {
// iSpeed = (Math.round(iDiff * 100 / 1024) / 100).toString() + 'KB/s';
// }
//
// document.getElementById('speed').innerHTML = iSpeed;
// document.getElementById('remaining').innerHTML = '| '
// + secondsToTime(secondsRemaining);
// }

function uploadProgress(e) { // upload process in progress
	if (e.lengthComputable) {
		// iBytesUploaded = e.loaded;
		// iBytesTotal = e.total;
		// var iPercentComplete = Math.round(e.loaded * 100 / e.total);
		// var iBytesTransfered = bytesToSize(iBytesUploaded);
		//
		// document.getElementById('progress_percent').innerHTML =
		// iPercentComplete
		// .toString()
		// + '%';
		// document.getElementById('progress').style.width = (iPercentComplete *
		// 4)
		// .toString()
		// + 'px';
		// document.getElementById('b_transfered').innerHTML = iBytesTransfered;
		// if (iPercentComplete == 100) {
		// var oUploadResponse = document.getElementById('upload_response');
		// oUploadResponse.innerHTML = '<h1>Please wait...processing</h1>';
		// oUploadResponse.style.display = 'block';
		// }
		$("#file_share2_progress").attr("value", e.loaded).attr("max", e.total);

	} else {
		// document.getElementById('progress').innerHTML = 'unable to compute';
		console.info("uploadProgress unable to compute");
	}
}

function uploadFinish(e) { // upload successfully finished
// console.info("uploadFinish ...");
// console.info("e.target.responseText:" + e.target.responseText);

	$("#file_share2_progress").fadeOut("slow");

	clearInterval(GtempFileoTimer);

	// send file share event
	var fileShareContent = new EventContent();
	EventContent.prototype.userName;
	EventContent.prototype.fileTag;
	EventContent.prototype.fileName;
	EventContent.prototype.fileSize;
	EventContent.prototype.fileType;

	fileShareContent.userName = GuserName;
	fileShareContent.fileTag = GtempFileTag;//
	fileShareContent.fileName = GtempFileName;
	fileShareContent.fileSize = GtempFileSize;
	fileShareContent.fileType = GtempFileType;

	sendFileInfoBag(fileShareContent);

	$("#upload_share_file2").val("");
}

function uploadError(e) { // upload error
	console.info("uploadError ..." + e.message);
	clearInterval(GtempFileoTimer);
}

function uploadAbort(e) { // upload abort
	console.info("uploadAbort ..." + e.message);
	clearInterval(GtempFileoTimer);
}

function receiveFileShare(enventContent) {
	var fileShareShowMsg = "<p>" + enventContent.userName
			+ " has shared a file : " + "<a href='retriveFile?fileName="
			+ enventContent.fileName + "&amp;fileTag=" + enventContent.fileTag
			+ "' >" + enventContent.fileName + "</a></p>";
	console.info(fileShareShowMsg);
	$(".file_share_output_div").append(fileShareShowMsg);
	var divHeight = $("#file_share_output_div")[0].scrollHeight;
	$(".file_share_output_div").scrollTop(divHeight);

	// http://blog.csdn.net/fwwdn/article/details/8349657/
	// http://www.html5rocks.com/zh/tutorials/websockets/basics/
	// BLOB FileWriter W3C API

	// var reader = new FileReader();
	// reader.onload = function(evt) {
	// if (evt.target.readyState == FileReader.DONE) {
	//			
	//			
	// // $("#file_share_output_div").html("<img src = " + url + " />");
	// }
	// };
	// reader.readAsArrayBuffer(enventContent);
}

function updateClassroomInfo(enventContent) {
	var classroomName = enventContent.classroomName;
	if (!$.trim(classroomName) == "") {
		$("#header_tips").html(classroomName);
	}
}
