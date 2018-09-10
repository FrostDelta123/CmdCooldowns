package ru.frostdelta.cmdcooldowns;

import jdk.nashorn.internal.objects.annotations.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.ResultSet;

public class Network {

    public String url, username, password;
    private static HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private Connection connection;

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url +
                    "?useUnicode=true&characterEncoding=UTF-8", this.username, this.password);

            preparedStatements.put("getCases", connection.prepareStatement(
                    "SELECT * FROM `cases` WHERE `uuid`=?"));
        }
    }

    public Integer getCases(String col, String uuid) {
        try {
            openConnection();
            PreparedStatement getCases = preparedStatements.get("getCases");

            getCases.setString(1, uuid);
            try (ResultSet rs = getCases.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt(col);
                }
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Setter
    public void setUrl(String url){
        this.url = url;
    }

    @Setter
    public void setUsername(String username){
        this.username = username;
    }

    @Setter
    public void setPassword(String password){
        this.password = password;
    }
}
