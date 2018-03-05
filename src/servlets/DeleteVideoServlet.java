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

import model.Role;
import model.User;
import model.Video;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/DeleteVideoServlet")
public class DeleteVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
		System.out.println(id);
		
		VideoDAO videoDAO = new VideoDAO();
		Video video = videoDAO.get(id);
		boolean adminMode = false;
		if (loggedInUser != null)
			if(loggedInUser.getRole() == Role.ADMIN)
				adminMode = true;
		String status =  videoDAO.delete(video, adminMode) == true ? "success" : "failure";
		
		Map<String, Object> data = new HashMap<>();
		if(loggedInUser.getUsername().equals(video.getOwner().getUsername()))
			data.put("redirectionPage", loggedInUser.getUsername());
		else
			data.put("redirectionPage", video.getOwner().getUsername());
		data.put("status", status);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
	}

}
