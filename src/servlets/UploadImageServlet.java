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

@WebServlet("/UploadImageServlet")
public class UploadImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String imagesPath = (String) getServletContext().getAttribute("imagesPath");
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		
		String username = "";
		String thumbnailUrl = "";
		
		try
		{
			List<FileItem> multiFiles = servletFileUpload.parseRequest(request);
			for (FileItem item : multiFiles)
			{
				if (item.isFormField())
					username = item.getString();
				
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
		
		UserDAO userDAO = new UserDAO();
		Map<String, Object> data = new HashMap<>();

		User user = userDAO.get(username);
		user.setThumbnailUrl(thumbnailUrl);
		
		boolean uploadSuccessful = userDAO.update(user);
		
		if (uploadSuccessful)
		{
			data.put("status", "success");
		}
		else
		{
			data.put("status", "failure");
			data.put("message", "Failed to upload image. Try again");
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(data);

		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
