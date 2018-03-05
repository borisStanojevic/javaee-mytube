package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Subscription;

/*
	Ova klasa je dovoljno da ima metode za dodavanje pretplate i uklanjanje pretplate.
	Kada se korisnik pretplati na neciji kanal kreira se jedna pretplata i smjesta se u tabelu pretplata.
	Kada korisnik otkaze pretplatu na neciji kanal, ta pretplata se uklanja iz tabele pretplata.
*/
public class SubscriptionDAO {
	
	public boolean add(Subscription subscription) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "INSERT INTO subscribes (subscriber_user_id, subscribed_user_id) "
					   + "VALUES (?, ?)";
			ps = con.prepareStatement(sql);
			
			byte index = 1;
			ps.setString(index++, subscription.getSubscriber().getUsername());
			ps.setString(index++, subscription.getSubscribed().getUsername());
			System.out.println(ps);
			
			return ps.executeUpdate() == 1; 	
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch (SQLException e2) {e2.printStackTrace();}
		}
		return false;
	}
	
	public boolean delete(Subscription subscription) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "DELETE FROM subscribes WHERE subscriber_user_id = ? AND subscribed_user_id = ?";
			ps = con.prepareStatement(sql);
			
			ps.setString(1, subscription.getSubscriber().getUsername());
			ps.setString(2, subscription.getSubscribed().getUsername());
			System.out.println(ps);
			
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
}
