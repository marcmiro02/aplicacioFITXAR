package java_fitxar;

import java.awt.BorderLayout;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class SuperUserDashboard extends javax.swing.JFrame {
    
    private javax.swing.JComboBox<String> jComboBoxEmpresas;

    /**
     * Creates new form FitxarGraficament
     */
    public SuperUserDashboard() {
        initComponents();
        setTitle("Super User Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creamos un panel contenedor donde vamos a colocar el contenido de frameEmpreses
        panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout());

        // Llamar al método que agrega el frameEmpreses dentro del panel
        openFrameEmpreses();

        // Agregamos el panel contenedor al JFrame principal
        add(panelContenedor, BorderLayout.CENTER);

        setVisible(true);  // Hacemos visible el JFrame principal
        
    }
        private void openFrameEmpreses() {
        // Crear e inicializar frameEmpreses como un JPanel (en lugar de un JFrame)
        frameEmpreses empresasPanel = new frameEmpreses();
        
        // Agregar el JPanel de frameEmpreses al panel contenedor
        panelContenedor.add(empresasPanel, BorderLayout.CENTER);

        // Actualizamos el layout para que se muestre correctamente
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
                                                            

    /**
     * @param args the command line arguments
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameEmpreses = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelContenedor = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        bttnEmpreses = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jMenu3 = new javax.swing.JMenu();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NIF", "Nom", "Ciutat", "Telefon", "Email"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout frameEmpresesLayout = new javax.swing.GroupLayout(frameEmpreses.getContentPane());
        frameEmpreses.getContentPane().setLayout(frameEmpresesLayout);
        frameEmpresesLayout.setHorizontalGroup(
            frameEmpresesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameEmpresesLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1029, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        frameEmpresesLayout.setVerticalGroup(
            frameEmpresesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameEmpresesLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout panelContenedorLayout = new javax.swing.GroupLayout(panelContenedor);
        panelContenedor.setLayout(panelContenedorLayout);
        panelContenedorLayout.setHorizontalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1226, Short.MAX_VALUE)
        );
        panelContenedorLayout.setVerticalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        bttnEmpreses.setText("Empreses");
        bttnEmpreses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnEmpresesActionPerformed(evt);
            }
        });
        jMenuBar1.add(bttnEmpreses);

        jMenu1.setText("Usuaris");

        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("Gestionar usuaris");
        jMenu1.add(jRadioButtonMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Rols");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(panelContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(panelContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnEmpresesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnEmpresesActionPerformed
        
    }//GEN-LAST:event_bttnEmpresesActionPerformed

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
            java.util.logging.Logger.getLogger(SuperUserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SuperUserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SuperUserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SuperUserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SuperUserDashboard().setVisible(true);
            }
        });
    }
    
    //**METODES PIPOLIANS QUE JO QUE SE ON POLLES FOTRELS**
    
    List<String> obtenerNombresEmpresas() {       
        List<String> nombresEmpresas = new ArrayList<>();
        String query = "SELECT id_empresa, nif, nom_empresa, telefon, email, ciutat FROM empreses";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                nombresEmpresas.add(resultSet.getString("nom_empresa"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los nombres de las empresas: " + e.getMessage());
        }
        return nombresEmpresas;
    }
    



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu bttnEmpreses;
    private javax.swing.JFrame frameEmpreses;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelContenedor;
    // End of variables declaration//GEN-END:variables

    public class frameEmpreses extends JPanel {

        public frameEmpreses() {
            
        setLayout(new BorderLayout());  // Usamos BorderLayout para distribuir los componentes

        // Crear una tabla para mostrar las empresas (este es solo un ejemplo básico)
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{{"1", "Empresa A", "12345678A", "Barcelona"}, {"2", "Empresa B", "12345678B", "Madrid"}},
                new String[]{"ID", "Nombre", "NIF", "Ciudad"}
        );
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);  // Agregamos la tabla al centro del panel

        // Agregar botones o cualquier otro componente que desees en el panel
    


    }
}
}
