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

import model.User;
import model.Video;
import model.Visibility;
import model.dao.LikeDAO;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/VideoServlet")
public class VideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
							null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser != null)
			loggedInUser.setVideoLikes(new LikeDAO().getAllVideoLikes(loggedInUser));
		
		long id = Long.parseLong(request.getParameter("id"));
		String load = request.getParameter("load");
		
		VideoDAO videoDAO = new VideoDAO();
		Video video = videoDAO.get(id);
		if(load != null)
		{
			long currentViews = video.getViews();
			video.setViews(currentViews + 1); 
			videoDAO.update(video);
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("video", video);
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
		if(loggedInUser == null)
			return;
		
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Visibility visibility = Visibility.valueOf(request.getParameter("visibility"));
		boolean commentsAllowed = Boolean.parseBoolean(request.getParameter("commentsAllowed"));
		boolean ratingVisible = Boolean.parseBoolean(request.getParameter("ratingVisible"));
		
		Video video = new VideoDAO().get(id);
		video.setName(name);
		video.setDescription(description);
		video.setVisibility(visibility);
		video.setCommentsAllowed(commentsAllowed);
		video.setRatingVisible(ratingVisible);
		
		String status = new VideoDAO().update(video) == true ? "success" : "failure";
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
