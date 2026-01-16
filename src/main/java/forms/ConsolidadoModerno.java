package forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import logic.Datarequest;

public class ConsolidadoModerno extends JPanel {

    // ==========================================
    // 1. VARIABLES DE LÓGICA 
    // ==========================================
    private int idSelectedProvider = 0;
    private int idSelectedProviderDel = 0;
    private int idSelectedProduct = 0;
    private int idSelectedProductDel = 0;
    private int idSelectedProductInv = 0;
    private float productSold = 0.00f;
    private final Datarequest dr = new Datarequest();
    private boolean puntoDecimalIngresado = false;
    private boolean puntoDecimalIngresado1 = false;
    
    // COMPONENTES
    private JTable productTable;
    private JTextField txtNombre, txtPrecioCompra, txtBusquedaProduct;
    private JComboBox<String> cbRuta, jComboBox1;
    private JButton btnAgregar, btnEditar, btnEliminar;
    
    private JTable jTable2; 
    private JTextField txtNameProveedor, txtBusquedaProve;
    private JButton btnIngresoProve, btnEditProve, btnDeleteProve;
    private JComboBox<String> cbFiltroProve;
    
    private JTable productTable3;
    private JLabel lblProduct, lblGastos;
    private JTextField txtInventario, txtBusqueda;
    private JButton btnAgregar1, btnCancelar2, btnExportarPDF; 
    private JComboBox<String> cbFiltro1;

    // DISEÑO
    private JTabbedPane jTabbedPane1;
    private JPanel pnlProductos, pnlProveedores, pnlInventario;
    
    // Colores
    private final Color COLOR_BG = new Color(245, 245, 250);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BTN = new Color(55, 60, 68);
    private final Color COLOR_ACCENT = new Color(13, 110, 253); 
    private final Color COLOR_DANGER = new Color(220, 53, 69);

