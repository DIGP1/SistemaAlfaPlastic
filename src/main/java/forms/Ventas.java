/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package forms;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import logic.SearchValue;
import logic.Datarequest;
import logic.SearchProduct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;
import logic.ImprimirFactura;

/**
 *
 * @author Diego
 */
public class Ventas extends javax.swing.JPanel {

    /**
     * Creates new form Ventas
     */
    private SearchValue sv;
    public Datarequest dr = new Datarequest();
    //Datos necesarios de receptar del cliente
    private int idCustomerSelected;
   private List<SearchValue> resultCustomer;
   private float total_sold;
   //Datos necesarios de receptar de los productos
   private int idProductSelected;
   private int idProductSelected1;
   private List<SearchProduct> resultProduct;
   private int rowSelected;
      private int rowSelected1;
   private float total_sold_Product;
   private int id_sale;
   private DefaultTableModel modelTable4;
   private List<List<Object>> orderTempsLoad;
   
   private boolean puntoDecimalIngresado = false;
   
    public Ventas() {
        initComponents();
        

        System.out.println(this.getPreferredSize());
        jList1.setVisible(false);
        jScrollPane1.setVisible(false);
         jList2.setVisible(false);
        jScrollPane2.setVisible(false);
        jScrollPane6.setVisible(false);
        jList4.setVisible(false);
        jPanel10.setVisible(false);
        
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar1.setVisible(false);
        btnEliminar1.setVisible(false);
        
        loadOrders();
        if (!orderTempsLoad.isEmpty()) {
            loadOrder(0);
        }
        
        //Obtengo el valor seleccionado en la lista de busqueda
          jList1.addListSelectionListener(e -> {
            if (!jList1.isSelectionEmpty()) { //Si la lista de busqueda es diferente de vacio se almacena el indice seleccionado
                int valorSeleccionado = jList1.getSelectedIndex();
                lblNombreCliente.setText(resultCustomer.get(valorSeleccionado).getName());//De la lista de busqueda que retorna la base se obtiene el cliente
                lblDireccion.setText(resultCustomer.get(valorSeleccionado).getAddress());//en la posicion seleccionada en la lista de busqueda y se manda a los labels
                idCustomerSelected = resultCustomer.get(valorSeleccionado).getId(); 
                total_sold = resultCustomer.get(valorSeleccionado).getTotalSold();//Se almacena el id del cliente seleccionado y el total comprado para uso posterior
                jList1.setVisible(false);
                jScrollPane1.setVisible(false);//Una vez seleccionado el cliente se oculta la lista
            }
        });
          
          //Obtengo el valor seleccionado en la lista de busqueda (Aplica la misma logica que el anterior solo que para la lista de productos)
          jList2.addListSelectionListener(e -> {
            if (!jList2.isSelectionEmpty()) { 
                int valorSeleccionado = jList2.getSelectedIndex();
                idProductSelected  = resultProduct.get(valorSeleccionado).getIdProduct();
                lblProducto.setText(resultProduct.get(valorSeleccionado).getName_product());
                total_sold_Product = resultProduct.get(valorSeleccionado).getTotal_sold();
                jList2.setVisible(false);
                jScrollPane2.setVisible(false);
            }
        });
                    //Obtengo el valor seleccionado en la lista de busqueda (Aplica la misma logica que el anterior solo que para la lista de productos)
          jList4.addListSelectionListener(e -> {
            if (!jList4.isSelectionEmpty()) { 
                int valorSeleccionado = jList4.getSelectedIndex();
                idProductSelected1  = resultProduct.get(valorSeleccionado).getIdProduct();
                lblProducto1.setText(resultProduct.get(valorSeleccionado).getName_product());
                total_sold_Product = resultProduct.get(valorSeleccionado).getTotal_sold();
                jList4.setVisible(false);
                jScrollPane6.setVisible(false);
            }
        });
          
  //Todo esto es como el metodo keypress de c# o textChange
  txtBusquedaCliente.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    private void buscarNombreCuenta() {
        buscarEnBase(txtBusquedaCliente.getText());//Se almacena la informacion que tiene el textfield en ese momento y se manda a buscar en la base
    }
    private void buscarEnBase(String Dato){
        
        if("".equals(Dato)){//Si el dato esta vacio no se muestra la lista, esto es mas que todo para cuando se elimine la informacion del textfield
            jList1.setVisible(false);
            jScrollPane1.setVisible(false);
        }else{
            String nameCustomer = Dato.trim();//Elimina los espacios o simbolos especiales
            resultCustomer = dr.SearchCustomer(nameCustomer, jComboBox1.getSelectedItem().toString());//Se iguala una variable tipo resultCustomer con la informacion que lleva de la base 
            DefaultListModel<String> modeloLista = new DefaultListModel<>();//Se crea una lista para el jList
            for (SearchValue resultado : resultCustomer) {//Se recorre resultCustomer mediante un foreach para poder almacenar los nombres de los clientes
                modeloLista.addElement(resultado.getName());//encontrados en la base y mostrarselos al usuario
            }

            if (!resultCustomer.isEmpty()) {//Se detecta si resultCustomer esta vacio en el caso de que no
                jList1.setModel(modeloLista);//Se manda el la lista de nombres a jList y se hace visible al usuario
                jList1.setVisible(true);
                jScrollPane1.setVisible(true);
            } else {
                //des.setText(""); 
                //En el caso de que no este solo se oculta
                jList1.setVisible(false);
                jScrollPane1.setVisible(false);
            }
        }
    }
});
   //Todo esto es como el metodo keypress de c# o textChange
  txtProducto.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    private void buscarNombreCuenta() {
        buscarEnBase(txtProducto.getText());
    }
    private void buscarEnBase(String Dato){
        
        if("".equals(Dato)){
            jList2.setVisible(false);
            jScrollPane2.setVisible(false);
        }else{
            String nameProduct = Dato.trim();
            resultProduct = dr.SearchProduct(nameProduct);
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (SearchProduct resultado : resultProduct) {
                modeloLista.addElement(resultado.getName_product());
            }

            if (!resultProduct.isEmpty()) {
                jList2.setModel(modeloLista);
                jList2.setVisible(true);
                jScrollPane2.setVisible(true);
            } else {
                jList2.setVisible(false);
                jScrollPane2.setVisible(false);
            }
        }
    }
});
     //Todo esto es como el metodo keypress de c# o textChange
  txtProducto2.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        buscarNombreCuenta();
    }

    private void buscarNombreCuenta() {
        buscarEnBase(txtProducto2.getText());
    }
    private void buscarEnBase(String Dato){
        
        if("".equals(Dato)){
            jList4.setVisible(false);
            jScrollPane6.setVisible(false);
        }else{
            String nameProduct = Dato.trim();
            resultProduct = dr.SearchProduct(nameProduct);
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (SearchProduct resultado : resultProduct) {
                modeloLista.addElement(resultado.getName_product());
            }

            if (!resultProduct.isEmpty()) {
                jList4.setModel(modeloLista);
                jList4.setVisible(true);
                jScrollPane6.setVisible(true);
            } else {
                jList4.setVisible(false);
                jScrollPane6.setVisible(false);
            }
        }
    }
});
  // Agregar DocumentFilter al Document del JTextField
         ((AbstractDocument) txtCantidad.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (esNumeroConDecimal(text)) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (esNumeroConDecimal(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean esNumeroConDecimal(String texto) {
                if (texto.equals(".")) {
                    // Si el texto es un punto, verificar si ya hay un punto decimal presente
                    if (!puntoDecimalIngresado && !txtCantidad.getText().contains(".")) {
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
         ((AbstractDocument) txtPrecioUni.getDocument()).setDocumentFilter(new DocumentFilter() {
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
                    if (!puntoDecimalIngresado && !txtPrecioUni.getText().contains(".")) {
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
         ((AbstractDocument) txtCantidad2.getDocument()).setDocumentFilter(new DocumentFilter() {
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
                    if (!puntoDecimalIngresado && !txtCantidad2.getText().contains(".")) {
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
         ((AbstractDocument) txtPrecioUni2.getDocument()).setDocumentFilter(new DocumentFilter() {
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
                    if (!puntoDecimalIngresado && !txtPrecioUni2.getText().contains(".")) {
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
         
       //Realiza la operacion de Cantidad x Precio unitario mientras el usuario esta escribiendo en txtPrecioUni
      txtPrecioUni.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        realizarOperacion();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        realizarOperacion();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        realizarOperacion();
    }});
      txtCantidad.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        realizarOperacion();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        realizarOperacion();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        realizarOperacion();
    }});
    txtPrecioUni2.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        realizarOperacion1();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        realizarOperacion1();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        realizarOperacion1();
    }});
      txtCantidad2.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
        realizarOperacion1();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        realizarOperacion1();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        realizarOperacion1();
    }});
      //Controla el evento click de la tabla
       jTable2.addMouseListener(new MouseInputAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditar.setVisible(true);
                        btnAgregarCarrito.setEnabled(false);
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        idProductSelected = Integer.parseInt(model.getValueAt(row, 0).toString());
                        lblProducto.setText(model.getValueAt(row, 1).toString());
                        txtCantidad.setText(model.getValueAt(row, 2).toString());
                        txtPrecioUni.setText(model.getValueAt(row, 3).toString());
                        rowSelected = row;
                        
                    }
                    
                }else if(e.getClickCount() == 3) {
                      JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        
                        btnEditar.setVisible(false);
                        btnEliminar.setVisible(true);
                        rowSelected = row;
                    }
                }else{
                    btnAgregarCarrito.setEnabled(true);
                }
            }
        });
             //Controla el evento click de la tabla
       jTable4.addMouseListener(new MouseInputAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        btnEditar1.setVisible(true);
                        btnAgregarCarrito1.setEnabled(false);
                        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
                         idProductSelected1 = Integer.parseInt(model.getValueAt(row, 0).toString());
                        lblProducto1.setText(model.getValueAt(row, 1).toString());
                        txtCantidad2.setText(model.getValueAt(row, 2).toString());
                        txtPrecioUni2.setText(model.getValueAt(row, 3).toString());
                        rowSelected1 = row;
                        
                    }
                    
                }else if(e.getClickCount() == 3) {
                      JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                    
                    if(row >= 0){
                        
                        btnEditar1.setVisible(false);
                        btnEliminar1.setVisible(true);
                        rowSelected1 = row;
                    }
                }else{
                    btnAgregarCarrito1.setEnabled(true);
                }
            }
        });
    }
    
   private void loadOrders(){
       orderTempsLoad = dr.loadOrdersTemp();
       if(!orderTempsLoad.isEmpty()){
            lblNumberOrder.setText(String.valueOf(orderTempsLoad.size()));
            lblMax.setText(String.valueOf(orderTempsLoad.size()));
            float totalSold = 0.00f;
            for (List<Object> order : orderTempsLoad) {
            totalSold = totalSold + Float.parseFloat(order.get(2).toString());
            }
            lblTotalSold.setText(String.valueOf(totalSold));
            lblActual.setText("1");
       }
   }
   
   private void loadOrder(int nOrder){
       if(!orderTempsLoad.isEmpty() && nOrder >= 0){
            SearchValue svc = dr.SearchCustomer(Integer.parseInt(orderTempsLoad.get(nOrder).get(0).toString()));
            lblNombreCliente6.setText(svc.getName());
            lblDireccion6.setText(svc.getAddress());
            loadSaleDetailTable2(Integer.parseInt(orderTempsLoad.get(nOrder).get(3).toString()));
            lblTotalVenta3.setText(orderTempsLoad.get(nOrder).get(2).toString());
       }
   }
    
 private void realizarOperacion() {
        try {
            // Obtener el número actual del JTextField
            String texto = txtPrecioUni.getText();
            String texto2 = txtCantidad.getText();
            if (!texto.isEmpty() || !texto2.isEmpty()) {
                float numero = Float.parseFloat(texto);

                // Multiplicar por el valor de txtCantidad
                float resultado = Float.parseFloat(txtCantidad.getText()) * numero;

                // Mostrar el resultado en el label
                lblSubTotalProduct.setText(String.valueOf(resultado));
            }
        } catch (NumberFormatException ex) {
            // Manejar la excepción si el texto no es un número válido
            System.out.println("Ingrese un número válido");
        }
    }
 private void realizarOperacion1() {
        try {
            // Obtener el número actual del JTextField
            String texto = txtPrecioUni2.getText();
            String texto2 = txtCantidad2.getText();
            if (!texto.isEmpty() || !texto2.isEmpty()) {
                float numero = Float.parseFloat(texto);

                // Multiplicar por el valor de txtCantidad
                float resultado = Float.parseFloat(txtCantidad2.getText()) * numero;

                // Mostrar el resultado en el label
                lblSubTotalProduct2.setText(String.valueOf(resultado));
            }
        } catch (NumberFormatException ex) {
            // Manejar la excepción si el texto no es un número válido
            System.out.println("Ingrese un número válido");
        }
    }
 
 private void clientTextClean(){
     txtBusquedaCliente.setText("");
     lblNombreCliente.setText("");
     lblDireccion.setText("");
     txtNumeroPedido.setText("");
     lblNombreCliente2.setText("");
     lblDireccion2.setText("");
 }
 private void productCleanText(){
     txtProducto.setText("");
     lblProducto.setText("Aun no seleccionado");
     txtCantidad.setText("");
     txtPrecioUni.setText("");
     lblSubTotalProduct.setText("0.00");
     
     txtProducto2.setText("");
     lblProducto1.setText("Aun no seleccionado");
     txtCantidad2.setText("");
     txtPrecioUni2.setText("");
     lblSubTotalProduct2.setText("0.00");
 }
    private void loadSaleDetailTable(int id_sale){
        List<List<Object>> saleDetail = dr.loadDetailSale(id_sale);
        DefaultTableModel modelo = (DefaultTableModel) jTable4.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : saleDetail) {
            modelo.addRow(new Object[]{list.get(0),dr.SearchNameProduct(Integer.parseInt(list.get(0).toString())),list.get(1),list.get(2),list.get(3)});
        }
        modelTable4  = new DefaultTableModel();
        // Configurar las columnas en la copia del modelo
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            modelTable4.addColumn(modelo.getColumnName(i));
        }

        // Agregar filas a la copia del modelo
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Vector<Object> row = new Vector<>();
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                row.add(modelo.getValueAt(i, j));
            }
            modelTable4.addRow(row);
        }
        jTable4.setModel(modelo);
    }
    private void loadSaleDetailTable2(int id_sale){
        List<List<Object>> saleDetail = dr.loadDetailSale(id_sale);
        DefaultTableModel modelo = (DefaultTableModel) jTable5.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : saleDetail) {
            modelo.addRow(new Object[]{list.get(0),dr.SearchNameProduct(Integer.parseInt(list.get(0).toString())),list.get(1),list.get(2),list.get(3)});
        }
        jTable5.setModel(modelo);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblNombreCliente = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPrecioUni = new javax.swing.JTextField();
        btnAgregarCarrito = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        lblSubTotalProduct = new javax.swing.JLabel();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        lblProducto = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtBusquedaCliente = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblTotalVenta = new javax.swing.JLabel();
        btnGuardarVenta = new javax.swing.JButton();
        btnCancelarVenta = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        lblTotalVenta3 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        lblNombreCliente6 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblDireccion6 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblActual = new javax.swing.JLabel();
        lblMax = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnPrintCurrent = new javax.swing.JButton();
        btnPrintAll = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        lblTotalSold = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblNumberOrder = new javax.swing.JLabel();
        btnRestablecer = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtDay = new javax.swing.JTextField();
        txtMonth = new javax.swing.JTextField();
        txtYear = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroPedido = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblNombreCliente2 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblDireccion2 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtProducto2 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtCantidad2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtPrecioUni2 = new javax.swing.JTextField();
        btnAgregarCarrito1 = new javax.swing.JButton();
        btnCancelar2 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        lblSubTotalProduct2 = new javax.swing.JLabel();
        btnEditar1 = new javax.swing.JButton();
        btnEliminar1 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        lblProducto1 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblTotalVenta2 = new javax.swing.JLabel();
        btnGuardarVenta2 = new javax.swing.JButton();
        btnCancelarVenta2 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblError = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(750, 560));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(64, 146));

        jScrollPane1.setViewportView(jList1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 22, 160, 110));

        jScrollPane2.setViewportView(jList2);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 191, 183, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del cliente"));

        jLabel2.setText("Nombre:");

        lblNombreCliente.setText("Aun sin seleccionar");

        jLabel5.setText("Dirección:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombreCliente)
                .addGap(144, 144, 144)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDireccion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblNombreCliente)
                    .addComponent(jLabel5)
                    .addComponent(lblDireccion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 37, 750, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Añadir producto"));

        jLabel7.setText("Buscar producto");

        jLabel8.setText("Cantidad");

        jLabel10.setText("Precio unitario");

        btnAgregarCarrito.setText("Agregar");
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("SubTotal: $");

        lblSubTotalProduct.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSubTotalProduct.setText("0.00");

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

        jLabel4.setText("Producto:");

        lblProducto.setText("Aun no seleccionado");

        jButton3.setText("Nuevo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtPrecioUni))))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(btnAgregarCarrito)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnCancelar))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSubTotalProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblProducto)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProducto)
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(txtPrecioUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lblSubTotalProduct))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCarrito)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, 290));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(250);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTable2.getColumnModel().getColumn(4).setResizable(false);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(10);
        }

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 532, 230));

        jLabel9.setText("Detalles de la venta");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, -1, -1));

        jLabel3.setText("Buscar cliente");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 3, -1, -1));
        jPanel2.add(txtBusquedaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 0, 160, -1));

        jButton1.setText("No encuentras al cliente? Añadelo!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Total: $");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, -1, -1));

        lblTotalVenta.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalVenta.setText("0.00");
        jPanel2.add(lblTotalVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 380, 76, -1));

        btnGuardarVenta.setText("Guardar venta");
        btnGuardarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarVentaActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 481, -1, -1));

        btnCancelarVenta.setText("Cancelar venta");
        jPanel2.add(btnCancelarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(611, 481, -1, -1));

        jLabel20.setText("Ruta:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "US-SEBT", "US-SJNC", "US-OCTSA", "US-JPSN" }));
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, -1, -1));

        jTabbedPane1.addTab("Registrar venta", jPanel2);

        jLabel38.setText("Detalles de la venta");

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable5);
        if (jTable5.getColumnModel().getColumnCount() > 0) {
            jTable5.getColumnModel().getColumn(0).setResizable(false);
            jTable5.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable5.getColumnModel().getColumn(1).setResizable(false);
            jTable5.getColumnModel().getColumn(1).setPreferredWidth(250);
            jTable5.getColumnModel().getColumn(2).setResizable(false);
            jTable5.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTable5.getColumnModel().getColumn(3).setResizable(false);
            jTable5.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTable5.getColumnModel().getColumn(4).setResizable(false);
            jTable5.getColumnModel().getColumn(4).setPreferredWidth(10);
        }

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel39.setText("Total: $");

        lblTotalVenta3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalVenta3.setText("0.00");

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del cliente"));

        jLabel40.setText("Nombre:");

        jLabel41.setText("Dirección:");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombreCliente6)
                .addGap(144, 144, 144)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDireccion6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(lblNombreCliente6)
                    .addComponent(jLabel41)
                    .addComponent(lblDireccion6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(6, 6, 6)
                        .addComponent(lblTotalVenta3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(lblTotalVenta3))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        btnBack.setText("<<");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        lblActual.setText("0");

        lblMax.setText("0");

        jLabel6.setText("de");

        jLabel13.setText("Imprimir factura actual");

        btnPrintCurrent.setText("Imprimir");
        btnPrintCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintCurrentActionPerformed(evt);
            }
        });

        btnPrintAll.setText("Imprimir todo");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Venta total: $");

        lblTotalSold.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalSold.setText("0.00");

        jLabel16.setText("Numero de pedidos: ");

        lblNumberOrder.setText("0");

        btnRestablecer.setText("Reiniciar ventas");
        btnRestablecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestablecerActionPerformed(evt);
            }
        });

        jLabel15.setText("Fecha de entrega:");

        txtMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMonthActionPerformed(evt);
            }
        });

        jLabel17.setText("Dia");

        lblMes.setText("Mes");

        jLabel18.setText("Año");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnRestablecer)
                        .addGap(178, 178, 178)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblActual)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrintCurrent)
                        .addGap(54, 54, 54))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnPrintAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNumberOrder)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalSold)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtDay, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(24, 24, 24)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(lblMes)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel18)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblMes)
                    .addComponent(jLabel18))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrintAll)
                    .addComponent(jLabel14)
                    .addComponent(lblTotalSold)
                    .addComponent(jLabel16)
                    .addComponent(lblNumberOrder)
                    .addComponent(jLabel15)
                    .addComponent(txtDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnNext)
                    .addComponent(lblActual)
                    .addComponent(lblMax)
                    .addComponent(jLabel6)
                    .addComponent(jLabel13)
                    .addComponent(btnPrintCurrent)
                    .addComponent(btnRestablecer))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mostrar ventas realizadas", jPanel4);

        jLabel1.setText("Introduce el numero del pedido");

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setViewportView(jList4);

        jPanel10.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 186, 183, -1));

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del cliente"));

        jLabel23.setText("Nombre:");

        lblNombreCliente2.setText("Aun sin seleccionar");

        jLabel24.setText("Dirección:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombreCliente2)
                .addGap(144, 144, 144)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDireccion2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(lblNombreCliente2)
                    .addComponent(jLabel24)
                    .addComponent(lblDireccion2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 37, 750, -1));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Añadir producto"));

        jLabel25.setText("Buscar producto");

        jLabel26.setText("Cantidad");

        jLabel27.setText("Precio unitario");

        btnAgregarCarrito1.setText("Agregar");
        btnAgregarCarrito1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarrito1ActionPerformed(evt);
            }
        });

        btnCancelar2.setText("Cancelar");
        btnCancelar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar2ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("SubTotal: $");

        lblSubTotalProduct2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSubTotalProduct2.setText("0.00");

        btnEditar1.setText("Editar");
        btnEditar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditar1ActionPerformed(evt);
            }
        });

        btnEliminar1.setText("Eliminar");
        btnEliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar1ActionPerformed(evt);
            }
        });

        jLabel29.setText("Producto:");

        lblProducto1.setText("Aun no seleccionado");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtProducto2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(txtCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(txtPrecioUni2))))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(btnEditar1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(btnAgregarCarrito1)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnCancelar2))
                                    .addGroup(jPanel12Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSubTotalProduct2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel25))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(lblProducto1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProducto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProducto1)
                .addGap(23, 23, 23)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(txtPrecioUni2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(lblSubTotalProduct2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarCarrito1)
                    .addComponent(btnCancelar2))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditar1)
                    .addComponent(btnEliminar1))
                .addGap(18, 18, 18))
        );

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, -1, 290));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            jTable4.getColumnModel().getColumn(0).setPreferredWidth(1);
            jTable4.getColumnModel().getColumn(1).setResizable(false);
            jTable4.getColumnModel().getColumn(1).setPreferredWidth(250);
            jTable4.getColumnModel().getColumn(2).setResizable(false);
            jTable4.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTable4.getColumnModel().getColumn(3).setResizable(false);
            jTable4.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTable4.getColumnModel().getColumn(4).setResizable(false);
            jTable4.getColumnModel().getColumn(4).setPreferredWidth(10);
        }

        jPanel10.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 532, 230));

        jLabel30.setText("Detalles de la venta");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, -1, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel32.setText("Total: $");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, -1, -1));

        lblTotalVenta2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalVenta2.setText("0.00");
        jPanel10.add(lblTotalVenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 380, 76, -1));

        btnGuardarVenta2.setText("Modificar venta");
        btnGuardarVenta2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarVenta2ActionPerformed(evt);
            }
        });
        jPanel10.add(btnGuardarVenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 430, -1, -1));

        btnCancelarVenta2.setText("Cancelar");
        btnCancelarVenta2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarVenta2ActionPerformed(evt);
            }
        });
        jPanel10.add(btnCancelarVenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, -1, -1));

        jButton2.setText("Eliminar venta");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 430, 110, -1));

        lblError.setForeground(new java.awt.Color(255, 0, 0));
        jPanel10.add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, -1));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jCheckBox1.setText("En tabla temporal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumeroPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Modificar venta", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVentaActionPerformed
        // TODO add your handling code here:
        //Se toma el modelo de la tabla donde se almacenan los productos
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        //Creacion de variables que almacenaran informacion de los productos para su posterior uso
        int id_product;
        float product_quantity;
        float unit_price;
        float sub_total;
        //Primero se verifica si el modelo tiene filas en el dado caso que no tenga no se deja guardar
        if(model.getRowCount() == 0 ){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta ya que ningun producto ha sido seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        //Pasa lo mismo en el dado caso aun no se haya seleccionado un cliente
        }else if("Aun sin seleccionar".equals(lblNombreCliente.getText()) && "".equals(lblDireccion.getText())){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta, cliente no seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            //Una vez validado que si haya productos en la tabla y un cliente seleccionado se obtiene la fecha del ordenador
            LocalDate fechaActual = LocalDate.now();
           DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaActual.format(formato);
            //Se almacena el total que se mostraba en el label lblTotalVenta en la variable total
            float total = Float.parseFloat(lblTotalVenta.getText());
            //Se pone un if para saber si al agregar la venta esta no da problemas
            if(dr.addSale(idCustomerSelected, fechaFormateada, total)){
                //En el caso de que no hayan problemas se almacena el ultimo id de la tabla de ventas
                int lastID = dr.loadLastIDSale();
                //Luego de ello se utiliza un for para recorrer los row del modelo
                for (int i = 0; i < model.getRowCount(); i++) {
                    //Se toman los valores del modelo
                    id_product =  Integer.parseInt(model.getValueAt(i, 0).toString());
                    product_quantity =  Float.parseFloat(model.getValueAt(i, 2).toString());
                    unit_price = Float.parseFloat(model.getValueAt(i, 3).toString());
                    sub_total = Float.parseFloat(model.getValueAt(i, 4).toString());
                    //Una vez ya almacenado en las variables los datos del producto se guardan el la base de datos
                    dr.addDetailSale(lastID, id_product, product_quantity, unit_price, sub_total);
                    
                                        
                    float productSold = dr.loadProductSold(id_product);
                    productSold = product_quantity + productSold;
                    
                    dr.EditProductSold(id_product, productSold);
                }//este proceso se repite con todos los productos de la tabla
                //Una vez finalizado que se haya guardado todos los productos se manda a guardar la venta en la tabla temporal
                dr.addSaleTemp(idCustomerSelected, fechaFormateada, total, lastID);
                
                
                loadOrders();
                if (!orderTempsLoad.isEmpty()) {
                    loadOrder(0);
                }
                
                clientTextClean();
                productCleanText();
                model.setRowCount(0);
                jTable2.setModel(model);
                lblTotalVenta.setText("0.00");
            }
        }
    }//GEN-LAST:event_btnGuardarVentaActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
          if(!"Aun no seleccionado".equals(lblProducto.getText()) && !"".equals(txtCantidad.getText()) && !"".equals(txtPrecioUni.getText()) ){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
            model.setValueAt(idProductSelected,rowSelected,0);
            model.setValueAt(lblProducto.getText(), rowSelected, 1);
            model.setValueAt(txtCantidad.getText(), rowSelected, 2);
            model.setValueAt(txtPrecioUni.getText(), rowSelected, 3);
            model.setValueAt(lblSubTotalProduct.getText(), rowSelected, 4);
            for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta.setText(String.valueOf(total));
            productCleanText();
            
            jTable2.setModel(model);
        }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos y seleccionar un producto!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        // TODO add your handling code here:
        if(!"Aun no seleccionado".equals(lblProducto.getText()) && !"".equals(txtCantidad.getText()) && !"".equals(txtPrecioUni.getText()) ){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
            model.addRow(new Object[]{idProductSelected, lblProducto.getText(),txtCantidad.getText(),txtPrecioUni.getText(),lblSubTotalProduct.getText()});
            
            for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta.setText(String.valueOf(total));
            productCleanText();
            
            jTable2.setModel(model);
        }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos y seleccionar un producto!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        productCleanText();
        btnAgregarCarrito.setEnabled(true);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        float total = 0;
         DefaultTableModel model = (DefaultTableModel)jTable2.getModel();
         model.removeRow(rowSelected);
         for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta.setText(String.valueOf(total));
         jTable2.setModel(model);
         productCleanText();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnAgregarCarrito1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarrito1ActionPerformed
        // TODO add your handling code here:
          if(!"Aun no seleccionado".equals(lblProducto1.getText()) && !"".equals(txtCantidad2.getText()) && !"".equals(txtPrecioUni2.getText()) ){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)jTable4.getModel();
            model.addRow(new Object[]{idProductSelected1, lblProducto1.getText(),txtCantidad2.getText(),txtPrecioUni2.getText(),lblSubTotalProduct2.getText()});
            
            for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta2.setText(String.valueOf(total));
            productCleanText();
            
            jTable4.setModel(model);
        }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos y seleccionar un producto!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarCarrito1ActionPerformed

    private void btnCancelar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar2ActionPerformed
        // TODO add your handling code here:
       productCleanText();
        btnAgregarCarrito1.setEnabled(true);
        btnEditar1.setVisible(false);
        btnEliminar1.setVisible(false);
    }//GEN-LAST:event_btnCancelar2ActionPerformed

    private void btnEditar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditar1ActionPerformed
        // TODO add your handling code here:
          if(!"Aun no seleccionado".equals(lblProducto1.getText()) && !"".equals(txtCantidad2.getText()) && !"".equals(txtPrecioUni2.getText()) ){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)jTable4.getModel();
            model.setValueAt(idProductSelected1,rowSelected1,0);
            model.setValueAt(lblProducto1.getText(), rowSelected1, 1);
            model.setValueAt(txtCantidad2.getText(), rowSelected1, 2);
            model.setValueAt(txtPrecioUni2.getText(), rowSelected1, 3);
            model.setValueAt(lblSubTotalProduct2.getText(), rowSelected1, 4);
            for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta2.setText(String.valueOf(total));
            productCleanText();
            
            jTable4.setModel(model);
        }else{
            JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos y seleccionar un producto!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditar1ActionPerformed

    private void btnEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar1ActionPerformed
        // TODO add your handling code here:
         float total = 0;
         DefaultTableModel model = (DefaultTableModel)jTable4.getModel();
         model.removeRow(rowSelected1);
         for (int i = 0; i < model.getRowCount(); i++) {
                total = total + Float.parseFloat(model.getValueAt(i, 4).toString());
            }
            lblTotalVenta2.setText(String.valueOf(total));
         jTable4.setModel(model);
         productCleanText();
    }//GEN-LAST:event_btnEliminar1ActionPerformed

    private void btnGuardarVenta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarVenta2ActionPerformed
        // TODO add your handling code here:
                //Se toma el modelo de la tabla donde se almacenan los productos
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        //Creacion de variables que almacenaran informacion de los productos para su posterior uso
        int id_product;
        float product_quantity;
        float unit_price;
        float sub_total;
        //Primero se verifica si el modelo tiene filas en el dado caso que no tenga no se deja guardar
        if(model.getRowCount() == 0 ){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta ya que ningun producto ha sido seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        //Pasa lo mismo en el dado caso aun no se haya seleccionado un cliente
        }else if("Aun sin seleccionar".equals(lblNombreCliente2.getText()) && "".equals(lblDireccion2.getText())){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta, cliente no seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            //Una vez validado que si haya productos en la tabla y un cliente seleccionado se obtiene la fecha del ordenador
            LocalDate fechaActual = LocalDate.now();
           DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaActual.format(formato);
            //Se almacena el total que se mostraba en el label lblTotalVenta en la variable total
            float total = Float.parseFloat(lblTotalVenta2.getText());
            //Se pone un if para saber si al agregar la venta esta no da problemas
            if(dr.modSale(id_sale, fechaFormateada, total)){
                if(dr.modSaleTemp(id_sale, fechaFormateada, total)){
                //Luego de ello se utiliza un for para recorrer los row del modelo
                for(int i = 0; i < modelTable4.getRowCount(); i++){
                    float productSold = dr.loadProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()));
                    System.out.println(modelTable4.getValueAt(i, 1).toString()+" "+modelTable4.getValueAt(i, 0).toString() );
                    if(productSold > 0.0f && dr.boolSearchSaleTemp(id_sale)){
                        System.out.println("Producto" +modelTable4.getValueAt(i, 0).toString()+ " vendidos: "+productSold);
                        productSold = productSold- Float.parseFloat(modelTable4.getValueAt(i, 2).toString());
                        System.out.println("Valor de la tabla: "+ modelTable4.getValueAt(i, 2).toString());
                        dr.EditProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()), productSold);
                        System.out.println("Lusgo de la resta queda: "+productSold);
                    }
                }
                dr.DeleteDetailSale(id_sale);
                for (int i = 0; i < model.getRowCount(); i++) {
                    //Se toman los valores del modelo
                    id_product =  Integer.parseInt(model.getValueAt(i, 0).toString());
                    product_quantity =  Float.parseFloat(model.getValueAt(i, 2).toString());
                    unit_price = Float.parseFloat(model.getValueAt(i, 3).toString());
                    sub_total = Float.parseFloat(model.getValueAt(i, 4).toString());
                    //Una vez ya almacenado en las variables los datos del producto se guardan el la base de datos
                    System.out.println("Producto "+model.getValueAt(i, 1).toString()+" en tabla modificada: "+ model.getValueAt(i, 2).toString());
                    dr.addDetailSale(id_sale, id_product, product_quantity, unit_price, sub_total);
                    
                                        
                    float productSold = dr.loadProductSold(id_product);
                    productSold = product_quantity + productSold;
                    
                    dr.EditProductSold(id_product, productSold);
                    
                    loadOrders();
                    if (!orderTempsLoad.isEmpty()) {
                        loadOrder(0);
                    }
                 }
                
             }//este proceso se repite con todos los productos de la tabla
                
                clientTextClean();
                productCleanText();
                model.setRowCount(0);
                jTable4.setModel(model);
                lblTotalVenta2.setText("0.00");
                jPanel10.setVisible(false);
                txtNumeroPedido.setText("");
                JOptionPane.showMessageDialog(null,"Venta modificada con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuardarVenta2ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        try {
            lblError.setText("");
            List<Object> sale =  dr.searchSale(Integer.parseInt(txtNumeroPedido.getText()),jCheckBox1.isSelected());
            if(!sale.isEmpty()){
            jPanel10.setVisible(true);
            id_sale = Integer.parseInt(sale.get(0).toString());
            SearchValue svc = dr.SearchCustomer(Integer.parseInt(sale.get(1).toString()));
            lblNombreCliente2.setText(svc.getName());
            lblDireccion2.setText(svc.getAddress());
            loadSaleDetailTable(Integer.parseInt(sale.get(0).toString()));
            lblTotalVenta2.setText(sale.get(2).toString());
       }
        } catch (NumberFormatException e) {
            lblError.setText("Pedido no encontrado!!" + e);
        }


    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
      for(int i = 0; i < modelTable4.getRowCount(); i++){
        float productSold = dr.loadProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()));
                if(productSold > 0.0f && dr.boolSearchSaleTemp(id_sale)){
                    productSold = productSold- Float.parseFloat(modelTable4.getValueAt(i, 2).toString());
                    dr.EditProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()), productSold);
                    
                    loadOrders();
                    if (!orderTempsLoad.isEmpty()) {
                        loadOrder(0);
                    }
                }
        }
      dr.DeleteDetailSale(id_sale);
      dr.DeleteSaleTemp(id_sale);
      dr.DeleteSale(id_sale);
       clientTextClean();
        productCleanText();
        model.setRowCount(0);
        jTable4.setModel(model);
        lblTotalVenta2.setText("0.00");
        jPanel10.setVisible(false);
        txtNumeroPedido.setText("");
        JOptionPane.showMessageDialog(null,"Venta eliminada con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnCancelarVenta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarVenta2ActionPerformed
        // TODO add your handling code here:
        jPanel10.setVisible(false);
        txtNumeroPedido.setText("");
    }//GEN-LAST:event_btnCancelarVenta2ActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        int currentPag = Integer.parseInt(lblActual.getText());
        int maxPag = Integer.parseInt(lblMax.getText());
        currentPag = currentPag +1;
        if (currentPag > maxPag) {
            JOptionPane.showMessageDialog(null,"No se puede avanzar más!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            loadOrder(currentPag -1);
            lblActual.setText(String.valueOf(currentPag));
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        int currentPag = Integer.parseInt(lblActual.getText());
        currentPag = currentPag -1;
        if (currentPag == 0) {
            JOptionPane.showMessageDialog(null,"No se puede retroceder más!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            loadOrder(currentPag -1);
            lblActual.setText(String.valueOf(currentPag));
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnPrintCurrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintCurrentActionPerformed
        // TODO add your handling code here:
        if(!"".equals(txtDay.getText()) &&  !"".equals(txtMonth.getText()) && !"".equals(txtYear.getText())){
            int order = Integer.parseInt(lblActual.getText());
            int cupo = 18;
            SearchValue  svc = dr.SearchCustomer(Integer.parseInt(orderTempsLoad.get(order-1).get(0).toString()));
            List<String> list = new ArrayList<>();
            List<List<String>> productos = new ArrayList<>();
        
            list.add("                "+svc.getName());
            list.add("                           "+svc.getAddress());
            list.add("");
            list.add("");
            list.add("");
        
            DefaultTableModel model = (DefaultTableModel)jTable5.getModel();
        
            for (int i = 0; i <model.getRowCount(); i++) {
                List<String>productDetail = new ArrayList<>();
                String productName = model.getValueAt(i,1).toString();
                String pu = model.getValueAt(i, 3).toString();
                String pt = model.getValueAt(i, 4).toString();

                productDetail.add(model.getValueAt(i, 2).toString());
                productDetail.add(productName);
                productDetail.add("$"+pu);
                productDetail.add("$"+pt);
            
                productos.add(productDetail);
                list.add("");
            }
            if(model.getRowCount() < 18 ){
                cupo = cupo - model.getRowCount();
                for (int i = 0; i < cupo; i++) {
                    List<String>productDetail = new ArrayList<>();
                    productDetail.add("");
                    productDetail.add("");
                    productDetail.add("");
                    productDetail.add("");
                    productos.add(productDetail);
                    list.add("");
                }
            }
            String total = "$"+orderTempsLoad.get(order-1).get(2).toString();
            System.out.println("");
            list.add(total);
            String nOrderString = "N°: "+orderTempsLoad.get(order-1).get(3).toString();
            System.out.println(nOrderString);
            ImprimirFactura printOrder = new ImprimirFactura(list,productos,nOrderString,txtDay.getText(),txtMonth.getText(),txtYear.getText(),lblActual.getText());
        
            printOrder.imprimir();
        }else{
            JOptionPane.showMessageDialog(null,"Debe de ingresar la fecha de entrega!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    }//GEN-LAST:event_btnPrintCurrentActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here
        NewClientePane ncp = new NewClientePane();
        ncp.mostrarDialogoConPanel(this);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        NewProductPane npp = new NewProductPane();
        npp.mostrarDialogoConPanel(this);
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnRestablecerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestablecerActionPerformed
        // TODO add your handling code here:
        if(JOptionPane.showConfirmDialog(null, "Desea restablecer las ventas?","Verificacion",JOptionPane.YES_NO_OPTION) == 0){
                if(dr.deleteTableSaleTemp()){
                    if(dr.createTableSaleTemp()){
                        JOptionPane.showMessageDialog(null,"Venta restablecidas con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        dr.restoreSoldProduct();
                    }else{
                        JOptionPane.showMessageDialog(null,"Error al restablecer las ventas!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Error al eliminar la tabla!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
        }
        
    }//GEN-LAST:event_btnRestablecerActionPerformed

    private void txtMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMonthActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnAgregarCarrito1;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCancelar2;
    private javax.swing.JButton btnCancelarVenta;
    private javax.swing.JButton btnCancelarVenta2;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEditar1;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminar1;
    private javax.swing.JButton btnGuardarVenta;
    private javax.swing.JButton btnGuardarVenta2;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrintAll;
    private javax.swing.JButton btnPrintCurrent;
    private javax.swing.JButton btnRestablecer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JLabel lblActual;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDireccion2;
    private javax.swing.JLabel lblDireccion6;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblMax;
    private javax.swing.JLabel lblMes;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblNombreCliente2;
    private javax.swing.JLabel lblNombreCliente6;
    private javax.swing.JLabel lblNumberOrder;
    private javax.swing.JLabel lblProducto;
    private javax.swing.JLabel lblProducto1;
    private javax.swing.JLabel lblSubTotalProduct;
    private javax.swing.JLabel lblSubTotalProduct2;
    private javax.swing.JLabel lblTotalSold;
    private javax.swing.JLabel lblTotalVenta;
    private javax.swing.JLabel lblTotalVenta2;
    private javax.swing.JLabel lblTotalVenta3;
    private javax.swing.JTextField txtBusquedaCliente;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCantidad2;
    private javax.swing.JTextField txtDay;
    private javax.swing.JTextField txtMonth;
    private javax.swing.JTextField txtNumeroPedido;
    private javax.swing.JTextField txtPrecioUni;
    private javax.swing.JTextField txtPrecioUni2;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtProducto2;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
