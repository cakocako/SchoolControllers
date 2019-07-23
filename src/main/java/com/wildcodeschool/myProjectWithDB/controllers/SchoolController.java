package com.wildcodeschool.myProjectWithDB.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
public class SchoolController<School> {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "ULcaroLI";

    @GetMapping("/api/schools")
    public List<School> getSchool(@RequestParam(defaultValue = "%") String country)  {
        try(
            Connection connection = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
            PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM school WHERE country LIKE ?"
            );
        ) {
            statement.setString(1, country);
            try(
                ResultSet resulSet = statement.executeQuery();
            ) {
                List<School> school = new ArrayList<School>();

                while(resulSet.next()){
                    int id = resulSet.getInt("id");
                    String name = resulSet.getString("name");
                    String capacity = resulSet.getString("capacity");
                    String Country = resulSet.getString("country");
                    school.add(new School(id, name, capacity, Country));
                }

            return school;
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }


    public class School {

        private int id;
        private String name;
        private String capacity;
        private String country;

        public School(int id, String name, String capacity, String country) {
            this.id = id;
            this.name = name;
            this.capacity = capacity;
            this.country = country;
        }

        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getCapacity() {
            return capacity;
        }
        public String getCountry() {
            return country;
        }
    }
}
