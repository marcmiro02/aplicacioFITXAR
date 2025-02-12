/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fitxar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marc
 */
public class GestioHoraris extends javax.swing.JFrame {

        private class ComboItem {
        private int id;
        private String displayText;

        public ComboItem(int id, String displayText) {
            this.id = id;
            this.displayText = displayText;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayText; // Lo que se mostrará en el JComboBox
        }
    }

    public GestioHoraris() {
        initComponents();
        cargarUsuaris(ComboBoxUser);
                setTitle("Gestió d'horaris");
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ComboBoxUser = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        taulaHoraris = new javax.swing.JTable();
        bttnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ComboBoxUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxUserActionPerformed(evt);
            }
        });

        taulaHoraris.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Dia", "Hora Inici", "Hora Final"
            }
        ));
        jScrollPane1.setViewportView(taulaHoraris);

        bttnEliminar.setText("Eliminar Horari");
        bttnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ComboBoxUser, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bttnEliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(191, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBoxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bttnEliminar))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ComboBoxUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxUserActionPerformed
           ComboItem selectedItem = (ComboItem) ComboBoxUser.getSelectedItem();
    if (selectedItem != null) {
        int idUsuario = selectedItem.getId();
        // Llamamos a cargarHorarios para mostrar los horarios en la tabla
        cargarHoraris(idUsuario);
    }
    }//GEN-LAST:event_ComboBoxUserActionPerformed

    private void bttnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnEliminarActionPerformed
            ComboItem selectedItem = (ComboItem) ComboBoxUser.getSelectedItem();
    if (selectedItem != null) {
        int idUsuario = selectedItem.getId();
        
        // Confirmar con el usuario si está seguro de eliminar los horarios
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Estàs segur de voler eliminar tots els horaris d'aquest usuari?", 
            "Confirmar eliminació", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        // Si el usuario confirma la eliminación
        if (confirm == JOptionPane.YES_OPTION) {
            eliminarHorarios(idUsuario);  // Llamamos al método que eliminará los horarios
        }
    } else {
        JOptionPane.showMessageDialog(this, "No s'ha seleccionat cap usuari.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_bttnEliminarActionPerformed

private void cargarUsuaris(JComboBox<ComboItem> comboBox) {
    String query = "SELECT id_usuari, nom, cognoms FROM usuaris";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        // Usamos un modelo para ComboBox que acepte ComboItem
        DefaultComboBoxModel<ComboItem> model = new DefaultComboBoxModel<>();
        comboBox.setModel(model);

        // Cargar los elementos en el ComboBox
        while (resultSet.next()) {
            int id = resultSet.getInt("id_usuari");
            String nom = resultSet.getString("nom");
            String cognom = resultSet.getString("cognoms");

            // Crear un ComboItem con la ID y el nombre completo
            ComboItem item = new ComboItem(id, cognom + ", " + nom);

            // Añadir el ComboItem al ComboBox
            model.addElement(item);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void eliminarHorarios(int idUsuario) {
    String queryEliminar = "DELETE FROM horarios_clase WHERE usuari_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(queryEliminar)) {
        
        // Establecer el ID del usuario para eliminar sus horarios
        preparedStatement.setInt(1, idUsuario);
        
        // Ejecutar la consulta de eliminación
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Horarios eliminats correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            // Después de eliminar, recargar la tabla con los nuevos horarios (vacíos en este caso)
            cargarHoraris(idUsuario);
        } else {
            JOptionPane.showMessageDialog(this, "No s'han trobat horaris per eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error en eliminar els horaris: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    

    
private void cargarHoraris(int idUsuario) {
    // Suponiendo que tienes una tabla llamada 'horaris' y las columnas 'dia', 'hora_inici', 'hora_final'
    String query = "SELECT dia, hora_inicio, hora_fin FROM horarios_clase WHERE usuari_id = ? ORDER BY FIELD(dia, 'DILLUNS', 'DIMARTS', 'DIMECRES', 'DIJOUS', 'DIVENDRES');";
    
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        
        // Establecer la id del usuario seleccionado
        preparedStatement.setInt(1, idUsuario);
        
        // Ejecutar la consulta
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            
            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Dia", "Hora Inici", "Hora Final"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hacer que todas las celdas no sean editables
                }
            };
            
            // Limpiar la tabla antes de agregar los nuevos datos
            taulaHoraris.setModel(model);
            
            // Rellenar la tabla con los datos del horario
            while (resultSet.next()) {
                String dia = resultSet.getString("dia");
                String horaInici = resultSet.getString("hora_inicio");
                String horaFinal = resultSet.getString("hora_fin");
                
                Object[] fila = {dia, horaInici, horaFinal};
                model.addRow(fila);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los horarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    
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
            java.util.logging.Logger.getLogger(GestioHoraris.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestioHoraris.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestioHoraris.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestioHoraris.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestioHoraris().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<ComboItem> ComboBoxUser;
    private javax.swing.JButton bttnEliminar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable taulaHoraris;
    // End of variables declaration//GEN-END:variables
}
