package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Video;
import model.dao.VideoDAO;

@WebServlet("/BlockVideoServlet")
public class BlockVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		long id = Long.parseLong(request.getParameter("id"));
		boolean blocked = "block".equals(request.getParameter("blockOrUnblock")) ? true : false;
		
		VideoDAO videoDAO = new VideoDAO();
		Video video = videoDAO.get(id);
		video.setBlocked(blocked);
		
		String status = videoDAO.update(video) == true ? "success"  : "failure";
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		System.out.println(json);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
