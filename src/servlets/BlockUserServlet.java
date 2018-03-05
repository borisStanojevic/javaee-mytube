package servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import model.dao.UserDAO;

@WebServlet("/BlockUserServlet")
public class BlockUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("username");
		String blockOrUnblock = request.getParameter("blockOrUnblock");
		boolean blocked = "block".equalsIgnoreCase(blockOrUnblock) ? true : false;
		
		UserDAO userDAO = new UserDAO();
		User user = userDAO.get(username);
		user.setBlocked(blocked);
		
		boolean success = new UserDAO().update(user);
		String status = success == true ? "success" : "failure";
		
		HashMap<String, String> data = new HashMap<>();
		data.put("status", status);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
