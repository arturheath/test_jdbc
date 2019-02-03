package com.arturheath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException, FileNotFoundException {

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Nastya", "+7(926)423-75-23", new FileInputStream(
                new File("C:\\Users\\arsha\\IdeaProjects\\MyDatabase\\src\\pics\\Nastya.jpg"))));
        contacts.add(new Contact("Timofey", "+7(926)080-36-04", new FileInputStream(
                new File("C:\\Users\\arsha\\IdeaProjects\\MyDatabase\\src\\pics\\Timofey.jpeg"))));
        contacts.add(new Contact("Sanya", "+7(985)723-55-87", new FileInputStream(
                new File("C:\\Users\\arsha\\IdeaProjects\\MyDatabase\\src\\pics\\Sanya-min.JPG"))));

        Datasourse datasourse = Datasourse.getInstance();

        datasourse.openConnection();

        datasourse.createDatabase(Datasourse.DB_NAME);

        datasourse.dropTable();

        datasourse.createTable();

        for (Contact contact : contacts){
            int id = datasourse.insertContact(contact.getName(), contact.getPhoneNumber(), contact.getPhoto());
            System.out.println("id of a contact " + id);
        }

        System.out.println(datasourse.queryContact("Nastya"));

        datasourse.closeConnection();
    }
}
