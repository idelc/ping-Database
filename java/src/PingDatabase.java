/*
 * Template JAVA User Interface - MODIFIED BY Ivann De la Cruz
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science and Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 * Ping Database TEST UI
 * 
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class PingDatabase {
    // reference to physical database connection.
    private Connection _connection = null;

    // handling the keyboard inputs through a BufferedReader
    // This variable can be global for convenience.
    static BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));

    /**
     * Creates a new instance of Messenger
     *
     * @param hostname the MySQL or PostgreSQL server hostname
     * @param database the name of the database
     * @param username the user name used to login to the database
     * @param password the user login password
     * @throws java.sql.SQLException when failed to make a connection.
     */
    public PingDatabase(String dbname, String dbport, String user, String passwd) throws SQLException {

        System.out.print("Connecting to database...");
        try {
            // constructs the connection URL
            String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
            System.out.println("Connection URL: " + url + "\n");

            // obtain a physical connection
            this._connection = DriverManager.getConnection(url, user, passwd);
            System.out.println("Done");
        } catch (Exception e) {
            System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
            System.out.println("Make sure you started postgres on this machine");
            System.exit(-1);
        } // end catch
    }// end PingDatabase

    /**
     * Method to execute an update SQL statement. Update SQL instructions
     * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
     *
     * @param sql the input SQL string
     * @throws java.sql.SQLException when update failed
     */
    public void executeUpdate(String sql) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the update instruction
        stmt.executeUpdate(sql);

        // close the instruction
        stmt.close();
    }// end executeUpdate

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT). This
     * method issues the query to the DBMS and outputs the results to
     * standard out.
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQueryAndPrintResult(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);

        /*
         ** obtains the metadata object for the returned result set. The metadata
         ** contains row and column info.
         */
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCol = rsmd.getColumnCount();
        int rowCount = 0;

        // iterates through the result set and output them to standard out.
        boolean outputHeader = true;
        while (rs.next()) {
            if (outputHeader) {
                for (int i = 1; i <= numCol; i++) {
                    System.out.print(rsmd.getColumnName(i) + "\t");
                }
                System.out.println();
                outputHeader = false;
            }
            for (int i = 1; i <= numCol; ++i)
                System.out.print(rs.getString(i) + "\t");
            System.out.println();
            ++rowCount;
        } // end while
        stmt.close();
        return rowCount;
    }// end executeQuery

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT). This
     * method issues the query to the DBMS and returns the results as
     * a list of records. Each record in turn is a list of attribute values
     *
     * @param query the input query string
     * @return the query result as a list of records
     * @throws java.sql.SQLException when failed to execute the query
     */
    public List<List<String>> executeQueryAndReturnResult(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);

        /*
         ** obtains the metadata object for the returned result set. The metadata
         ** contains row and column info.
         */
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCol = rsmd.getColumnCount();
        int rowCount = 0;

        // iterates through the result set and saves the data returned by the query.
        boolean outputHeader = false;
        List<List<String>> result = new ArrayList<List<String>>();
        while (rs.next()) {
            List<String> record = new ArrayList<String>();
            for (int i = 1; i <= numCol; ++i)
                record.add(rs.getString(i));
            result.add(record);
        } // end while
        stmt.close();
        return result;
    }// end executeQueryAndReturnResult

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT). This
     * method issues the query to the DBMS and returns the number of results
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQuery(String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery(query);

        int rowCount = 0;

        // iterates through the result set and count nuber of results.
        if (rs.next()) {
            rowCount++;
        } // end while
        stmt.close();
        return rowCount;
    }

    /**
     * Method to fetch the last value from sequence. This
     * method issues the query to the DBMS and returns the current
     * value of sequence used for autogenerated keys
     *
     * @param sequence name of the DB sequence
     * @return current value of a sequence
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int getCurrSeqVal(String sequence) throws SQLException {
        Statement stmt = this._connection.createStatement();

        ResultSet rs = stmt.executeQuery(String.format("Select currval('%s')", sequence));
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    /**
     * Method to close the physical connection if it is open.
     */
    public void cleanup() {
        try {
            if (this._connection != null) {
                this._connection.close();
            } // end if
        } catch (SQLException e) {
            // ignored.
        } // end try
    }// end cleanup

    /**
     * The main execution method
     *
     * @param args the command line arguments this inclues the <mysql|pgsql> <login
     *             file>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(
                    "Usage: " +
                            "java [-classpath <classpath>] " +
                            PingDatabase.class.getName() +
                            " <dbname> <port> <user>");
            return;
        } // end if

        Greeting();
        PingDatabase esql = null;
        try {
            // use postgres JDBC driver.
            Class.forName("org.postgresql.Driver").newInstance();
            // instantiate the Messenger object and creates a physical
            // connection.
            String dbname = args[0];
            String dbport = args[1];
            String user = args[2];
            esql = new PingDatabase(dbname, dbport, user, "");

            boolean keepon = true;
            while (keepon) {
                // These are sample SQL statements
                System.out.println("\nMAIN MENU");
                System.out.println("---------");
                System.out.println("1. Add Employee");
                System.out.println("2. Log In");
                System.out.println("9. EXIT");
                String authorisedUser = null;
                switch (readChoice()) {
                    case 1:
                        CreateUser(esql);
                        break;
                    case 2:
                        authorisedUser = LogIn(esql);
                        break;
                    case 9:
                        keepon = false;
                        break;
                    default:
                        System.out.println("Unrecognized choice!");
                        break;
                }// end switch
                if (authorisedUser != null) {
                    boolean usermenu = true;
                    while (usermenu) {
                        System.out.println("\nMAIN MENU");
                        System.out.println("---------");
                        System.out.println("1. View Profile");
                        System.out.println("2. Update Profile");
                        System.out.println("3. Message Dashboard");
                        System.out.println("4. Pinboard");
                        System.out.println("5. Search For People");
                        System.out.println(".........................");
                        System.out.println("9. Log out");
                        switch (readChoice()) {
                            case 1:
                                displayProf(esql, authorisedUser);
                                break;
                            case 2:
                                // UpdateProfile(esql, authorisedUser);
                                break;
                            case 3:
                                // messageDash(esql, authorisedUser);
                                break;
                            case 4:
                                // pinboardMenu(esql, authorisedUser);
                                break;
                            case 5:
                                // srcPpl(esql, authorisedUser);
                                break;
                            case 9:
                                usermenu = false;
                                break;
                            default:
                                System.out.println("Unrecognized choice!");
                                break;
                        }
                    }
                }
            } // end while
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // make sure to cleanup the created table and close the connection.
            try {
                if (esql != null) {
                    System.out.print("Disconnecting from database...");
                    esql.cleanup();
                    System.out.println("Done\n\nBye !");
                } // end if
            } catch (Exception e) {
                // ignored.
            } // end try
        } // end try
    }// end main

    public static void Greeting() {
        System.out.println(
                "\n\n*******************************************************\n" +
                        "              User Interface      	               \n" +
                        "*******************************************************\n");
    }// end Greeting

    /*
     * Reads the users choice given from the keyboard
     * 
     * @int
     **/
    public static int readChoice() {
        int input;
        // returns only if a correct value is given.
        do {
            System.out.print("Please make your choice: ");
            try { // read the integer, parse it and break.
                input = Integer.parseInt(in.readLine());
                break;
            } catch (Exception e) {
                System.out.println("Your input is invalid!");
                continue;
            } // end try
        } while (true);
        return input;
    }// end readChoice

    /*
     * * Creates a new user with privided login, passowrd and phoneNum
     * * An empty block and contact list would be generated and associated with a
     * user
     **/
    public static void CreateUser(PingDatabase esql) {
        try {
            System.out.print("\tEnter company login: ");
            String login = in.readLine();
            System.out.print("\tEnter password: ");
            String password = in.readLine();
            System.out.print("\tEnter user email: ");
            String email = in.readLine();
            String query = String.format("SELECT D.dept_ID as Department_Code, D.dept_Name as Department FROM DEPARTMENTS D");
            esql.executeQuery(query);
            System.out.print("\tEnter department code: ");
            Integer deptNum = readChoice();
            // Creating empty contact\block lists for a user
            query = String.format("SELECT * FROM EMPLOYEE WHERE emp_ID = '%s'", login);
            int validIn = esql.executeQuery(query);
            if (validIn > 0) {
                System.out.println("\nSomeone already has that username, please try again");
                return;
            }
            query = String.format("INSERT INTO EMPLOYEE (emp_ID, emp_Pass, empEmail, works_In) VALUES ('%s','%s','%s', '%s')", login, password,
                    email, deptNum);
            esql.executeUpdate(query);
            System.out.println("User successfully created!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }// end

    /*
     * Check log in credentials for an existing user
     * 
     * @return User login or null is the user does not exist
     **/
    public static String LogIn(PingDatabase esql) {
        try {
            System.out.print("\tEnter user login: ");
            String login = in.readLine();
            System.out.print("\tEnter user password: ");
            String password = in.readLine();

            String query = String.format("SELECT * FROM EMPLOYEE WHERE emp_ID = '%s' AND emp_Pass = '%s'", login, password);
            int userNum = esql.executeQuery(query);
            if (userNum == 1) {
                return login;
            } else {
                System.out.println("\tIncorrect userid or password! Please try again!\n");
            }
            return null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }// end

    public static void displayProf(PingDatabase esql, String fName){
        try{
           String query = String.format("SELECT E.emp_ID, E.empEmail, D.dept_Name, E.name FROM EMPLOYEE E, DEPARTMENTS D WHERE E.emp_ID = '%s' AND D.dept_ID = E.works_In", fName);
           System.out.print("\n");
           esql.executeQueryAndPrintResult(query);
           return;
        }catch(Exception e){
           System.err.println (e.getMessage ());
           return;
        }
     }//end
}