/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.*;
import java.lang.Process;
import javax.swing.JTree;
import javax.swing.JFrame;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class TP1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame mainFrame = new JFrame();
        IpTree testTree = new IpTree();
        mainFrame.add(testTree.getTree());
        mainFrame.setSize(800, 600);
        mainFrame.setVisible(true);
        String example = "Détermination de l'itinéraire vers rr.lopar.wikimedia.org [212.85.150.132]\n"
                + "avec un maximum de 30 sauts :\n"
                + "\n"
                + " 1    24 ms    22 ms    22 ms  1.32.202.62.cust.bluewin.ch [62.202.32.1]\n"
                + " 2    22 ms    24 ms    22 ms  1.32.202.62.cust.bluewin.ch [62.202.32.1]\n"
                + " 3    24 ms    23 ms    22 ms  net481.bwrt2zhb.bluewin.ch [195.186.121.1]\n"
                + " 4   314 ms   162 ms    22 ms  net125.bwrt1inb.bluewin.ch [195.186.125.71]\n"
                + " 5    34 ms    23 ms    24 ms  if114.ip-plus.bluewin.ch [195.186.0.114]\n"
                + " 6    27 ms    29 ms    29 ms  i68geb-005-gig4-2.bb.ip-plus.net [138.187.130.158]\n"
                + " 7    39 ms    39 ms    38 ms  i00par-005-pos4-0.bb.ip-plus.net [138.187.129.34]\n"
                + " 8    38 ms   320 ms    39 ms  feth2-kara-ielo.freeix.net [213.228.3.203]\n"
                + " 9   284 ms    39 ms    39 ms  feth0-bestelle.tlcy.fr.core.ielo.net [212.85.144.6]\n"
                + "10    90 ms   158 ms    83 ms  chloe.wikimedia.org [212.85.150.132]\n"
                + "Itinéraire déterminé.\n"
                + "";
        String line = "";
        
        ArrayList<String> IPtab = new ArrayList<String>();
        
        try {
            //create the process
            ProcessBuilder pBuilder = new ProcessBuilder("tracert", "fr.wikipedia.org");
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
                    //Register into the IP tab
                    IPtab.add(matcher.group());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(IPtab);
        System.out.println("TEST");
    }
}
