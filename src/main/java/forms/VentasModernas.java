package forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import logic.Datarequest;
import logic.ImprimirFactura;
import logic.ImprimirTodo;
import logic.SearchProduct;
import logic.SearchValue;

public class VentasModernas extends JPanel {

    // ==========================================
    // 1. VARIABLES DE TU LÓGICA ORIGINAL
    // ==========================================
    public Datarequest dr = new Datarequest();
    private SearchValue sv;
    
    // TAB 1 & 2 VARS
    private int idCustomerSelected;
    private List<SearchValue> resultCustomer;
    private int idProductSelected;
    private List<SearchProduct> resultProduct;
    private int rowSelected;
    private float total_sold_Product;
    private float total_sold;
    private List<List<Object>> orderTempsLoad;
    private boolean puntoDecimalIngresado = false;

    // TAB 3 VARS (MODIFICAR)
    private int idProductSelected1;
    private int rowSelected1;
    private int id_sale;
    private DefaultTableModel modelTable4; // Modelo sombra para lógica

    // --- Componentes Lógicos Tab 1 (Registrar) ---
    private JTable jTable2; 
    private JTextField txtBusquedaCliente;
    private JLabel lblNombreCliente;
    private JLabel lblDireccion;
    private JComboBox<String> jComboBox1;
    private JTextField txtProducto, txtCantidad, txtPrecioUni;
    private JLabel lblProducto, lblSubTotalProduct, lblTotalVenta;
    private JList<String> jList1, jList2; 
    private JScrollPane jScrollPane1, jScrollPane2;
    private JPopupMenu popupCliente, popupProducto; 
    private JButton btnAgregarCarrito, btnCancelar, btnEditar, btnEliminar, jButton3, jButton1, btnGuardarVenta, btnCancelarVenta;

    // --- Componentes Lógicos Tab 2 (Historial) ---
    private JLabel lblNumberOrder, lblMax, lblTotalSold, lblActual, lblNombreCliente6, lblDireccion6, lblTotalVenta3, lblTotalProductos;
    private JTable jTable5;
    private JTextField txtDay = new JTextField(), txtMonth = new JTextField(), txtYear = new JTextField();
    private JButton btnPrintAll, btnBack, btnNext, btnRestablecer, btnPrintCurrent;
    private JSpinner dateSpinner;
    private int currentOrderIndex = 1;

    // --- Componentes Lógicos Tab 3 (Modificar) ---
    private JTextField txtNumeroPedido;
    private JCheckBox jCheckBox1;
    private JButton btnBuscar, btnPrintMod;
    private JPanel pnlContentModificar; // Panel que se oculta/muestra
    
    private JLabel lblNombreCliente2, lblDireccion2;
    private JTextField txtProducto2, txtCantidad2, txtPrecioUni2;
    private JLabel lblProducto1, lblSubTotalProduct2, lblTotalVenta2;
    private JList<String> jList4; // Lista productos mod
    private JPopupMenu popupProductoMod;
    private JTable jTable4;
    private JButton btnAgregarCarrito1, btnCancelar2, btnEditar1, btnEliminar1, btnGuardarVenta2, btnCancelarVenta2, btnEliminarVentaTotal;
    private JLabel lblError;

    // ==========================================
    // 2. VARIABLES VISUALES
    // ==========================================
    private JTabbedPane jTabbedPane1;
    private JPanel pnlRegistrarVenta, pnlHistorial, pnlModificarVenta;

    // Colores Flat
    private final Color COLOR_BG = new Color(245, 245, 250);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BTN = new Color(55, 60, 68);
    private final Color COLOR_ACCENT = new Color(13, 110, 253); 
    private final Color COLOR_DANGER = new Color(220, 53, 69);

    public VentasModernas() {
        initComponentsModernos(); 
        initLogicEvents();        
        
        // --- ESTADO INICIAL ---
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar1.setVisible(false);
        btnEliminar1.setVisible(false);
        
        // Ocultar contenido de modificar al inicio
        pnlContentModificar.setVisible(false);
        
        loadOrders();
        if (!orderTempsLoad.isEmpty()) {
            loadOrder(0);
        }
        actualizarFechaOculta(); 
    }

    // ==========================================
    // 3. DISEÑO VISUAL
    // ==========================================
    private void initComponentsModernos() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        initTabRegistrarVenta();
        initTabHistorial(); 
        initTabModificarVenta(); // <--- NUEVO TAB

        jTabbedPane1.addTab("Registrar Venta", pnlRegistrarVenta);
        jTabbedPane1.addTab("Mostrar Ventas Realizadas", pnlHistorial);
        jTabbedPane1.addTab("Modificar Venta", pnlModificarVenta);
        
