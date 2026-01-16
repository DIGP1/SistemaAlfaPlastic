package forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;      

import javax.imageio.ImageIO;     
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class PrincipalModerno extends JFrame {

    // Componentes
    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private JScrollPane scrollPane;
    
    // Instancias de tus formularios
    private VentasModernas vn = new VentasModernas();
    // private ConsolidadoModerno consolidado = new ConsolidadoModerno(); 
    // private ClientesModerno cl = new ClientesModerno(); 

    // --- PALETA DE COLORES ---
    private final Color COLOR_SIDEBAR = new Color(30, 30, 35);    // Fondo oscuro
    
    // Gris azulado para botones normales
    private final Color COLOR_BTN_NORMAL = new Color(55, 60, 68); 
    private final Color COLOR_BTN_HOVER  = new Color(75, 80, 90); // Gris más claro al pasar mouse
    
    // Colores para botón Salir
    private final Color COLOR_BTN_SALIR  = new Color(192, 57, 43); // Rojo oscuro plano
    private final Color COLOR_SALIR_HOVER= new Color(231, 76, 60); // Rojo brillante
    
    private final Color COLOR_TEXTO = new Color(245, 245, 245); 

    public PrincipalModerno() {
        configurarVentana();
        inicializarComponentes();
        loadForm(vn); // Cargar inicio
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Ventas");
        setSize(1280, 720); // Ventana un poco más grande por defecto
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // --- 1. SIDEBAR ---
        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        // Ancho fijo de 250px, alto dinámico
        pnlSidebar.setPreferredSize(new Dimension(250, 0)); 
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));

        // -- LOGO / TÍTULO --
        JPanel pnlLogo = new JPanel();
        pnlLogo.setBackground(COLOR_SIDEBAR);
        pnlLogo.setMaximumSize(new Dimension(250, 120)); 
        pnlLogo.setLayout(new GridBagLayout()); 
        
        JLabel lblTitulo = new JLabel("AlfaPlastic");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pnlLogo.add(lblTitulo);
        
        pnlSidebar.add(pnlLogo);
        
        // Espacio debajo del logo
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // -- BOTONES DEL MENÚ --
        // NOTA: Asegúrate que las carpetas "resources" e "imagenes" existan dentro de src
        
        JButton btnVender = crearBotonMenu("Vender", "/shopping-bag.png", COLOR_BTN_NORMAL, COLOR_BTN_HOVER);
        pnlSidebar.add(btnVender);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10))); 
        
        JButton btnConsolidado = crearBotonMenu("Consolidado", "/logistics.png", COLOR_BTN_NORMAL, COLOR_BTN_HOVER);
        pnlSidebar.add(btnConsolidado);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton btnClientes = crearBotonMenu("Clientes", "/people.png", COLOR_BTN_NORMAL, COLOR_BTN_HOVER);
        pnlSidebar.add(btnClientes);
        
        // Empuja el botón de salir hacia el final
        pnlSidebar.add(Box.createVerticalGlue()); 
        
        // -- BOTÓN SALIR (ROJO) --
        JButton btnSalir = crearBotonMenu("Cerrar", "/exit.png", COLOR_BTN_SALIR, COLOR_SALIR_HOVER);
        pnlSidebar.add(btnSalir);
        
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 0))); 

        // -- EVENTOS --
        btnVender.addActionListener(e -> loadForm(vn));
        
        btnConsolidado.addActionListener(e -> {
             ConsolidadoModerno consolidado = new ConsolidadoModerno();
             loadForm(consolidado);
        });
        
        btnClientes.addActionListener(e -> {
             ClientesModerno cl = new ClientesModerno();
             loadForm(cl);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // --- 2. CONTENIDO PRINCIPAL ---
        pnlContent = new JPanel();
        pnlContent.setBackground(new Color(240, 240, 245)); 
        pnlContent.setLayout(new BorderLayout());

        scrollPane = new JScrollPane(pnlContent);
        scrollPane.setBorder(null);
        
        add(pnlSidebar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Método mejorado con Opción 2 (ImageIO)
     */
    private JButton crearBotonMenu(String texto, String rutaIcono, Color bgNormal, Color bgHover) {
        JButton btn = new JButton();

        // --- LÓGICA DE ICONO SEGURA Y REDIMENSIONADA ---
        if (rutaIcono != null && !rutaIcono.isEmpty()) {
            try {
                // 1. Cargar como flujo de datos (funciona dentro de JARs)
                InputStream is = getClass().getResourceAsStream(rutaIcono);
                
                if (is != null) {
                    // 2. Leer la imagen
                    Image imgRaw = ImageIO.read(is);
                    
                    // 3. Redimensionar a 24x24 para uniformidad
                    Image imgScaled = imgRaw.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    
                    // 4. Asignar
                    btn.setIcon(new ImageIcon(imgScaled));
                    btn.setText("  " + texto); // Espacio estético
                } else {
                    System.err.println("No se encontró la imagen: " + rutaIcono);
                    btn.setText(texto);
                }
            } catch (Exception e) {
                System.err.println("Error cargando imagen (" + rutaIcono + "): " + e.getMessage());
                btn.setText(texto);
            }
        } else {
             btn.setText(texto);
        }

        // --- ESTILO FLAT (PLANO) MODERNO ---
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(bgNormal);
        
        // 1. QUITAR BORDES Y EFECTOS 3D
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true); 

        // 2. DIMENSIONES
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        btn.setPreferredSize(new Dimension(250, 60));
        
        // 3. ALINEACIÓN
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- EFECTO HOVER ---
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bgHover);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgNormal);
            }
        });
        
        return btn;
    }

    private void loadForm(JPanel formObject) {
        pnlContent.removeAll();
        pnlContent.add(formObject, BorderLayout.CENTER); 
        pnlContent.revalidate();
        pnlContent.repaint();
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) { }
        
        java.awt.EventQueue.invokeLater(() -> new PrincipalModerno().setVisible(true));
    }
}