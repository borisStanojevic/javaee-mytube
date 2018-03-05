package servlets;

import java.io.IOException;
import java.time.LocalDateTime;

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

@WebServlet("/LikeCommentServlet")
public class LikeCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		System.out.println("ulogovan je : " + loggedInUser);
		if(loggedInUser == null)
			return;
		
		long id = Long.parseLong(request.getParameter("id"));
		boolean isLike = "like".equals(request.getParameter("likeOrDislike")) ? true : false;
		
		System.out.println(id + "\t" + isLike);
		
		loggedInUser.setCommentLikes(new LikeDAO().getAllCommentLikes(loggedInUser));
		Comment comment = new CommentDAO().get(id);
		Like<Comment> like = new Like<>();
		like.setLike(isLike);
		like.setDateTimeCreated(LocalDateTime.now());
		like.setUser(loggedInUser);
		like.setObject(comment);
		
		String status = "failure";
		
		if(!loggedInUser.getCommentLikes().containsKey(comment.getId()))
		{
			status = new LikeDAO().addCommentLike(like) == true ? "success" : "failure";
		}
		else
		{			
			status = new LikeDAO().updateCommentLike(like) == true ? "success" : "failure";
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
