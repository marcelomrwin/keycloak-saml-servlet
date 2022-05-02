package com.github.marcelomrwin;

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


public class NoAccessServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
       response.getWriter().println("FORBIDDEN");
       response.setStatus(HttpStatus.FORBIDDEN_403);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            doGet(request,response);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}