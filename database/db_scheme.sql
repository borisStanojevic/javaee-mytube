create schema mytube;
use mytube;
CREATE TABLE user
(
 thumbnail_url VARCHAR(100),
 username VARCHAR(30) PRIMARY KEY,
 password VARCHAR(30) NOT NULL,
 first_name VARCHAR(30) DEFAULT '',
 last_name VARCHAR(30) DEFAULT '',
 email VARCHAR(60) NOT NULL,
 channel_description VARCHAR(250) DEFAULT '',
 registration_date DATE NOT NULL,
 role ENUM('USER', 'ADMIN') NOT NULL,
 -- thumbnail_url VARCHAR(500) DEFAULT '00',
 blocked BIT(1) DEFAULT 0,
 deleted BIT(1) DEFAULT 0
);

CREATE TABLE video
(
 id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
 video_url VARCHAR(2000) NOT NULL,
 name VARCHAR(50) NOT NULL,
 description VARCHAR(250) DEFAULT '',
 visibility ENUM('PUBLIC', 'UNLISTED', 'PRIVATE') NOT NULL,
 comments_allowed BIT(1) DEFAULT 1,
 rating_visible BIT(1) DEFAULT 1,
 blocked BIT(1) DEFAULT 0,
 views BIGINT UNSIGNED NOT NULL,
 date_time_uploaded DATETIME NOT NULL,
 deleted BIT(1) DEFAULT 0,
 user_id VARCHAR(30) NOT NULL,
 
 FOREIGN KEY (user_id) REFERENCES user(username)
);

CREATE TABLE comment
(
 id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
 content VARCHAR(500) NOT NULL,
 date_time_posted DATETIME NOT NULL,
 user_id VARCHAR(30) NOT NULL,
 video_id INT UNSIGNED NOT NULL,
 deleted BIT(1) DEFAULT 0,
 
 FOREIGN KEY (user_id) REFERENCES user(username),
 FOREIGN KEY (video_id) REFERENCES video(id)
);
	
CREATE TABLE video_like
(
 is_like BIT(1) DEFAULT 1,
 date_time_created DATETIME NOT NULL,
 user_id VARCHAR(30) NOT NULL,
 video_id INT UNSIGNED NOT NULL,
 
 FOREIGN KEY (user_id) REFERENCES mytube.user(username) ON DELETE CASCADE,
 FOREIGN KEY (video_id) REFERENCES video(id) ON DELETE CASCADE,
 PRIMARY KEY (user_id, video_id)
);

CREATE TABLE comment_like
(
 is_like BIT(1) DEFAULT 1,
 date_time_created DATETIME NOT NULL,
 user_id VARCHAR(30) NOT NULL,
 comment_id BIGINT UNSIGNED NOT NULL,
 
 --
 FOREIGN KEY (user_id) REFERENCES mytube.user(username) ON DELETE CASCADE,
 FOREIGN KEY (comment_id) REFERENCES mytube.comment(id) ON DELETE CASCADE,
 PRIMARY KEY (user_id, comment_id)
);

CREATE TABLE subscribes
(
 subscriber_user_id VARCHAR(30) NOT NULL,
 subscribed_user_id VARCHAR(30) NOT NULL,
 
 FOREIGN KEY (subscriber_user_id) REFERENCES mytube.user(username) ON DELETE CASCADE,
 FOREIGN KEY (subscribed_user_id) REFERENCES mytube.user(username) ON DELETE CASCADE,
 PRIMARY KEY (subscriber_user_id, subscribed_user_id)
);