package com.arturheath;

import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

public class Datasourse {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/" +
            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String DB_NAME = "my_db";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String COLUMN_CONTACTS_ID = "id";
    public static final String COLUMN_CONTACTS_NAME = "name";
    public static final String COLUMN_CONTACTS_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_CONTACTS_PICTURE = "picture";

    private Scanner scanner = new Scanner(System.in);

    private static final String CREATE_DB_MY_DB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
    private static final String USE_DB_MY_DB = "USE " + DB_NAME;
    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ("
            + COLUMN_CONTACTS_ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
            + COLUMN_CONTACTS_NAME + " VARCHAR(50) NOT NULL, "
            + COLUMN_CONTACTS_PHONE_NUMBER + " VARCHAR(20) NOT NULL, "
            + COLUMN_CONTACTS_PICTURE + " BLOB NOT NULL"
            + ")";
    public static final String DROP_TABLE_CONTACTS = "DROP TABLE " + TABLE_CONTACTS;
    public static final String QUERY_CONTACT = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
            + COLUMN_CONTACTS_NAME + " = ?";
    public static final String INSERT_INTO_CONTACTS = "INSERT INTO " + TABLE_CONTACTS + " ("
            + COLUMN_CONTACTS_NAME + ", " + COLUMN_CONTACTS_PHONE_NUMBER + ", " + COLUMN_CONTACTS_PICTURE + ")"
            + "VALUES ( ?, ?, ?)";

    private Connection conn;

    private PreparedStatement queryContact;
    private PreparedStatement insertContact;

    private Datasourse() {
    }

    private static final Datasourse datasourse = new Datasourse();

    public static Datasourse getInstance() {
        return datasourse;
    }

    public boolean openConnection() {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        try {
            conn = DriverManager.getConnection(CONNECTION_URL, username, password);
            queryContact = conn.prepareStatement(QUERY_CONTACT);
            insertContact = conn.prepareStatement(INSERT_INTO_CONTACTS, Statement.RETURN_GENERATED_KEYS);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create a connection " + e.getMessage());
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (queryContact != null) {
                queryContact.close();
            }
            if (insertContact != null) {
                insertContact.close();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't close a connection " + e.getMessage());
            return false;
        }
    }

    public boolean createDatabase(String dbName) {
        try (Statement createDB = conn.createStatement();
             Statement useDB = conn.createStatement()) {

            createDB.execute(CREATE_DB_MY_DB);
            System.out.println("Query executed: " + CREATE_DB_MY_DB);

            useDB.execute(USE_DB_MY_DB);
            System.out.println("Query executed: " + USE_DB_MY_DB);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create a database " + dbName + " " + e.getMessage());
            return false;
        }
    }

    public boolean createTable() {
        try (Statement createTable = conn.createStatement()) {
            createTable.execute(CREATE_TABLE_CONTACTS);
            System.out.println("Query executed: " + CREATE_TABLE_CONTACTS);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't create table " + TABLE_CONTACTS + " " + e.getMessage());
            return false;
        }
    }

    public boolean dropTable() {
        try (Statement statement = conn.createStatement()) {
            statement.execute(DROP_TABLE_CONTACTS);
            System.out.println("Query executed: " + DROP_TABLE_CONTACTS);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't drop table " + TABLE_CONTACTS + " " + e.getMessage());
            return false;
        }
    }

    public Contact queryContact(String name) {
        try {
            queryContact.setString(1, name);
            ResultSet result = queryContact.executeQuery();

            Contact contact = new Contact();

            if (result.next()) {
                contact.setName(result.getString(2));
                contact.setPhoneNumber(result.getString(3));
                contact.setPhoto(result.getBinaryStream(4));
            }
            return contact;
        } catch (SQLException e) {
            System.out.println("Query of a contact failed " + e.getMessage());
            return null;
        }
    }

    public int insertContact(String name, String phoneNumber, InputStream photo) throws SQLException {
        queryContact.setString(1, name);
        ResultSet result = queryContact.executeQuery();

        if (result.next()) {
            System.out.println("Contact already exists");
            return result.getInt(1);
        } else {
            insertContact.setString(1, name);
            insertContact.setString(2, phoneNumber);
            insertContact.setBinaryStream(3, photo);

            int affectedRows = insertContact.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert contact");
            }

            ResultSet generatedKeys = insertContact.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get id for a new contact");
            }
        }
    }
}















