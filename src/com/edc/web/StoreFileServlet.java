package com.edc.web;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.edc.service.EdcBaseService;
import com.edc.utils.ProjectConfigUtils;

@MultipartConfig
public class StoreFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EdcBaseService edcBaseService;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// super.service(request, response);

		String fileTag = getValue(request.getPart("fileTag"));
		InputStream fileBlob = request.getPart("fileBlob").getInputStream();

		OutputStream outFileOutputStream = new FileOutputStream(
				ProjectConfigUtils.getDefaultFileShareRestorePath() + fileTag);

		IOUtils.copy(fileBlob, outFileOutputStream);
		fileBlob.close();
		outFileOutputStream.close();
	}

	private static String getValue(Part part) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				part.getInputStream(), "UTF-8"));
		StringBuilder value = new StringBuilder();
		char[] buffer = new char[1024];
		for (int length = 0; (length = reader.read(buffer)) > 0;) {
			value.append(buffer, 0, length);
		}
		return value.toString();
	}

}
