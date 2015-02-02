/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.awt.BorderLayout;
import java.io.*;
import java.lang.Process;
import java.util.regex.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.LinkedList;

/**
 *
 * @author msi
 */
public class Ui {

    //Les noms de dossier et fichier
    private JFrame mainFrame = new JFrame();
    private JLabel feedback = new JLabel("");
    private String url = "";
    private JTextField urlField = new JTextField("Enter a URL");
    private BorderLayout layout = new BorderLayout();
    private BorderLayout layout2 = new BorderLayout();
    private JLabel picLabel = new JLabel("PROCESS");
    private Graph graph = new SingleGraph("Tutorial 1");
    private JPanel rPan = new JPanel();
    private JPanel lPan = new JPanel();

    ////LANCEMENT DE LA FENETRE GRAPHIQUE
    public void Start() {
        urlField.setSize(50, 25);
        lPan.add(urlField, layout2.NORTH);
        JButton addButton = new JButton("Add IP");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                url = urlField.getText();
                //Feedback stock le statut de la construction des branches
                ProcessTrace traceRoute = new ProcessTrace(url, graph);
                traceRoute.start();
                traceRoute.getFeedback();
                lPan.add(feedback, layout.SOUTH);
                lPan.setVisible(true);
                mainFrame.invalidate();
                mainFrame.revalidate();
                mainFrame.repaint();

            }
        });
        lPan.add(addButton, layout2.WEST);
        JButton clearButton = new JButton("Clear IP");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                url = "";
                for (Node n : graph.getEachNode()) {
                    graph.removeNode(n);
                }
            }
        });
        graph.display();
        lPan.add(clearButton, layout2.CENTER);
        mainFrame.add(lPan, BorderLayout.WEST);
        mainFrame.add(rPan, BorderLayout.CENTER);
        mainFrame.setSize(400, 200);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
