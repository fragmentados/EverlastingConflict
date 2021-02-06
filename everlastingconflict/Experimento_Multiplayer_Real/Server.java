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
import everlastingconflict.gestion.Partida;

/*
 * The server that can be run both as a console application or a GUI
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {

    // a unique ID for each connection
    private static int uniqueId;
    // an ArrayList to keep the list of the Client
    private ArrayList<ClientThread> al;
    // if I am in a GUI
    //private ServerGUI sg;
    // to display time
    private SimpleDateFormat sdf;
    // the port number to listen for connection
    private int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;
    private Partida partida;

    /*
     *  server constructor that receive the port to listen to for connection as parameter
     *  in console
     */
    public Server(int port) {
        // GUI or not
        //this.sg = sg;
        // the port
        this.port = port;
        // to display hh:mm:ss
        sdf = new SimpleDateFormat("HH:mm:ss");
        // ArrayList for the Client list
        al = new ArrayList<ClientThread>();
    }

    public void start() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                display("Server waiting for Clients on port " + port + ".");

                Socket socket = serverSocket.accept();  	// accept connection
                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }
                //ObjectInputStream sInput = new ObjectInputStream(socket.getInputStream());                
                ClientThread t = new ClientThread(socket);  // make a thread of it
                broadcastLoginOtherUsers(t);
                broadcastUser(t.username);
                al.add(t);									// save it in the ArrayList
                t.start();
            }
            // I was asked to stop
            try {
                serverSocket.close();
                for (int i = 0; i < al.size(); ++i) {
                    ClientThread tc = al.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        } // something went bad
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }
    /*
     * For the GUI to stop the server
     */

    protected void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement 
        // Socket socket = serverSocket.accept();
        try {
            new Socket("localhost", port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }
    /*
     * Display an event (not a message) to the console or the GUI
     */

    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
//        if (sg == null) {
        System.out.println(time);
//        } else {
//            sg.appendEvent(time + "\n");
//        }
    }
    /*
     *  to broadcast a message to all Clients
     */

    private synchronized void broadcastLogoutOtherUsers() {
        for (int i = 0; i < al.size(); i++) {
            ClientThread ct = al.get(i);
            ct.writeMsg(new Message(Message.elimination_type, ""));
        }
    }

    private synchronized void broadcastLoginOtherUsers(ClientThread c) {
        for (int i = 0; i < al.size(); i++) {
            ClientThread ct = al.get(i);
            c.writeMsg(new Message(Message.user_type, ct.username));
        }
    }

    private synchronized void broadcastUser(String username) {
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);
            // try to write to the Client if it fails remove it from the list            
            if (!ct.writeMsg(new Message(Message.user_type, username))) {
                al.remove(i);
                display("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    private synchronized void broadcastRaze(ClientThread c, String razename) {
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);
            // try to write to the Client if it fails remove it from the list            
            if (ct != c) {
                if (!ct.writeMsg(new Message(Message.raze_type, razename))) {
                    al.remove(i);
                    display("Disconnected Client " + ct.username + " removed from list.");
                }
            }
        }
    }

    private synchronized void broadcastText(ClientThread c, String text) {
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);
            // try to write to the Client if it fails remove it from the list            
            if (ct != c) {
                if (!ct.writeMsg(new Message(Message.text_type, text))) {
                    al.remove(i);
                    display("Disconnected Client " + ct.username + " removed from list.");
                }
            }
        }
    }

    private synchronized void broadcastStart(ClientThread c) {
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);
            // try to write to the Client if it fails remove it from the list            
            if (ct != c) {
                if (!ct.writeMsg(new Message(Message.start_type, ""))) {
                    al.remove(i);
                    display("Disconnected Client " + ct.username + " removed from list.");
                }
            }
        }
    }

    private synchronized void broadcast(String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
        String messageLf = time + " " + message + "\n";
        // display message on console or GUI
//        if (sg == null) {
        System.out.print(messageLf);
//        } else {
//            sg.appendRoom(messageLf);     // append in the room window
//        }
        // we loop in reverse order in case we would have to remove a Client
        // because it has disconnected
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);
            // try to write to the Client if it fails remove it from the list
            if (!ct.writeMsg(new Message("", messageLf))) {
                al.remove(i);
                display("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    // for a client who logoff using the LOGOUT message
    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (int i = 0; i < al.size(); ++i) {
            ClientThread ct = al.get(i);
            // found it
            if (ct.id == id) {
                al.remove(i);
                broadcastLogoutOtherUsers();
                return;
            }
        }
    }

    /*
     *  To run as a console application just open a console window and: 
     * > java Server
     * > java Server portNumber
     * If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        // start server on port 1500 unless a PortNumber is specified 
        int portNumber = 1500;
        switch (args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;

        }
        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();
    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {

        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        // my unique id (easier for deconnection)
        int id;
        // the Username of the Client
        String username;
        // the only type of message a will receive
        Message message;
        // the date I connect        

        // Constructore
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                try {
                    // read the username
                    Message msg = (Message) sInput.readObject();
                    this.username = msg.text;
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                display(username + " just connected.");
            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            } // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work                        
        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    message = (Message) sInput.readObject();
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                // Switch on the type of message receive
                switch (message.type) {
                    case Message.raze_type:
                        broadcastRaze(this, message.text);
                        break;
                    case Message.start_type:
                        broadcastStart(this);
                        break;
                    case Message.text_type:
                        broadcastText(this, message.text);
                        break;
                }
            }
            // remove myself from the arrayList containing the list of the
            // connected Clients
            remove(id);
            close();
        }

        // try to close everything
        private void close() {
            // try to close the connection             
            try {
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }

        /*
         * Write a String to the Client output stream
         */
        private boolean writeMsg(Message msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(msg);
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }
    }
}
