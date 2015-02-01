/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.*;
import java.lang.Process;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.regex.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.*;

public class TP1 extends Application {

    //Les noms de dossier et fichier
    private String directory = "./usr/bin";
    private String input = "monFichier.dot";
    private String output = "monGraphe.png";
    private String feedback = "";
    private Tree myTree;
    private String url = "";
    private TextField urlField = new TextField();
    //Construit les éléments de la fenêtre FX
    private Group group = new Group();
    private HBox hbox = new HBox();
    private VBox vbox = new VBox();
    private ImageView iv1 = new ImageView();
    private GridPane grid = new GridPane();

    ////LANCEMENT DE LA FENETRE GRAPHIQUE
    @Override
    public void start(Stage primaryStage) {
        //Initialisation de l'arbre
        myTree = new Tree("Start"); //on passe la racine en paramètre
        //j'effectue plusieurs construction pour tester l'ajout de branches
        //processTraceroute(myTree,"campus.ece.fr");
        //processTraceroute(myTree,"www.facebook.com");

        grid.setHgap(10);
        grid.setVgap(10);

        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.setTitle("TP1");
        primaryStage.sizeToScene();
        primaryStage.show();
        ////*****FIN ******/////
        //vbox.getChildren().add(urlField);
        grid.add(urlField, 1, 0);
        Button addButton = new Button("Add IP");
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                url = urlField.getText();
                System.out.println(url);
                //Feedback stock le statut de la construction des branches
                if (processTraceroute(myTree, url)) //construction
                //récup le feedback => voir la classe Tree
                {
                    feedback = myTree.getSucess();
                } else {
                    feedback = myTree.getStop();
                }
                //Créer le .png de notre graphe
                processGraphviz(myTree.toString(), directory, input, output);
                Image myImage = new Image("file:usr/bin/monGraphe.png");
                iv1.setImage(myImage);
                iv1.setFitWidth(600);
                iv1.setFitHeight(600);
                iv1.setPreserveRatio(true);
                iv1.setSmooth(true);
                iv1.setCache(true);
                grid.add(iv1, 1, 1);
                grid.add(new Text(feedback), 4, 0);
            }
        });
        vbox.getChildren().add(addButton);
        grid.add(addButton, 2, 0);
        Button clearButton = new Button("Clear IP");
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                url = "";
            }
        });
        grid.add(clearButton, 3, 0);
        group.getChildren().add(grid);
        primaryStage.setScene(new Scene(group));

    }

    ///Renvoie true si le traceroute est allé jusqu'au bout, false s'il a renvoyé
    ///une '*' sur un des noeuds (la construction est alors interrompue)
    public static boolean processTraceroute(Tree myTree, String url) {
        String line = "";
        String previous = myTree.getRoot();
        String ip;

        try {
            //create the process
            ProcessBuilder pBuilder = new ProcessBuilder("traceroute", url);
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
                if (matcher.find()) {//If a match is find
                    //Register into the IP variable
                    if (!line.startsWith("traceroute")) {
                        ip = matcher.group();
                        if (myTree.add(previous, ip)) {
                            previous = ip;
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
