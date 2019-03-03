package by.chmut.xml.controller;

import by.chmut.xml.command.CommandType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/frontController")
public class Controller extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandType commandType = RequestHandler.get(request);
        String page = commandType.getCommand().execute(request);
        request.getRequestDispatcher(page).forward(request,response);
    }
}
