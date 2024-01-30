/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Diego
 */
public class SearchValue {
    private int id;
    private String name;
    private String address;
    private String route;
    private float total_sold;
   
    
    public SearchValue(int id,String name, String address, String rute, float total_sold){
        this.id = id;
        this.name = name;
        this.address = address;
        this.route = rute;
        this.total_sold = total_sold;
    }
    // Setter para id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para id
    public int getId() {
        return id;
    }
        // Setter para address
    public void setName(String name) {
        this.name = name;
    }

    // Getter para address
    public String getName() {
        return name;
    }

    // Setter para address
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter para address
    public String getAddress() {
        return address;
    }

    // Setter para route
    public void setRoute(String route) {
        this.route = route;
    }

    // Getter para route
    public String getRoute() {
        return route;
    }

    // Setter para totalSold
    public void setTotalSold(float totalSold) {
        this.total_sold = totalSold;
    }

    // Getter para totalSold
    public float getTotalSold() {
        return total_sold;
    }
    
}
