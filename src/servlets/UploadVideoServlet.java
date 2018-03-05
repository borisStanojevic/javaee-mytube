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

import model.User;
import model.Video;
import model.Visibility;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/UploadVideoServlet")
public class UploadVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

		//Korisnik koji uploaduje video
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if (loggedInUser == null)
			return;
		
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String description = request.getParameter("description") == null ? "" : request.getParameter("description");
		String visiblityParam = request.getParameter("visibility");
		Visibility visibility;
		switch (visiblityParam)
		{
		case "PRIVATE":
			visibility = Visibility.PRIVATE;
			break;
		case "UNLISTED":
			visibility = Visibility.UNLISTED;
			break;
		default:
			visibility = Visibility.PUBLIC;
			break;
		}
		boolean commentsAllowed = Boolean.parseBoolean(request.getParameter("commentsAllowed"));
		boolean ratingVisible = Boolean.parseBoolean(request.getParameter("ratingVisible"));
		
		System.out.println(name + ' ' + url + ' ' + visibility + ' ' + commentsAllowed + ' ' + ratingVisible);
		
		Video video = new Video();
		video.setName(name);
		video.setVideoUrl(url);
		video.setDescription(description);
		video.setVisibility(visibility);
		video.setCommentsAllowed(commentsAllowed);
		video.setRatingVisible(ratingVisible);
		video.setOwner(loggedInUser);
		video.setDateTimeUploaded(LocalDateTime.now());
		video.setViews(0);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> data = new HashMap<>();

		
		String status = new VideoDAO().add(video) == true ? "success" : "failure";
		data.put("status", status);
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
