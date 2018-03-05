package servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Role;
import model.User;
import model.dao.UserDAO;

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());

		String username = request.getParameter("usernameInput");
		String password = request.getParameter("passwordInput");
		String firstName = request.getParameter("firstNameInput");
		String lastName = request.getParameter("lastNameInput");
		String channelDescription = request.getParameter("channelDescriptionInput");
		String email = request.getParameter("emailInput");
		Role role = Role.valueOf(request.getParameter("roleSelect"));
		
		User user = new UserDAO().get(username);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		if(channelDescription != null)
			user.setChannelDescription(channelDescription);
		user.setEmail(email);
		user.setRole(role);
		
		boolean success = new UserDAO().update(user);
		String status = success == true ? "success" : "failure";
		
		HashMap<String, Object> data = new HashMap<>();
		data.put("status", status);
		data.put("loggedInUser", loggedInUser);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);

	}

}
