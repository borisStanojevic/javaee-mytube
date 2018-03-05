package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import model.dao.UserDAO;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		
		String username = request.getParameter("username");
		System.out.println("Parametar stigao :" + username);
		User user = new UserDAO().get(username);
		HashMap<String, Object> data = new HashMap<>();
		data.put("user", user);
		data.put("loggedInUser", loggedInUser);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		//Preuzimam parametre search forme
		String usernameFilter = request.getParameter("usernameInput") == null ? "" : request.getParameter("usernameInput");
		String firstNameFilter = request.getParameter("firstNameInput") == null ? "" : request.getParameter("firstNameInput");
		String lastNameFilter = request.getParameter("lastNameInput") == null ? "" : request.getParameter("lastNameInput");
		String emailFilter = request.getParameter("emailInput") == null ? "" : request.getParameter("emailInput");
		String roleFilter = request.getParameter("roleSelect");
		
		String sortFilter = request.getParameter("sortSelect");
		
		//Na osnovu parametara dobavljam sve korisnike iz baze
		List<User> users = new UserDAO().getAll(usernameFilter, firstNameFilter, lastNameFilter, emailFilter, roleFilter);
		switch (sortFilter) {
		case "usernameAsc":
			users.sort(User.USERNAME_COMPARATOR_ASC);
			break;
		case "usernameDesc":
			users.sort(User.USERNAME_COMPARATOR_DESC);
			break;
		case "firstNameAsc":
			users.sort(User.FIRST_NAME_COMPARATOR_ASC);
			break;
		case "firstNameDesc":
			users.sort(User.FIRST_NAME_COMPARATOR_DESC);
			break;
		case "lastNameAsc":
			users.sort(User.LAST_NAME_COMPARATOR_ASC);
			break;
		case "lastNameDesc":
			users.sort(User.LAST_NAME_COMPARATOR_DESC);
			break;
		case "emailAsc":
			users.sort(User.EMAIL_COMPARATOR_ASC);
			break;
		case "emailDesc":
			users.sort(User.EMAIL_COMPARATOR_DESC);
			break;
		case "roleAsc":
			users.sort(User.ROLE_COMPARATOR_ASC);
			break;
		case "roleDesc":
			users.sort(User.ROLE_COMPARATOR_DESC);
			break;
		default:
			break;
		}
		HashMap<String, Object> data = new HashMap<>();
		data.put("users", users);
		data.put("loggedInUser", loggedInUser);
		
		//Konvertujem listu korisnika u JSON format
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		//Saljem JSON klijentu 
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
