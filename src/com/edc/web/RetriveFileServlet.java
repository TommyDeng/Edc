package com.edc.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.edc.service.EdcBaseService;
import com.edc.utils.ProjectConfigUtils;

public class RetriveFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EdcBaseService edcBaseService;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// super.service(request, response);
		
		request.getAttribute("fileName");
		request.getAttribute("upload_file");
		
		
		int BUFSIZE = 512;
		String fileTag = request.getParameter("fileTag");
		String fileName = request.getParameter("fileName");

		// fileTag = "ad783f0a-ae04-4a5c-a7ac-0bc1118393a2";
		// fileName = "a2.txt";

		String filePath = retriveStoredFilePath(fileTag, fileName);

		File file = new File(filePath);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(filePath);

		// sets response content type
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		response.setContentType(mimetype);
		response.setContentLength((int) file.length());

		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}
		in.close();
		outStream.close();
		// response.flushBuffer();
	}

	private static String retriveStoredFilePath(String fileTag, String fileName) {
		return ProjectConfigUtils.getDefaultFileShareRestorePath() + fileTag
				+ FilenameUtils.EXTENSION_SEPARATOR_STR
				+ FilenameUtils.getExtension(fileName);
	}

}
