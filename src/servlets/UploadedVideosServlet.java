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
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;

import model.User;
import model.Video;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/UploadedVideosServlet")
public class UploadedVideosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		String username = request.getParameter("username");
		String sortUploadedSelect = request.getParameter("sortUploadedSelect");
		User user = new UserDAO().get(username);
		List<Video> uploadedVideos = new VideoDAO().getAll(user);
		switch (sortUploadedSelect) {
		case "dtUploadedAsc":
			uploadedVideos.sort(Video.DTU_COMPARATOR_ASC);
			break;
		case "viewsAsc":
			uploadedVideos.sort(Video.VIEWS_COMPARATOR_ASC);
			break;
		case "viewsDesc":
			uploadedVideos.sort(Video.VIEWS_COMPARATOR_DESC);
			break;
		default:
			uploadedVideos.sort(Video.DTU_COMPARATOR_DESC);
			break;
		}
		Map<String, Object> data = new HashMap<>();
		data.put("uploadedVideos", uploadedVideos);
		data.put("loggedInUser", loggedInUser);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
