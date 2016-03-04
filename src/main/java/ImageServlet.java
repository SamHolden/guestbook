package com.example.guestbook;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Blob;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.*;
import javax.servlet.http.Part;
import javax.servlet.ServletException;
import java.util.logging.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.fileupload.*;
import org.apache.commons.io.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.disk.*;
import java.util.logging.Logger;

import javax.servlet.annotation.MultipartConfig;

import com.googlecode.objectify.ObjectifyService;

/**
 * Form Handling Servlet
 * Most of the action for this sample is in webapp/guestbook.jsp, which displays the
 * {@link Greeting}'s. This servlet has one method
 * {@link #doPost(<#HttpServletRequest req#>, <#HttpServletResponse resp#>)} which takes the form
 * data and saves it.
 */
 @MultipartConfig
public class ImageServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ImageServlet.class.getName());

  // Process the http POST of the form
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	try
	{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();  // Find out who the user is.
		String imageName = req.getParameter("imageName");
		
		log.log(Level.INFO, imageName);
		
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(req);
		FileItemStream imageItem = iter.next();
		InputStream imgStream = imageItem.openStream();

		// construct our entity objects
		Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));

		Image newImage = new Image(imageName, imageBlob);

		// Use Objectify to save the greeting and now() is used to make the call synchronously as we
		// will immediately get a new page using redirect and we want the data to be present.
		ObjectifyService.ofy().save().entity(newImage).now();
	}
	catch(FileUploadException e)
	{
		e.printStackTrace();
	}
	
    resp.sendRedirect("/guestbook.jsp");
  }
}