/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackupsForms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import logic.Datarequest;

/**
 *
 * @author Diego
 */
public class NewProductPane {
     private boolean validado = false;
    private Datarequest dr = new Datarequest();
    private JTextField txtUsername;
    private JComboBox<String> cbRuta;
    private JTextField txtPrecio;
    private JPanel panelPrincipal;
    private boolean puntoDecimalIngresado = false;
    public void setValidacion(boolean a){
        this.validado = a;
    }
    public boolean getValidacion(){
        return this.validado;
    }
    
public void mostrarDialogoConPanel(JPanel parentFrame) {
        JLabel labelUsuario = new JLabel("Nombre del producto:");
        txtUsername = new JTextField();

        JLabel labelRuta = new JLabel("Proveedor:");
        cbRuta = new JComboBox<>();
        loadComboBoxProvider(cbRuta);

        JLabel labelDireccion = new JLabel("Precio de compra:");
        txtPrecio = new JTextField();

        // Crear el panel principal con GridBagLayout
        panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes

        // Configurar GridBagConstraints para el Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelPrincipal.add(labelUsuario, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(txtUsername, gbc);

        // Configurar GridBagConstraints para la Ruta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(labelRuta, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(cbRuta, gbc);

        // Configurar GridBagConstraints para la Dirección
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panelPrincipal.add(labelDireccion, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(txtPrecio, gbc);

        // Configurar GridBagConstraints para los botones
        
        JButton[] customButtons = {
            new JButton("Agregar"),
            new JButton("Cancelar")
        };
        
        customButtons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            if (!"".equals(txtUsername.getText()) && !"".equals(txtPrecio.getText())) {
                String[] provider = cbRuta.getSelectedItem().toString().split("-");
                if(dr.registerProduct(txtUsername.getText(), 0.00f,Integer.parseInt(provider[0]) , Float.parseFloat(txtPrecio.getText()))){
                        setValidacion(true);
                        JOptionPane.showMessageDialog(null, "Producto agregado con exito!!!", "Información",JOptionPane.INFORMATION_MESSAGE);
                        Window window = SwingUtilities.getWindowAncestor(panelPrincipal);
                        if (window instanceof JDialog) {
                        JDialog dialog = (JDialog) window;
                        dialog.dispose();  // Cierra el diálogo
                        }
                   }else{
                       JOptionPane.showMessageDialog(null, "Error al ingresar el producto, intentelo nuevamente", "Error",JOptionPane.ERROR_MESSAGE);
                   }
            }else{
                JOptionPane.showMessageDialog(null,"Debe de llenar todos los campos!!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            }
        });
        customButtons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setValidacion(false);
                 Window window = SwingUtilities.getWindowAncestor(panelPrincipal);
                if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                dialog.dispose();  // Cierra el diálogo
        }
            }
        });
        ((AbstractDocument) txtPrecio.getDocument()).setDocumentFilter(new DocumentFilter() {
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
                    if (!puntoDecimalIngresado && !txtPrecio.getText().contains(".")) {
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

        int opcion = JOptionPane.showOptionDialog(parentFrame, panelPrincipal, "Ingreso nuevo producto",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, customButtons, customButtons[0]);
    }
        private void loadComboBoxProvider(JComboBox comboBox){
            List<List<Object>> provider = dr.loadProviders();
            comboBox.removeAllItems();
            for (List<Object> list : provider) {
                comboBox.addItem(list.get(0) + "-"+list.get(1));
            }
        }
}
