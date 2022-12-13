package it.fi.meucci;

import java.net.Socket;


public class Main
{
    public static void main( String[] args )
    {
        Socket sock;
        ClientSocket client = new ClientSocket();
        try{
            
            sock = client.connetti();
            // messaggio che condividono i 2 thread;
            Messaggio m1 = new Messaggio();
            ThreadComunica tCom = new ThreadComunica(sock, m1);
            ThreadRicevi tRic = new ThreadRicevi(sock, m1);
            tCom.start();
            tRic.start();
        }catch(Exception e)
        {
            System.out.println( e.getMessage());
        }
        
    }
}
