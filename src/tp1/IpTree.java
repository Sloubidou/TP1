/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 * @author msi
 */
public class IpTree {
    private JTree tree;

    public JTree getTree() {
        return tree;
    }
    
    public IpTree(){
        tree = new JTree(fillTree());
    }
    
    public DefaultMutableTreeNode fillTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        int i=0;
        for (i=0;i<5;i++){
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(Integer.toString(i));
        DefaultMutableTreeNode mainNode = new DefaultMutableTreeNode("MAIN"+Integer.toString(i));
        mainNode.add(newNode);
        root.add(mainNode);
        }
        
        return root;
    }
}
