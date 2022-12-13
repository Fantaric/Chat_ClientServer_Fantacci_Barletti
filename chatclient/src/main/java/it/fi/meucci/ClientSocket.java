package it.fi.meucci;


import java.net.Socket;


public class ClientSocket {
    
    private Socket sock;
    
    
    public Socket connetti()
    {
        try {
            sock = new Socket("localhost", 5678);
        } catch (Exception e) {
           System.out.println("Errore durante la connessione da parte del Client");
        }

        return sock;
    }
}
