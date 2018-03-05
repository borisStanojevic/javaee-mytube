package servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.User;
import model.Video;
import model.dao.UserDAO;
import model.dao.VideoDAO;
import model.util.DateTimeUtil;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		
		String nameFilter = request.getParameter("videoNameInput");
		String ownerUsernameFilter = request.getParameter("videoOwnerInput");
		String minViewsInput = request.getParameter("minViewsInput");
		long minViewsFilter = "".equals(minViewsInput) ? 0 : Long.valueOf(minViewsInput);
		String maxViewsInput = request.getParameter("maxViewsInput");
		long maxViewsFilter = "".equals(maxViewsInput) ? Long.MAX_VALUE : Long.valueOf(maxViewsInput);
		String minDateTimeInput = request.getParameter("minDateTimeInput");
		LocalDateTime minDateTimeFilter = "".equals(minDateTimeInput) ? DateTimeUtil.getDateTimeMin(): LocalDateTime.parse(minDateTimeInput);
		String maxDateTimeInput = request.getParameter("maxDateTimeInput");
		LocalDateTime maxDateTimeFilter = "".equals(maxDateTimeInput) ? DateTimeUtil.getDateTimeMax() : LocalDateTime.parse(maxDateTimeInput);
		String sortSelect = request.getParameter("sortSelect");
		
		System.out.println(nameFilter + '\n' + ownerUsernameFilter + '\n' + minDateTimeFilter + '\n' + maxDateTimeFilter);
		
		ArrayList<Video> top5 = new VideoDAO().getTopFive();
		 
		LinkedList<Video> videos = new VideoDAO().getAll(nameFilter, ownerUsernameFilter, minViewsFilter, maxViewsFilter, minDateTimeFilter, maxDateTimeFilter);
		switch (sortSelect) {
		case "videoNameAsc":
			videos.sort(Video.NAME_COMPARATOR_ASC);
			break;
		case "videoNameDesc":
			videos.sort(Video.NAME_COMPARATOR_DESC);
			break;
		case "videoOwnerAsc":
			videos.sort(Video.OWNER_COMPARATOR_ASC);
			break;
		case "videoOwnerDesc":
			videos.sort(Video.OWNER_COMPARATOR_DESC);
			break;
		case "viewsAsc":
			videos.sort(Video.VIEWS_COMPARATOR_ASC);
			break;
		case "viewsDesc":
			videos.sort(Video.VIEWS_COMPARATOR_DESC);
			break;
		case "dateTimeAsc":
			videos.sort(Video.DTU_COMPARATOR_ASC);
			break;
		case "dateTimeDesc":
			videos.sort(Video.DTU_COMPARATOR_DESC);
			break;
		default:
			break;
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("top5", top5);
		data.put("videos", videos);
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
		
		String mainSearchInput = request.getParameter("mainSearchInput") == null ? "" : request.getParameter("mainSearchInput");
		boolean nameCb = Boolean.parseBoolean(request.getParameter("nameCb"));
		LinkedList<Video> l1 = nameCb == false ? new LinkedList<Video>() : new VideoDAO().getAllByName(mainSearchInput);
		boolean ownerCb = Boolean.parseBoolean(request.getParameter("ownerCb"));
		LinkedList<Video> l2 = ownerCb == false ? new LinkedList<Video>() : new VideoDAO().getAllByUsername(mainSearchInput);
		boolean commentContentCb = Boolean.parseBoolean(request.getParameter("commentContentCb"));
		LinkedList<Video> l3 = commentContentCb == false ? new LinkedList<Video>() : new VideoDAO().getAllByComment(mainSearchInput);
		
		HashMap<Long,Video> videos = new HashMap<>();
		for (Video video : l1) {
			if(videos.containsKey(video.getId()))
				continue;
			videos.put(video.getId(), video);
		}
		for (Video video : l2) {
			if(videos.containsKey(video.getId()))
				continue;
			videos.put(video.getId(), video);
		}
		for (Video video : l3) {
			if(videos.containsKey(video.getId()))
				continue;
			videos.put(video.getId(), video);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> data = new HashMap<>();
		data.put("videos", videos.values());
		data.put("loggedInUser", loggedInUser);
		
		String json = objectMapper.writeValueAsString(data);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
