package it.fi.meucci;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerStr {
 
    ServerSocket server = null;
    Socket client = null;
    String strRicevuta;
    String strModificata;

    public Socket avvia() throws IOException {

        ServerSocket server = new ServerSocket(5678);
        HashMap<String, Socket> lista = new HashMap<>();
        for (;;) {
            client = server.accept();
            ClientThread t1 = new ClientThread(client, server, lista);
            t1.start();
        }
    }

}
