package it.fi.meucci;


public class Main 
{
    public static void main( String[] args )
    {
        ServerStr server = new ServerStr();
        try {
            System.out.print("SERVER IN ESECUZIONE");
            server.avvia();
        } catch (Exception e) { 
            System.out.println("Errore durante l'avvio del server");}
    }
   
}

