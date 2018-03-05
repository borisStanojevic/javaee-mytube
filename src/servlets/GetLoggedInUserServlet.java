package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import model.dao.LikeDAO;
import model.dao.SubscriptionDAO;
import model.dao.UserDAO;

@WebServlet("/GetLoggedInUserServlet")
public class GetLoggedInUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser == null)
			return;
		
		LikeDAO likeDAO = new LikeDAO();
		loggedInUser.setVideoLikes(likeDAO.getAllVideoLikes(loggedInUser));
		loggedInUser.setCommentLikes(likeDAO.getAllCommentLikes(loggedInUser));
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(loggedInUser);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
