COPY EMPLOYEE(
    emp_ID,
    emp_Pass,
    empEmail,
    works_In,
    name,
    dob,
    hireDate
)
FROM 'EMP.csv'
DELIMITER ',' CSV HEADER;

COPY MSG(
    msg_ID,
    msg_Txt,
    send_ID,
    recv_ID,
    isAnon,
    tm_Stmp,
    del_Stat,
    msg_Stat
)
FROM 'MSG.csv'
DELIMITER ',' CSV HEADER;

COPY POST(
    post_ID,
    content,
    isAnnou,
    author,
    board,
    rating
)
FROM 'POSTS.csv'
DELIMITER ',' CSV HEADER;

COPY DEPARTMENTS(
    dept_ID,
    dept_Name,
    dept_Mngmt
)
FROM 'DEPARTMENTS.csv'
DELIMITER ',' CSV HEADER;