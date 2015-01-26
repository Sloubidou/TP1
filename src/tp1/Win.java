/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author msi
 */
public class Win extends JFrame {
    private JPanel leftPan;
    private JPanel rightPan;
 
    public Win(){
        leftPan = new JPanel();
        rightPan = new JPanel();
        this.setSize(800, 600);
        this.setLayout(new GridBagLayout());
    }
}
