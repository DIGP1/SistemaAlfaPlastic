/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import logic.Datarequest;

/**
 *
 * @author Diego
 */
public class Consolidado extends javax.swing.JPanel {

    /**
     * Creates new form Ventas
     */
    private int idSelectedProvider = 0;
    private int idSelectedProviderDel = 0;
    private int idSelectedProduct = 0;
    private int idSelectedProductDel = 0;
    private final Datarequest dr = new Datarequest();
    private boolean puntoDecimalIngresado = false;
    
    public Consolidado() {
        initComponents();
        
        productTable1.setVisible(false);
        jScrollPane3.setVisible(false);
        loadComboBoxProvider(cbRuta);
        loadComboBoxProvider(cbFiltro);
        loadProviderTable();
        loadProductTable(dr.loadProducts(),productTable);
        btnEditProve.setEnabled(false);
        btnDeleteProve.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);
        System.out.println(this.getPreferredSize());
        
        //Control de la Jtable2 es la tabla de proveedores
            jTable2.addMouseListener(new MouseInputAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditProve.setEnabled(true);
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        txtNameProveedor.setText(model.getValueAt(row, 1).toString());
                        idSelectedProvider = Integer.parseInt(model.getValueAt(row, 0).toString());
                    }
                    
                }else if(e.getClickCount() == 3) {
                      JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditProve.setEnabled(false);
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        idSelectedProviderDel = Integer.parseInt(model.getValueAt(row, 0).toString());
                        System.out.println("ID: "+idSelectedProviderDel);
                        btnDeleteProve.setEnabled(true);
                    }
                }
            }
        });
            
            //Control de la tabla de productos
            productTable.addMouseListener(new MouseInputAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditar.setEnabled(true);
                        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                        txtNombre.setText(model.getValueAt(row, 1).toString());
                        cbRuta.setSelectedItem(model.getValueAt(row, 2));
                        txtPrecioCompra.setText(model.getValueAt(row, 4).toString());
                        idSelectedProduct = Integer.parseInt(model.getValueAt(row, 0).toString());
                    }
                    
                }else if(e.getClickCount() == 3) {
                      JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditar.setEnabled(false);
                        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                        idSelectedProductDel = Integer.parseInt(model.getValueAt(row, 0).toString());
                        btnEliminar.setEnabled(true);
                    }
                }
            }
        });
            
            ((AbstractDocument) txtPrecioCompra.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (esNumeroConDecimal(text)) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (esNumeroConDecimal(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean esNumeroConDecimal(String texto) {
                if (texto.equals(".")) {
                    // Si el texto es un punto, verificar si ya hay un punto decimal presente
                    if (!puntoDecimalIngresado && !txtPrecioCompra.getText().contains(".")) {
                        puntoDecimalIngresado = true;
                        return true;
                    } else {
                        return false;
                    }
                } else if (texto.matches("\\d*")) {
                    // Si el texto es un número, permitirlo y restablecer la bandera del punto decimal
                    puntoDecimalIngresado = false;
                    return true;
                } else {
                    return false;
                }
            }
        });
            
             cbFiltro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Aquí se ejecutará el código cuando cambie el valor del combo box
                    ShowTable2();
                    String[] provider = cbFiltro.getSelectedItem().toString().split("-");
                    loadProductTable(dr.loadProductsSold(Integer.parseInt(provider[0]),jCheckBox1.isSelected()),productTable1);
                }
            });
             jCheckBox1.addItemListener(e -> {
                    ShowTable2();
                    String[] provider = cbFiltro.getSelectedItem().toString().split("-");
                    loadProductTable(dr.loadProductsSold(Integer.parseInt(provider[0]),jCheckBox1.isSelected()),productTable1);
            });
            
    }
    //Carga la informacion en la tabla de proveedores o jTable2
        private void loadProviderTable(){
        List<List<Object>> provider = dr.loadProviders();
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : provider) {
            modelo.addRow(new Object[]{list.get(0),list.get(1),list.get(2),list.get(3)});
        }
        jTable2.setModel(modelo);
    }
        private void loadComboBoxProvider(JComboBox comboBox){
            List<List<Object>> provider = dr.loadProviders();
            comboBox.removeAllItems();
            for (List<Object> list : provider) {
                comboBox.addItem(list.get(0) + "-"+list.get(1));
            }
        }
        
       private void loadProductTable(List<List<Object>> provider, JTable table){
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount(0);
        if(modelo.getColumnCount() == 3){
                for (List<Object> list : provider) {
                    modelo.addRow(new Object[]{list.get(0),list.get(1),list.get(2)});
                }
        }else{
            for (List<Object> list : provider) {
                modelo.addRow(new Object[]{list.get(0),list.get(1),list.get(3)+"-"+dr.loadNameProvider(Integer.parseInt(list.get(3).toString())),list.get(2),list.get(4)});
            }
        }

        table.setModel(modelo);
       }
       private void cleanTXT(){
           txtNombre.setText("");
           txtPrecioCompra.setText("");
       }
       private void ShowTable2(){
          jScrollPane1.setVisible(false);
          productTable.setVisible(false);
          productTable1.setVisible(true);
          jScrollPane3.setVisible(true);
       }
       private void ShowTable1(){
          productTable.setVisible(true);
         jScrollPane1.setVisible(true);
         productTable1.setVisible(false);
         jScrollPane3.setVisible(false);
       }
        
    
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        cbFiltro = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbRuta = new javax.swing.JComboBox<>();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtPrecioCompra = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        productTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtNameProveedor = new javax.swing.JTextField();
        btnIngresoProve = new javax.swing.JButton();
        btnEditProve = new javax.swing.JButton();
        btnDeleteProve = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        cbFiltroProve = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtBusquedaProve = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(750, 560));

        jPanel2.setPreferredSize(new java.awt.Dimension(750, 560));

        productTable.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre del producto", "Proveedor", "Total vendidos", "Precio de compra"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setMinWidth(1);
            productTable.getColumnModel().getColumn(0).setPreferredWidth(1);
            productTable.getColumnModel().getColumn(0).setMaxWidth(5);
            productTable.getColumnModel().getColumn(1).setResizable(false);
            productTable.getColumnModel().getColumn(1).setPreferredWidth(170);
            productTable.getColumnModel().getColumn(2).setResizable(false);
            productTable.getColumnModel().getColumn(2).setPreferredWidth(60);
            productTable.getColumnModel().getColumn(2).setHeaderValue("Proveedor");
            productTable.getColumnModel().getColumn(3).setResizable(false);
            productTable.getColumnModel().getColumn(3).setPreferredWidth(25);
            productTable.getColumnModel().getColumn(4).setResizable(false);
            productTable.getColumnModel().getColumn(4).setPreferredWidth(25);
            productTable.getColumnModel().getColumn(4).setHeaderValue("Precio de compra");
        }

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Informacion del producto");

        jLabel5.setText("Nombre del producto");

        jLabel7.setText("Proveedor");

        cbRuta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jLabel3.setText("Precio de compra");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnAgregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar))
                            .addComponent(jLabel4)
                            .addComponent(cbRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 5, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel1.setText("Filtrar proveedor");

        jCheckBox1.setText("Solo productos vendidos");

        jButton1.setText("Mostrar todos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        productTable1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        productTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre del producto", "Total vendidos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(productTable1);
        if (productTable1.getColumnModel().getColumnCount() > 0) {
            productTable1.getColumnModel().getColumn(0).setMinWidth(1);
            productTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
            productTable1.getColumnModel().getColumn(0).setMaxWidth(5);
            productTable1.getColumnModel().getColumn(1).setResizable(false);
            productTable1.getColumnModel().getColumn(1).setPreferredWidth(170);
            productTable1.getColumnModel().getColumn(2).setResizable(false);
            productTable1.getColumnModel().getColumn(2).setPreferredWidth(25);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(6, 6, 6)
                        .addComponent(cbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jCheckBox1)
                        .addGap(6, 6, 6)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(cbFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jCheckBox1))
                    .addComponent(jButton1))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Productos", jPanel2);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Información del proveedor");

        jLabel17.setText("Nombre del proveedor");

        btnIngresoProve.setText("Ingresar");
        btnIngresoProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresoProveActionPerformed(evt);
            }
        });

        btnEditProve.setText("Editar");
        btnEditProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProveActionPerformed(evt);
            }
        });

        btnDeleteProve.setText("Eliminar");
        btnDeleteProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(23, 23, 23))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(txtNameProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnIngresoProve)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditProve)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteProve)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(27, 27, 27)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNameProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIngresoProve)
                    .addComponent(btnEditProve)
                    .addComponent(btnDeleteProve))
                .addContainerGap(314, Short.MAX_VALUE))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre ", "N° Ordenes realizadas", "Total comprado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(1);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(5);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(250);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(25);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(25);
        }

        jLabel6.setText("Filtrar por:");

        cbFiltroProve.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("Buscar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbFiltroProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtBusquedaProve, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtBusquedaProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cbFiltroProve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proveedores", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 763, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresoProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresoProveActionPerformed
        // TODO add your handling code here:
          if (!"".equals(txtNameProveedor.getText())) {
            dr.registerProvider(txtNameProveedor.getText(), "0", 0.00f);
            loadProviderTable();
            loadComboBoxProvider(cbRuta);
        }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnIngresoProveActionPerformed

    private void btnEditProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProveActionPerformed
        // TODO add your handling code here:
          if (!"".equals(txtNameProveedor.getText())) {
              if (dr.EditProvider(txtNameProveedor.getText(), idSelectedProvider)) {
                  JOptionPane.showMessageDialog(null,"Datos actualizados con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                  btnEditar.setEnabled(false);
                  loadProviderTable();
                  loadComboBoxProvider(cbRuta);
                  
              }else{
                  JOptionPane.showMessageDialog(null,"Error al actualizar los datos", "ERROR", JOptionPane.ERROR_MESSAGE);
              }
        }else{
              JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
          }
    }//GEN-LAST:event_btnEditProveActionPerformed

    private void btnDeleteProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProveActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de realizar esta acción?", "Advertencia", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (dr.DeleteProvider(idSelectedProviderDel)) {
                JOptionPane.showMessageDialog(null,"Proveedor eliminado con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
                loadProviderTable();
                loadComboBoxProvider(cbRuta);
                btnEliminar.setEnabled(false);
            }else{
                JOptionPane.showMessageDialog(null,"Error al eliminar al proveedor", "ERROR", JOptionPane.ERROR_MESSAGE);
                btnEliminar.setEnabled(false);
            }
        }else{
            btnEliminar.setEnabled(false);
        }
    }//GEN-LAST:event_btnDeleteProveActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
          if (!"".equals(txtNombre.getText()) && !"".equals(txtPrecioCompra.getText())) {
            String[] provider = cbRuta.getSelectedItem().toString().split("-");
            dr.registerProduct(txtNombre.getText(), 0.00f,Integer.parseInt(provider[0]) , Float.parseFloat(txtPrecioCompra.getText()));
            loadProductTable(dr.loadProducts(),productTable);
            cleanTXT();
         }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
         }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        if (!"".equals(txtNombre.getText()) && !"".equals(txtPrecioCompra.getText())) {
            String[] provider = cbRuta.getSelectedItem().toString().split("-");
              if (dr.EditProduct(txtNombre.getText(), Integer.parseInt(provider[0]), Float.parseFloat(txtPrecioCompra.getText()), idSelectedProduct)) {
                  JOptionPane.showMessageDialog(null,"Datos actualizados con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                  btnEditar.setEnabled(false);
                  loadProductTable(dr.loadProducts(),productTable);
                  
              }else{
                  JOptionPane.showMessageDialog(null,"Error al actualizar los datos", "ERROR", JOptionPane.ERROR_MESSAGE);
              }
        }else{
              JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
          }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de realizar esta acción?", "Advertencia", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (dr.DeleteProduct(idSelectedProductDel)) {
                JOptionPane.showMessageDialog(null,"Producto eliminado con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
                loadProductTable(dr.loadProducts(),productTable);
                btnEliminar.setEnabled(false);
            }else{
                JOptionPane.showMessageDialog(null,"Error al eliminar el producto", "ERROR", JOptionPane.ERROR_MESSAGE);
                btnEliminar.setEnabled(false);
            }
        }else{
            btnEliminar.setEnabled(false);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ShowTable1();

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnDeleteProve;
    private javax.swing.JButton btnEditProve;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnIngresoProve;
    private javax.swing.JComboBox<String> cbFiltro;
    private javax.swing.JComboBox<String> cbFiltroProve;
    private javax.swing.JComboBox<String> cbRuta;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable productTable;
    private javax.swing.JTable productTable1;
    private javax.swing.JTextField txtBusquedaProve;
    private javax.swing.JTextField txtNameProveedor;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioCompra;
    // End of variables declaration//GEN-END:variables
}
