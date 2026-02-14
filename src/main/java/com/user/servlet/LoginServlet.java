package com.user.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.DAO.UserDAO;
import com.entity.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String username = req.getParameter("empn");
            String password = req.getParameter("password");

            UserDAO dao = new UserDAO();
            User user = dao.login(username, password);

            HttpSession session = req.getSession();

            if (user != null) {
                session.setAttribute("userObj", user);
                resp.sendRedirect("home.jsp");
            } else {
                session.setAttribute("errorMsg", "Invalid Credentials");
                resp.sendRedirect("index.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
