package it.fi.meucci;



public class Main 
{
    public static void main( String[] args )
    {
        ServerStr server = new ServerStr();
        try {
            server.avvia();
        } catch (Exception e) { 
            System.out.println("Errore durante l'avvio del server");}
    }
   
}

