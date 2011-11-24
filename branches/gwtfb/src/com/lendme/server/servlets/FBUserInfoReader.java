package com.lendme.server.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FBUserInfoReader extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setHeader("lendme_response", request.getContentType());
		response.setContentType("/options/friends");
		
	}
	
}
