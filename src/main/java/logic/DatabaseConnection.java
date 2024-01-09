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
            File dbFile = new File("src/main/java/resources/catalogo.db");
            String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            
            conn = DriverManager.getConnection(jdbcUrl);
            System.out.println("Conexión a SQLite establecida.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
