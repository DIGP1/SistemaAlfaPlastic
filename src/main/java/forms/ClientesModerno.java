package forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logic.Datarequest;

public class ClientesModerno extends JPanel {

    // ==========================================
    // 1. VARIABLES DE LÓGICA
    // ==========================================
    private int idSelectedClient = 0;
    private int rowSelected = -1;
    private final Datarequest dr = new Datarequest();

    // --- COMPONENTES VISUALES ---
    private JTable jTable1;
    private JTextField txtNombre, txtBusqueda;
    private JTextArea taDireccion;
    private JComboBox<String> cbRuta, cbFiltro;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;

    // --- DISEÑO ---
    private JPanel pnlFormulario; // Panel Izquierdo
    private JPanel pnlTabla;      // Panel Derecho

    // Colores
    private final Color COLOR_BG = new Color(245, 245, 250);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BTN = new Color(55, 60, 68);
    private final Color COLOR_ACCENT = new Color(13, 110, 253); 
    private final Color COLOR_DANGER = new Color(220, 53, 69);

    public ClientesModerno() {
        initComponentsModernos();
        initLogicEvents();
        
        // Carga inicial
        loadTable();
        
        // Estado inicial botones
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    // ==========================================
    // 2. DISEÑO VISUAL
    // ==========================================
    private void initComponentsModernos() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- 1. PANEL SUPERIOR (Buscador y Filtro) ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(COLOR_PANEL);
        pnlTop.setBorder(new LineBorder(new Color(220,220,220)));
        
        pnlTop.add(new JLabel("Buscar Cliente:"));
        txtBusqueda = new JTextField(20); estilizarCampo(txtBusqueda);
        pnlTop.add(txtBusqueda);
        
        pnlTop.add(Box.createHorizontalStrut(20)); // Separador
        
        pnlTop.add(new JLabel("Filtrar por Ruta:"));
        cbFiltro = new JComboBox<>(new String[] { "Nombre", "Direccion", "Ruta"});
        pnlTop.add(cbFiltro);
        
        add(pnlTop, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL (Split Formulario / Tabla) ---
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(COLOR_BG);
        GridBagConstraints gc = new GridBagConstraints();

        // --- IZQUIERDA: FORMULARIO ---
        pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBackground(COLOR_PANEL);
        pnlFormulario.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Datos del Cliente"));
        pnlFormulario.setPreferredSize(new Dimension(300, 0)); // Ancho fijo
        
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 10, 5, 10); 
        f.fill = GridBagConstraints.HORIZONTAL; 
        f.anchor = GridBagConstraints.WEST;
        
        // Nombre
        f.gridx=0; f.gridy=0; f.weightx=1.0;
        pnlFormulario.add(new JLabel("Nombre Completo:"), f);
        txtNombre = new JTextField(); estilizarCampo(txtNombre);
        f.gridy=1; pnlFormulario.add(txtNombre, f);
        
        // Dirección (TextArea con Scroll)
        f.gridy=2; pnlFormulario.add(new JLabel("Dirección:"), f);
        taDireccion = new JTextArea(3, 20); 
        taDireccion.setLineWrap(true); taDireccion.setWrapStyleWord(true);
        JScrollPane scrollDir = new JScrollPane(taDireccion);
        estilizarScrollArea(scrollDir);
        f.gridy=3; pnlFormulario.add(scrollDir, f);
        
        // Datos de Entrega
        f.gridy=4; pnlFormulario.add(new JLabel("Datos de Entrega / Referencia:"), f);
        f.gridy=5; pnlFormulario.add(Box.createVerticalStrut(10), f);
        
        // Ruta
        f.gridy=6; pnlFormulario.add(new JLabel("Ruta Asignada:"), f);
        cbRuta = new JComboBox<>(new String[] { "US-SEBT", "US-SJNC", "US-OCTSA", "US-JPSN" });
        f.gridy=7; pnlFormulario.add(cbRuta, f);
        
        // Botones de Acción
        f.gridy=8;
        f.insets = new Insets(15, 10, 5, 10); // Más margen arriba
        JPanel pnlBtns = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlBtns.setOpaque(false);
        
        btnAgregar = new JButton("Guardar"); estilizarBoton(btnAgregar, COLOR_ACCENT);
        btnEditar = new JButton("Actualizar"); estilizarBoton(btnEditar, new Color(255, 193, 7)); btnEditar.setForeground(Color.BLACK);
        btnEliminar = new JButton("Eliminar"); estilizarBoton(btnEliminar, COLOR_DANGER);
        btnLimpiar = new JButton("Limpiar"); estilizarBoton(btnLimpiar, new Color(108, 117, 125));
        
        pnlBtns.add(btnAgregar); pnlBtns.add(btnEditar);
        pnlBtns.add(btnEliminar); pnlBtns.add(btnLimpiar);
        pnlFormulario.add(pnlBtns, f);
        
        // Espaciador final
        f.gridy=9; f.weighty=1.0; 
        pnlFormulario.add(Box.createVerticalGlue(), f);

        // --- DERECHA: TABLA ---
        pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(new EmptyBorder(0, 10, 0, 0));
        pnlTabla.setBackground(COLOR_BG);
        
        jTable1 = new JTable(); 
        jTable1.setRowHeight(25);
        jTable1.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "ID", "Nombre", "Dirección", "Ruta", "Total Comprado" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });

        
        configurarTabla(jTable1);
        
        JScrollPane scrollTable = new JScrollPane(jTable1);
        scrollTable.setPreferredSize(new Dimension(0, 200)); // Evita scroll en ventana pequeña
        
        pnlTabla.add(new JLabel("Listado de Clientes:"), BorderLayout.NORTH);
        pnlTabla.add(scrollTable, BorderLayout.CENTER);

        // Agregar al Layout Principal
        gc.gridx=0; gc.gridy=0; gc.weightx=0.0; gc.weighty=1.0; gc.fill = GridBagConstraints.VERTICAL;
        pnlCenter.add(pnlFormulario, gc);
        
        gc.gridx=1; gc.weightx=1.0; gc.fill = GridBagConstraints.BOTH;
        pnlCenter.add(pnlTabla, gc);
        
        add(pnlCenter, BorderLayout.CENTER);
    }
    
    private void configurarTabla(JTable tabla) {
        // ID Oculto
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        
        // Anchos
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tabla.getColumnModel().getColumn(2).setPreferredWidth(350); // Dirección
        tabla.getColumnModel().getColumn(3).setPreferredWidth(70); // Entrega
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);  // Ruta

        // Centrar columna 3 (Ruta)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            {
            setHorizontalAlignment(JLabel.CENTER);
            }
            @Override
            protected void setValue(Object value) {
            if (value != null) {
                try {
                float val = Float.parseFloat(value.toString());
                value = String.format("$ %.2f", val);
                
                } catch(Exception e){}
            }
            super.setValue(value);
            }
        });
    }

    // ==========================================
    // 3. LOGICA Y EVENTOS
    // ==========================================
    private void initLogicEvents() {
        // Botones
        btnAgregar.addActionListener(e -> btnAgregarActionPerformed(e));
        btnEditar.addActionListener(e -> btnEditarActionPerformed(e));
        btnEliminar.addActionListener(e -> btnEliminarActionPerformed(e));
        btnLimpiar.addActionListener(e -> cleanText());
        
        // Filtros
        cbFiltro.addActionListener(e -> loadTable());
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { loadTable(); }
            public void removeUpdate(DocumentEvent e) { loadTable(); }
            public void changedUpdate(DocumentEvent e) { loadTable(); }
        });
        
        // Selección en Tabla
        jTable1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                // Click simple carga datos
                if(e.getClickCount() == 2 || e.getClickCount() == 3){
                    int row = jTable1.getSelectedRow();
                    if(row >= 0){
                        btnAgregar.setEnabled(false);
                        btnEditar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                        
                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        idSelectedClient = Integer.parseInt(model.getValueAt(row, 0).toString());
                        
                        txtNombre.setText(model.getValueAt(row, 1).toString());
                        taDireccion.setText(model.getValueAt(row, 2).toString());
                        
                        // Seleccionar Ruta en el combo
                        String rutaTabla = model.getValueAt(row, 3).toString();
                        cbRuta.setSelectedItem(rutaTabla);
                        
                        rowSelected = row;
                    }
                }
            }
        });
    }

    // --- MÉTODOS CRUD ---

    private void btnAgregarActionPerformed(ActionEvent evt) {
        if (!"".equals(txtNombre.getText()) && !"".equals(taDireccion.getText())) {
            if (dr.registerClient(txtNombre.getText(), taDireccion.getText(), cbRuta.getSelectedItem().toString(), 0.00f)) {
                JOptionPane.showMessageDialog(null, "Cliente registrado con éxito");
                loadTable();
                cleanText();
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nombre y Dirección son obligatorios", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnEditarActionPerformed(ActionEvent evt) {
        if (!"".equals(txtNombre.getText()) && !"".equals(taDireccion.getText())) {
            if (dr.EditClient(txtNombre.getText(), taDireccion.getText(), cbRuta.getSelectedItem().toString(), 0.00f, idSelectedClient)) {
                JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito");
                loadTable();
                cleanText();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un cliente para editar", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnEliminarActionPerformed(ActionEvent evt) {
        if (idSelectedClient != 0) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas eliminar a " + txtNombre.getText() + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                if (dr.DeleteClient(idSelectedClient)) {
                    JOptionPane.showMessageDialog(null, "Cliente eliminado");
                    loadTable();
                    cleanText();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar (Posiblemente tenga ventas asociadas)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void loadTable() {
        String busqueda = txtBusqueda.getText();
        String filtro = cbFiltro.getSelectedItem().toString();
        
        String nameColumn = "";
        switch (filtro) {
            case "Nombre" -> nameColumn = "fullname";
            case "Direccion" -> nameColumn = "address";
             case "Ruta" -> nameColumn = "rute";
            default -> nameColumn = "fullname";
        }
        
        List<List<Object>> clients = dr.SearchClient(busqueda, nameColumn);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        for (List<Object> client : clients) {
            // Se asume orden: ID, Nombre, Dirección, Entrega, Ruta
            model.addRow(new Object[]{
                client.get(0), 
                client.get(1), 
                client.get(2), 
                client.get(3), // Ojo: Verifica si en tu query el índice 4 es Entrega
                client.get(4), // Ojo: Verifica si en tu query el índice 3 es Ruta
            });
        }
        jTable1.setModel(model);
    }

    private void cleanText() {
        txtNombre.setText("");
        taDireccion.setText("");
        cbRuta.setSelectedIndex(0);
        idSelectedClient = 0;
        rowSelected = -1;
        
        btnAgregar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        jTable1.clearSelection();
    }

    // --- ESTILOS ---
    private void estilizarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
    }
    private void estilizarScrollArea(JScrollPane scroll) {
        scroll.setBorder(new LineBorder(Color.GRAY, 1));
    }
    private void estilizarBoton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}