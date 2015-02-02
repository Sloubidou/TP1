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

/**
 *
 * @author msi
 */
public class Ui {

    //Les noms de dossier et fichier

    private JFrame mainFrame = new JFrame();
    private String directory = "./usr/bin";
    private String input = "monFichier.dot";
    private String output = "monGraphe.png";
    private JLabel feedback = new JLabel("Enter URLS");
    private Tree myTree;
    private String url = "";
    private JTextField urlField = new JTextField("google.com");
    private BorderLayout layout = new BorderLayout();
    private BorderLayout layout2 = new BorderLayout();
    private JLabel picLabel=new JLabel("PROCESS");

    public JPanel getrPan() {
        return rPan;
    }

    public JPanel getlPan() {
        return lPan;
    }
    private JPanel rPan = new JPanel();
    private JPanel lPan = new JPanel();

    ////LANCEMENT DE LA FENETRE GRAPHIQUE
    public void Start() {
        //Initialisation de l'arbre
        myTree = new Tree("Start"); //on passe la racine en paramètre
        urlField.setSize(50, 10);
        lPan.add(urlField, layout2.NORTH);

        JButton addButton = new JButton("Add IP");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                url = urlField.getText();
                System.out.println(url);
                //Feedback stock le statut de la construction des branches
                if (true)//processTraceroute(myTree, url)) //construction
                //récup le feedback => voir la classe Tree
                {
                    feedback.setText(myTree.getSucess());
                } else {
                    feedback.setText(myTree.getStop());
                }
                //Créer le .png de notre graphe
                // processGraphviz(myTree.toString(), directory, input, output);
                try {
                    BufferedImage myImage = ImageIO.read(new File("usr/bin/truc.png"));
                    picLabel = new JLabel(new ImageIcon(myImage));
                    picLabel.setSize(400,400);
                    rPan.removeAll();
                    rPan.add(picLabel, layout.CENTER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                rPan.add(feedback, layout.NORTH);
                rPan.setVisible(true);
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
                myTree.getBranches().clear();
                System.out.println(myTree.getBranches().get(0));
            }
        });
        lPan.add(clearButton, layout2.CENTER);
        mainFrame.add(lPan, BorderLayout.WEST);
        mainFrame.add(rPan, BorderLayout.CENTER);
        mainFrame.setSize(1200, 800);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    ///Renvoie true si le traceroute est allé jusqu'au bout, false s'il a renvoyé
    ///une '*' sur un des noeuds (la construction est alors interrompue)
    public static boolean processTraceroute(Tree myTree, String url) {
        String line = "";
        String previous = myTree.getRoot();
        String ip = "";
        String destination = "";

        try {
            //create the process
            ProcessBuilder pBuilder = new ProcessBuilder("tracert", url);
            pBuilder.redirectErrorStream(true);//redirect the error stream
            Process myProcess = pBuilder.start(); //run the process
            //Get the output
            BufferedReader b = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
            //then print it !
            while ((line = b.readLine()) != null) {
                System.out.println(line);
                //Create a pattern for IPV4
                String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
                Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
                //Match the pattern and the string containing the IP
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    //If a match is find
                    //Register into the IP variable
                    if (line.contains(url)) {
                        destination = matcher.group();
                        System.out.println(destination);
                    } else {
                        ip = matcher.group();
                        if (myTree.add(previous, ip)) {
                            previous = ip;
                        }
                        if (destination.equals(ip)) {
                            System.out.println("SUCCEED");
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    ///GENERE LE .png (output) A PARTIR DU FICHIER .dot passé en paramètre (input)
    ///dans le répertoire (directory)
    ///Il faut également passé l'implémentation du fichier .dot (chaine)
    public static void processGraphviz(String chaine, String directory, String input, String output) {
        String line = "";
        try {
            //create the process
            ProcessBuilder pBuilder = new ProcessBuilder("dot", "-Tpng", input, "-o", output);
            pBuilder.directory(new File(directory));
            pBuilder.redirectErrorStream(true);//redirect the error stream
            ecritureGraphe(directory + "/" + input, chaine);
            Process myProcess = pBuilder.start(); //run the process

            //Get the output
            BufferedReader b = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
            //then print it !
            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///ECRIT DANS LE FICHER A PASSé à GRAPHVIZ !
    public static void ecritureGraphe(String fichier, String chaine) {

        //création ou ajout dans le fichier texte
        try {
            FileWriter fw = new FileWriter(fichier);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter fichierSortie = new PrintWriter(bw);
            fichierSortie.println(chaine);
            fichierSortie.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
