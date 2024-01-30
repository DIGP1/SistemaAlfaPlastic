/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

/**
 *
 * @author Diego
 */
public class SearchProduct {
    private int id_product;
    private String name_product;
    private float total_sold;
    
    public SearchProduct(int id, String name, float totalSold){
        this.id_product = id;
        this.name_product = name;
        this.total_sold = totalSold;
    }
    
    public void setIdProduct(int id){
        this.id_product = id;
    }
    public int getIdProduct(){
        return id_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setTotal_sold(float total_sold) {
        this.total_sold = total_sold;
    }

    public float getTotal_sold() {
        return total_sold;
    }
    
    
}
