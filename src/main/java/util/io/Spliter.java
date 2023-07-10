/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.io;

/**
 *
 * @author elahi
 */
public class Spliter {
    public static String splitString(String value){
          if(value.contains("/"))
                return value.split("/")[0];
          return value;
    } 
}
