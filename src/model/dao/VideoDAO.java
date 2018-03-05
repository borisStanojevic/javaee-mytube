package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import model.User;
import model.Video;
import model.Visibility;

public class VideoDAO {
	
	//Po nazivu
	
	public LinkedList<Video> getAllByName(String name){
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> videos = new LinkedList<>();
		

		try
		{
			String sql = "SELECT * FROM video WHERE name LIKE ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + name +"%");
			System.out.println(ps);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String videoUrl = rs.getString("video_url");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				
				User owner = new UserDAO().get(rs.getString("user_id"));
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(rs.getString("name"));
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				video.setLikes(new LikeDAO().getLikes(video));
				video.setDislikes(new LikeDAO().getDisLikes(video));
				
				videos.add(video);
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
		
		return videos;
		
	}
	
	//Po ksadrzaju komentara
	public LinkedList<Video> getAllByComment(String commentContent){
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> videos = new LinkedList<>();
		

		try
		{
			String sql = "SELECT * FROM video WHERE id IN (SELECT video_id FROM mytube.comment WHERE content LIKE ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + commentContent +"%");
			System.out.println(ps);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String name = rs.getString("name");
				String videoUrl = rs.getString("video_url");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				
				User owner = new UserDAO().get(rs.getString("user_id"));
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				video.setLikes(new LikeDAO().getLikes(video));
				video.setDislikes(new LikeDAO().getDisLikes(video));
				
				videos.add(video);
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
		
		return videos;
		
	}
	
	
	//Po username korisnika
	public LinkedList<Video> getAllByUsername(String ownerUsername){
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> videos = new LinkedList<>();
		

		try
		{
			String sql = "SELECT * FROM video WHERE user_id IN (SELECT username FROM user WHERE username LIKE ?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + ownerUsername +"%");
			System.out.println(ps);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String name = rs.getString("name");
				String videoUrl = rs.getString("video_url");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				
				User owner = new UserDAO().get(rs.getString("user_id"));
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				video.setLikes(new LikeDAO().getLikes(video));
				video.setDislikes(new LikeDAO().getDisLikes(video));
				
				videos.add(video);
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
		
		return videos;
	}
	
	public boolean update(Video video) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try
		{
			String sql = "UPDATE video SET name = ?, description = ?, views = ?, visibility = ?, comments_allowed = ?, rating_visible = ?, blocked = ? WHERE id = ?";
			
			ps = con.prepareStatement(sql);
			byte index = 1;
			ps.setString(index++, video.getName());
			ps.setString(index++, video.getDescription());
			ps.setLong(index++, video.getViews());
			ps.setString(index++, video.getVisibility().toString());
			ps.setBoolean(index++, video.isCommentsAllowed());
			ps.setBoolean(index++, video.isRatingVisible());
			ps.setBoolean(index++, video.isBlocked());
			ps.setLong(index++, video.getId());
			
			
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
	
	public ArrayList<Video> getTopFive() {
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Video> top5 = new ArrayList<Video>();
		

		try
		{
			String sql = "SELECT * FROM video LEFT OUTER JOIN user ON video.user_id = user.username LEFT OUTER JOIN subscribes ON user.username = subscribes.subscribed_user_id GROUP BY video.id LIMIT 5";
			ps = con.prepareStatement(sql);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String videoUrl = rs.getString("video_url");
				String name = rs.getString("name");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(new UserDAO().get(rs.getString("user_id")));
				
				top5.add(video);
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
		
		return top5;
		
	}
	
	//Dobavi sve videe koje je proslijedjeni korisnik uploadovao
	public LinkedList<Video> getAll(User user){
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> userVideos = new LinkedList<>();
		

		try
		{
			String sql = "SELECT * FROM video WHERE user_id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			System.out.println(ps);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String videoUrl = rs.getString("video_url");
				String name = rs.getString("name");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(user);
				
				userVideos.add(video);
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
		
		return userVideos;
		
	}
	
	public Video get(long id) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			String sql = "SELECT * FROM video WHERE id = ?";
			ps = con.prepareStatement(sql);
			ps.setLong(1, id);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				String videoUrl = rs.getString("video_url");
				String name = rs.getString("name");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				UserDAO userDAO = new UserDAO();
				User owner = userDAO.get(rs.getString("user_id"));
				LikeDAO likeDAO = new LikeDAO();
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setLikes(likeDAO.getLikes(video));
				video.setDislikes(likeDAO.getDisLikes(video));
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				
				return video;
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

	public boolean add(Video video) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		
		try 
		{
			String sql = "INSERT INTO video (video_url, name, description, visibility, comments_allowed, rating_visible, views, date_time_uploaded, user_id) "
					   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = con.prepareStatement(sql);
			
			byte index = 1;
			ps.setString(index++, video.getVideoUrl());
			ps.setString(index++, video.getName());
			ps.setString(index++, video.getDescription());
			ps.setString(index++, video.getVisibility().toString());
			ps.setBoolean(index++, video.isCommentsAllowed());
			ps.setBoolean(index++, video.isRatingVisible());
			ps.setLong(index++, video.getViews());
			ps.setTimestamp(index++, Timestamp.valueOf(video.getDateTimeUploaded()));
			ps.setString(index++, video.getOwner().getUsername());
			
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
	
	//U zavisnosti od flag-a odredjuje se da li je brisanje videa logicko ili fizicko
	public boolean delete(Video video, boolean adminMode) {
		
		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		try
		{
			if(adminMode == true)
			{
				String sql = "DELETE FROM video WHERE id = ?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, (int)video.getId());
				
				return ps.executeUpdate() == 1;
			}
			else
			{
				String sql = "UPDATE video SET deleted = 1 WHERE id = ?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, (int)video.getId());
				
				return ps.executeUpdate() == 1;
			}
		}
		catch(SQLException e)
		{
			System.out.println("Error in sql query 1");
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e2) {e2.printStackTrace();}
		}
		
		return false;
	}
	
	@SuppressWarnings("null")
	public LinkedList<Video> getAll(String mainSearchFilter, boolean nameIncluded, boolean ownerIncluded, boolean commentContentIncluded){

		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> videos = new LinkedList<>();

		try
		{
			String sql = "SELECT * FROM video";
			sql += " WHERE video.name LIKE ? OR video.user_id IN (SELECT user.username FROM user WHERE user.username = video.user_id AND user.username LIKE ?) OR video.id IN (SELECT comment.video_id FROM mytube.comment WHERE video.id = comment.video_id AND mytube.comment.content LIKE ?)";
			ps = con.prepareStatement(sql);
			byte index = 1;
			ps.setString(index++, "%" + mainSearchFilter +"%");
			ps.setString(index++, "%" + mainSearchFilter +"%");
			ps.setString(index++, "%" + mainSearchFilter +"%");
			rs = ps.executeQuery();
			
			System.out.println(ps);
			
			
			UserDAO userDAO = new UserDAO();
			LikeDAO likeDAO = new LikeDAO();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String videoUrl = rs.getString("video_url");
				String name = rs.getString("name");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				System.out.println(dateTimeUploaded);
				boolean deleted = rs.getBoolean("deleted");
				User owner = userDAO.get(rs.getString("user_id"));	
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setLikes(likeDAO.getLikes(video));
				video.setDislikes(likeDAO.getDisLikes(video));
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				
				videos.add(video);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e) {e.printStackTrace();}
			try {rs.close();} catch(SQLException e) {e.printStackTrace();}
		}

		return videos;
	}


	public LinkedList<Video> getAll(String nameFilter, String ownerUsernameFilter, long viewsMinFilter,
			long viewsMaxFilter, LocalDateTime minDateTimeFilter, LocalDateTime maxDateTimeFilter) {

		Connection con = ConnectionManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		LinkedList<Video> videos = new LinkedList<>();

		try
		{

			String sql = "SELECT * FROM video WHERE name LIKE ? AND user_id IN (SELECT username FROM user WHERE username LIKE ?) "
					   + "AND views BETWEEN ? AND ? AND date_time_uploaded BETWEEN ? AND ?";
			
			ps = con.prepareStatement(sql);

			byte index = 1;
			ps.setString(index++, "%" + nameFilter + "%");
			ps.setString(index++, "%" + ownerUsernameFilter + "%");
			ps.setLong(index++, viewsMinFilter);
			ps.setLong(index++, viewsMaxFilter);
			ps.setTimestamp(index++, Timestamp.valueOf(minDateTimeFilter));
			ps.setTimestamp(index++, Timestamp.valueOf(maxDateTimeFilter));
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			UserDAO userDAO = new UserDAO();
			LikeDAO likeDAO = new LikeDAO();
			
			while(rs.next())
			{
				long id = rs.getLong("id");
				String videoUrl = rs.getString("video_url");
				String name = rs.getString("name");
				String description = rs.getString("description");
				Visibility visibility = Visibility.valueOf(rs.getString("visibility"));
				boolean commentsAllowed = rs.getBoolean("comments_allowed");
				boolean ratingVisible = rs.getBoolean("rating_visible");
				boolean blocked = rs.getBoolean("blocked");
				long views = rs.getLong("views");
				LocalDateTime dateTimeUploaded = (rs.getTimestamp("date_time_uploaded")).toLocalDateTime();
				System.out.println(dateTimeUploaded);
				boolean deleted = rs.getBoolean("deleted");
				User owner = userDAO.get(rs.getString("user_id"));	
				
				Video video = new Video();
				
				video.setId(id);
				video.setVideoUrl(videoUrl);
				video.setName(name);
				video.setDescription(description);
				video.setVisibility(visibility);
				video.setCommentsAllowed(commentsAllowed);
				video.setRatingVisible(ratingVisible);
				video.setBlocked(blocked);
				video.setLikes(likeDAO.getLikes(video));
				video.setDislikes(likeDAO.getDisLikes(video));
				video.setViews(views);
				video.setDateTimeUploaded(dateTimeUploaded);
				video.setDeleted(deleted);
				video.setOwner(owner);
				
				videos.add(video);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {ps.close();} catch(SQLException e) {e.printStackTrace();}
			try {rs.close();} catch(SQLException e) {e.printStackTrace();}
		}

		return videos;
	}
	
}
