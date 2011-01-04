/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tingoo.client.ims;

import javax.swing.JFrame;
import org.tingoo.client.ims.ui.MainJP;

/**
 *
 * @author rainbow
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainJP p = new MainJP();
        JFrame f = new JFrame();
        f.getContentPane().add(p);

        f.setSize(600, 450);
        f.setVisible(true);
        f.setTitle("hello");
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        
    }

}
