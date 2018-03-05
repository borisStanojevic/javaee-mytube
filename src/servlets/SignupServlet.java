package servlets;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Role;
import model.User;
import model.dao.UserDAO;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String imagesPath = (String) getServletContext().getAttribute("imagesPath");
		String username = "";
		String password = "";
		String email = "";
		String thumbnailUrl = "default.png";
		
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		
		try
		{
			List<FileItem> multiFiles = servletFileUpload.parseRequest(request);
			for (FileItem item : multiFiles)
			{
				if (item.isFormField())
				{
					switch (item.getFieldName())
					{
					case "username":
						username = item.getString();
						break;
					case "password":
						password = item.getString();
						break;
					case "email":
						email = item.getString();
						break;
					default:
						break;
					}
				}
				if (!item.isFormField())
				{
					if(item.getSize() > 0)
					{
						String imageExtension = "." + item.getContentType().split("/")[1];
						File saveLocation = new File(imagesPath + username + imageExtension);
						item.write(saveLocation);
						thumbnailUrl = username + imageExtension;
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		Map<String, Object> data = new HashMap<>();
		UserDAO userDAO = new UserDAO();

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setRegistrationDate(LocalDate.now());
		user.setRole(Role.USER);
		user.setThumbnailUrl(thumbnailUrl);
		
		boolean signupSuccessful = userDAO.add(user);
		
		if (signupSuccessful)
		{
			data.put("status", "success");
			data.put("user", user);
			request.getSession().setAttribute("loggedInUser", user);
		}
		else
		{
			data.put("status", "failure");
			data.put("message", "Username already exists");
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);

		response.setContentType("application/json");
		response.getWriter().write(json);

	}

}
