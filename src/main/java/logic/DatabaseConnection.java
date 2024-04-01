/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.io.File;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego
 */
public class DatabaseConnection {
    private Connection conn = null;
    private String ruta = "";

    public Connection connect() {
    boolean connected = false;
    while (!connected) {
        try {
            // Obtener la ubicación del directorio del archivo ejecutable
            String directory = System.getProperty("user.dir");
            
            // Construir la ruta absoluta de la base de datos
            String dbPath = directory + File.separator + "Data" + File.separator + "db_sistema.db";
            ruta = dbPath;
            
            
            // Obtener la URL JDBC para la conexión
            String jdbcUrl = "jdbc:sqlite:" + dbPath;
            
            // Establecer la conexión
            conn = DriverManager.getConnection(jdbcUrl);
            connected = true; // Si llega hasta aquí sin lanzar una excepción, la conexión fue exitosa
        } catch (SQLException e) {
            int option = JOptionPane.showConfirmDialog(null, ruta, ruta, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            }
            System.out.println("Error en Conexión: " + e.getMessage());
        }
    }
    return conn;
    }
}
