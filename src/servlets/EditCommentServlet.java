package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Comment;
import model.Like;
import model.User;
import model.dao.CommentDAO;
import model.dao.LikeDAO;
import model.dao.UserDAO;

@WebServlet("/EditCommentServlet")
public class EditCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if (loggedInUser == null)
			return;
		
		long id = Long.parseLong(request.getParameter("id"));
		String content = request.getParameter("content");
		
		CommentDAO commentDAO = new CommentDAO();
		Comment comment = commentDAO.get(id);
		comment.setContent(content);
		String status = commentDAO.update(comment) == true ? "success" : "failure";
		
		String likedOrDisliked = "liked";
		HashMap<Long, Like<Comment>> userCommentLikes = new LikeDAO().getAllCommentLikes(loggedInUser);
		if(userCommentLikes.get(comment.getId()) != null) {
			likedOrDisliked = userCommentLikes.get(comment.getId()).isLike() == true ? "liked" : "disliked";
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status", status);
		data.put("comment", comment);
		data.put("likedOrDisliked", likedOrDisliked);
		String json = objectMapper.writeValueAsString(data);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
