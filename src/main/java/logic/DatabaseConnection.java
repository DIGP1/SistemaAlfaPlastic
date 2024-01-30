/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Diego
 */
public class DatabaseConnection {
    private Connection conn = null;

    public Connection connect() {
        try {
            File dbFile = new File("src/main/java/resources/db_sistema.db");
            String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            
            conn = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            System.out.println("Error en Connexion: "+ e.getMessage());
        }
        return conn;
    }
}
