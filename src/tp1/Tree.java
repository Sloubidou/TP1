/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.util.ArrayList;
/**
 *
 * @author thomas
 */
public class Tree {
    
    private String sucessConstruction = "La construction de l'arbre s'est bien déroulée !";
    private String stopConstruction = "La construction de l'arbre a été interrompue !";
    private String root;
    private ArrayList<Branch> branches = new ArrayList<Branch>();
    
 
    public Tree(String theRoot){root = theRoot;}
    public ArrayList<Branch> getBranches(){return branches;}
    public String getRoot(){return root;}
    public String getSucess(){return sucessConstruction;}
    public String getStop(){return stopConstruction;}
    
    public boolean add(String chaine1, String chaine2)
    {
        Branch myBranch = new Branch(chaine1, chaine2);
        for(int i = 0; i < branches.size(); i++)
        {
            if(branches.get(i).equals(myBranch)) return false;
        }
        branches.add(myBranch);
        return true;
    }
    
    @Override
    public String toString()
    {
        String outPut = "digraph G {\n";
        for(int i = 0; i < branches.size(); i++)
        {
            outPut = outPut+(branches.get(i).toString());
        }
        outPut = outPut+"}";
        return outPut;
    }
    
  /*  public class Node{
        private String adress;
        private ArrayList<Node> children = new ArrayList<Node>(); 
        
        public Node(String machaine) { adress = machaine;}
        public String getAdress() {return adress;}
        public ArrayList<Node> getChildren() {return children;}
        public void add(Node myNode){
            children.add(myNode);
        }
    }
    
    private Node root;
    
    public Tree(String machaine){
        root = new Node(machaine);
    }
    
    public Node getRoot(){return root;}
    public Node findNode(Node start, String myContent)
    {
        for(int i = 0; i < start.getChildren().size(); i++)
        {   
            if(start.getChildren() != null)//si le noeaud à des fils
            {
                if(start.getChildren().get(i).getAdress() == myContent) 
                return start.getChildren().get(i);
                else return findNode(start.getChildren().get(i), myContent);
            }
        }//sinon renvoie null
        return null;
    } */  
}
