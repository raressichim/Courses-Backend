insert into student (id) values (1);
insert into student (id) values (2);

insert into teacher (id) values (1);
insert into teacher (id) values (2);


insert into user (id, email, user_student,first_name, last_name, password, username) values(1,'a@gmail.com',1, 'a', 'a','{bcrypt}$2a$10$RbFEj8aK.BScNVuD0CazCedNR4S7sBzcMzuNcuTl20070oc3nsZQe','a');
insert into user (id, email, user_student,first_name, last_name, password, username) values(2,'b@gmail.com',2, 'b', 'b','{bcrypt}$2a$10$XxXozJn3gYGA7V4WijdAv.kHKyCpaOHwPMH5obdkFDLiTB.WysF0a','b');

insert into user (id,email, user_teacher,first_name, last_name, password, username) values(3,'t1@gmail.com',1,'t1', 't1','{bcrypt}$2a$10$Xj2TZccefQ2AQcahc9JCS.GofqiwBTN3AyB.BBKTRiO6ZYeY6GLlq','t1');
insert into user (id,email, user_teacher,first_name, last_name, password, username) values(4,'t2@gmail.com',2, 't2', 't2','{bcrypt}$2a$10$PWNcdQNTKF6qJHcNpbdZc.6thuilWBN/OmeLHpTob5IL3s6V13uWu','t2');


insert into course (id, name, description, max_attendees, teacher_id, no_attendees, status) values (1, "OOP", "Lorem ipsum", 20, 1,0,0);
insert into course (id, name, description, max_attendees, teacher_id, no_attendees, status) values (2, "ML", "Lorem ipsum", 2, 2,0,0);

insert into schedule (id, course_id, name,start_date, end_date, start_time, end_time, week_day) values (1, 1,0,'2024-01-01', '2024-05-01', '12:00', '14:00', 0);
insert into schedule (id, course_id, name,start_date, end_date, start_time, end_time, week_day) values (2, 2,0,"2024-03-01", "2024-05-01", "17:00","18:00",1);


