/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Diego
 */
import forms.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Datarequest {
    private DatabaseConnection dbConnection = new DatabaseConnection();
    
    public List<List<Object>> loadClient(){
        List<List<Object>> client = new ArrayList<>();
        
        String sql = "SELECT * FROM tbClient";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("fullname"));
                        row.add(rs.getString("address"));
                        row.add(rs.getString("rute"));
                        row.add(rs.getFloat("total_sold"));
                        client.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return client;
    }
    
    public boolean registerClient(String fullname, String address, String rute, float total_sold){
        String sql = "INSERT INTO tbClient (fullname, address, rute, total_sold) VALUES (?,?,?,?)";
        boolean pass = false;
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, fullname);
            pstmt.setString(2, address);
            pstmt.setString(3, rute);
            pstmt.setFloat(4, total_sold);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                pass = true;
                System.out.println(filasAfectadas);
                return  pass;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return pass;
    }
 public boolean EditClient(String fullname, String address, String rute, float total_sold, int id){
        String sql = "UPDATE tbClient SET fullname = ?,address = ?, rute = ?, total_sold = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, fullname);
            pstmt.setString(2, address);
            pstmt.setString(3, rute);
            pstmt.setFloat(4, total_sold);
            pstmt.setInt(5, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
 public boolean DeleteClient(int id){
     String sql = "DELETE FROM tbClient WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
              
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
  public List<List<Object>> loadProviders(){
        List<List<Object>> provider = new ArrayList<>();
        
        String sql = "SELECT * FROM tbProvider";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("name"));
                        row.add(rs.getString("orders_placed"));
                        row.add(rs.getString("total_purchased"));
                        provider.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return provider;
    }
    public boolean registerProvider(String name, String orders_placed,float total_purchased){
        String sql = "INSERT INTO tbProvider (name, orders_placed, total_purchased) VALUES (?,?,?)";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setString(2, orders_placed);
            pstmt.setFloat(3, total_purchased);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean EditProvider(String name, int id){
        String sql = "UPDATE tbProvider SET name = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
 public boolean DeleteProvider(int id){
     String sql = "DELETE FROM tbProvider WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
  public List<List<Object>> loadProducts(){
        List<List<Object>> provider = new ArrayList<>();
        
        String sql = "SELECT * FROM tbProduct";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("product_name"));
                        row.add(rs.getFloat("total_sold"));
                        row.add(rs.getInt("id_provider"));
                        row.add(rs.getFloat("purchase_price"));
                        provider.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return provider;
    }
   public List<List<Object>> loadProductsSold( int id_provider, boolean  soldProduct){
        List<List<Object>> provider = new ArrayList<>();
        String sql;
        if(soldProduct){
            sql = "SELECT * FROM tbProduct WHERE total_sold > 0 AND id_provider = "+ id_provider;
        }else{
            sql = "SELECT * FROM tbProduct WHERE id_provider = "+id_provider;
        }
        
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("product_name"));
                        row.add(rs.getFloat("total_sold"));
                        row.add(rs.getInt("id_provider"));
                        row.add(rs.getFloat("purchase_price"));
                        provider.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return provider;
    }
   
    public boolean registerProduct(String name, float total_sold,int id_provider,float purchase_price){
        String sql = "INSERT INTO tbProduct (product_name, total_sold, id_provider, purchase_price) VALUES (?,?,?,?)";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setFloat(2, total_sold);
            pstmt.setInt(3, id_provider);
            pstmt.setFloat(4, purchase_price);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean EditProduct(String name,int id_provider ,float purchase_price, int id){
        String sql = "UPDATE tbProduct SET product_name = ?,id_provider = ?, purchase_price = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setInt(2, id_provider);
            pstmt.setFloat(3, purchase_price);
            pstmt.setInt(4, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
 public boolean DeleteProduct(int id){
     String sql = "DELETE FROM tbProduct WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
 public String loadNameProvider(int id){
     String provider = "";
     String sql = "SELECT name FROM tbProvider WHERE id = ?";
        try(Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
                    while(rs.next()){ 
                        provider = rs.getString("name");
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return provider;
 }
 public List<SearchValue> SearchCustomer(String codigoIngresado, String ruta) {
     List<SearchValue> listaResultados = new ArrayList<>();
    String sql = "SELECT * FROM tbClient WHERE fullname LIKE '" + codigoIngresado + "%' AND rute LIKE '" + ruta + "%'";
  
    try (Connection conn = dbConnection.connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        if(rs.next()){
            SearchValue searchValue = new SearchValue(rs.getInt("id"),rs.getString("fullname"), rs.getString("address"), rs.getString("rute"), rs.getFloat("total_sold"));
            listaResultados.add(searchValue);
            while (rs.next()) {
            SearchValue searchValues = new SearchValue(rs.getInt("id"),rs.getString("fullname"), rs.getString("address"), rs.getString("rute"), rs.getFloat("total_sold"));
            listaResultados.add(searchValues);
            }
            conn.close();
        }else{
            conn.close();
        }    
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return listaResultados;
}
 public List<SearchProduct> SearchProduct(String codigoIngresado) {
     List<SearchProduct> listaResultados = new ArrayList<>();
    String sql = "SELECT * FROM tbProduct WHERE product_name LIKE '%" + codigoIngresado +"%'";
  
    try (Connection conn = dbConnection.connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        if(rs.next()){
            SearchProduct searchProduct = new SearchProduct(rs.getInt("id"),rs.getString("product_name"),rs.getFloat("total_sold"));
            listaResultados.add(searchProduct);
            while (rs.next()) {
            SearchProduct searchProducts = new SearchProduct(rs.getInt("id"),rs.getString("product_name"),rs.getFloat("total_sold"));
            listaResultados.add(searchProducts);
            }
            System.out.println(searchProduct.getName_product());
            conn.close();
        }else{
            conn.close();
        }    
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        
    }
    return listaResultados;
}
 
 public boolean addSale(int id_Client, String date_sale, float total){
      String sql = "INSERT INTO tbSales (id_client, sale_date, total) VALUES (?,?,?)";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id_Client);
            pstmt.setString(2, date_sale);
            pstmt.setFloat(3, total);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar venta: "+ e.getMessage());
            return false;
        }
 }
 
  public boolean addDetailSale(int id_sale, int id_product, float product_quantity, float unit_price, float sub_total){
      String sql = "INSERT INTO tbSaleDetail (id_sale, id_product, product_quantity, unit_price, sub_total) VALUES (?,?,?,?,?)";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id_sale);
            pstmt.setInt(2, id_product);
            pstmt.setFloat(3, product_quantity);
            pstmt.setFloat(4, unit_price);
            pstmt.setFloat(5, sub_total);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar venta: "+ e.getMessage());
            return false;
        }
 }
  public int loadLastIDSale(){
      int lastId = 0;
       String sql = "SELECT MAX(id) AS last_id FROM tbSales";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                if(rs.next()){
                    lastId = rs.getInt("last_id");
                }
                conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lastId;
  }
   public boolean addSaleTemp(int id_Client, String date_sale, float total, int id_sale){
      String sql = "INSERT INTO tbSaleTemp (id_client, sale_date, total, id_sale) VALUES (?,?,?,?)";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id_Client);
            pstmt.setString(2, date_sale);
            pstmt.setFloat(3, total);
            pstmt.setInt(4, id_sale);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar venta: "+ e.getMessage());
            return false;
        }
 }
   public List<List<Object>> loadSaleTemp(){
        List<List<Object>> saleTemp = new ArrayList<>();
        
        String sql = "SELECT * FROM tbClient";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id_client"));
                        row.add(rs.getString("sale_date"));
                        row.add(rs.getFloat("total"));
                        row.add(rs.getInt("id_sale"));
                        saleTemp.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return saleTemp;
    }
    public float loadProductSold(int id_product){
        float productSold = 0;
        
        String sql = "SELECT total_sold FROM tbProduct WHERE id = " + id_product;
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                if(rs.next()){
                    productSold = rs.getFloat("total_sold");
                }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  productSold;
    }
    public boolean EditProductSold(int id_product, float product_sold){
        String sql = "UPDATE tbProduct SET total_sold = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setFloat(1, product_sold);
            pstmt.setInt(2, id_product);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se edito la venta total del producto");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public List<Object> searchSale(int id_sale, boolean inTemp){
        List<Object> row = new ArrayList<>();
        String sql = null;
        if(inTemp){
            sql = "SELECT * FROM tbSaleTemp WHERE id= "+id_sale;
        }else{
            sql = "SELECT * FROM tbSales WHERE id= "+id_sale;
        }
        
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            if(inTemp){
                    while(rs.next()){
                        row.add(rs.getInt("id_sale"));
                        row.add(rs.getString("id_client"));
                        row.add(rs.getString("total"));
                    }
            }else{
                    while(rs.next()){
                        row.add(rs.getInt("id"));
                        row.add(rs.getString("id_client"));
                        row.add(rs.getString("total"));
                    }
            }
             conn.close();      
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return row;
    }
    public SearchValue SearchCustomer(int id){
        SearchValue sv = null;
         String sql = "SELECT * FROM tbClient WHERE id= "+id;
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    if(rs.next()){
                        sv= new SearchValue(rs.getInt("id"), rs.getString("fullname"), rs.getString("address"), rs.getString("rute"), rs.getFloat("total_sold"));
                    }
                    conn.close();
                    
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return sv;
    }
        public List<List<Object>> loadDetailSale(int id_sale){
        List<List<Object>> client = new ArrayList<>();
        
        String sql = "SELECT * FROM tbSaleDetail WHERE id_sale ="+id_sale;
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id_product"));
                        row.add(rs.getFloat("product_quantity"));
                        row.add(rs.getFloat("unit_price"));
                        row.add(rs.getFloat("sub_total"));
                        client.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return client;
    }
        public String SearchNameProduct(int id){
        String sv = null;
         String sql = "SELECT * FROM tbProduct WHERE id= "+id;
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    if(rs.next()){
                        sv= rs.getString("product_name");
                    }
                    conn.close();
                    
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return sv;
    }
   public boolean modSale(int id_sale, String date_sale, float total){
      String sql = "UPDATE tbSales SET sale_date = ?, total = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, date_sale);
            pstmt.setFloat(2, total);
            pstmt.setInt(3, id_sale);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al editar la venta: "+ e.getMessage());
            return false;
        }
 }
  public boolean DeleteDetailSale(int id){
     String sql = "DELETE FROM tbSaleDetail WHERE id_sale = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
     public boolean modSaleTemp(int id_sale, String date_sale, float total){
      String sql = "UPDATE tbSaleTemp SET sale_date = ?, total = ? WHERE id_sale = ?";
        
        try (Connection conn = dbConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, date_sale);
            pstmt.setFloat(2, total);
            pstmt.setInt(3, id_sale);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al editar la venta: "+ e.getMessage());
            return false;
        }
 }
    public boolean DeleteSaleTemp(int id){
     String sql = "DELETE FROM tbSaleTemp WHERE id_sale = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
      public boolean DeleteSale(int id){
     String sql = "DELETE FROM tbSales WHERE id = ?";
        
        try (Connection conn = dbConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.close();
                return true;
            } else {
                conn.close();
                System.out.println("No se pudieron guardar los datos en la base de datos.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
 }
  public boolean boolSearchSaleTemp(int id){
     String sql = "SELECT * FROM tbSaleTemp WHERE id_sale ="+id;
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    if(rs.next()){
                        
                        return true;
                    }
                    conn.close();
                    
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
  
    public List<List<Object>> loadOrdersTemp(){
        List<List<Object>> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM tbSaleTemp";
        try(Connection conn = dbConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                    while(rs.next()){
                        List<Object> row = new ArrayList<>();
                        row.add(rs.getInt("id_client"));
                        row.add(rs.getString("sale_date"));
                        row.add(rs.getFloat("total"));
                        row.add(rs.getInt("id_sale"));
                        ordersList.add(row);  
                    }
                    conn.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return ordersList;
    }
    
    public boolean deleteTableSaleTemp(){
        try (Connection conn = dbConnection.connect();
            Statement stm = conn.createStatement()){
            String sql = "DROP TABLE tbSaleTemp";
            stm.executeUpdate(sql);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al eliminar la tabla: " + e.getMessage());
            return false;
        }
    }
    public boolean createTableSaleTemp(){
        try (Connection conn = dbConnection.connect();
            Statement stm = conn.createStatement()){
                        String sql = "CREATE TABLE IF NOT EXISTS \"tbSaleTemp\" (" +
                    "\"id\" INTEGER," +
                    "\"id_client\" INTEGER," +
                    "\"sale_date\" TEXT," +
                    "\"total\" TEXT," +
                    "\"id_sale\" INTEGER," +
                    "PRIMARY KEY(\"id\" AUTOINCREMENT)," +
                    "FOREIGN KEY(\"id_client\") REFERENCES \"tbClient\"(\"id\")," +
                    "FOREIGN KEY(\"id_sale\") REFERENCES \"tbSales\"(\"id\")" +
                    ")";
            stm.executeUpdate(sql);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al eliminar la tabla: " + e.getMessage());
            return false;
        }
    }
    
    public boolean restoreSoldProduct(){
        try (Connection conn = dbConnection.connect();
            Statement stm = conn.createStatement()){
            String sql = "UPDATE tbProduct SET total_sold = 0.00";
            stm.executeUpdate(sql);
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al eliminar la tabla: " + e.getMessage());
            return false;
        }
    }
    
}
