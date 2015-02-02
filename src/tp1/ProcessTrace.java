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
        String line = "";
        String destination = "";
        String previous = "Start";
        int j = 0;
        LinkedList<String> ipTab = new LinkedList<String>();
        if (graph.getNode(previous) == null) {
            graph.addNode(previous);
        }
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
                    if (line.contains(url)) {
                        destination = matcher.group();
                    } else {
                        if (graph.getNode(matcher.group()) == null) {
                            graph.addNode(matcher.group()).addAttribute(matcher.group());
                        }
                        if (graph.getNode(previous).getEdgeBetween(graph.getNode(matcher.group())) == null) {
                            graph.addEdge(previous + " to " + matcher.group(), previous, matcher.group(), true);
                        }
                        previous = matcher.group();
                        if (destination.equals(matcher.group())) {
                            System.out.println("SUCCEED");
                            feedback = "Succeed !";
                            previous = "Start";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
