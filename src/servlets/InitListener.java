package servlets;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.dao.ConnectionManager;

@WebListener
public class InitListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent e) {
		ConnectionManager.close();
	}

	public void contextInitialized(ServletContextEvent e) {
/*		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "images");
		if (!file.exists())
			file.mkdir();*/

		System.out.println("Initializing servlet context...");

		ConnectionManager.open();

		System.out.println("Done !");

		ServletContext servletContext = e.getServletContext();
		String realPath = servletContext.getRealPath(File.separator);
		

		String imagesPath = realPath + File.separator + "images" + File.separator;

		e.getServletContext().setAttribute("imagesPath", imagesPath);

		System.out.println(imagesPath);
		

	}

}
