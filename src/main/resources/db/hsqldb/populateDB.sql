INSERT INTO loan_users(username,password, role_name) VALUES ('admin','aaa','ROLE_ADMIN');
INSERT INTO loan_users(username,password, role_name) VALUES ('user','aaa','ROLE_USER');
INSERT INTO loan_users(username,password, role_name) VALUES ('testadmin','aaa','ROLE_ADMIN');
INSERT INTO loan_users(username,password, role_name) VALUES ('testuser','aaa','ROLE_USER');


INSERT INTO loans (id,amount,term,ipaddress,created_on,status,username,interest_rate) VALUES (1,12.5,3,'192.168.1.1','2019-10-12T18:41:53.714461','Accepted','user',12);
INSERT INTO loans (id,amount,term,ipaddress,created_on,status,username,interest_rate) VALUES (2,10.5,4,'192.168.1.1','2019-10-12T12:41:53.714461','Accepted','user',12);

