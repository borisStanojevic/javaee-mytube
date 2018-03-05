package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import model.Comment;
import model.Like;
import model.User;
import model.Video;

public class LikeDAO {
	
	public boolean updateVideoLike(Like<Video> like) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
				
				String sql = "UPDATE video_like SET is_like = ? WHERE user_id = ? AND video_id = ?";
				ps = con.prepareStatement(sql);
				
				byte index = 1;
				ps.setBoolean(index++, like.isLike());
				ps.setString(index++, like.getUser().getUsername());
				ps.setLong(index++, like.getObject().getId());
				
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
	
	public boolean addVideoLike(Like<Video> like) {

		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
				
				String sql = "INSERT INTO video_like (is_like, date_time_created, user_id, video_id) VALUES (?, ?, ?, ?)";
				ps = con.prepareStatement(sql);
				
				byte index = 1;
				ps.setBoolean(index++, like.isLike());
				ps.setTimestamp(index++, Timestamp.valueOf(like.getDateTimeCreated()));
				ps.setString(index++, like.getUser().getUsername());
				ps.setLong(index++, like.getObject().getId());
				
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
	public boolean updateCommentLike(Like<Comment> like) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
				
				String sql = "UPDATE comment_like SET is_like = ? WHERE user_id = ? AND comment_id = ?";
				ps = con.prepareStatement(sql);
				
				byte index = 1;
				ps.setBoolean(index++, like.isLike());
				ps.setString(index++, like.getUser().getUsername());
				ps.setLong(index++, like.getObject().getId());
				
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
	
	public boolean addCommentLike(Like<Comment> like) {

		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
				
				String sql = "INSERT INTO comment_like (is_like, date_time_created, user_id, comment_id) VALUES (?, ?, ?, ?)";
				ps = con.prepareStatement(sql);
				
				byte index = 1;
				ps.setBoolean(index++, like.isLike());
				ps.setTimestamp(index++, Timestamp.valueOf(like.getDateTimeCreated()));
				ps.setString(index++, like.getUser().getUsername());
				ps.setLong(index++, like.getObject().getId());
				
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
	
	//Dobavi broj lajkova za video
	public long getLikes(Video video) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long likes = 0;
		
		try
		{
			String sql = "SELECT COUNT(*) AS likes FROM video_like WHERE video_id = ? AND is_like = 1";
			
			ps = con.prepareStatement(sql);
			ps.setLong(1, video.getId());
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				likes = rs.getLong(1);
			}
			
			return likes;
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
		
		return likes;
	}
	
	//Dobavi broj dislajkova za video
	public long getDisLikes(Video video) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long dislikes = 0;
		
		try
		{
			String sql = "SELECT COUNT(*) AS dislikes FROM video_like WHERE video_id = ? AND is_like = 0";
			
			ps = con.prepareStatement(sql);
			ps.setLong(1, video.getId());
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				dislikes = rs.getLong(1);
			}
			
			return dislikes;
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
		
		return dislikes;
	}
	
	//Dobavi sve lajkove(dislajkove) videa za korisnika
	public HashMap<Long, Like<Video>> getAllVideoLikes(User user){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Long, Like<Video>> videoLikes = new HashMap<>();

		try
		{
			String sql = "SELECT * FROM video_like WHERE user_id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			
			VideoDAO videoDAO = new VideoDAO();
			
			while(rs.next())
			{
				boolean isLike = rs.getBoolean("is_like");
				LocalDateTime dateTimeCreated = (rs.getTimestamp("date_time_created")).toLocalDateTime();
				//Korisnika imam kao parametar
				Video video = videoDAO.get(rs.getLong("video_id"));
				
				Like<Video> videoLike = new Like<>();;
				videoLike.setLike(isLike);
				videoLike.setDateTimeCreated(dateTimeCreated);
				videoLike.setUser(user);
				videoLike.setObject(video);
				
				videoLikes.put(videoLike.getObject().getId(), videoLike);
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
		
		return videoLikes;
	}
	
	//Dobavi sve lajkove(dislajkove) komentara za korisnika
	public HashMap<Long, Like<Comment>> getAllCommentLikes(User user){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Long, Like<Comment>> commentLikes = new HashMap<>();

		try
		{
			String sql = "SELECT * FROM comment_like WHERE user_id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			
			CommentDAO commentDAO = new CommentDAO();
			
			while(rs.next())
			{
				boolean isLike = rs.getBoolean("is_like");
				LocalDateTime dateTimeCreated = (rs.getTimestamp("date_time_created")).toLocalDateTime();
				//Korisnika imam kao parametar
				Comment comment = commentDAO.get(rs.getLong("comment_id"));
				
				Like<Comment> commentLike = new Like<>();;
				commentLike.setLike(isLike);
				commentLike.setDateTimeCreated(dateTimeCreated);
				commentLike.setUser(user);
				commentLike.setObject(comment);
				
				commentLikes.put(commentLike.getObject().getId(), commentLike);
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
		
		return commentLikes;
	}
	
	//Dobavi broj lajkova za komentar
	public long getLikes(Comment comment) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long likes = 0;
		
		try
		{
			String sql = "SELECT COUNT(*) AS likes FROM comment_like WHERE comment_id = ? AND is_like = 1";
			
			ps = con.prepareStatement(sql);
			ps.setLong(1, comment.getId());
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				likes = rs.getLong(1);
			}
			
			return likes;
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
		
		return likes;
	}
	
	public long getDislikes(Comment comment) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long dislikes = 0;
		
		try
		{
			String sql = "SELECT COUNT(*) AS likes FROM comment_like WHERE comment_id = ? AND is_like = 0";
			
			ps = con.prepareStatement(sql);
			ps.setLong(1, comment.getId());
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				dislikes = rs.getLong(1);
			}
			
			return dislikes;
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
		
		return dislikes;
	}
}
