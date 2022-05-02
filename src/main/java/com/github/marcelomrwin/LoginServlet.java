package com.github.marcelomrwin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("DO GET");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        System.out.println("DO POST");
    }
}