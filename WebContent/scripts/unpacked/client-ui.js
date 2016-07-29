/*
 * 
 */
function pageRedirect(srcDivId, destDivId, deleteSrcDiv) {
	if (deleteSrcDiv) {
		$("#" + srcDivId).remove();
	} else {
		// $("#" + divId).hide(500);
		$("#" + srcDivId).fadeOut(500, function(){
			$("#" + destDivId).fadeIn(500);
		});

		// $("#" + divId).animate({
		// left:'512px',
		// opacity:'0',
		// height:'0px',
		// width:'0px'
		// },2000);
	}
}

function panelRedirect(destDivId) {
	$("#" + destDivId).siblings(".base-page-right").hide();
	openDiv(destDivId);
}

function openDiv(divId, speed) {
	$("#" + divId).show(speed);
}

function toggleDiv(divId, speed) {
	$("#" + divId).toggle(speed);
}

function checkHtml5Support() {
	if (typeof (Worker) == "undefined") {
		var globalErrorMsg = "你的浏览器不支持HTML5,请升级或者更换浏览器";
		alert(globalErrorMsg);
		location.href = "errorpage.xhtml";
	}
}

function guids4() {
	return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
}

function newGuid() {
	return guids4() + guids4() + '-' + guids4() + '-' + guids4() + '-'
			+ guids4() + '-' + guids4() + guids4() + guids4();
}

function bytesToSize(bytes) {
	var sizes = [ 'Bytes', 'KB', 'MB' ];
	if (bytes == 0)
		return 'n/a';
	var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
	return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
}

function getURLParameter(sParam) {
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) {
			return sParameterName[1];
		}
	}
}

function hightLightButton(destDivId) {
	$("#" + destDivId).siblings(".right-menu").css("background-color",
			"lightgrey").css("color", "darkcyan");

	$("#" + destDivId).css("background-color", "cadetblue").css("color",
			"ivory");
}

function twinkleUserIcon(destDivId) {
	$("#" + destDivId).css("background-color", "crimson");
}

function cancelTwinkleUserIcon(destDivId) {
	$("#" + destDivId).css("background-color", "limegreen");
}

function twinkleHiddenTipsDiv(destDivId, twinkleSpeed) {
	$("#" + destDivId).animate({
		opacity : '0'
	}).animate({
		opacity : '1'
	}, twinkleSpeed).animate({
		opacity : '0'
	}, twinkleSpeed).animate({
		opacity : '1'
	}, twinkleSpeed);
}

function printObjectAllProperties(targetObj) {
	jQuery.each(targetObj, function(i, val) {
		console.log(i + " : " + val);
	});
}

function deskTopNotifications() {
	if (window.webkitNotifications) {
		if (window.webkitNotifications.checkPermission() == 0) {
			window.webkitNotifications.createNotification("xx.png", "Title",
					"Body").show();
		} else {
			console.info("request Pemission");
			window.webkitNotifications.requestPermission();
			return;
		}
	}
}

function stringToBytes(str) {
	var ch, st, re = [];
	for (var i = 0; i < str.length; i++) {
		ch = str.charCodeAt(i); // get char
		st = []; // set up "stack"
		do {
			st.push(ch & 0xFF); // push byte to stack
			ch = ch >> 8; // shift value down by 1 byte
		} while (ch);
		// add stack contents to result
		// done because chars have "wrong" endianness
		re = re.concat(st.reverse());
	}
	// return an array of bytes
	return re;
}
