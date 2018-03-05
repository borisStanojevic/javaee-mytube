package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

import model.Comment;
import model.User;
import model.Video;

public class CommentDAO {
	
	//Daj mi sve komentare na proslijedjeni video
	public LinkedList<Comment> getAll(Video video, int limit){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Comment> comments = new LinkedList<>();
		
		try
		{
			String sql = "SELECT * FROM mytube.comment WHERE video_id = ? LIMIT ?";
			ps = con.prepareStatement(sql);
			ps.setLong(1, video.getId());
			ps.setInt(2, limit);
			rs = ps.executeQuery();
			
			UserDAO userDAO = new UserDAO();
			LikeDAO likeDAO = new LikeDAO();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String content = rs.getString("content");
				LocalDateTime dateTimePosted = (rs.getTimestamp("date_time_posted")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				User user = userDAO.get(rs.getString("user_id"));
				//Video imam kao parametar
				
				Comment comment = new Comment();
				comment.setId(id);
				comment.setContent(content);
				comment.setDateTimePosted(dateTimePosted);
				comment.setUser(user);
				comment.setVideo(video);
				comment.setDeleted(deleted);
				comment.setLikes(likeDAO.getLikes(comment));
				comment.setDislikes(likeDAO.getDislikes(comment));
				
				comments.add(comment);
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
		
		return comments;
	}
	
	public Comment get(long id) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String sql = "SELECT * FROM mytube.comment WHERE id = ?";
			ps = con.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				String content = rs.getString("content");
				LocalDateTime dateTimePosted = (rs.getTimestamp("date_time_posted")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				User user = new UserDAO().get(rs.getString("user_id"));
				Video video = new VideoDAO().get(rs.getLong("video_id"));
				
				Comment comment = new Comment();
				comment.setId(id);
				comment.setContent(content);
				comment.setDateTimePosted(dateTimePosted);
				comment.setDeleted(deleted);
				comment.setUser(user);
				comment.setVideo(video);
				comment.setLikes(new LikeDAO().getLikes(comment));
				comment.setDislikes(new LikeDAO().getDislikes(comment));
				
				return comment;
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
	
	public boolean add(Comment comment) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "INSERT INTO mytube.comment (content, date_time_posted, user_id, video_id) "
					   + "VALUES (?, ?, ?, ?)";
			ps = con.prepareStatement(sql);
			
			byte index = 1;
			ps.setString(index++, comment.getContent());
			ps.setTimestamp(index++, Timestamp.valueOf(comment.getDateTimePosted()));
			ps.setString(index++, comment.getUser().getUsername());
			ps.setLong(index++, comment.getVideo().getId());
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
	
	public boolean update(Comment comment) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "UPDATE mytube.comment SET content = ? WHERE id = ?";
			ps = con.prepareStatement(sql);
			
			ps.setString(1, comment.getContent());
			ps.setLong(2, comment.getId());
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
	
	//U zavisnosti od proslijedjenog flag-a odredjuje se da li ce brisanje komentara biti logicko ili fizicko
	public boolean delete(Comment comment, boolean adminMode) {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			if(adminMode)
			{
				String sql = "DELETE FROM mytube.comment WHERE id = ?";
				ps = con.prepareStatement(sql);
				
				ps.setLong(1, comment.getId());
				
				return ps.executeUpdate() == 1;
			}
			else
			{
				String sql = "UPDATE mytube.comment SET deleted = 1 WHERE id = ?";
				ps = con.prepareStatement(sql);
				
				ps.setLong(1, comment.getId());
				
				return ps.executeUpdate() == 1;
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
		}
		
		return false;
	}
}
