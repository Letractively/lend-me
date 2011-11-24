package com.lendme.server.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FBUserInfoReader extends HttpServlet{

	private static final long serialVersionUID = 1L;

	private void receiveRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setHeader("lendme_response", request.getContentType());
		response.setContentType("/options/friends");
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		receiveRequest(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		receiveRequest(request, response);
	}
	
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException{
		receiveRequest(request, response);
	}
	
	@Override
	public void doHead(HttpServletRequest request, HttpServletResponse response) throws IOException{
		receiveRequest(request, response);
	}

}
