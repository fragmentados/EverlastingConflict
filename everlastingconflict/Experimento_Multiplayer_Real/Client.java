/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package everlastingconflict.Experimento_Multiplayer_Real;

/**
 *
 * @author Elías
 */
/*
 * The Client that can be run both as a console or a GUI
 */
import everlastingconflict.mapas.VentanaPrincipal;
import everlastingconflict.mapas.MensajeChat;
import everlastingconflict.razas.Fenix;

import java.net.*;
import java.io.*;
import javax.swing.JFrame;

import org.newdawn.slick.CanvasGameContainer;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    // if I use a GUI or not
    //private ClientGUI cg;
    // the server, the port and the username
    public String server, username;
    private int port;
    public static CanvasGameContainer canvas;
    public static JFrame ventana_mapa;
    /*
     *  Constructor called by console mode
     *  server: the server address
     *  port: the port number
     *  username: the username
     */
    /*
     * Constructor call when used from a GUI
     * in console mode the ClienGUI parameter is null
     */
    public Client(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
        // save if we are in GUI mode or not        
    }

    public void send_message(Message m) {
        try {
            sOutput.writeObject(m);
        } catch (IOException eIO) {
            display("Exception sending raze : " + eIO);
            disconnect();
        }
    }

    public void send_start() {
        try {
            sOutput.writeObject(new Message(Message.start_type, ""));
        } catch (IOException eIO) {
            display("Exception sending raze : " + eIO);
            disconnect();
        }
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        // Send our username tof the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(new Message(Message.user_type, username));
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to the console or the GUI
     */
    private void display(String msg) {
//        if (cg == null) {
        System.out.println(msg);      // println in console mode
//        } else {
//            cg.append(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
//        }
    }

    /*
     * To send a message to the server
     */
    void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

        // inform the GUI       
    }

    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = (Message) sInput.readObject();
                    switch (msg.type) {
                        case Message.elimination_type:
                            VentanaPrincipal.mapam.otro_usuario = "Libre";
                            VentanaPrincipal.mapam.otro_usuario_raza = "-";
                            break;
                        case Message.user_type:
                            VentanaPrincipal.mapam.otro_usuario = msg.text;
                            VentanaPrincipal.mapam.otro_usuario_raza = Fenix.nombre_raza;
                            break;
                        case Message.raze_type:
                            VentanaPrincipal.mapam.otro_usuario_raza = msg.text;
                            break;
                        case Message.start_type:
                            VentanaPrincipal.mapam.start = true;
                            break;
                        case Message.text_type:                            
                            VentanaPrincipal.mapac.anadir_mensaje_chat(new MensajeChat(msg.text.split("\n")[0], msg.text.split("\n")[1]));
                            break;
                    }
                    // if console mode print the message and add back the prompt
//                    if (cg == null) {
                    System.out.println("Username = " + username + ". Msg.type = " + msg.type + ".Msg.text = " + msg.text + ".");
                    //System.out.print("> ");
//                    } else {
//                        cg.append(msg);
//                    }
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
//                    if (cg != null) {
//                        cg.connectionFailed();
//                    }
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
    }
}
