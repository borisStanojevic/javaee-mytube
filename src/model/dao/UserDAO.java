package model.dao;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import model.Role;
import model.User;

public class UserDAO {
	
	public boolean update(User user) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "UPDATE user SET thumbnail_url = ?, password = ?, first_name = ?, last_name = ?, channel_description = ?, email = ?, role = ?, blocked = ? WHERE username = ?";
			ps = con.prepareStatement(sql);
			byte index = 1;
			ps.setString(index++, user.getThumbnailUrl());
			ps.setString(index++, user.getPassword());
			ps.setString(index++, user.getFirstName());
			ps.setString(index++, user.getLastName());
			ps.setString(index++, user.getChannelDescription());
			ps.setString(index++, user.getEmail());
			ps.setString(index++, user.getRole().toString());
			ps.setBoolean(index++, user.isBlocked());
			ps.setString(index++, user.getUsername());
			
			return ps.executeUpdate() == 1;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
		}
		
		return false;
	}
	
	//Za proslijedjenog korisnika dobavlja sve korisnike kojima je ON pretplacen
	private HashMap<String, User> getSubscribeds(User user){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, User> subscribeds = new HashMap<>();

		try
		{
			String sql = "SELECT * FROM user WHERE username IN (SELECT subscribed_user_id FROM subscribes WHERE subscriber_user_id = ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				//Dobavi podatke o korisniku
				String thumbnailUrl = rs.getString("thumbnail_url");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String channelDescription = rs.getString("channel_description");
				LocalDate registrationDate = (rs.getDate("registration_date")).toLocalDate();
				Role role = Role.valueOf(rs.getString("role"));
				boolean blocked = rs.getBoolean("blocked");
				boolean deleted = rs.getBoolean("deleted");
				
				User subscribed = new User();
				
				//Postavi vrijednosti polja korisnika
				subscribed.setThumbnailUrl(thumbnailUrl);
				subscribed.setUsername(username);
				subscribed.setPassword(password);
				subscribed.setFirstName(firstName);
				subscribed.setLastName(lastName);
				subscribed.setEmail(email);
				subscribed.setChannelDescription(channelDescription);
				subscribed.setRegistrationDate(registrationDate);
				subscribed.setRole(role);
				subscribed.setBlocked(blocked);
				subscribed.setDeleted(deleted);
				
				//Dodaj korisnika(pretlacenog) u mapu pretplacenih
				subscribeds.put(subscribed.getUsername(), subscribed);
			}
		}
		catch(SQLException e)
		{
			System.out.println("Error in sql query !");
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
			try {rs.close();} catch(SQLException e3) {e3.printStackTrace();}
		}
		
		return subscribeds;
	}
	
	//Za proslijedjenog korisnika dobavlja sve pretplatnike koji su NJEMU pretplaceni
	private HashMap<String, User> getSubscribers(User user){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, User> subscribers = new HashMap<>();
		

		try
		{
			String sql = "SELECT * FROM user WHERE username IN (SELECT subscriber_user_id FROM subscribes WHERE subscribed_user_id = ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				//Dobavi podatke o korisniku	
				String thumbnailUrl = rs.getString("thumbnail_url");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String channelDescription = rs.getString("channel_description");
				LocalDate registrationDate = (rs.getDate("registration_date")).toLocalDate();
				Role role = Role.valueOf(rs.getString("role"));
				boolean blocked = rs.getBoolean("blocked");
				boolean deleted = rs.getBoolean("deleted");
				
				User subscriber = new User();
				
				//Postavi vrijednosti polja korisnika
				subscriber.setThumbnailUrl(thumbnailUrl);
				subscriber.setUsername(username);
				subscriber.setPassword(password);
				subscriber.setFirstName(firstName);
				subscriber.setLastName(lastName);
				subscriber.setEmail(email);
				subscriber.setChannelDescription(channelDescription);
				subscriber.setRegistrationDate(registrationDate);
				subscriber.setRole(role);
				subscriber.setBlocked(blocked);
				subscriber.setDeleted(deleted);
				
				//Dodaj korisnika(pretplatnika) u mapu pretplatnika
				subscribers.put(subscriber.getUsername(), subscriber);
			}
		}
		catch(SQLException e)
		{
			System.out.println("Error in sql query !");
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
			try {rs.close();} catch(SQLException e3) {e3.printStackTrace();}
		}
		
		return subscribers;
	}
	
	public ArrayList<User> getAll(String usernameFilter, String firstNameFilter,String lastNameFilter , String emailFilter, String roleFilter){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<User> users = new ArrayList<>();
		
		try
		{

			String sql = "SELECT * FROM user WHERE username LIKE ? AND first_name LIKE ? AND last_name LIKE ? AND email LIKE ? AND role LIKE ?";
			
			ps = con.prepareStatement(sql);

			byte index = 1;
			ps.setString(index++, "%" + usernameFilter + "%");
			ps.setString(index++, "%" + firstNameFilter + "%");
			ps.setString(index++, "%" + lastNameFilter + "%");
			ps.setString(index++, "%" + emailFilter + "%");
			ps.setString(index++, "%" + roleFilter + "%");
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				String thumbnailUrl = rs.getString("thumbnail_url");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String channelDescription = rs.getString("channel_description");
				LocalDate registrationDate = (rs.getDate("registration_date")).toLocalDate();
				Role role = Role.valueOf(rs.getString("role"));
				boolean blocked = rs.getBoolean("blocked");
				boolean deleted = rs.getBoolean("deleted");
				
				User user = new User();
				
				user.setThumbnailUrl(thumbnailUrl);
				user.setUsername(username);
				user.setPassword(password);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setChannelDescription(channelDescription);
				user.setRegistrationDate(registrationDate);
				user.setRole(role);
				user.setBlocked(blocked);
				user.setDeleted(deleted);
				
				user.setSubscribers(this.getSubscribers(user));
				user.setSubscribeds(this.getSubscribeds(user));
				
				users.add(user);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch (SQLException e) {e.printStackTrace();}
			try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		}
		
		return users;
	}
	
	public User get(String username) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String sql = "SELECT * FROM user WHERE username = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				String thumbnailUrl = rs.getString("thumbnail_url");;
				String password = rs.getString("password");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String channelDescription = rs.getString("channel_description");
				LocalDate registrationDate = (rs.getDate("registration_date")).toLocalDate();
				Role role = Role.valueOf(rs.getString("role"));
				boolean blocked = rs.getBoolean("blocked");
				boolean deleted = rs.getBoolean("deleted");
				
				User user = new User();
				user.setThumbnailUrl(thumbnailUrl);
				user.setUsername(username);
				user.setPassword(password);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(email);
				user.setChannelDescription(channelDescription);
				user.setRegistrationDate(registrationDate);
				user.setRole(role);
				user.setBlocked(blocked);
				user.setDeleted(deleted);
				
				user.setSubscribers(this.getSubscribers(user));
				user.setSubscribeds(this.getSubscribeds(user));
				
				return user;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
			try {rs.close();} catch(SQLException e3) {e3.printStackTrace();}
		}
		
		return null;
	}
	
	public boolean add(User user) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{

				String sql = "INSERT INTO user (thumbnail_url, username, password, first_name, last_name, email, registration_date, role) "
						   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				ps = con.prepareStatement(sql);
				
				byte index = 1;
				ps.setString(index++, user.getThumbnailUrl());
				ps.setString(index++, user.getUsername());
				ps.setString(index++, user.getPassword());
				ps.setString(index++, user.getFirstName());
				ps.setString(index++, user.getLastName());
				ps.setString(index++, user.getEmail());
				ps.setDate(index++, Date.valueOf(user.getRegistrationDate()));
				ps.setString(index++, user.getRole().toString());
				
				System.out.println(ps);
				
				return ps.executeUpdate() == 1;
			
		}
		catch(SQLException e)
		{
			System.out.println("Error in sql query !");
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
		}
		
		return false;
	}
	
	//S obzirom da ce samo admin moci da brise korisnike, brisanje korisnika je fizicko
	public boolean delete(User user) {
		boolean done = false;
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "DELETE FROM user WHERE username = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			
			if (ps.executeUpdate() == 1)
				done = true;
		}
		catch(MySQLIntegrityConstraintViolationException e) {
			done = false;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			done = false;
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
		}
		
		return done;
	}
}
