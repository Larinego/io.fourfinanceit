DROP TABLE loan_extensions IF EXISTS;
DROP TABLE loans IF EXISTS;
DROP TABLE loan_users IF EXISTS;


CREATE TABLE loan_users (
  username    VARCHAR(20) NOT NULL ,
  password    VARCHAR(20) NOT NULL ,
  role_name   VARCHAR(20) NOT NULL ,
  PRIMARY KEY (username)
);

CREATE TABLE loans (
  id            INTEGER IDENTITY PRIMARY KEY ,
  amount        DECIMAL NOT NULL ,
  term          INTEGER NOT NULL ,
  ipaddress     VARCHAR(40) ,
  created_on    TIMESTAMP WITH TIME ZONE ,
  status        VARCHAR(10) ,
  interest_rate DOUBLE ,
  username      VARCHAR(20) NOT NULL
);
ALTER TABLE loans ADD CONSTRAINT fk_loans_user FOREIGN KEY (username) REFERENCES loan_users (username);
CREATE INDEX loans_ipaddress ON loans (ipaddress);

CREATE TABLE loan_extensions (
  id            INTEGER IDENTITY PRIMARY KEY ,
  loan_id       INTEGER NOT NULL ,
  interest_rate VARCHAR(20) NOT NULL ,
  created_on    TIMESTAMP WITH TIME ZONE
);
ALTER TABLE loan_extensions ADD CONSTRAINT fk_loan_extensions_id FOREIGN KEY (loan_id) REFERENCES loans (id);
