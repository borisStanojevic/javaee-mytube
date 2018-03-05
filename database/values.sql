
INSERT INTO user(username, password, thumbnail_url, first_name, last_name, email, registration_date, role)
VALUES ('mitar123','123456','default.png', 'Mitar','Miric','mitar@ludilo.com', '2012-01-01', 'ADMIN'),
	   ('pero123','123456','default.png', 'Petar','Petrovic','petar@ludilo.com', '2012-01-01', 'USER'),
       ('jovan123','123456','default.png', 'Jovan','Jovanovic','jovan@ludilo.com', '2012-01-01', 'USER'),
       ('saban123','123456','default.png', 'Saban','Sabanovic','saban@ludilo.com', '2012-01-01', 'USER');

INSERT INTO video(video_url, name, visibility, views, date_time_uploaded, user_id)
VALUES ('https://www.youtube.com/embed/rrVDATvUitA?rel=0&amp;showinfo=0', 'First Video', 'PUBLIC', 12, '2018-01-01', 'pero123'),
	   ('https://www.youtube.com/embed/FLwD60hPK4I?rel=0&amp;showinfo=0', 'Second Video', 'PUBLIC', 280, '2013-02-01', 'pero123'),
       ('https://www.youtube.com/embed/iPLpiyX6sSY?rel=0&amp;showinfo=0', 'Third Video', 'PUBLIC', 14402, '2014-05-06', 'pero123'),
       ('https://www.youtube.com/embed/qEja72NSg5Q?rel=0&amp;showinfo=0', 'Fourth Video', 'PUBLIC', 113000, '2011-07-10', 'jovan123'),
       ('https://www.youtube.com/embed/KjNe9fuqQ8o?rel=0&amp;showinfo=0', 'Fifth Video', 'PRIVATE', 2, '2018-12-12', 'jovan123'),
       ('https://www.youtube.com/embed/QE3D3TEZDD4?rel=0&amp;showinfo=0', 'Sixth Video', 'PUBLIC', 212, '2017-06-01', 'saban123'),
       ('https://www.youtube.com/embed/dMNxiBCKvMU?rel=0&amp;showinfo=0', 'Seventh Video', 'UNLISTED', 33, '2009-11-12', 'saban123');
       
INSERT INTO comment(content, date_time_posted, user_id, video_id)
VALUES ('A komentar', '2018-01-01', 'jovan123', 1),
	   ('B komentar', '2018-01-05', 'saban123', 1),
       ('C komentar', '2018-02-01', 'pero123', 1),
       ('A komentar', '2018-01-04', 'jovan123', 2),
	   ('B komentar', '2018-01-05', 'saban123', 2),
       ('C komentar', '2018-02-01', 'pero123', 2),
	   ('A komentar', '2018-01-03', 'jovan123', 3),
	   ('B komentar', '2018-01-05', 'saban123', 3),
       ('C komentar', '2018-02-02', 'pero123', 3),
	   ('A komentar', '2018-01-03', 'jovan123', 4),
	   ('B komentar', '2018-01-05', 'saban123', 4),
       ('C komentar', '2018-02-02', 'pero123', 4),
       ('Komentar admina', '2018-12-12', 'mitar123', 5),
	   ('A komentar', '2018-01-03', 'jovan123', 6),
	   ('B komentar', '2018-01-05', 'saban123', 6),
       ('C komentar', '2018-02-02', 'pero123', 6),
	   ('A komentar', '2018-01-01', 'jovan123', 7),
	   ('B komentar', '2018-01-04', 'saban123', 7),
       ('C komentar', '2018-02-12', 'pero123', 7);
       
INSERT INTO video_like(is_like, date_time_created, user_id, video_id)
VALUES (1,'2018-01-01', 'saban123', 1),
	   (1,'2018-01-01', 'pero123', 1),
       (1,'2018-01-01', 'saban123', 2),
       (0,'2018-01-01', 'pero123', 6);
       
INSERT INTO comment_like(is_like, date_time_created, user_id, comment_id)
VALUES (1, '2018-02-01', 'saban123', 19),
	   (1,'2018-01-01', 'saban123', 7),
       (1,'2018-01-01', 'saban123', 4);
       
INSERT INTO subscribes
VALUES
('pero123', 'jovan123'),
('pero123', 'mitar123'),
('pero123', 'saban123'),
('mitar123', 'pero123'),
('saban123', 'jovan123'),
('saban123', 'pero123'),
('jovan123', 'pero123'),
('jovan123', 'mitar123');
       

		
       
       
        