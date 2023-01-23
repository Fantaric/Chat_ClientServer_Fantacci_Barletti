package it.fi.meucci;

import javax.sound.sampled.SourceDataLine;

public class Main 
{
    public static void main( String[] args )
    {
        ServerStr server = new ServerStr();
        try {
            server.avvia();
            System.out.print("SERVER IN ESECUZIONE");
        } catch (Exception e) { 
            System.out.println("Errore durante l'avvio del server");}
    }
   
}

