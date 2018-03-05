package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Subscription;
import model.User;
import model.dao.SubscriptionDAO;
import model.dao.UserDAO;

@WebServlet("/SubscriptionServlet")
public class SubscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User subscriber = (User)request.getSession().getAttribute("loggedInUser") == null ? 
				null : new UserDAO().get(((User)request.getSession().getAttribute("loggedInUser")).getUsername());
		if(subscriber == null)
			return;
		
		String subscribedUsername = request.getParameter("subscribedUsername");
		User subscribed = new UserDAO().get(subscribedUsername);
		
		Subscription subscription = new Subscription();
		subscription.setSubscriber(subscriber);
		subscription.setSubscribed(subscribed);
		
		System.out.println("Preplacuje se " + subscriber.getUsername() + " korisniku " + subscribed.getUsername());
		String status = "failure"; 
		
		//Ako onaj ko se pretplacuje u svojoj mapi pretplacenih nema korisnika kome se pretlacuje, dodaj pretplatu, u suprotnom ukloni
		if(!subscriber.getSubscribeds().containsKey(subscribed.getUsername()))
		{	
			status = new SubscriptionDAO().add(subscription) == true ? "success" : "failure";
			subscriber.getSubscribeds().put(subscribed.getUsername(), subscribed);
		}
		else
		{
			status = new SubscriptionDAO().delete(subscription) == true ? "success" : "failure";
			subscriber.getSubscribeds().remove(subscribed.getUsername());
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(status);
		
		response.setContentType("application/json");
		response.getWriter().write(json);
		
	}

}
