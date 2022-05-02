package com.github.marcelomrwin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
       response.getWriter().println("GET logout");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            response.getWriter().println("POST logout");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}