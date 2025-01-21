/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_fitxar;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java_fitxar.DatabaseConnection;

import javax.swing.JFrame;

/**
 *
 * @author marc
 */
public class GestioProfessors extends javax.swing.JFrame {
 
    private JButton btnCrear, btnEditar, btnEliminar;  // Botones para las acciones

    /**
     * Creates new form GestioProfessors
     */
    public GestioProfessors() {
        initComponents();
        setTitle("Gestió de Professors");
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear los botones de acciónl
        JPanel panelBotones = new JPanel();
        btnCrear = new JButton("Afegir");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        // Conectar los botones a las acciones
        btnCrear.addActionListener(e -> crearProfesor());
        btnEditar.addActionListener(e -> editarProfesor());
        btnEliminar.addActionListener(e -> eliminarProfesor());

        // Añadir los componentes al JFrame
        add(panelBotones, BorderLayout.NORTH);
        //add(new JScrollPane(taulaProfessors), BorderLayout.CENTER); // Aquí usamos la tabla ya creada

        // Cargar los datos al iniciar la ventana
        cargarProfessores();

        setVisible(true);
    }

    public void cargarProfessores() {
        String query = "SELECT * FROM usuaris";  // Asegúrate de que 'professores' es el nombre correcto de la tabla
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Obtenemos el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) taulaProfessors.getModel();
            
            // Limpiamos la tabla antes de agregar nuevos datos
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id_usuari");
                String dni = rs.getString("dni");
                String nombre = rs.getString("nom");
                String cognom = rs.getString("cognoms");
                String email = rs.getString("email");

                
                
                String rol = rs.getString("rol");

