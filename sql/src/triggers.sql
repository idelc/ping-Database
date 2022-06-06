CREATE SEQUENCE msgID_seq START WITH 21; /* TODO: CHANGE TO BE CORRECT*/
CREATE SEQUENCE dept_seq START WITH 6;
CREATE SEQUENCE post_seq START WITH 21;

CREATE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION newMsg()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.msg_ID = nextval('msgID_seq');

RETURN NEW;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER newMessage BEFORE INSERT
ON MSG FOR EACH ROW
EXECUTE PROCEDURE newMsg();


CREATE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION newDept()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.dept_ID = nextval('dept_seq');

RETURN NEW;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER newDepartment BEFORE INSERT
ON DEPARTMENTS FOR EACH ROW
EXECUTE PROCEDURE newDept();


CREATE LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION newPost()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.post_ID = nextval('post_seq');

RETURN NEW;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE TRIGGER newPostMade BEFORE INSERT
ON POST FOR EACH ROW
EXECUTE PROCEDURE newPost();