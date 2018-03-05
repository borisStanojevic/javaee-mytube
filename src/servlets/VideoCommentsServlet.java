package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import model.dao.LikeDAO;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/VideoCommentsServlet")
public class VideoCommentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser != null)
			loggedInUser.setCommentLikes(new LikeDAO().getAllCommentLikes(loggedInUser));
	
		Long id = Long.valueOf(request.getParameter("id"));
		System.out.println("STA SE DESAVA : " + id);
		String sortSelect = request.getParameter("sortSelect");
		int limit = Integer.MAX_VALUE;
		try
		{
			if ("all".equals(request.getParameter("limitSelect").trim()))
				throw new Exception();
			limit = Integer.parseInt(request.getParameter("limitSelect"));

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		Video video = new VideoDAO().get(id);
		List<Comment> videoComments = new CommentDAO().getAll(video, limit);
		
		switch (sortSelect) {
		case "ratingAsc":
			videoComments.sort(Comment.RATING_COMPARATOR_ASC);
			break;
		case "dtPostedAsc":
			videoComments.sort(Comment.DTP_COMPARATOR_ASC);
			break;
		case "dtPostedDesc":
			videoComments.sort(Comment.DTP_COMPARATOR_DESC);
			break;
		default:
			videoComments.sort(Comment.RATING_COMPARATOR_DESC);
			break;
		}
		
		Map<String, Object> data = new  HashMap<>();
		data.put("loggedInUser", loggedInUser);
		data.put("video", video);
		data.put("videoComments", videoComments);
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