                Object[] fila = {id, dni,nombre, cognom, email, rol};
                model.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los profesores: " + e.getMessage());
        }
    }

    // Método para crear un profesor (puedes abrir un formulario de creación)
    private void crearProfesor() {
    CrearUsuari frame = new CrearUsuari(this); // Pasar referencia al marco principal
    frame.setVisible(true);
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            cargarProfessores(); // Recargar la tabla después de cerrar el formulario de creación
        }
    });
    }

    // Método para editar un profesor

    private void editarProfesor() {
        int selectedRow = taulaProfessors.getSelectedRow();
        if (selectedRow != -1) {
            try {
                // Obtener datos del profesor
                int idProfesor = (int) taulaProfessors.getValueAt(selectedRow, 0);
                String nombre = (String) taulaProfessors.getValueAt(selectedRow, 1);
                String apellido = (String) taulaProfessors.getValueAt(selectedRow, 2);
                String email = (String) taulaProfessors.getValueAt(selectedRow, 3);
                int idRolActual = Integer.parseInt(taulaProfessors.getValueAt(selectedRow, 4).toString()); // Corrección aquí
                String contrasenya = "";

                // Crear diálogo
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(taulaProfessors), "Editar - " + nombre, true);
                dialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);

                // Título del formulario
                JLabel tituloLabel = new JLabel("Editar - " + nombre);
                tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
                gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
                dialog.add(tituloLabel, gbc);
                gbc.gridwidth = 1; // Restablecer ancho de columna

                // Campos
                JTextField txtNombre = new JTextField(nombre);
                JTextField txtApellido = new JTextField(apellido);
                JTextField txtEmail = new JTextField(email);
                JPasswordField txtContrasenya = new JPasswordField(contrasenya);
                JPasswordField txtRepetirContrasenya = new JPasswordField(contrasenya);

                JComboBox<String> comboRoles = new JComboBox<>();
                Map<String, Integer> rolesMap = new HashMap<>();
                try (Connection connection = DatabaseConnection.getConnection();
                     Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id_rol, nom_rol FROM rols_usuaris")) {
                    while (rs.next()) {
                        int idRol = rs.getInt("id_rol");
                        String nomRol = rs.getString("nom_rol");
                        comboRoles.addItem(nomRol);
                        rolesMap.put(nomRol, idRol);
                        if (idRol == idRolActual) {
                            comboRoles.setSelectedItem(nomRol);
                        }
                    }
                }

                // Añadir componentes al diálogo
                gbc.gridx = 0; gbc.gridy = 1;
                dialog.add(new JLabel("Nom:"), gbc);
                gbc.gridx = 1;
                dialog.add(txtNombre, gbc);

                gbc.gridx = 0; gbc.gridy = 2;
                dialog.add(new JLabel("Cognoms:"), gbc);
                gbc.gridx = 1;
                dialog.add(txtApellido, gbc);

                gbc.gridx = 0; gbc.gridy = 3;
                dialog.add(new JLabel("Email:"), gbc);
                gbc.gridx = 1;
                dialog.add(txtEmail, gbc);

                gbc.gridx = 0; gbc.gridy = 4;
                dialog.add(new JLabel("Rol:"), gbc);
                gbc.gridx = 1;
                dialog.add(comboRoles, gbc);

                gbc.gridx = 0; gbc.gridy = 5;
                dialog.add(new JLabel("Contrasenya:"), gbc);
                gbc.gridx = 1;
                dialog.add(txtContrasenya, gbc);

                gbc.gridx = 0; gbc.gridy = 6;
                dialog.add(new JLabel("Repetir Contrasenya:"), gbc);
                gbc.gridx = 1;
                dialog.add(txtRepetirContrasenya, gbc);

                // Botones
                JPanel panelBotones = new JPanel();
                JButton btnGuardar = new JButton("Guardar");
                JButton btnCancelar = new JButton("Cancelar");
                panelBotones.add(btnGuardar);
                panelBotones.add(btnCancelar);

                gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2;
                dialog.add(panelBotones, gbc);

                // Acción de guardar
                btnGuardar.addActionListener(e -> {
                    try {
                        String nuevoNombre = txtNombre.getText();
                        String nuevoApellido = txtApellido.getText();
                        String nuevoEmail = txtEmail.getText();
                        String nuevaContrasenya = new String(txtContrasenya.getPassword());
                        String repetirContrasenya = new String(txtRepetirContrasenya.getPassword());

                        // Validar contraseñas
                        if (!nuevaContrasenya.equals(repetirContrasenya)) {
                            JOptionPane.showMessageDialog(dialog, "Les contrasenyes no coincideixen");
                            return;
                        }

                        // Obtener ID del rol seleccionado
                        String rolSeleccionado = (String) comboRoles.getSelectedItem();
                        int nuevoIdRol = rolesMap.get(rolSeleccionado);

                        // Actualizar los datos en la base de datos
                        try (Connection connection = DatabaseConnection.getConnection()) {
                            String updateQuery = "UPDATE usuaris SET nom = ?, cognoms = ?, email = ?, rol = ?, password = ? WHERE id_usuari = ?";
                            try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                                stmt.setString(1, nuevoNombre);
                                stmt.setString(2, nuevoApellido);
                                stmt.setString(3, nuevoEmail);
                                stmt.setInt(4, nuevoIdRol);
                                stmt.setString(5, nuevaContrasenya.isEmpty() ? contrasenya : nuevaContrasenya);
                                stmt.setInt(6, idProfesor);
                                stmt.executeUpdate();
                                JOptionPane.showMessageDialog(dialog, "Professor actualitzat correctament.");
                                cargarProfessores(); // Recargar la tabla después de la edición
                                dialog.dispose();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Error al guardar los cambios: " + ex.getMessage());
                    }
                });

                // Acción de cancelar
                btnCancelar.addActionListener(e -> dialog.dispose());

                // Configuración del diálogo
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(taulaProfessors);
                dialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir el formulario: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un profesor para editar.");
        }
    }








    // Método para eliminar un profesor seleccionado
    private void eliminarProfesor() {
        int selectedRow = taulaProfessors.getSelectedRow();
        if (selectedRow != -1) {
            int idProfesor = (int) taulaProfessors.getValueAt(selectedRow, 0);  // Obtener el ID del profesor

            // Confirmar la eliminación
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el profesor con ID: " + idProfesor + "?",
                    "Eliminar Profesor", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String deleteQuery = "DELETE FROM usuaris WHERE id_usuari= ?";
                    try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
                        stmt.setInt(1, idProfesor);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Profesor eliminado correctamente.");
                        cargarProfessores();  // Recargar la tabla después de la eliminación
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el profesor: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un profesor para eliminar.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taulaProfessors = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taulaProfessors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "DNI", "Nom", "Cognoms", "Email", "Rol"
            }
        ));
        jScrollPane1.setViewportView(taulaProfessors);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestioProfessors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestioProfessors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestioProfessors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestioProfessors.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestioProfessors().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable taulaProfessors;
    // End of variables declaration//GEN-END:variables
}
