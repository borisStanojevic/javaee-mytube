package servlets;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Like;
import model.User;
import model.Video;
import model.dao.LikeDAO;
import model.dao.UserDAO;
import model.dao.VideoDAO;

@WebServlet("/LikeVideoServlet")
public class LikeVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User loggedInUser = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(loggedInUser == null)
			return;

		long id = Long.parseLong(request.getParameter("id"));
		boolean isLike = "like".equals(request.getParameter("likeOrDislike")) ? true : false;
		
		System.out.println(id + "\t" + isLike);
		
		loggedInUser.setVideoLikes(new LikeDAO().getAllVideoLikes(loggedInUser));
		Video video = new VideoDAO().get(id);
		Like<Video> like = new Like<>();
		like.setLike(isLike);
		like.setDateTimeCreated(LocalDateTime.now());
		like.setUser(loggedInUser);
		like.setObject(video);
		
		//Ako korisnik u svojoj mapi lajkovanih/dislajkovanih videa nema video sa id-jem koji je proslijedjen, to znaci da prvi put lajkuje
		//ili dislajkuje video i tad pozivam metodu da taj lajk/dislajk DODAM u bazu, u suprotnom samo se mijenja vrijednost da li je lajk ili dislajk
		String status = "failure";
		if((loggedInUser.getVideoLikes().get(video.getId())) == null)
		{
			status = new LikeDAO().addVideoLike(like) == true ? "success" : "failure";
			loggedInUser.getVideoLikes().put(video.getId(), like);
		}
		else
		{			
			status = new LikeDAO().updateVideoLike(like) == true ? "success" : "failure";
			loggedInUser.getVideoLikes().put(video.getId(), like);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
