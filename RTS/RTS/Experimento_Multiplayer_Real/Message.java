/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RTS.Experimento_Multiplayer_Real;

import java.io.Serializable;

/**
 *
 * @author Elías
 */
public class Message implements Serializable {

    public String type;
    public String text;
    public static final String elimination_type = "ELIMINATION";
    public static final String raze_type = "RAZE";
    public static final String user_type = "USER";
    public static final String start_type = "START";
    public static final String text_type = "TEXT";

    public Message(String t, String te) {
        type = t;
        text = te;
    }

}
