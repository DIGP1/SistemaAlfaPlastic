/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.util.List;

/**
 *
 * @author Diego
 */
public class ListNameCustomer {
     private List<String> nameCustomer;    
     public ListNameCustomer(List<String> nameCustomer){
         this.nameCustomer = nameCustomer;
     }
         public void setNameCustomer(List<String> nameCustomer){
        this.nameCustomer = nameCustomer;
    }
    public List<String> getNameCustomer(){
        return nameCustomer;
    }
}
