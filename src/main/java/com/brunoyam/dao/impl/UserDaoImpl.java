package com.brunoyam.dao.impl;

import com.brunoyam.dao.UserDao;
import com.brunoyam.dto.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/brunoyam";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String ID_COLUMN = "ID";
    private static final String NAME_COLUMN = "NAME";
    private static final String AGE_COLUMN = "AGE";

    @Override
    public List<User> getAllUsers() {
        List<User> users = new LinkedList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            String sqlSelect = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            while (resultSet.next()) {
                Long id = resultSet.getLong(ID_COLUMN);
                String name = resultSet.getString(NAME_COLUMN);
                Integer age = resultSet.getInt(AGE_COLUMN);

                User user = new User();

                user.setId(id);
                user.setName(name);
                user.setAge(age);

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public void addUser(User user) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            /*Statement statement = connection.createStatement();

            String sqlInsert = String.format("INSERT INTO users(name, age) VALUES('%s', %d)",
                    user.getName(), user.getAge());
            statement.executeUpdate(sqlInsert);*/

            String sqlInsert = "INSERT INTO users(name, age) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