        add(jTabbedPane1, BorderLayout.CENTER);
    }

    // --- TAB 1: REGISTRAR VENTA ---
    private void initTabRegistrarVenta() {
        pnlRegistrarVenta = new JPanel(new BorderLayout(10, 10)); 
        pnlRegistrarVenta.setBackground(COLOR_BG);
        pnlRegistrarVenta.setBorder(new EmptyBorder(10, 10, 10, 10));

        // PANEL SUPERIOR
        JPanel pnlTop = new JPanel(new GridBagLayout());
        pnlTop.setBackground(COLOR_PANEL);
        pnlTop.setBorder(new CompoundBorder(new LineBorder(new Color(220,220,220)), new EmptyBorder(5, 10, 5, 10)));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(5,5,5,5);
        pnlTop.add(new JLabel("Ruta:"), gbc);
        jComboBox1 = new JComboBox<>(new String[] { "US-SEBT", "US-SJNC", "US-OCTSA", "US-JPSN" });
        gbc.gridx=1; pnlTop.add(jComboBox1, gbc);
        gbc.gridx=2; pnlTop.add(new JLabel("Buscar Cliente:"), gbc);
        
        txtBusquedaCliente = new JTextField(); estilizarCampo(txtBusquedaCliente);
        gbc.gridx=3; gbc.weightx = 0.5; gbc.fill = GridBagConstraints.HORIZONTAL; 
        pnlTop.add(txtBusquedaCliente, gbc);
        
        popupCliente = new JPopupMenu(); popupCliente.setFocusable(false);
        jList1 = new JList<>(); jScrollPane1 = new JScrollPane(jList1); jScrollPane1.setBorder(null);
        popupCliente.add(jScrollPane1);
        
        jButton1 = new JButton("+ Cliente"); estilizarBoton(jButton1, COLOR_BTN);
        gbc.gridx=4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        pnlTop.add(jButton1, gbc);

        // Info Cliente
        JPanel pnlInfoCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlInfoCliente.setBackground(COLOR_PANEL);
        // Se combina el TitledBorder con un EmptyBorder para agregar padding interno
        pnlInfoCliente.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Información del Cliente"),
            new EmptyBorder(5, 10, 5, 10) // Padding: arriba, izquierda, abajo, derecha
        ));
        
        lblNombreCliente = new JLabel("Aun sin seleccionar");
        lblNombreCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDireccion = new JLabel("Aun sin seleccionar");
        pnlInfoCliente.add(new JLabel("Nombre:")); pnlInfoCliente.add(lblNombreCliente);
        pnlInfoCliente.add(Box.createHorizontalStrut(10)); // Espacio extra
        pnlInfoCliente.add(new JLabel("Dirección:")); pnlInfoCliente.add(lblDireccion);
        
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=5; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5,5,5,5);
        pnlTop.add(pnlInfoCliente, gbc);
        pnlRegistrarVenta.add(pnlTop, BorderLayout.NORTH);

        // CENTRAL
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();
        
        // IZQUIERDA: FORMULARIO (270px)
        JPanel pnlFormProd = new JPanel(new GridBagLayout());
        pnlFormProd.setBackground(COLOR_PANEL);
        pnlFormProd.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Añadir Producto"));
        pnlFormProd.setPreferredSize(new Dimension(270, 0)); 
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5,5,5,5); f.fill = GridBagConstraints.HORIZONTAL; f.anchor = GridBagConstraints.WEST;
        
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlFormProd.add(new JLabel("Buscar Producto:"), f);
        
        JPanel pnlSearchProd = new JPanel(new BorderLayout(5, 0)); pnlSearchProd.setBackground(COLOR_PANEL);
        txtProducto = new JTextField(); estilizarCampo(txtProducto);
        jButton3 = new JButton("Nuevo"); estilizarBoton(jButton3, COLOR_BTN); jButton3.setPreferredSize(new Dimension(70, 30));
        pnlSearchProd.add(txtProducto, BorderLayout.CENTER); pnlSearchProd.add(jButton3, BorderLayout.EAST);
        f.gridy=1; pnlFormProd.add(pnlSearchProd, f);

        popupProducto = new JPopupMenu(); popupProducto.setFocusable(false);
        jList2 = new JList<>(); jScrollPane2 = new JScrollPane(jList2); jScrollPane2.setBorder(null);
        popupProducto.add(jScrollPane2);

        f.gridy=2;
        lblProducto = new JLabel("Aun no seleccionado"); lblProducto.setForeground(COLOR_ACCENT); lblProducto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlFormProd.add(lblProducto, f);
        
        JPanel pnlCantPrecio = new JPanel(new GridLayout(1, 2, 5, 0)); 
        pnlCantPrecio.setBackground(COLOR_PANEL);
        JPanel p1 = new JPanel(new BorderLayout()); p1.setBackground(COLOR_PANEL);
        p1.add(new JLabel("Cantidad:"), BorderLayout.NORTH);
        txtCantidad = new JTextField(); estilizarCampo(txtCantidad); p1.add(txtCantidad, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout()); p2.setBackground(COLOR_PANEL);
        p2.add(new JLabel("Precio ($):"), BorderLayout.NORTH);
        txtPrecioUni = new JTextField(); estilizarCampo(txtPrecioUni); p2.add(txtPrecioUni, BorderLayout.CENTER);
        pnlCantPrecio.add(p1); pnlCantPrecio.add(p2);
        f.gridy=3; pnlFormProd.add(pnlCantPrecio, f);
        
        f.gridy=4; 
        JPanel pnlSub = new JPanel(new FlowLayout(FlowLayout.LEFT)); pnlSub.setBackground(COLOR_PANEL);
        lblSubTotalProduct = new JLabel("0.00"); lblSubTotalProduct.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlSub.add(new JLabel("SubTotal: $")); pnlSub.add(lblSubTotalProduct);
        pnlFormProd.add(pnlSub, f);
        
        f.gridy=5;
        JPanel pnlBtnsProd = new JPanel(new GridLayout(1, 2, 5, 0)); pnlBtnsProd.setBackground(COLOR_PANEL);
        btnAgregarCarrito = new JButton("Agregar"); estilizarBoton(btnAgregarCarrito, new Color(40, 167, 69));
        btnCancelar = new JButton("Limpiar"); estilizarBoton(btnCancelar, new Color(108, 117, 125));
        pnlBtnsProd.add(btnAgregarCarrito); pnlBtnsProd.add(btnCancelar);
        pnlFormProd.add(pnlBtnsProd, f);
        
        f.gridy=6;
        JPanel pnlBtnsEdit = new JPanel(new GridLayout(1, 2, 5, 0)); pnlBtnsEdit.setBackground(COLOR_PANEL);
        btnEditar = new JButton("Editar"); estilizarBoton(btnEditar, new Color(255, 193, 7)); btnEditar.setForeground(Color.BLACK);
        btnEliminar = new JButton("Eliminar"); estilizarBoton(btnEliminar, COLOR_DANGER);
        pnlBtnsEdit.add(btnEditar); pnlBtnsEdit.add(btnEliminar);
        pnlFormProd.add(pnlBtnsEdit, f);
        f.gridy=7; f.weighty=1.0; pnlFormProd.add(Box.createVerticalGlue(), f);

        // DERECHA: TABLA
        JPanel pnlTable = new JPanel(new BorderLayout()); pnlTable.setBorder(new EmptyBorder(0, 10, 0, 0)); pnlTable.setBackground(COLOR_BG);
        jTable2 = new JTable(); jTable2.setRowHeight(25);
        jTable2.setModel(new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal" }) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        configurarTabla(jTable2);
        
        JScrollPane scrollTable = new JScrollPane(jTable2);
        scrollTable.setPreferredSize(new Dimension(0, 200)); 
        pnlTable.add(new JLabel("Detalles de la Venta:"), BorderLayout.NORTH);
        pnlTable.add(scrollTable, BorderLayout.CENTER);

        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL; 
        pnlCenter.add(pnlFormProd, gc);
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH; 
        pnlCenter.add(pnlTable, gc);
        pnlRegistrarVenta.add(pnlCenter, BorderLayout.CENTER);

        // INFERIOR
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBottom.setBackground(new Color(230, 230, 235)); pnlBottom.setBorder(new MatteBorder(1,0,0,0, Color.LIGHT_GRAY));
        lblTotalVenta = new JLabel("$ 0.00"); lblTotalVenta.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblTotalVenta.setForeground(new Color(192, 57, 43));
        btnGuardarVenta = new JButton("GUARDAR VENTA"); estilizarBoton(btnGuardarVenta, COLOR_ACCENT); btnGuardarVenta.setPreferredSize(new Dimension(150, 40));
        btnCancelarVenta = new JButton("CANCELAR"); estilizarBoton(btnCancelarVenta, COLOR_DANGER);
        pnlBottom.add(new JLabel("TOTAL A PAGAR: ")); pnlBottom.add(lblTotalVenta);
        pnlBottom.add(Box.createHorizontalStrut(20)); pnlBottom.add(btnGuardarVenta); pnlBottom.add(btnCancelarVenta);
        pnlRegistrarVenta.add(pnlBottom, BorderLayout.SOUTH);
    }

    // --- TAB 2: HISTORIAL ---
    private void initTabHistorial() {
        pnlHistorial = new JPanel(new BorderLayout(15, 15));
        pnlHistorial.setBackground(COLOR_BG);
        pnlHistorial.setBorder(new EmptyBorder(10, 10, 10, 10));

        // HEADER
        JPanel pnlHeader = new JPanel(new GridBagLayout());
        pnlHeader.setBackground(COLOR_PANEL);
        pnlHeader.setBorder(new CompoundBorder(new LineBorder(new Color(220,220,220)), new EmptyBorder(5, 10, 5, 10)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        JPanel pnlStats = new JPanel(new GridLayout(2, 2, 10, 5));
        pnlStats.setBackground(COLOR_PANEL);
        pnlStats.add(new JLabel("Pedidos Totales:"));
        lblNumberOrder = new JLabel("0"); lblNumberOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlStats.add(lblNumberOrder);
        pnlStats.add(new JLabel("Venta Total ($):"));
        lblTotalSold = new JLabel("0.00"); lblTotalSold.setFont(new Font("Segoe UI", Font.BOLD, 14)); lblTotalSold.setForeground(new Color(40, 167, 69));
        pnlStats.add(lblTotalSold);
        
        gbc.gridx=0; gbc.gridy=0; gbc.anchor = GridBagConstraints.WEST;
        pnlHeader.add(pnlStats, gbc);
        gbc.gridx=1; gbc.weightx=1.0;
        pnlHeader.add(Box.createGlue(), gbc);
        
        JPanel pnlDate = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlDate.setBackground(COLOR_PANEL);
        pnlDate.add(new JLabel("Fecha de Entrega:"));
        
        SpinnerDateModel modelDate = new SpinnerDateModel();
        dateSpinner = new JSpinner(modelDate);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yy");
        dateSpinner.setEditor(editor);
        dateSpinner.setPreferredSize(new Dimension(120, 30));
        
        // Center the text in the spinner's text field
        if (editor.getTextField() != null) {
            editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        }
        
        pnlDate.add(dateSpinner);
        
        btnPrintAll = new JButton("Imprimir Todo"); estilizarBoton(btnPrintAll, COLOR_BTN);
        pnlDate.add(btnPrintAll);
        
        gbc.gridx=2; gbc.weightx=0;
        pnlHeader.add(pnlDate, gbc);
        pnlHistorial.add(pnlHeader, BorderLayout.NORTH);

        // VISOR
        JPanel pnlVisor = new JPanel(new BorderLayout(0, 10));
        pnlVisor.setBackground(COLOR_BG);
        
        JPanel pnlInfoVisor = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlInfoVisor.setBackground(COLOR_PANEL);
        pnlInfoVisor.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Datos del Cliente (Factura Actual)"));
        lblNombreCliente6 = new JLabel("---"); lblNombreCliente6.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDireccion6 = new JLabel("---");
        pnlInfoVisor.add(new JLabel("Cliente:")); pnlInfoVisor.add(lblNombreCliente6);
        pnlInfoVisor.add(new JLabel("Dirección:")); pnlInfoVisor.add(lblDireccion6);
        pnlVisor.add(pnlInfoVisor, BorderLayout.NORTH);
        
        jTable5 = new JTable(); jTable5.setRowHeight(25);
        jTable5.setModel(new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal" }) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        configurarTabla(jTable5);
        
        JScrollPane scrollVisor = new JScrollPane(jTable5);
        scrollVisor.setPreferredSize(new Dimension(0, 200)); 
        scrollVisor.setBorder(new LineBorder(Color.LIGHT_GRAY));
        scrollVisor.getViewport().setBackground(Color.WHITE);
        pnlVisor.add(scrollVisor, BorderLayout.CENTER);
        
        JPanel pnlTotalVisor = new JPanel(new BorderLayout());
        pnlTotalVisor.setBackground(Color.WHITE);
        pnlTotalVisor.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        lblTotalProductos = new JLabel("Cant. Productos: 0");
        lblTotalProductos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalProductos.setForeground(Color.GRAY);
        pnlTotalVisor.add(lblTotalProductos, BorderLayout.WEST);

        JPanel pnlPrecioDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlPrecioDerecha.setOpaque(false);
        lblTotalVenta3 = new JLabel("0.00"); 
        lblTotalVenta3.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        lblTotalVenta3.setForeground(COLOR_ACCENT);
        pnlPrecioDerecha.add(new JLabel("TOTAL FACTURA: ")); pnlPrecioDerecha.add(lblTotalVenta3);
        pnlTotalVisor.add(pnlPrecioDerecha, BorderLayout.EAST);
        
        pnlVisor.add(pnlTotalVisor, BorderLayout.SOUTH);
        pnlHistorial.add(pnlVisor, BorderLayout.CENTER);

        // NAVEGACIÓN
        JPanel pnlNav = new JPanel(new GridBagLayout());
        pnlNav.setBackground(new Color(230, 230, 235));
        pnlNav.setBorder(new MatteBorder(1,0,0,0, Color.LIGHT_GRAY));
        GridBagConstraints gn = new GridBagConstraints();
        gn.insets = new Insets(10, 10, 10, 10);
        
        btnRestablecer = new JButton("Reiniciar Ventas"); estilizarBoton(btnRestablecer, COLOR_DANGER);
        gn.gridx=0; gn.weightx=0.3; gn.anchor = GridBagConstraints.WEST;
        pnlNav.add(btnRestablecer, gn);
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlControls.setOpaque(false);
        btnBack = new JButton("<<"); estilizarBoton(btnBack, COLOR_BTN);
        btnNext = new JButton(">>"); estilizarBoton(btnNext, COLOR_BTN);
        lblActual = new JLabel("0"); lblActual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMax = new JLabel("0"); lblMax.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlControls.add(btnBack); pnlControls.add(lblActual); pnlControls.add(new JLabel("de")); pnlControls.add(lblMax); pnlControls.add(btnNext);
        gn.gridx=1; gn.weightx=0.4; gn.anchor = GridBagConstraints.CENTER;
        pnlNav.add(pnlControls, gn);
        
        JPanel pnlPrintCur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlPrintCur.setOpaque(false);
        pnlPrintCur.add(new JLabel("Imprimir esta factura:"));
        btnPrintCurrent = new JButton("Imprimir"); estilizarBoton(btnPrintCurrent, COLOR_BTN);
        pnlPrintCur.add(btnPrintCurrent);
        gn.gridx=2; gn.weightx=0.3; gn.anchor = GridBagConstraints.EAST;
        pnlNav.add(pnlPrintCur, gn);
        
        pnlHistorial.add(pnlNav, BorderLayout.SOUTH);
    }

    // --- TAB 3: MODIFICAR VENTA (NUEVO) ---
    private void initTabModificarVenta() {
        pnlModificarVenta = new JPanel(new BorderLayout(10, 10));
        pnlModificarVenta.setBackground(COLOR_BG);
        pnlModificarVenta.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. BARRA SUPERIOR (BÚSQUEDA)
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(COLOR_PANEL);
        pnlSearch.setBorder(new LineBorder(new Color(220,220,220)));
        
        pnlSearch.add(new JLabel("Introduce el número de pedido:"));
        txtNumeroPedido = new JTextField(15); estilizarCampo(txtNumeroPedido);
        pnlSearch.add(txtNumeroPedido);
        
        jCheckBox1 = new JCheckBox("En tabla temporal");
        jCheckBox1.setBackground(COLOR_PANEL);
        pnlSearch.add(jCheckBox1);
        
        btnBuscar = new JButton("Buscar"); estilizarBoton(btnBuscar, COLOR_BTN);
        pnlSearch.add(btnBuscar);
        
        // Mensaje de error
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        pnlSearch.add(lblError);
        
        // Botón imprimir a la derecha
        btnPrintMod = new JButton("Imprimir"); estilizarBoton(btnPrintMod, COLOR_BTN);
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT)); pnlRight.setOpaque(false);
        pnlRight.add(btnPrintMod);
        
        JPanel pnlTopWrapper = new JPanel(new BorderLayout());
        pnlTopWrapper.add(pnlSearch, BorderLayout.CENTER);
        pnlTopWrapper.add(pnlRight, BorderLayout.EAST);
        
        pnlModificarVenta.add(pnlTopWrapper, BorderLayout.NORTH);

        // 2. CONTENIDO PRINCIPAL (Oculto al inicio)
        pnlContentModificar = new JPanel(new BorderLayout(10, 10));
        pnlContentModificar.setBackground(COLOR_BG);
        
        // --- Header Cliente Modificar ---
        JPanel pnlInfoClientMod = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        pnlInfoClientMod.setBackground(COLOR_PANEL);
        pnlInfoClientMod.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Información del Cliente"));
        pnlInfoClientMod.setPreferredSize(new Dimension(0, 60));
        
        lblNombreCliente2 = new JLabel("---"); lblNombreCliente2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDireccion2 = new JLabel("---");
        pnlInfoClientMod.add(new JLabel("Nombre:")); pnlInfoClientMod.add(lblNombreCliente2);
        pnlInfoClientMod.add(new JLabel("Dirección:")); pnlInfoClientMod.add(lblDireccion2);
        
        pnlContentModificar.add(pnlInfoClientMod, BorderLayout.NORTH);
        
        // --- Centro (Formulario + Tabla) ---
        JPanel pnlCenterMod = new JPanel(new GridBagLayout());
        pnlCenterMod.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();
        
        // Izquierda: Formulario (270px)
        JPanel pnlFormMod = new JPanel(new GridBagLayout());
        pnlFormMod.setBackground(COLOR_PANEL);
        pnlFormMod.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Editar Producto"));
        pnlFormMod.setPreferredSize(new Dimension(270, 0));
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5,5,5,5); f.fill = GridBagConstraints.HORIZONTAL; f.anchor = GridBagConstraints.WEST;
        
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlFormMod.add(new JLabel("Buscar Producto:"), f);
        
        txtProducto2 = new JTextField(); estilizarCampo(txtProducto2);
        f.gridy=1; pnlFormMod.add(txtProducto2, f);
        
        popupProductoMod = new JPopupMenu(); popupProductoMod.setFocusable(false);
        jList4 = new JList<>(); 
        JScrollPane jScrollPane6 = new JScrollPane(jList4); 
        jScrollPane6.setBorder(null);
        popupProductoMod.add(jScrollPane6);

        f.gridy=2;
        lblProducto1 = new JLabel("Aun no seleccionado"); lblProducto1.setForeground(COLOR_ACCENT);
        pnlFormMod.add(lblProducto1, f);
        
        JPanel pnlCPMod = new JPanel(new GridLayout(1, 2, 5, 0)); pnlCPMod.setBackground(COLOR_PANEL);
        JPanel p1 = new JPanel(new BorderLayout()); p1.setBackground(COLOR_PANEL);
        p1.add(new JLabel("Cantidad:"), BorderLayout.NORTH);
        txtCantidad2 = new JTextField(); estilizarCampo(txtCantidad2); p1.add(txtCantidad2, BorderLayout.CENTER);
        
        JPanel p2 = new JPanel(new BorderLayout()); p2.setBackground(COLOR_PANEL);
        p2.add(new JLabel("Precio ($):"), BorderLayout.NORTH);
        txtPrecioUni2 = new JTextField(); estilizarCampo(txtPrecioUni2); p2.add(txtPrecioUni2, BorderLayout.CENTER);
        pnlCPMod.add(p1); pnlCPMod.add(p2);
        f.gridy=3; pnlFormMod.add(pnlCPMod, f);
        
        f.gridy=4; 
        JPanel pnlSubMod = new JPanel(new FlowLayout(FlowLayout.LEFT)); pnlSubMod.setBackground(COLOR_PANEL);
        lblSubTotalProduct2 = new JLabel("0.00"); lblSubTotalProduct2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlSubMod.add(new JLabel("SubTotal: $")); pnlSubMod.add(lblSubTotalProduct2);
        pnlFormMod.add(pnlSubMod, f);
        
        f.gridy=5;
        JPanel pnlBtnsMod = new JPanel(new GridLayout(1, 2, 5, 0)); pnlBtnsMod.setBackground(COLOR_PANEL);
        btnAgregarCarrito1 = new JButton("Agregar"); estilizarBoton(btnAgregarCarrito1, new Color(40, 167, 69));
        btnCancelar2 = new JButton("Limpiar"); estilizarBoton(btnCancelar2, new Color(108, 117, 125));
        pnlBtnsMod.add(btnAgregarCarrito1); pnlBtnsMod.add(btnCancelar2);
        pnlFormMod.add(pnlBtnsMod, f);
        
        f.gridy=6;
        JPanel pnlBtnsEditMod = new JPanel(new GridLayout(1, 2, 5, 0)); pnlBtnsEditMod.setBackground(COLOR_PANEL);
        btnEditar1 = new JButton("Editar"); estilizarBoton(btnEditar1, new Color(255, 193, 7)); btnEditar1.setForeground(Color.BLACK);
        btnEliminar1 = new JButton("Eliminar"); estilizarBoton(btnEliminar1, COLOR_DANGER);
        pnlBtnsEditMod.add(btnEditar1); pnlBtnsEditMod.add(btnEliminar1);
        pnlFormMod.add(pnlBtnsEditMod, f);
        f.gridy=7; f.weighty=1.0; pnlFormMod.add(Box.createVerticalGlue(), f);

        // Derecha: Tabla Modificar
        JPanel pnlTableMod = new JPanel(new BorderLayout()); pnlTableMod.setBorder(new EmptyBorder(0, 10, 0, 0)); pnlTableMod.setBackground(COLOR_BG);
        jTable4 = new JTable(); jTable4.setRowHeight(25);
        jTable4.setModel(new DefaultTableModel(new Object [][] {}, new String [] { "ID", "Producto", "Cantidad", "Precio Unit", "SubTotal" }) {
            public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
        });
        configurarTabla(jTable4);
        
        JScrollPane scrollTableMod = new JScrollPane(jTable4);
        scrollTableMod.setPreferredSize(new Dimension(0, 200)); 
        pnlTableMod.add(new JLabel("Detalles de la Venta (Edición):"), BorderLayout.NORTH);
        pnlTableMod.add(scrollTableMod, BorderLayout.CENTER);

        // Layout Center
        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL;
        pnlCenterMod.add(pnlFormMod, gc);
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH;
        pnlCenterMod.add(pnlTableMod, gc);
        
        pnlContentModificar.add(pnlCenterMod, BorderLayout.CENTER);

        // --- Bottom Modificar ---
        JPanel pnlBottomMod = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBottomMod.setBackground(new Color(230, 230, 235)); pnlBottomMod.setBorder(new MatteBorder(1,0,0,0, Color.LIGHT_GRAY));
        
        lblTotalVenta2 = new JLabel("$ 0.00"); lblTotalVenta2.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblTotalVenta2.setForeground(new Color(192, 57, 43));
        
        btnGuardarVenta2 = new JButton("Modificar Venta"); estilizarBoton(btnGuardarVenta2, COLOR_ACCENT); btnGuardarVenta2.setPreferredSize(new Dimension(150, 40));
        btnEliminarVentaTotal = new JButton("Eliminar Venta"); estilizarBoton(btnEliminarVentaTotal, COLOR_DANGER);
        btnCancelarVenta2 = new JButton("Cerrar"); estilizarBoton(btnCancelarVenta2, new Color(108, 117, 125));
        
        pnlBottomMod.add(new JLabel("TOTAL: ")); pnlBottomMod.add(lblTotalVenta2);
        pnlBottomMod.add(Box.createHorizontalStrut(20)); 
        pnlBottomMod.add(btnGuardarVenta2);
        pnlBottomMod.add(btnEliminarVentaTotal);
        pnlBottomMod.add(btnCancelarVenta2);
        
        pnlContentModificar.add(pnlBottomMod, BorderLayout.SOUTH);
        
        pnlModificarVenta.add(pnlContentModificar, BorderLayout.CENTER);
    }
    
    private void configurarTabla(JTable tabla) {
        // 1. CAMBIO GLOBAL DE FUENTE (Aplica a todas las celdas)
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Cambia el 16 por el tamaño que gustes
        tabla.setRowHeight(30); // Aumenta la altura de fila para que quepa la letra grande

        // 2. ANCHOS DE COLUMNA
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(300);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(2).setMaxWidth(80);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setMaxWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);

        // 3. RENDERER CENTRADO (Columnas 0 y 2)
        // Nota: Al no asignar una fuente específica aquí, heredan la de la tabla (tamaño 16)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        tabla.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 

        // 4. RENDERER MONEDA (Columnas 3 y 4)
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value != null) {
                    try {
                        float numero = Float.parseFloat(value.toString().replace("$", "").trim());
                        value = String.format("$ %.3f", numero);
                    } catch (NumberFormatException e) { }
                }
                super.setValue(value);
            }
        };
        currencyRenderer.setHorizontalAlignment(JLabel.CENTER); 
        
        tabla.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer); 
        tabla.getColumnModel().getColumn(4).setCellRenderer(currencyRenderer); 
    }
    
    // ==========================================
    // 4. VINCULACIÓN DE EVENTOS 
    // ==========================================
    private void initLogicEvents() {
        // --- TAB 1 EVENTS ---
        txtBusquedaCliente.getDocument().addDocumentListener(createDocListener(() -> buscarEnBaseCliente(txtBusquedaCliente.getText())));
        jList1.addListSelectionListener(e -> {
            if (!jList1.isSelectionEmpty()) { 
                int sel = jList1.getSelectedIndex();
                lblNombreCliente.setText(resultCustomer.get(sel).getName());
                lblDireccion.setText(resultCustomer.get(sel).getAddress());
                idCustomerSelected = resultCustomer.get(sel).getId(); 
                total_sold = resultCustomer.get(sel).getTotalSold();
                popupCliente.setVisible(false); 
            }
        });
        
        txtProducto.getDocument().addDocumentListener(createDocListener(() -> buscarEnBaseProducto(txtProducto.getText(), jList2, popupProducto, txtProducto)));
        jList2.addListSelectionListener(e -> {
            if (!jList2.isSelectionEmpty()) { 
                int sel = jList2.getSelectedIndex();
                idProductSelected  = resultProduct.get(sel).getIdProduct();
                lblProducto.setText(resultProduct.get(sel).getName_product());
                total_sold_Product = resultProduct.get(sel).getTotal_sold();
                popupProducto.setVisible(false); txtProducto.setText(""); 
            }
        });
        
        setupDocumentFilter(txtCantidad); setupDocumentFilter(txtPrecioUni);
        DocumentListener calc1 = createDocListener(() -> realizarOperacion(txtCantidad, txtPrecioUni, lblSubTotalProduct));
        txtCantidad.getDocument().addDocumentListener(calc1); txtPrecioUni.getDocument().addDocumentListener(calc1);

        btnAgregarCarrito.addActionListener(e -> btnAgregarCarritoActionPerformed(e));
        btnCancelar.addActionListener(e -> btnCancelarActionPerformed(e));
        btnEditar.addActionListener(e -> btnEditarActionPerformed(e));
        btnEliminar.addActionListener(e -> btnEliminarActionPerformed(e));
        btnGuardarVenta.addActionListener(e -> btnGuardarVentaActionPerformed(e));
        btnCancelarVenta.addActionListener(e -> btnCancelarVentaActionPerformed(e));
        jButton1.addActionListener(e -> jButton1ActionPerformed(e)); 
        jButton3.addActionListener(e -> jButton3ActionPerformed(e)); 

        jTable2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){ tableClick(jTable2, e, false); }
        });
        
        // --- TAB 2 EVENTS ---
        dateSpinner.addChangeListener(e -> actualizarFechaOculta());
        btnNext.addActionListener(e -> btnNextActionPerformed(e));
        btnBack.addActionListener(e -> btnBackActionPerformed(e));
        btnPrintCurrent.addActionListener(e -> btnPrintCurrentActionPerformed(e));
        btnRestablecer.addActionListener(e -> btnRestablecerActionPerformed(e));
        btnPrintAll.addActionListener(e -> btnPrintAllActionPerformed(e));

        // --- TAB 3 EVENTS (MODIFICAR) ---
        txtProducto2.getDocument().addDocumentListener(createDocListener(() -> buscarEnBaseProducto(txtProducto2.getText(), jList4, popupProductoMod, txtProducto2)));
        jList4.addListSelectionListener(e -> {
            if (!jList4.isSelectionEmpty()) { 
                int sel = jList4.getSelectedIndex();
                idProductSelected1 = resultProduct.get(sel).getIdProduct();
                lblProducto1.setText(resultProduct.get(sel).getName_product());
                total_sold_Product = resultProduct.get(sel).getTotal_sold();
                popupProductoMod.setVisible(false); txtProducto2.setText(""); 
            }
        });
        
        setupDocumentFilter(txtCantidad2); setupDocumentFilter(txtPrecioUni2);
        DocumentListener calc2 = createDocListener(() -> realizarOperacion(txtCantidad2, txtPrecioUni2, lblSubTotalProduct2));
        txtCantidad2.getDocument().addDocumentListener(calc2); txtPrecioUni2.getDocument().addDocumentListener(calc2);
        
        btnBuscar.addActionListener(e -> btnBuscarActionPerformed(e));
        btnAgregarCarrito1.addActionListener(e -> btnAgregarCarrito1ActionPerformed(e));
        btnEditar1.addActionListener(e -> btnEditar1ActionPerformed(e));
        btnEliminar1.addActionListener(e -> btnEliminar1ActionPerformed(e));
        btnCancelar2.addActionListener(e -> btnCancelar2ActionPerformed(e));
        btnGuardarVenta2.addActionListener(e -> btnGuardarVenta2ActionPerformed(e));
        btnEliminarVentaTotal.addActionListener(e -> jButton2ActionPerformed(e)); // Eliminar venta completa
        btnCancelarVenta2.addActionListener(e -> btnCancelarVenta2ActionPerformed(e)); // Cerrar modificar
        btnPrintMod.addActionListener(e -> btnPrintModActionPerformed(e));

        jTable4.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){ tableClick(jTable4, e, true); }
        });
    }

    private DocumentListener createDocListener(Runnable action) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { action.run(); }
            public void removeUpdate(DocumentEvent e) { action.run(); }
            public void changedUpdate(DocumentEvent e) { action.run(); }
        };
    }

    private void actualizarFechaOculta() {
        Date date = (Date) dateSpinner.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        txtDay.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        txtMonth.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));

        int yearTwoDigits = cal.get(Calendar.YEAR) % 100;
        txtYear.setText(String.format("%02d", yearTwoDigits));
    }

    // =========================================================
    // 5. MÉTODOS DE LÓGICA (TODOS)
    // =========================================================

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        NewClientePane ncp = new NewClientePane();
        ncp.mostrarDialogoConPanel(this, jComboBox1.getSelectedIndex());
        if(ncp.verificacion){
            int idClient = dr.loadLastIDCustomer();
            sv = dr.SearchCustomer(idClient);
            if(sv !=null){
                 lblNombreCliente.setText(sv.getName());
                 lblDireccion.setText(sv.getAddress());
                 idCustomerSelected = sv.getId(); 
                 total_sold = sv.getTotalSold();
            }
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        NewProductPane npp = new NewProductPane();
        npp.mostrarDialogoConPanel(this);
    }

    private void buscarEnBaseCliente(String Dato){
        if("".equals(Dato)){
            popupCliente.setVisible(false);
        }else{
            String nameCustomer = Dato.trim();
            resultCustomer = dr.SearchCustomer(nameCustomer, jComboBox1.getSelectedItem().toString());
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (SearchValue resultado : resultCustomer) {
                modeloLista.addElement(resultado.getName());
            }
            if (!resultCustomer.isEmpty()) {
                jList1.setModel(modeloLista);
                popupCliente.setPopupSize(txtBusquedaCliente.getWidth(), 120); 
                popupCliente.show(txtBusquedaCliente, 0, txtBusquedaCliente.getHeight());
                txtBusquedaCliente.requestFocus(); 
            } else {
                popupCliente.setVisible(false);
            }
        }
    }

    private void buscarEnBaseProducto(String Dato, JList<String> lista, JPopupMenu popup, JTextField txt){
        if("".equals(Dato)){
            popup.setVisible(false);
        }else{
            String nameProduct = Dato.trim();
            resultProduct = dr.SearchProduct(nameProduct);
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (SearchProduct resultado : resultProduct) {
                modeloLista.addElement(resultado.getName_product());
            }
            if (!resultProduct.isEmpty()) {
                lista.setModel(modeloLista);
                popup.setPopupSize(txt.getWidth(), 120);
                popup.show(txt, 0, txt.getHeight());
                txt.requestFocus();
            } else {
                popup.setVisible(false);
            }
        }
    }

    private void realizarOperacion(JTextField txtC, JTextField txtP, JLabel lblRes) {
        try {
            String texto = txtP.getText();
            String texto2 = txtC.getText();
            if (!texto.isEmpty() && !texto2.isEmpty()) { 
                float numero = Float.parseFloat(texto);
                float resultado = Float.parseFloat(texto2) * numero;
                lblRes.setText(String.format("$ %.2f", resultado).replace(",", "."));
            }
        } catch (NumberFormatException ex) { }
    }

    // --- TAB 1 LOGIC ---
    private void btnGuardarVentaActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        int id_product; float product_quantity, unit_price, sub_total;
        
        if(model.getRowCount() == 0 ){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta ya que ningun producto ha sido seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else if("Aun sin seleccionar".equals(lblNombreCliente.getText()) && "".equals(lblDireccion.getText())){
            JOptionPane.showMessageDialog(null,"No se puede realizar la venta, cliente no seleccionado!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaActual.format(formato);
            float total = Float.parseFloat(lblTotalVenta.getText().replace("$", "").replace(",", "."));
            
            if(dr.addSale(idCustomerSelected, fechaFormateada, total)){
                int lastID = dr.loadLastIDSale();
                for (int i = 0; i < model.getRowCount(); i++) {
                    id_product =  Integer.parseInt(model.getValueAt(i, 0).toString());
                    product_quantity =  Float.parseFloat(model.getValueAt(i, 2).toString());
                    unit_price = Float.parseFloat(model.getValueAt(i, 3).toString().replace("$", ""));
                    sub_total = Float.parseFloat(model.getValueAt(i, 4).toString().replace("$", ""));
                    dr.addDetailSale(lastID, id_product, product_quantity, unit_price, sub_total);
                    float productSold = dr.loadProductSold(id_product);
                    dr.EditProductSold(id_product, product_quantity + productSold);
                }
                dr.addSaleTemp(idCustomerSelected, fechaFormateada, total, lastID);
                loadOrders();
                if (!orderTempsLoad.isEmpty()) loadOrder(0);
                
                clientTextClean(); productCleanText();
                model.setRowCount(0); jTable2.setModel(model);
                lblTotalVenta.setText("$ 0.00");
                sv = null;
            }
        }
    }

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {
        agregarAFila(jTable2, idProductSelected, lblProducto, txtCantidad, txtPrecioUni, lblSubTotalProduct, lblTotalVenta);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
        productCleanText();
    }
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        editarFila(jTable2, rowSelected, idProductSelected, lblProducto, txtCantidad, txtPrecioUni, lblSubTotalProduct, lblTotalVenta);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
        btnAgregarCarrito.setEnabled(true);
        productCleanText();
    }
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarFila(jTable2, rowSelected, lblTotalVenta);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
        btnAgregarCarrito.setEnabled(true);
        productCleanText();
    }
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        productCleanText();
        btnAgregarCarrito.setEnabled(true);
        btnEditar.setVisible(false);
        btnEliminar.setVisible(false);
    }
    private void btnCancelarVentaActionPerformed(java.awt.event.ActionEvent evt) {
        clientTextClean(); productCleanText();
        ((DefaultTableModel)jTable2.getModel()).setRowCount(0);
        lblTotalVenta.setText("0.00");
    }

    // --- TAB 3 LOGIC (MODIFICAR) ---
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            lblError.setText("");
            List<Object> sale =  dr.searchSale(Integer.parseInt(txtNumeroPedido.getText()),jCheckBox1.isSelected());
            if(!sale.isEmpty()){
                pnlContentModificar.setVisible(true); // MOSTRAR CONTENIDO
                id_sale = Integer.parseInt(sale.get(0).toString());
                SearchValue svc = dr.SearchCustomer(Integer.parseInt(sale.get(1).toString()));
                lblNombreCliente2.setText(svc.getName());
                lblDireccion2.setText(svc.getAddress());
                loadSaleDetailTable(Integer.parseInt(sale.get(0).toString()));
                lblTotalVenta2.setText(String.format("$ %.2f", Float.parseFloat(sale.get(2).toString())));
            } else {
                lblError.setText("Pedido no encontrado!!");
                pnlContentModificar.setVisible(false);
            }
        } catch (NumberFormatException e) {
            lblError.setText("Ingrese un número de pedido válido!!");
        }
    }

    private void loadSaleDetailTable(int id_sale){
        List<List<Object>> saleDetail = dr.loadDetailSale(id_sale);
        DefaultTableModel modelo = (DefaultTableModel) jTable4.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : saleDetail) {
            modelo.addRow(new Object[]{list.get(0),dr.SearchNameProduct(Integer.parseInt(list.get(0).toString())),list.get(1),list.get(2),list.get(3)});
        }
        // Crear copia del modelo para calculos internos
        modelTable4 = new DefaultTableModel();
        for (int i = 0; i < modelo.getColumnCount(); i++) modelTable4.addColumn(modelo.getColumnName(i));
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Vector<Object> row = new Vector<>();
            for (int j = 0; j < modelo.getColumnCount(); j++) row.add(modelo.getValueAt(i, j));
            modelTable4.addRow(row);
        }
        jTable4.setModel(modelo);
    }

    private void btnGuardarVenta2ActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        int id_product; float product_quantity, unit_price, sub_total;
        
        if(model.getRowCount() == 0 ){
            JOptionPane.showMessageDialog(null,"Error: Venta vacía", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fechaActual.format(formato);
            float total = Float.parseFloat(lblTotalVenta2.getText().replace("$", "").replace(",", "."));
            
            if(dr.modSale(id_sale, fechaFormateada, total)){
                if(dr.modSaleTemp(id_sale, fechaFormateada, total)){
                    // Restaurar inventario antiguo
                    for(int i = 0; i < modelTable4.getRowCount(); i++){
                        float productSold = dr.loadProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()));
                        if(productSold > 0.0f && dr.boolSearchSaleTemp(id_sale)){
                            productSold = productSold - Float.parseFloat(modelTable4.getValueAt(i, 2).toString());
                            dr.EditProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()), productSold);
                        }
                    }
                    dr.DeleteDetailSale(id_sale);
                    // Guardar nuevo detalle
                    for (int i = 0; i < model.getRowCount(); i++) {
                        id_product =  Integer.parseInt(model.getValueAt(i, 0).toString());
                        product_quantity =  Float.parseFloat(model.getValueAt(i, 2).toString());
                        unit_price = Float.parseFloat(model.getValueAt(i, 3).toString().replace("$", ""));
                        sub_total = Float.parseFloat(model.getValueAt(i, 4).toString().replace("$", ""));
                        dr.addDetailSale(id_sale, id_product, product_quantity, unit_price, sub_total);
                        
                        float productSold = dr.loadProductSold(id_product);
                        dr.EditProductSold(id_product, product_quantity + productSold);
                    }
                    loadOrders();
                    if (!orderTempsLoad.isEmpty()) loadOrder(currentOrderIndex - 1);
                }
                clientTextClean(); productCleanText();
                model.setRowCount(0); jTable4.setModel(model);
                lblTotalVenta2.setText("0.00");
                pnlContentModificar.setVisible(false);
                txtNumeroPedido.setText("");
                JOptionPane.showMessageDialog(null,"Venta modificada con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { // Eliminar Venta
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        for(int i = 0; i < modelTable4.getRowCount(); i++){
            float productSold = dr.loadProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()));
            if(productSold > 0.0f && dr.boolSearchSaleTemp(id_sale)){
                productSold = productSold - Float.parseFloat(modelTable4.getValueAt(i, 2).toString());
                dr.EditProductSold(Integer.parseInt(modelTable4.getValueAt(i, 0).toString()), productSold);
            }
        }
        dr.DeleteDetailSale(id_sale);
        dr.DeleteSaleTemp(id_sale);
        dr.DeleteSale(id_sale);
        loadOrders();
        clientTextClean(); productCleanText();
        model.setRowCount(0); jTable4.setModel(model);
        pnlContentModificar.setVisible(false);
        txtNumeroPedido.setText("");
        JOptionPane.showMessageDialog(null,"Venta eliminada con exito!", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnAgregarCarrito1ActionPerformed(java.awt.event.ActionEvent evt) {
        agregarAFila(jTable4, idProductSelected1, lblProducto1, txtCantidad2, txtPrecioUni2, lblSubTotalProduct2, lblTotalVenta2);
        btnAgregarCarrito1.setEnabled(true); btnEditar1.setVisible(false); btnEliminar1.setVisible(false);
    }
    private void btnEditar1ActionPerformed(java.awt.event.ActionEvent evt) {
        editarFila(jTable4, rowSelected1, idProductSelected1, lblProducto1, txtCantidad2, txtPrecioUni2, lblSubTotalProduct2, lblTotalVenta2);
        btnAgregarCarrito1.setEnabled(true); btnEditar1.setVisible(false); btnEliminar1.setVisible(false);
    }
    private void btnEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarFila(jTable4, rowSelected1, lblTotalVenta2);
        btnAgregarCarrito1.setEnabled(true); btnEditar1.setVisible(false); btnEliminar1.setVisible(false);
    }
    private void btnCancelar2ActionPerformed(java.awt.event.ActionEvent evt) {
        productCleanText(); btnAgregarCarrito1.setEnabled(true); btnEditar1.setVisible(false); btnEliminar1.setVisible(false);
    }
    private void btnCancelarVenta2ActionPerformed(java.awt.event.ActionEvent evt) {
        pnlContentModificar.setVisible(false); txtNumeroPedido.setText("");
    }
    private void btnPrintModActionPerformed(java.awt.event.ActionEvent evt) {
        // Verificar que hay una venta cargada y fecha válida
        if (pnlContentModificar.isVisible() && !"".equals(txtDay.getText()) && !"".equals(txtMonth.getText()) && !"".equals(txtYear.getText())) {
            
            List<String> list = new ArrayList<>();
            List<List<String>> productos = new ArrayList<>();
        
            // 1. Encabezado (Tomado de los labels de MODIFICAR)
            list.add("                 " + lblNombreCliente2.getText());
            list.add("                        " + lblDireccion2.getText());
            list.add("");
            list.add("");
            list.add("");
            
            // 2. Productos (Leídos de la tabla de MODIFICAR - jTable4)
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            int cupo = 18;
        
            for (int i = 0; i < model.getRowCount(); i++) {
                List<String> productDetail = new ArrayList<>();
                String productName = model.getValueAt(i, 1).toString();
                
                // Limpieza de valores
                String puStr = model.getValueAt(i, 3).toString().replace("$", "").replace(",", ".").trim();
                String ptStr = model.getValueAt(i, 4).toString().replace("$", "").replace(",", ".").trim();
                
                double pu = Double.parseDouble(puStr);
                double pt = Double.parseDouble(ptStr);

                productDetail.add(quitarCerosNoSignificativos(Float.parseFloat(model.getValueAt(i, 2).toString())));
                productDetail.add(productName);
                
                // Formato $
                productDetail.add(String.format("$ %.3f", pu).replace(",", "."));
                productDetail.add(String.format("$ %.3f", pt).replace(",", "."));
            
                productos.add(productDetail);
                list.add("");
            }
            
            // 3. Rellenar espacios vacíos (para mantener el largo de la factura)
            if (model.getRowCount() < 18) {
                cupo = cupo - model.getRowCount();
                for (int i = 0; i < cupo; i++) {
                    List<String> productDetail = new ArrayList<>();
                    productDetail.add(""); productDetail.add(""); productDetail.add(""); productDetail.add("");
                    productos.add(productDetail);
                    list.add("");
                }
            }
            
            // 4. Total (Tomado del label de MODIFICAR)
            String totalRaw = lblTotalVenta2.getText().replace("$", "").replace(",", ".").trim();
            double totalVal = Double.parseDouble(totalRaw);
            String total = String.format("$ %.2f", totalVal).replace(",", ".");
            
            list.add(total);
            
            // Usamos el ID de la venta actual (id_sale) como número de pedido
            String nOrderString = String.valueOf(id_sale);
            
            // Llamar a tu clase de impresión
            ImprimirFactura printOrder = new ImprimirFactura(list, productos, nOrderString, txtDay.getText(), txtMonth.getText(), txtYear.getText(), "1"); // "1" es pagina dummy
            printOrder.imprimir();
            
        } else {
            if (!pnlContentModificar.isVisible()) {
                JOptionPane.showMessageDialog(null, "Primero debes buscar y cargar un pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Verifica la fecha de entrega.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- UTILS COMUNES PARA TABLAS ---
    private void agregarAFila(JTable table, int idProd, JLabel lblProd, JTextField txtCant, JTextField txtPrec, JLabel lblSub, JLabel lblTot) {
        if(table.getRowCount() >= 18){
            JOptionPane.showMessageDialog(null,"Máximo 18 elementos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }else{
            if(!"Aun no seleccionado".equals(lblProd.getText()) && !"".equals(txtCant.getText())){
                float total = 0;
                DefaultTableModel model = (DefaultTableModel)table.getModel();
                String subLimpio = lblSub.getText().replace("$", "").replace(",", ".").trim();
                model.addRow(new Object[]{idProd, lblProd.getText(), txtCant.getText(), txtPrec.getText(), subLimpio});
                
                for (int i = 0; i < model.getRowCount(); i++) {
                    total += Float.parseFloat(model.getValueAt(i, 4).toString().replace("$", "").replace(",", ".").trim());
                }
                lblTot.setText(String.format("$ %.2f", total));
                productCleanText();
                table.setModel(model);
            }
        }
    }
    private void editarFila(JTable table, int row, int idProd, JLabel lblProd, JTextField txtCant, JTextField txtPrec, JLabel lblSub, JLabel lblTot) {
        if(row >= 0){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            String subLimpio = lblSub.getText().replace("$", "").replace(",", ".").trim();
            model.setValueAt(idProd, row, 0);
            model.setValueAt(lblProd.getText(), row, 1);
            model.setValueAt(txtCant.getText(), row, 2);
            model.setValueAt(txtPrec.getText(), row, 3);
            model.setValueAt(subLimpio, row, 4);
            
            for (int i = 0; i < model.getRowCount(); i++) total += Float.parseFloat(model.getValueAt(i, 4).toString().replace("$", "").replace(",", ".").trim());
            lblTot.setText(String.format("$ %.2f", total));
            productCleanText();
            table.setModel(model);
        }
    }
    private void eliminarFila(JTable table, int row, JLabel lblTot) {
        if(row >= 0){
            float total = 0;
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.removeRow(row);
            for (int i = 0; i < model.getRowCount(); i++) total += Float.parseFloat(model.getValueAt(i, 4).toString().replace("$", "").replace(",", ".").trim());
            lblTot.setText(String.format("$ %.2f", total));
            table.setModel(model);
            productCleanText();
        }
    }
    
    private void tableClick(JTable table, MouseEvent e, boolean isMod) {
        if(e.getClickCount() == 2){
            int row = table.getSelectedRow();
            if(row >= 0){
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if(isMod) {
                    btnEditar1.setVisible(true); btnAgregarCarrito1.setEnabled(false); btnEliminar1.setVisible(false);
                    idProductSelected1 = Integer.parseInt(model.getValueAt(row, 0).toString());
                    lblProducto1.setText(model.getValueAt(row, 1).toString());
                    txtCantidad2.setText(model.getValueAt(row, 2).toString());
                    txtPrecioUni2.setText(model.getValueAt(row, 3).toString().replace("$", ""));
                    rowSelected1 = row;
                } else {
                    btnEditar.setVisible(true); btnAgregarCarrito.setEnabled(false); btnEliminar.setVisible(false);
                    idProductSelected = Integer.parseInt(model.getValueAt(row, 0).toString());
                    lblProducto.setText(model.getValueAt(row, 1).toString());
                    txtCantidad.setText(model.getValueAt(row, 2).toString());
                    txtPrecioUni.setText(model.getValueAt(row, 3).toString().replace("$", ""));
                    rowSelected = row;
                }
            }
        }else if(e.getClickCount() == 3) {
            int row = table.getSelectedRow();
            if(row >= 0){
                if(isMod) { btnEditar1.setVisible(false); btnEliminar1.setVisible(true); rowSelected1 = row; }
                else { btnEditar.setVisible(false); btnEliminar.setVisible(true); rowSelected = row; }
            }
        }else{
            if(isMod) btnAgregarCarrito1.setEnabled(true);
            else btnAgregarCarrito.setEnabled(true);
        }
    }

    // --- RESTO DE METODOS ---
    private void loadOrders(){
       orderTempsLoad = dr.loadOrdersTemp();
       if(!orderTempsLoad.isEmpty()){
            lblNumberOrder.setText(String.valueOf(orderTempsLoad.size()));
            lblMax.setText(String.valueOf(orderTempsLoad.size()));
            float totalSold = 0.00f;
            for (List<Object> order : orderTempsLoad) totalSold += Float.parseFloat(order.get(2).toString());
            lblTotalSold.setText(String.format("%.2f", totalSold)); 
            lblActual.setText(String.valueOf(currentOrderIndex));
       }
    }
    private void loadOrder(int nOrder){
       if(!orderTempsLoad.isEmpty() && nOrder >= 0){
            SearchValue svc = dr.SearchCustomer(Integer.parseInt(orderTempsLoad.get(nOrder).get(0).toString()));
            lblNombreCliente6.setText(svc.getName());
            lblDireccion6.setText(svc.getAddress());
            loadSaleDetailTable2(Integer.parseInt(orderTempsLoad.get(nOrder).get(3).toString()));
            lblTotalVenta3.setText("$ " + orderTempsLoad.get(nOrder).get(2).toString());
       }
    }
    private void loadSaleDetailTable2(int id_sale){
        List<List<Object>> saleDetail = dr.loadDetailSale(id_sale);
        DefaultTableModel modelo = (DefaultTableModel) jTable5.getModel();
        modelo.setRowCount(0);
        for (List<Object> list : saleDetail) {
            modelo.addRow(new Object[]{list.get(0),dr.SearchNameProduct(Integer.parseInt(list.get(0).toString())),quitarCerosNoSignificativos(Float.parseFloat(list.get(1).toString())),list.get(2),list.get(3)});
        }
        jTable5.setModel(modelo);
        lblTotalProductos.setText("Cant. Productos: " + modelo.getRowCount());
    }
    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {
        int c = Integer.parseInt(lblActual.getText()), m = Integer.parseInt(lblMax.getText());
        if (c < m) { loadOrder(c); lblActual.setText(String.valueOf(c + 1)); currentOrderIndex = c+1; }
        else{ JOptionPane.showMessageDialog(null,"No se puede avanzar mas!!", "ERROR", JOptionPane.ERROR_MESSAGE); }
    }
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        int c = Integer.parseInt(lblActual.getText());
        if (c > 1) { loadOrder(c - 2); lblActual.setText(String.valueOf(c - 1)); currentOrderIndex = c-1; }
        else{ JOptionPane.showMessageDialog(null,"No se puede retroceder mas!!", "ERROR", JOptionPane.ERROR_MESSAGE); }
    }
    private void btnRestablecerActionPerformed(java.awt.event.ActionEvent evt) {
        if(JOptionPane.showConfirmDialog(null, "Desea restablecer las ventas?","Verificacion",JOptionPane.YES_NO_OPTION) == 0){
            if(dr.deleteTableSaleTemp() && dr.createTableSaleTemp()){
                dr.restoreSoldProduct(); loadOrders(); loadOrder(0);
                JOptionPane.showMessageDialog(null,"Exito", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    private void btnPrintCurrentActionPerformed(java.awt.event.ActionEvent evt) {
        if(!"".equals(txtDay.getText()) &&  !"".equals(txtMonth.getText()) && !"".equals(txtYear.getText())){
            int order = Integer.parseInt(lblActual.getText());
            int cupo = 18;
            
            // Obtenemos el cliente del pedido actual
            SearchValue svc = dr.SearchCustomer(Integer.parseInt(orderTempsLoad.get(order-1).get(0).toString()));
            
            List<String> list = new ArrayList<>();
            List<List<String>> productos = new ArrayList<>();
        
            // Encabezado
            list.add("                 "+svc.getName());
            list.add("                        "+svc.getAddress());
            list.add("");
            list.add("");
            list.add("");
            
            DefaultTableModel model = (DefaultTableModel)jTable5.getModel();
        
            // Recorremos la tabla visual para obtener los productos
            for (int i = 0; i < model.getRowCount(); i++) {
                List<String> productDetail = new ArrayList<>();
                String productName = model.getValueAt(i,1).toString();
                
                // Leemos los valores de la tabla, limpiamos $ y , para poder convertir a Double
                String puStr = model.getValueAt(i, 3).toString().replace("$", "").replace(",", ".").trim();
                String ptStr = model.getValueAt(i, 4).toString().replace("$", "").replace(",", ".").trim();
                
                double pu = Double.parseDouble(puStr);
                double pt = Double.parseDouble(ptStr);

                productDetail.add(quitarCerosNoSignificativos(Float.parseFloat(model.getValueAt(i, 2).toString())));
                productDetail.add(productName);
                
                // --- CAMBIO AQUÍ: Formato Moneda ($ 0.00) ---
                productDetail.add(String.format("$ %.2f", pu).replace(",", "."));
                productDetail.add(String.format("$ %.2f", pt).replace(",", "."));
            
                productos.add(productDetail);
                list.add("");
            }
            
            // Rellenar espacios vacíos
            if(model.getRowCount() < 18 ){
                cupo = cupo - model.getRowCount();
                for (int i = 0; i < cupo; i++) {
                    List<String> productDetail = new ArrayList<>();
                    productDetail.add("");
                    productDetail.add("");
                    productDetail.add("");
                    productDetail.add("");
                    productos.add(productDetail);
                    list.add("");
                }
            }
            
            // --- CAMBIO AQUÍ: Total de la Factura con formato ---
            String totalRaw = orderTempsLoad.get(order-1).get(2).toString().replace("$", "").replace(",", ".").trim();
            double totalVal = Double.parseDouble(totalRaw);
            String total = String.format("$ %.2f", totalVal).replace(",", ".");
            
            list.add(total);
            
            String nOrderString = orderTempsLoad.get(order-1).get(3).toString();
            
            ImprimirFactura printOrder = new ImprimirFactura(list, productos, nOrderString, txtDay.getText(), txtMonth.getText(), txtYear.getText(), lblActual.getText());
            printOrder.imprimir();
            
        } else {
            JOptionPane.showMessageDialog(null,"Debe de ingresar la fecha de entrega!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnPrintAllActionPerformed(java.awt.event.ActionEvent evt) {
        if (!"".equals(txtDay.getText()) && !"".equals(txtMonth.getText()) && !"".equals(txtYear.getText())) {
            List<List<String>> fracturas = new ArrayList<>();
            List<List<List<String>>> Product = new ArrayList<>();
            List<String> NOrders = new ArrayList<>();
            
            for (List<Object> obj : orderTempsLoad) {
                int cupo = 18;
                List<String> list = new ArrayList<>();
                SearchValue client = dr.SearchCustomer(Integer.parseInt(obj.get(0).toString()));
                
                // Encabezado
                list.add("                 " + client.getName());
                list.add("                        " + client.getAddress());
                list.add("");
                list.add("");
                list.add("");
                
                // Productos
                List<List<Object>> product = dr.loadDetailSale(Integer.parseInt(obj.get(3).toString()));
                List<List<String>> productFactura = new ArrayList<>();
                
                for (int i = 0; i < product.size(); i++) {
                    List<String> productDetail = new ArrayList<>();
                    String productName = dr.SearchNameProduct(Integer.parseInt(product.get(i).get(0).toString()));
                    
                    // Convertimos a Double para dar formato
                    double precioUnit = Double.parseDouble(product.get(i).get(2).toString());
                    double subTotal = Double.parseDouble(product.get(i).get(3).toString());
                    
                    productDetail.add(quitarCerosNoSignificativos(Float.parseFloat(product.get(i).get(1).toString())));
                    productDetail.add(productName);
                    
                    // --- CAMBIO AQUÍ: Formato Moneda ($ 0.00) ---
                    productDetail.add(String.format("$ %.3f", precioUnit).replace(",", "."));
                    productDetail.add(String.format("$ %.3f", subTotal).replace(",", "."));
                    
                    productFactura.add(productDetail);
                    list.add("");
                }
                
                // Rellenar espacios vacíos si son menos de 18 productos
                if (product.size() < 18) {
                    cupo = cupo - product.size();
                    for (int i = 0; i < cupo; i++) {
                        List<String> productDetail = new ArrayList<>();
                        productDetail.add("");
                        productDetail.add("");
                        productDetail.add("");
                        productDetail.add("");
                        productFactura.add(productDetail);
                        list.add("");
                    }
                }
                
                Product.add(productFactura);
                
                // --- CAMBIO AQUÍ: Total de la Factura con formato ---
                double totalVenta = Double.parseDouble(obj.get(2).toString());
                String total = String.format("$ %.2f", totalVenta).replace(",", ".");
                
                list.add(total);
                NOrders.add(obj.get(3).toString());
                fracturas.add(list);
            }
            
            ImprimirTodo it = new ImprimirTodo(fracturas, Product, NOrders, txtDay.getText(), txtMonth.getText(), txtYear.getText());
            it.imprimir();

        } else {
            JOptionPane.showMessageDialog(null, "Debe de ingresar la fecha de entrega!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clientTextClean(){
         txtBusquedaCliente.setText(""); lblNombreCliente.setText(""); lblDireccion.setText("");
         lblNombreCliente2.setText("---"); lblDireccion2.setText("---");
    }
    private void productCleanText(){
         txtProducto.setText(""); lblProducto.setText("Aun no seleccionado"); txtCantidad.setText(""); txtPrecioUni.setText(""); lblSubTotalProduct.setText("0.00");
         txtProducto2.setText(""); lblProducto1.setText("Aun no seleccionado"); txtCantidad2.setText(""); txtPrecioUni2.setText(""); lblSubTotalProduct2.setText("0.00");
    }
    private static String quitarCerosNoSignificativos(float numero) {
        return String.valueOf(numero).replaceAll("\\.?0*$", "");
    }
    private void setupDocumentFilter(JTextField txtField) {
        ((AbstractDocument) txtField.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (esNumeroConDecimal(text, txtField.getText())) super.insertString(fb, offset, text, attr);
            }
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (esNumeroConDecimal(text, txtField.getText())) super.replace(fb, offset, length, text, attrs);
            }
        });
    }
    private boolean esNumeroConDecimal(String texto, String currentText) {
        if (texto.equals(".")) return !currentText.contains(".");
        return texto.matches("\\d*");
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