/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

/**
 *
 * @author thomas
 */
public class Branch{
        private String base;
        private String pointe;
        
        public Branch(String bottom, String head) {base = bottom; pointe = head;}
        public String getBase() {return base;}
        public String getPointe() {return pointe;}
        public boolean equals(Branch myBranch)
        {
            if(myBranch.getBase().equals(base) && myBranch.getPointe().equals(pointe)) return true;
            else return false;
        }
        public String toString()
        {
            String chaine = "\""+getBase()+
            "\" -> \""+getPointe()+"\"; \n";
            return chaine;
        }
    }
