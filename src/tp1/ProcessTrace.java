/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 *
 * @author msi
 */
public class ProcessTrace extends Thread {

    private String url;
    private Graph graph;
    private String feedback = "";

    public ProcessTrace(String url, Graph graph) {
        this.url = url;
        this.graph = graph;
    }

    public String getFeedback() {
        return feedback;
    }

    public void run() {
        Runtime rt = Runtime.getRuntime();
        Process myProcess = null;
        String line = "";
        String destination = "";
        ArrayList<String> previousIp = new ArrayList<String>();
        previousIp.add("Start");
        ArrayList<String> listIp = new ArrayList<String>();
        if (graph.getNode("Start") == null) {
            graph.addNode("Start").addAttribute("ui.label", "Start");
        }
        try {
            //create the process
            myProcess = rt.exec("java -jar ./fakeroute.jar " + url);
            //Get the output
            BufferedReader b = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
            //then print it !
            b.readLine();
            while ((line = b.readLine()) != null) {
                System.out.println(line);
                //Create a pattern for IPV4
                String IPADDRESS_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
                Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
                //Match the pattern and the string containing the IP
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    listIp.add(matcher.group());
                }
                for (String s : listIp) {
                    if (graph.getNode(s) == null) {
                        graph.addNode(s).addAttribute("ui.label", s);
                    }
                    for (String previous : previousIp) {
                        if (graph.getNode(previous).getEdgeBetween(graph.getNode(s)) == null) {
                            graph.addEdge(previous + " to " + s, previous, s, true);
                        }
                    }
                }
                if (!listIp.isEmpty()) {
                    System.out.println("Previous : " + previousIp);
                    System.out.println("IP : " + listIp);
                    previousIp.clear();
                    previousIp.addAll(listIp);
                    listIp.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