    public ConsolidadoModerno() {
        initComponentsModernos();
        initLogicEvents();
        
        loadComboBoxProvider(cbRuta);
        loadComboBoxProvider(cbFiltro1);
        loadProviderTable();
        loadProductTable(dr.loadProducts(), productTable, false);
        filterTable(); 
        
        btnEditProve.setEnabled(false);
        btnDeleteProve.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);
    }

    private void initComponentsModernos() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        initTabProductos();
        initTabProveedores();
        initTabInventario();

        jTabbedPane1.addTab("Productos", pnlProductos);
        jTabbedPane1.addTab("Proveedores", pnlProveedores);
        jTabbedPane1.addTab("Inventario", pnlInventario);
        
        add(jTabbedPane1, BorderLayout.CENTER);
    }

    // --- TAB 1: PRODUCTOS ---
    private void initTabProductos() {
        pnlProductos = new JPanel(new BorderLayout(5, 5)); // Márgenes reducidos
        pnlProductos.setBackground(COLOR_BG);
        pnlProductos.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.setBackground(COLOR_PANEL);
        pnlTop.setBorder(new LineBorder(Color.LIGHT_GRAY));
        pnlTop.add(new JLabel("Buscar por:"));
        jComboBox1 = new JComboBox<>(new String[] { "Nombre", "Proveedor" });
        pnlTop.add(jComboBox1);
        txtBusquedaProduct = new JTextField(20); estilizarCampo(txtBusquedaProduct);
        pnlTop.add(txtBusquedaProduct);
        pnlProductos.add(pnlTop, BorderLayout.NORTH);

        // Center
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();
        
        // Formulario (Izquierda)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(COLOR_PANEL);
        pnlForm.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Producto"));
        pnlForm.setPreferredSize(new Dimension(260, 0)); // Ancho muy ajustado
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(2, 5, 2, 5); // Márgenes internos mínimos
        f.fill = GridBagConstraints.HORIZONTAL; f.anchor = GridBagConstraints.WEST;
        
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlForm.add(new JLabel("Nombre:"), f);
        txtNombre = new JTextField(); estilizarCampo(txtNombre);
        f.gridy=1; pnlForm.add(txtNombre, f);
        
        f.gridy=2; pnlForm.add(new JLabel("Proveedor:"), f);
        cbRuta = new JComboBox<>();
        f.gridy=3; pnlForm.add(cbRuta, f);
        
        f.gridy=4; pnlForm.add(new JLabel("Precio Compra ($):"), f);
        txtPrecioCompra = new JTextField(); estilizarCampo(txtPrecioCompra);
        f.gridy=5; pnlForm.add(txtPrecioCompra, f);
        
        f.gridy=6; 
        JPanel pnlBtns = new JPanel(new GridLayout(1, 3, 5, 0)); pnlBtns.setOpaque(false);
        btnAgregar = new JButton("Add"); estilizarBoton(btnAgregar, COLOR_ACCENT);
        btnEditar = new JButton("Edit"); estilizarBoton(btnEditar, new Color(255, 193, 7)); btnEditar.setForeground(Color.BLACK);
        btnEliminar = new JButton("Del"); estilizarBoton(btnEliminar, COLOR_DANGER);
        pnlBtns.add(btnAgregar); pnlBtns.add(btnEditar); pnlBtns.add(btnEliminar);
        pnlForm.add(pnlBtns, f);
        f.gridy=7; f.weighty=1.0; pnlForm.add(Box.createVerticalGlue(), f); 

        // Tabla (Derecha)
        JPanel pnlTable = new JPanel(new BorderLayout()); 
        pnlTable.setBorder(new EmptyBorder(0, 5, 0, 0)); pnlTable.setBackground(COLOR_BG);
        
        productTable = new JTable(); productTable.setRowHeight(25);
        productTable.setModel(new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Nombre", "Proveedor", "Ventas", "Precio" }) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        
        productTable.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane scrollTable = new JScrollPane(productTable);
        // TRUCO SCROLL: Altura preferida muy baja (100px) para que nunca empuje el layout
        scrollTable.setPreferredSize(new Dimension(0, 100)); 
        
        pnlTable.add(new JLabel("Lista Productos:"), BorderLayout.NORTH);
        pnlTable.add(scrollTable, BorderLayout.CENTER);

        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL;
        pnlCenter.add(pnlForm, gc);
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH;
        pnlCenter.add(pnlTable, gc);
        
        pnlProductos.add(pnlCenter, BorderLayout.CENTER);
    }

    // --- TAB 2: PROVEEDORES ---
    private void initTabProveedores() {
        pnlProveedores = new JPanel(new BorderLayout(5, 5));
        pnlProveedores.setBackground(COLOR_BG);
        pnlProveedores.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.setBackground(COLOR_PANEL);
        pnlTop.setBorder(new LineBorder(Color.LIGHT_GRAY));
        pnlTop.add(new JLabel("Filtrar:"));
        cbFiltroProve = new JComboBox<>(new String[] { "Todos" });
        pnlTop.add(cbFiltroProve);
        pnlTop.add(Box.createHorizontalStrut(15));
        pnlTop.add(new JLabel("Buscar:"));
        txtBusquedaProve = new JTextField(15); estilizarCampo(txtBusquedaProve);
        pnlTop.add(txtBusquedaProve);
        pnlProveedores.add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(COLOR_PANEL);
        pnlForm.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Proveedor"));
        pnlForm.setPreferredSize(new Dimension(260, 0));
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(2, 5, 2, 5); f.fill = GridBagConstraints.HORIZONTAL; f.anchor = GridBagConstraints.WEST;
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlForm.add(new JLabel("Nombre:"), f);
        txtNameProveedor = new JTextField(); estilizarCampo(txtNameProveedor);
        f.gridy=1; pnlForm.add(txtNameProveedor, f);
        
        f.gridy=2;
        JPanel pnlBtns = new JPanel(new GridLayout(1, 3, 5, 0)); pnlBtns.setOpaque(false);
        btnIngresoProve = new JButton("Add"); estilizarBoton(btnIngresoProve, COLOR_ACCENT);
        btnEditProve = new JButton("Edit"); estilizarBoton(btnEditProve, new Color(255, 193, 7)); btnEditProve.setForeground(Color.BLACK);
        btnDeleteProve = new JButton("Del"); estilizarBoton(btnDeleteProve, COLOR_DANGER);
        pnlBtns.add(btnIngresoProve); pnlBtns.add(btnEditProve); pnlBtns.add(btnDeleteProve);
        pnlForm.add(pnlBtns, f);
        f.gridy=3; f.weighty=1.0; pnlForm.add(Box.createVerticalGlue(), f);

        JPanel pnlTable = new JPanel(new BorderLayout()); 
        pnlTable.setBorder(new EmptyBorder(0, 5, 0, 0)); pnlTable.setBackground(COLOR_BG);
        jTable2 = new JTable(); jTable2.setRowHeight(25);
        jTable2.setModel(new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Nombre", "Ordenes", "Total ($)" }) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        
        jTable2.getColumnModel().getColumn(0).setMaxWidth(40);
        JScrollPane scrollTable = new JScrollPane(jTable2);
        scrollTable.setPreferredSize(new Dimension(0, 100)); // Altura segura
        pnlTable.add(new JLabel("Lista Proveedores:"), BorderLayout.NORTH);
        pnlTable.add(scrollTable, BorderLayout.CENTER);

        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL;
        pnlCenter.add(pnlForm, gc);
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH;
        pnlCenter.add(pnlTable, gc);
        
        pnlProveedores.add(pnlCenter, BorderLayout.CENTER);
    }

    // --- TAB 3: INVENTARIO (CORREGIDO) ---
    private void initTabInventario() {
        pnlInventario = new JPanel(new BorderLayout(5, 5));
        pnlInventario.setBackground(COLOR_BG);
        pnlInventario.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Top
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTop.setBackground(COLOR_PANEL);
        pnlTop.setBorder(new LineBorder(Color.LIGHT_GRAY));
        
        pnlTop.add(new JLabel("Buscar:"));
        txtBusqueda = new JTextField(12); estilizarCampo(txtBusqueda);
        pnlTop.add(txtBusqueda);
        
        pnlTop.add(new JLabel("Proveedor:"));
        cbFiltro1 = new JComboBox<>();
        pnlTop.add(cbFiltro1);
        
        pnlTop.add(Box.createHorizontalStrut(10));
        lblGastos = new JLabel("Gastos: $ 0.00");
        lblGastos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblGastos.setForeground(new Color(40, 167, 69)); 
        pnlTop.add(lblGastos);
        
        btnExportarPDF = new JButton("PDF"); 
        estilizarBoton(btnExportarPDF, new Color(220, 53, 69));
        btnExportarPDF.setPreferredSize(new Dimension(60, 25));
        pnlTop.add(btnExportarPDF);
        
        pnlInventario.add(pnlTop, BorderLayout.NORTH);

        // Center
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();
        
        // Formulario (Izquierda) - Aunque es menos útil con edición en tabla, se mantiene por si acaso
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(COLOR_PANEL);
        pnlForm.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Stock"));
        pnlForm.setPreferredSize(new Dimension(260, 0));
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(2, 5, 2, 5); f.fill = GridBagConstraints.HORIZONTAL; f.anchor = GridBagConstraints.WEST;
        
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlForm.add(new JLabel("Seleccionado:"), f);
        lblProduct = new JLabel("---"); 
        lblProduct.setFont(new Font("Segoe UI", Font.BOLD, 12)); lblProduct.setForeground(COLOR_ACCENT);
        f.gridy=1; pnlForm.add(lblProduct, f);
        f.gridy=2; pnlForm.add(Box.createVerticalStrut(10), f);
        f.gridy=3; pnlForm.add(new JLabel("En Inventario:"), f);
        txtInventario = new JTextField(); estilizarCampo(txtInventario);
        f.gridy=4; pnlForm.add(txtInventario, f);
        
        f.gridy=5;
        JPanel pnlBtns = new JPanel(new GridLayout(1, 2, 5, 0)); pnlBtns.setOpaque(false);
        btnAgregar1 = new JButton("Update"); estilizarBoton(btnAgregar1, new Color(40, 167, 69)); 
        btnCancelar2 = new JButton("Clear"); estilizarBoton(btnCancelar2, new Color(108, 117, 125));
        pnlBtns.add(btnAgregar1); pnlBtns.add(btnCancelar2);
        pnlForm.add(pnlBtns, f);
        f.gridy=6; f.weighty=1.0; pnlForm.add(Box.createVerticalGlue(), f);

        // Tabla Inventario (Derecha)
        JPanel pnlTable = new JPanel(new BorderLayout()); 
        pnlTable.setBorder(new EmptyBorder(0, 5, 0, 0)); pnlTable.setBackground(COLOR_BG);
        
        productTable3 = new JTable(); 
        productTable3.setRowHeight(30); // Filas más altas para editar cómodo
        
        // --- MODELO CON EDICIÓN AUTOMÁTICA ---
        DefaultTableModel modelInventario = new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Nombre del Producto", "Total Vendidos" }) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 2; // Solo 'Total Vendidos' editable
            }
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                try {
                    float nuevoValor = Float.parseFloat(aValue.toString());
                    super.setValueAt(aValue, row, column);
                    int idProd = Integer.parseInt(getValueAt(row, 0).toString());
                    dr.EditProductSold(idProd, nuevoValor); // Guardar en BD
                    filterTable(); // Recalcular gastos
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        productTable3.setModel(modelInventario);
        
        // Configuracion Columnas
        productTable3.getColumnModel().getColumn(0).setMinWidth(0);
        productTable3.getColumnModel().getColumn(0).setMaxWidth(0); // ID Oculto
        productTable3.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        productTable3.getColumnModel().getColumn(1).setPreferredWidth(800); // Nombre 80%
        productTable3.getColumnModel().getColumn(2).setPreferredWidth(200); // Total 20%
        
        // Renderer para Total Vendidos (Editable)
        productTable3.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSel, hasFocus, row, col);
                setFont(getFont().deriveFont(Font.BOLD));
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSel) setBackground(new Color(240, 255, 240)); // Verde suave para indicar editable
                return this;
            }
        });
        
        JScrollPane scrollTable = new JScrollPane(productTable3);
        scrollTable.setPreferredSize(new Dimension(0, 100)); // Altura segura
        pnlTable.add(new JLabel("Inventario (Doble click en 'Total Vendidos' para editar):"), BorderLayout.NORTH);
        pnlTable.add(scrollTable, BorderLayout.CENTER);

        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL;
        pnlCenter.add(pnlForm, gc);
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH;
        pnlCenter.add(pnlTable, gc);
        
        pnlInventario.add(pnlCenter, BorderLayout.CENTER);
    }

    // ==========================================
    // 3. LOGIC & EVENTS
    // ==========================================
    private void initLogicEvents() {
        // --- TAB 1 EVENTS ---
        btnAgregar.addActionListener(e -> btnAgregarActionPerformed(e));
        btnEditar.addActionListener(e -> btnEditarActionPerformed(e));
        btnEliminar.addActionListener(e -> btnEliminarActionPerformed(e));
        
        productTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = productTable.getSelectedRow();
                    if(row >= 0){
                        btnEditar.setEnabled(true);
                        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                        txtNombre.setText(model.getValueAt(row, 1).toString());
                        String provStr = model.getValueAt(row, 2).toString();
                        for (int i = 0; i < cbRuta.getItemCount(); i++) {
                            if (cbRuta.getItemAt(i).startsWith(provStr.split("-")[0])) { 
                                cbRuta.setSelectedIndex(i); break;
                            }
                        }
                        
                        txtPrecioCompra.setText("");
                        String precioStr = model.getValueAt(row, 4).toString().replace("$", "").trim();
                        txtPrecioCompra.setText(precioStr);
                        
                        idSelectedProduct = Integer.parseInt(model.getValueAt(row, 0).toString());
                    }
                }else if(e.getClickCount() == 3) {
                    int row = productTable.getSelectedRow();
                    if(row >= 0){
                        btnEditar.setEnabled(false);
                        idSelectedProductDel = Integer.parseInt(productTable.getValueAt(row, 0).toString());
                        btnEliminar.setEnabled(true);
                    }
                }
            }
        });
        
        setupDocumentFilter(txtPrecioCompra, false);
        txtBusquedaProduct.getDocument().addDocumentListener(createDocListener(() -> {
            if("".equals(txtBusquedaProduct.getText())) loadProductTable(dr.loadProducts(), productTable, false);
            else loadProductTable(dr.SearchProductTable(txtBusquedaProduct.getText(), jComboBox1.getSelectedItem().toString(), dr.returnIdProdiver(txtBusquedaProduct.getText())), productTable, false);
        }));

        // --- TAB 2 EVENTS ---
        btnIngresoProve.addActionListener(e -> btnIngresoProveActionPerformed(e));
        btnEditProve.addActionListener(e -> btnEditProveActionPerformed(e));
        btnDeleteProve.addActionListener(e -> btnDeleteProveActionPerformed(e));
        jTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    int row = jTable2.getSelectedRow();
                    if(row >= 0){
                        btnEditProve.setEnabled(true);
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        txtNameProveedor.setText(model.getValueAt(row, 1).toString());
                        idSelectedProvider = Integer.parseInt(model.getValueAt(row, 0).toString());
                    }
                } else if(e.getClickCount() == 3) {
                    int row = jTable2.getSelectedRow();
                    if(row >= 0){
                        btnEditProve.setEnabled(false);
                        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                        idSelectedProviderDel = Integer.parseInt(model.getValueAt(row, 0).toString());
                        btnDeleteProve.setEnabled(true);
                    }
                }
            }
        });

        // --- TAB 3 EVENTS ---
        btnAgregar1.addActionListener(e -> btnAgregar1ActionPerformed(e));
        btnCancelar2.addActionListener(e -> btnCancelar2ActionPerformed(e));
        cbFiltro1.addActionListener(e -> filterTable());
        btnExportarPDF.addActionListener(e -> btnExportarPDFActionPerformed(e)); // PDF
        
        txtBusqueda.getDocument().addDocumentListener(createDocListener(() -> filterTable()));
        setupDocumentFilter(txtInventario, true);
        
        productTable3.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                // Click simple llena el form izquierdo (si no estas editando)
                if(e.getClickCount() == 2 && !productTable3.isEditing()){
                    int row = productTable3.getSelectedRow();
                    if(row >= 0){
                        DefaultTableModel model = (DefaultTableModel) productTable3.getModel();
                        lblProduct.setText(model.getValueAt(row, 1).toString());
                        String val = model.getValueAt(row, 2).toString();
                        productSold = Float.parseFloat(val);
                        idSelectedProductInv = Integer.parseInt(model.getValueAt(row, 0).toString());
                    } 
                }
            }
        });
    }

    // ==========================================
    // 4. MÉTODOS PRIVADOS
    // ==========================================
    
    private void btnExportarPDFActionPerformed(ActionEvent evt) {
        // 1. Obtener nombre del proveedor para el título
        String proveedor = "Todos";
        if (cbFiltro1.getSelectedItem() != null && !"-".equals(cbFiltro1.getSelectedItem().toString())) {
            String raw = cbFiltro1.getSelectedItem().toString();
            if(raw.contains("-")) {
                proveedor = raw.substring(raw.indexOf("-") + 1); // Quitar ID
            } else {
                proveedor = raw;
            }
        }

        // 2. Elegir dónde guardar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte PDF");
        // Sugerir nombre de archivo por defecto
        fileChooser.setSelectedFile(new File("Pedido_" + proveedor + ".pdf"));
        
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Asegurar extensión .pdf
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            // 3. Crear el PDF usando iText
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // A. Título
                com.itextpdf.text.Font fontTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
                Paragraph titulo = new Paragraph("Pedido a realizar", fontTitulo);
                titulo.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(titulo);
                
                // Subtítulo
                Paragraph subTitulo = new Paragraph(proveedor);
                subTitulo.setAlignment(Paragraph.ALIGN_CENTER);
                subTitulo.setSpacingAfter(20); // Espacio antes de la tabla
                document.add(subTitulo);

                // B. Tabla
                // Importaciones necesarias para el estilo
                com.itextpdf.text.Font fontEncabezado = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.WHITE);
                com.itextpdf.text.Font fontTotalVendido = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.BaseColor colorFondoVerde = new com.itextpdf.text.BaseColor(240, 255, 240);
                com.itextpdf.text.pdf.PdfPCell celda;

                PdfPTable pdfTable = new PdfPTable(2); 
                pdfTable.setWidthPercentage(100); // Ancho completo
                pdfTable.setWidths(new float[]{6, 2}); // Anchos relativos

                // Encabezados con estilo
                celda = new com.itextpdf.text.pdf.PdfPCell(new Paragraph("Nombre del Producto", fontEncabezado));
                celda.setBackgroundColor(new com.itextpdf.text.BaseColor(108, 117, 125)); // Un gris oscuro
                celda.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                celda.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                celda.setPadding(8); // Aumentar padding para más altura
                pdfTable.addCell(celda);

                celda = new com.itextpdf.text.pdf.PdfPCell(new Paragraph("Total ", fontEncabezado));
                celda.setBackgroundColor(new com.itextpdf.text.BaseColor(108, 117, 125));
                celda.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                celda.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                celda.setPadding(8);
                pdfTable.addCell(celda);

                // Datos del JTable (productTable3)
                for (int i = 0; i < productTable3.getRowCount(); i++) {
                    String nombre = productTable3.getValueAt(i, 1).toString();
                    String total = productTable3.getValueAt(i, 2).toString();

                    // Celda para el nombre (con más padding)
                    celda = new com.itextpdf.text.pdf.PdfPCell(new Paragraph(nombre));
                    celda.setPadding(6);
                    celda.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                    pdfTable.addCell(celda);

                    // Celda para el total (con estilo verde, bold y centrada)
                    celda = new com.itextpdf.text.pdf.PdfPCell(new Paragraph(total, fontTotalVendido));
                    celda.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    celda.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
                    celda.setBackgroundColor(colorFondoVerde);
                    celda.setPadding(6);
                    pdfTable.addCell(celda);
                }

                document.add(pdfTable);
                
                // C. Totales (Opcional, agregar el gasto aproximado al final)
                //Paragraph footer = new Paragraph("\n" + lblGastos.getText());
                //footer.setAlignment(Paragraph.ALIGN_RIGHT);
                //document.add(footer);

                document.close();
                
                JOptionPane.showMessageDialog(this, "PDF guardado correctamente en:\n" + filePath, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Opcional: Abrir el archivo automáticamente
                try {
                    Desktop.getDesktop().open(new File(filePath));
                } catch (Exception ex) { /* No hacer nada si falla abrirlo */ }

            } catch (DocumentException | FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
          if (!"".equals(txtNombre.getText()) && !"".equals(txtPrecioCompra.getText())) {
            String[] provider = cbRuta.getSelectedItem().toString().split("-");
            dr.registerProduct(txtNombre.getText(), 0.00f,Integer.parseInt(provider[0]) , Float.parseFloat(txtPrecioCompra.getText()));
            loadProductTable(dr.loadProducts(), productTable, false);
            cleanTXT();
         }else JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        if (!"".equals(txtNombre.getText()) && !"".equals(txtPrecioCompra.getText())) {
            String[] provider = cbRuta.getSelectedItem().toString().split("-");
            if (dr.EditProduct(txtNombre.getText(), Integer.parseInt(provider[0]), Float.parseFloat(txtPrecioCompra.getText()), idSelectedProduct)) {
                  JOptionPane.showMessageDialog(null,"Datos actualizados con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                  btnEditar.setEnabled(false);
                  loadProductTable(dr.loadProducts(), productTable, false);
                  cleanTXT();
              }else JOptionPane.showMessageDialog(null,"Error al actualizar los datos", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de realizar esta acción?", "Advertencia", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (dr.DeleteProduct(idSelectedProductDel)) {
                JOptionPane.showMessageDialog(null,"Producto eliminado con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
                loadProductTable(dr.loadProducts(), productTable, false);
                btnEliminar.setEnabled(false); cleanTXT();
            }else{
                JOptionPane.showMessageDialog(null,"Error al eliminar el producto", "ERROR", JOptionPane.ERROR_MESSAGE);
                btnEliminar.setEnabled(false);
            }
        }else btnEliminar.setEnabled(false);
    }

    private void btnIngresoProveActionPerformed(java.awt.event.ActionEvent evt) {
          if (!"".equals(txtNameProveedor.getText())) {
            dr.registerProvider(txtNameProveedor.getText(), "0", 0.00f);
            loadProviderTable(); loadComboBoxProvider(cbRuta); txtNameProveedor.setText("");
        }else JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void btnEditProveActionPerformed(java.awt.event.ActionEvent evt) {
          if (!"".equals(txtNameProveedor.getText())) {
              if (dr.EditProvider(txtNameProveedor.getText(), idSelectedProvider)) {
                  JOptionPane.showMessageDialog(null,"Datos actualizados con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                  btnEditProve.setEnabled(false); loadProviderTable(); loadComboBoxProvider(cbRuta); txtNameProveedor.setText("");
              }else JOptionPane.showMessageDialog(null,"Error al actualizar los datos", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void btnDeleteProveActionPerformed(java.awt.event.ActionEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de realizar esta acción?", "Advertencia", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (dr.DeleteProvider(idSelectedProviderDel)) {
                JOptionPane.showMessageDialog(null,"Proveedor eliminado con exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
                loadProviderTable(); loadComboBoxProvider(cbRuta); btnDeleteProve.setEnabled(false); txtNameProveedor.setText("");
            }else{
                JOptionPane.showMessageDialog(null,"Error al eliminar al proveedor", "ERROR", JOptionPane.ERROR_MESSAGE);
                btnDeleteProve.setEnabled(false);
            }
        }else btnDeleteProve.setEnabled(false);
    }

    private void btnAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {
        if(!"Aun no seleccionado!".equals(lblProduct.getText()) && !"".equals(txtInventario.getText()) && idSelectedProductInv != 0){
            float inventario = Float.parseFloat(txtInventario.getText());
            float total = productSold - inventario; 
            if (dr.EditProductSold(idSelectedProductInv, total)) {
                filterTable();
                JOptionPane.showMessageDialog(null,"Inventario del producto actualizado!!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                lblProduct.setText("Aun no seleccionado!"); txtInventario.setText(""); idSelectedProductInv = 0;
            }else JOptionPane.showMessageDialog(null,"Error al actualizar el inventario", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else JOptionPane.showMessageDialog(null,"Llene todos los campos!", "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private void btnCancelar2ActionPerformed(java.awt.event.ActionEvent evt) {
        lblProduct.setText("Aun no seleccionado!"); txtInventario.setText("");
    }

    private void filterTable() {
        String busqueda = txtBusqueda.getText();
        if(cbFiltro1.getSelectedItem() == null) return;
        String[] provider = cbFiltro1.getSelectedItem().toString().split("-");
        if(provider.length == 0 || "-".equals(provider[0])){
            loadProductTable(dr.loadProductsSold(0,true,true, busqueda), productTable3, true);
        }else{
            try {
               loadProductTable(dr.loadProductsSold(Integer.parseInt(provider[0]),true,false,busqueda), productTable3, true); 
            } catch(NumberFormatException e){}
        }
    }
    
    private void loadProviderTable(){
        List<List<Object>> provider = dr.loadProviders();
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : provider) modelo.addRow(new Object[]{list.get(0),list.get(1),list.get(2),list.get(3)});
        jTable2.setModel(modelo);
    }
        
    private void loadComboBoxProvider(JComboBox comboBox){
        List<List<Object>> provider = dr.loadProviders();
        comboBox.removeAllItems();
        comboBox.addItem("-");
        for (List<Object> list : provider) comboBox.addItem(list.get(0) + "-"+list.get(1));
    }
    
    private void loadProductTable(List<List<Object>> provider, JTable table, boolean isInventory){
        DefaultTableModel modelo = (DefaultTableModel) table.getModel();
        modelo.setRowCount(0);
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()); 
        
        if (!isInventory) { // Tabla Productos
            table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
                protected void setValue(Object value) {
                    if (value != null) {
                        try {
                            float val = Float.parseFloat(value.toString());
                            value = String.format("$ %.2f", val).replace(",", ".");
                        } catch (NumberFormatException e) {}
                    }
                    super.setValue(value);
                }
            });
            for (List<Object> list : provider) {
                 modelo.addRow(new Object[]{
                     list.get(0), list.get(1),
                     list.get(3).toString() + "-"+dr.loadNameProvider(Integer.parseInt(list.get(3).toString())),
                     quitarCerosNoSignificativos((float) list.get(2)),
                     list.get(4)
                 });
            }
        } else { // Tabla Inventario
            double totalGastos = 0.0;
            for (List<Object> list : provider) {
                 float cantidad = (float) list.get(2);
                 float precio = (float) list.get(4); 
                 totalGastos += (cantidad * precio);
                 
                 modelo.addRow(new Object[]{
                     list.get(0), list.get(1), quitarCerosNoSignificativos(cantidad)
                 });
            }
            lblGastos.setText(String.format("Gastos Aproximados: $ %.2f", totalGastos).replace(",", "."));
        }
        table.setModel(modelo);
    }

    private void cleanTXT(){ txtNombre.setText(""); txtPrecioCompra.setText(""); }
    private static String quitarCerosNoSignificativos(float numero) { return String.valueOf(numero).replaceAll("\\.?0*$", ""); }
    
    private void setupDocumentFilter(JTextField txtField, boolean isInv) {
        ((AbstractDocument) txtField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (esNumeroConDecimal(text, txtField.getText(), isInv)) super.insertString(fb, offset, text, attr);
            }
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (esNumeroConDecimal(text, txtField.getText(), isInv)) super.replace(fb, offset, length, text, attrs);
            }
        });
    }
    private boolean esNumeroConDecimal(String texto, String currentText, boolean isInv) {
        if (texto.isEmpty()) {
            return true;
        }
        // Regex to match a valid floating point number string
        if (texto.matches("\\d*\\.?\\d*")) {
            // If the new text contains a dot, check if one already exists in the field
            if (texto.contains(".")) {
                if (isInv) return !puntoDecimalIngresado1 && !currentText.contains(".");
                else return !puntoDecimalIngresado && !currentText.contains(".");
            }
            return true;
        }
        return false;
    }
    private DocumentListener createDocListener(Runnable action) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { action.run(); }
            public void removeUpdate(DocumentEvent e) { action.run(); }
            public void changedUpdate(DocumentEvent e) { action.run(); }
        };
    }
    private void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
    }
    private void estilizarBoton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12)); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}