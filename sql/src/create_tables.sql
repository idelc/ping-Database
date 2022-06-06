DROP TABLE IF EXISTS EMPLOYEE;
DROP TABLE IF EXISTS MSG;
DROP TABLE IF EXISTS POST;
DROP TABLE IF EXISTS DEPARTMENTS;
DROP TABLE IF EXISTS MNGMT;

CREATE TABLE DEPARTMENTS(
    dept_ID integer UNIQUE NOT NULL,
    dept_Name char(50) NOT NULL,
    PRIMARY KEY(dept_ID)
);

CREATE TABLE EMPLOYEE(
    emp_ID varchar(30) UNIQUE NOT NULL,
    emp_Pass varchar(30) NOT NULL,
    empEmail varchar(30) NOT NULL,
    works_In integer NOT NULL,
    name char(50),
    dob date,
    hireDate date,
    PRIMARY KEY(emp_ID),
    FOREIGN KEY(works_In) REFERENCES DEPARTMENTS(dept_ID)
);

CREATE TABLE MSG(
    msg_ID integer UNIQUE NOT NULL,
    msg_Txt char(500) NOT NULL,
    send_ID varchar(30) NOT NULL,
    recv_ID varchar(30) NOT NULL,
    isAnon boolean NOT NULL,
    tm_Stmp timestamp,
    del_Stat integer NOT NULL,
    msg_Stat integer NOT NULL,
    PRIMARY KEY(msg_ID),
    FOREIGN KEY(send_ID) REFERENCES EMPLOYEE(emp_ID),
    FOREIGN KEY(recv_ID) REFERENCES EMPLOYEE(emp_ID)
);

CREATE TABLE POST(
    post_ID integer UNIQUE NOT NULL,
    content char(500) NOT NULL,
    isAnnou boolean NOT NULL,
    author varchar(30) NOT NULL,
    board integer NOT NULL,
    rating integer NOT NULL,
    PRIMARY KEY(post_ID),
    FOREIGN KEY(author) REFERENCES EMPLOYEE(emp_ID),
    FOREIGN KEY(board) REFERENCES EMPLOYEE(works_In)
);

CREATE TABLE MNGMT(
    dept integer,
    mngr varchar(30),
    PRIMARY KEY(dept, mngr),
    FOREIGN KEY(dept) REFERENCES DEPARTMENTS(dept_ID),
    FOREIGN KEY(mngr) REFERENCES EMPLOYEE(emp_ID)
);