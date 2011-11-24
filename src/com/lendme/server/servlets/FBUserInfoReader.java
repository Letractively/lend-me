package com.lendme.server.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FBUserInfoReader extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setHeader("response", "value");
		response.setContentType("/options/friends");
		
		InputStream contentStream = request.getInputStream();
		PrintWriter logger = new PrintWriter(new File("processed.txt"));
		int buffer = -1;
		while ( (buffer = contentStream.read()) != -1 ){
			logger.write(buffer);
		}
		logger.close();
		response.setHeader("lendme_response", "server_processed_request");
		response.setContentType("/options/friends");
		
	}
	
}
