/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import java.io.*;
import java.util.*;




/**
 *
 * @author trejkev
 */
public class string {
    
    Scanner sc2 = null;
    try {
        sc2 = new Scanner(new File("C:/prueba.trace"));
    } 
    catch (FileNotFoundException e) {
        e.printStackTrace();  
    }
    while (sc2.hasNextLine()) {
            Scanner s2 = new Scanner(sc2.nextLine());
        while (s2.hasNext()) {
            String s = s2.next();
            System.out.println(s);
        }
    }
    
}
