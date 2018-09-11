package ru.frostdelta.cmdcooldowns;

import jdk.nashorn.internal.objects.annotations.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.ResultSet;

public class Network {

    private String url, username, password;
    private static HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private Connection connection;

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null || !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null || !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url +
                    "?useUnicode=true&characterEncoding=UTF-8", this.username, this.password);

            preparedStatements.put("getCases", connection.prepareStatement(
                    "SELECT * FROM `cases` WHERE `uuid`=?"));
            preparedStatements.put("addUUID", connection.prepareStatement(
                    "INSERT INTO `cases` (uuid, amountCase1, amountCase2, amountCase3) VALUES (?,?,?)"));
        }
    }

    public void addUUID(String uuid) {
        try {
            PreparedStatement addUUID = preparedStatements.get("addUUID");
            addUUID.setString(1, uuid);
            addUUID.setInt(2, 0);
            addUUID.setInt(3, 0);
            addUUID.setInt(4, 0);

            addUUID.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getCases(String col, String uuid) {
        try {
            PreparedStatement getCases = preparedStatements.get("getCases");

            getCases.setString(1, uuid);
            try (ResultSet rs = getCases.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt(col);
                }
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Setter
    public void setUrl(String url){
        this.url = url;
        System.out.println(this.url);
    }

    @Setter
    public void setUsername(String username){
        this.username = username;
        System.out.println(this.username);
    }

    @Setter
    public void setPassword(String password){
        this.password = password;
        System.out.println(this.password);
    }
}
