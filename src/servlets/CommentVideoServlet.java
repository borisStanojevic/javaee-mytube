package servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Comment;
import model.User;
import model.Video;
import model.dao.CommentDAO;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/CommentVideoServlet")
public class CommentVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Ko komentarise, sta komentarise
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser == null)
			return;
		
		String content = request.getParameter("content");
		long videoId = Long.parseLong(request.getParameter("videoId"));
		
		Video video = new VideoDAO().get(videoId);
		Comment comment =  new Comment();
		comment.setContent(content);
		comment.setUser(loggedInUser);
		comment.setVideo(video);
		comment.setDateTimePosted(LocalDateTime.now());
		
		String status = "failure";
		Map<String, Object> data = new HashMap<>();
		
		if(new CommentDAO().add(comment) == true)
		{
			status = "success";
			data.put("status", status);
			data.put("comment", comment);
		}
		else
		{
			data.put("status", status);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
