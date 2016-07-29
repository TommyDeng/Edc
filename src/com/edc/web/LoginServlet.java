package com.edc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.edc.service.EdcBaseService;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EdcBaseService edcBaseService;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
		System.out.println("Established....");
		request.getRequestDispatcher("/index.xhtml").forward(request, response);
	}

}
