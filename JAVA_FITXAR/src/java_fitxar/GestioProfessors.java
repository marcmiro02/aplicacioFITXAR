/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package java_fitxar;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
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
        add(new JScrollPane(taulaProfessors), BorderLayout.CENTER); // Aquí usamos la tabla ya creada

        // Cargar los datos al iniciar la ventana
        cargarProfessores();

        setVisible(true);
    }

    private void cargarProfessores() {
String query = "SELECT * FROM usuaris";  // Asegúrate de que 'professores' es el nombre correcto de la tabla
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Obtenemos el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) taulaProfessors.getModel();
            
            // Limpiamos la tabla antes de agregar nuevos datos
            model.setRowCount(0);

            // Añadimos los datos de cada fila a la tabla
            while (rs.next()) {
                int id = rs.getInt("id_usuari");
                String nombre = rs.getString("nom");
                String cognom = rs.getString("cognoms");
                String email = rs.getString("email");
                
                
                String rol = rs.getString("rol");

                // Agregar los datos a la tabla
                Object[] fila = {id, nombre, cognom, email, rol};
                model.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los profesores: " + e.getMessage());
        }
    }

    // Método para crear un profesor (puedes abrir un formulario de creación)
    private void crearProfesor() {
        JOptionPane.showMessageDialog(this, "Formulario para crear un nuevo profesor.");
        CrearUsuari frame = new CrearUsuari();
        frame.setVisible(true);
    }

    // Método para editar un profesor

private void editarProfesor() {
    int selectedRow = taulaProfessors.getSelectedRow();
    if (selectedRow != -1) {
        // Obtener los datos del profesor seleccionado
        int idProfesor = (int) taulaProfessors.getValueAt(selectedRow, 0);  // ID del profesor
        String nombre = (String) taulaProfessors.getValueAt(selectedRow, 1);  // Nombre
        String apellido = (String) taulaProfessors.getValueAt(selectedRow, 2);  // Apellido
        String email = (String) taulaProfessors.getValueAt(selectedRow, 3);  // Email
        String rol = (String) taulaProfessors.getValueAt(selectedRow, 4);  // Rol
        String contrasenya = ""; // Aquí puede ir el valor de la contraseña si tienes acceso a ella

        // Crear el formulario para editar los datos
        JDialog dialog = new JDialog(this, "Editar Profesor", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre los componentes

        // Campos del formulario
        JTextField txtNombre = new JTextField(nombre);
        JTextField txtApellido = new JTextField(apellido);
        JTextField txtEmail = new JTextField(email);
        JTextField txtRol = new JTextField(rol);
        JPasswordField txtContrasenya = new JPasswordField(contrasenya);
        JPasswordField txtRepetirContrasenya = new JPasswordField(contrasenya);

        // Etiquetas y campos de texto
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Apellido:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtApellido, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Rol:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtRol, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtContrasenya, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        dialog.add(new JLabel("Repetir Contraseña:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtRepetirContrasenya, gbc);

        // Botones para guardar o cancelar
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(panelBotones, gbc);

        // Lógica para el botón de guardar
        btnGuardar.addActionListener(e -> {
            String nuevoNombre = txtNombre.getText();
            String nuevoApellido = txtApellido.getText();
            String nuevoEmail = txtEmail.getText();
            String nuevoRol = txtRol.getText();
            String nuevaContrasenya = new String(txtContrasenya.getPassword());
            String repetirContrasenya = new String(txtRepetirContrasenya.getPassword());

            // Validar contraseñas
            if (!nuevaContrasenya.equals(repetirContrasenya)) {
                JOptionPane.showMessageDialog(dialog, "Las contraseñas no coinciden.");
                return;
            }

            // Actualizar los datos en la base de datos
            try (Connection connection = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE usuaris SET nom = ?, cognoms = ?, email = ?, rol = ?, contrasenya = ? WHERE id_usuari = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
                    stmt.setString(1, nuevoNombre);
                    stmt.setString(2, nuevoApellido);
                    stmt.setString(3, nuevoEmail);
                    stmt.setString(4, nuevoRol);
                    if (!nuevaContrasenya.isEmpty()) {
                        stmt.setString(5, nuevaContrasenya); // Solo actualiza la contraseña si ha sido cambiada
                    } else {
                        stmt.setString(5, contrasenya); // Si no se modificó la contraseña, se deja la actual
                    }
                    stmt.setInt(6, idProfesor);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Profesor actualizado correctamente.");
                    cargarProfessores();  // Recargar la tabla después de la edición
                    dialog.dispose();  // Cerrar el formulario
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error al editar el profesor: " + ex.getMessage());
            }
        });

        // Lógica para el botón de cancelar
        btnCancelar.addActionListener(e -> dialog.dispose());

        // Configurar propiedades del diálogo
        dialog.setSize(500, 400);  // Tamaño del formulario
        dialog.setLocationRelativeTo(this);  // Centrar el diálogo en la ventana principal
        dialog.setVisible(true);
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
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nom", "Cognoms", "Email", "Rol"
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
                .addContainerGap(90, Short.MAX_VALUE))
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
