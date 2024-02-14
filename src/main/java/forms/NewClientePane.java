/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forms;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import logic.Datarequest;

/**
 *
 * @author Diego
 */
public class NewClientePane {
    private Datarequest dr = new Datarequest();
        private boolean validado = false;
        private JTextField txtUsername;
        private JComboBox<String> cbRuta;
        private JTextArea textArea;
        private JPanel panelPrincipal;
        public void setValidacion(boolean a){
        this.validado = a;
    }
    public boolean getValidacion(){
        return this.validado;
    }
    
public void mostrarDialogoConPanel(JPanel parentFrame, int itemSelectecCB) {
 JLabel labelUsuario = new JLabel("Usuario:");
  txtUsername = new JTextField();

        DefaultComboBoxModel<String> modelCombo = new DefaultComboBoxModel<>();
        modelCombo.addElement("US-SEBT");
        modelCombo.addElement("US-SJNC");
        modelCombo.addElement("US-OCTSA");
        modelCombo.addElement("US-JPSN");
        JLabel labelRuta = new JLabel("Ruta:");
        cbRuta = new JComboBox<>(modelCombo);
        cbRuta.setSelectedIndex(itemSelectecCB);

        JLabel labelDireccion = new JLabel("Dirección:");
        textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);

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
        panelPrincipal.add(scrollPane, gbc);

        // Configurar GridBagConstraints para los botones
        
        JButton[] customButtons = {
            new JButton("Agregar"),
            new JButton("Cancelar")
        };
        
        customButtons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(!"".equals(txtUsername.getText()) && !"".equals(textArea.getText())){
                   if(dr.registerClient(txtUsername.getText(), textArea.getText(), cbRuta.getSelectedItem().toString(), 0.00f)){
                        setValidacion(true);
                        JOptionPane.showMessageDialog(null, "Cliente agregado con exito!!!", "Información",JOptionPane.INFORMATION_MESSAGE);
                        Window window = SwingUtilities.getWindowAncestor(panelPrincipal);
                        if (window instanceof JDialog) {
                        JDialog dialog = (JDialog) window;
                        dialog.dispose();  // Cierra el diálogo
                        }
                   }else{
                       JOptionPane.showMessageDialog(null, "Error al ingresar al cliente, intentelo nuevamente", "Error",JOptionPane.ERROR_MESSAGE);
                   }
               }else{
                   JOptionPane.showMessageDialog(null, "Debe de llenar todos los campos", "Error",JOptionPane.ERROR_MESSAGE);
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

        int opcion = JOptionPane.showOptionDialog(parentFrame, panelPrincipal, "Ingreso nuevo cliente",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, customButtons, customButtons[0]);
    }
}
