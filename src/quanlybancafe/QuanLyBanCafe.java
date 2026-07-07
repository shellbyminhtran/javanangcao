/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package quanlybancafe;

import quanlybancafe.helper.JdbcHelper;
import quanlybancafe.view.RoleSelectionFrame;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.SQLException;

/**
 *
 * @author minhs
 */
public class QuanLyBanCafe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RoleSelectionFrame().setVisible(true);
            }
        });
    }
    
}
