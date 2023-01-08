package it.fi.meucci;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadComunica extends Thread {

  Socket sock;
  Messaggio m = new Messaggio();
  Messaggio m1;
  ThreadRicevi tr = new ThreadRicevi();

  public ThreadComunica(Socket sock, Messaggio m1) {
    this.sock = sock;
    this.m1 = m1;
  }

  public ThreadComunica() {
  }

  public void comunica() throws IOException {
    try {
      DataOutputStream out = new DataOutputStream(sock.getOutputStream());
      BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));

      connessione(out, tastiera);

      for (;;) {
        // per gestire la risposta della chat
        String scelta = tastiera.readLine().toUpperCase();

        if (scelta.matches("[0-4]+")) {
          m.indice = Integer.parseInt(scelta);
        } else if (scelta.equals("Y") || scelta.equals("N"))
          m.indice = 5;
        else if (isInteger(scelta))
          m.indice = Integer.parseInt(scelta);
        else
          m.indice = 6;

        switch (m.indice) {

          case 1:
            if (m1.listaC.size() != 1) {
              m.indice = 2;
              System.out.print("Inserisci Destinatario: ");
              String destinatario = tastiera.readLine();
              System.out.println("Inserisci messaggio: ");
              String mes = tastiera.readLine();
              m.destinatario = destinatario;
              m.messaggio = mes;
              out.writeBytes(Serializzazione(m) + "\n");
              System.out.println("----------------------------------------------");
              System.out.println(destinatario.toUpperCase() + "\n");
            } else
              System.out.println("Non puoi inviare il messaggio, nessun utente connesso al momento");
            break;
          case 2:
            if (m1.listaC.size() != 1) {
              m.indice = 3;
              m.destinatario = null;
              System.out.println("Inserisci messaggio: ");
              m.messaggio = tastiera.readLine();
              out.writeBytes(Serializzazione(m) + "\n");
            } else
              System.out.println("Non puoi inviare il messaggio, nessun utente connesso al momento");
            break;
          case 3:
            m.indice = 4;
            out.writeBytes(Serializzazione(m) + "\n");
            break;
          case 4:
            m.indice = 5;
            out.writeBytes(Serializzazione(m) + "\n");
            break;
          case 5:
            if (scelta.toUpperCase().equals("Y")) {
              System.out.print("(Tu) --> ");
              m.messaggio = tastiera.readLine();
              // per controllare se la ripostà è signola o per tutti;
              m.indice = m1.getIndice();
              m.destinatario = m1.getDestinatario();
              out.writeBytes(Serializzazione(m) + "\n");
            }
            break;
          // per errore lettera
          case 6:
            System.out.println("Lettera inserita errata!");
            break;
          // per reinserire il nome
          case 0:
            connessione(out, tastiera);
            break;

        }
      }
    } catch (Exception e) {
      System.out.println("Errore di comunicazione");
    }

  }

  public String Serializzazione(Messaggio m) {

    String mes = "";
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      mes = objectMapper.writeValueAsString(m);
    } catch (Exception e) {
      e.getMessage();
      System.out.println("Errore durante la serializzazione");
    }
    return mes;
  }

  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    return true;
  }

  public void connessione(DataOutputStream out, BufferedReader tastiera) {
    try {
      System.out.print("\nInserisci Nome Per Connetterti alla Chat: ");
      String nome = tastiera.readLine();
      m.indice = 1;
      m.mittente = nome;
      m.listaC = new ArrayList<>();
      out.writeBytes(Serializzazione(m) + "\n");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  @Override
  public void run() {
    try {
      comunica();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
