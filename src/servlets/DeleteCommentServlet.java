package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Comment;
import model.Role;
import model.User;
import model.dao.CommentDAO;
import model.dao.UserDAO;

/**
 * Servlet implementation class DeleteCommentServlet
 */
@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser == null)
			return;
		
		long id = Long.parseLong(request.getParameter("id"));
		boolean adminMode = loggedInUser.getRole() == Role.ADMIN ? true : false;
		
		CommentDAO commentDAO = new CommentDAO();
		Comment comment = commentDAO.get(id);
		
		String status = commentDAO.delete(comment, adminMode) == true ? "success" : "failure";
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
