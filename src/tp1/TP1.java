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
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.regex.*;

public class TP1 extends Application {

    ////LANCEMENT DE LA FENETRE GRAPHIQUE
    @Override
    public void start(Stage primaryStage) {

        //Les noms de dossier et fichier
        String directory = "./usr/bin";
        String input = "monFichier.dot";
        String output = "monGraphe.png";

        //Initialisation de l'arbre
        Tree myTree = new Tree("Start"); //on passe la racine en paramètre

///////********( A ENLEVER !!!) *****/////////////////
        //j'effectue plusieurs construction pour tester l'ajout de branches
        //processTraceroute(myTree,"campus.ece.fr");
        //processTraceroute(myTree,"www.facebook.com");
        String feedback = "";

        ////******EXECUTER A CHAQUE NOUVEL AJOUT : *************//////
        //Feedback stock le statut de la construction des branches
        if (processTraceroute(myTree, "fr.wikipedia.org")) //construction
        //récup le feedback => voir la classe Tree
        {
            feedback = myTree.getSucess();
        } else {
            feedback = myTree.getStop();
        }

        //Créer le .png de notre graphe
        processGraphviz(myTree.toString(), directory, input, output);

       ///====> Rechargement de l'image affiché et du text feedback
        /// ==> + vider le field contenant l'adresse tracé ?
        ////*****FIN ******/////
        //Construit les éléments de la fenêtre FX
        Group group = new Group();
        VBox vbox = new VBox();
        Image myImage = new Image("file:usr/bin/monGraphe.png");

        ImageView iv1 = new ImageView();
        iv1.setImage(myImage);
        iv1.setFitWidth(1024);
        iv1.setFitHeight(700);
        iv1.setPreserveRatio(true);
        iv1.setSmooth(true);
        iv1.setCache(true);

        vbox.getChildren().add(new Text(feedback));
        vbox.getChildren().add(iv1);
        group.getChildren().add(vbox);
        primaryStage.setScene(new Scene(group));

        primaryStage.setWidth(1024);
        primaryStage.setHeight(968);
        primaryStage.setTitle("TP1");
        primaryStage.sizeToScene();
        primaryStage.show();
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
                    if (!line.startsWith("traceroute"))
                    {
                        ip = matcher.group();
                    if (myTree.add(previous, ip)) {
                            previous = ip;
                        }
                    }
                }
                else {
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
