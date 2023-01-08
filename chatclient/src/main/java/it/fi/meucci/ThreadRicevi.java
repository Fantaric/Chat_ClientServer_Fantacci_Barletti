package it.fi.meucci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadRicevi extends Thread {
    Socket sock;
    Messaggio m = new Messaggio();
    Messaggio m1;

    public ThreadRicevi(Socket sock, Messaggio m1) {
        this.sock = sock;
        this.m1 = m1;
    }

    public ThreadRicevi() {
    }

    public void ricevi() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        for (;;) {
            String outDalServer = in.readLine();
            m = DeSerializzazione(outDalServer);

            switch (m.getIndice()) {
                case 200:

                    System.out.println("Istruzioni Utilizzo Chat:" + "\n" +
                            "Inserire indice:" + "\n" +
                            "-1 invio messaggio in chat privata" + "\n" +
                            "-2 Invio messaggio per tutti" + "\n" +
                            "-3 Lista dei partecipanti" + "\n" +
                            "-4 Disconnetti" + "\n");

                    break;
                case 201:
                    if (m.getListaC().size() != 0) {
                        System.out.println("Lista Partecipanti: ");
                        for (int i = 0; i < m.getListaC().size(); i++) {
                            System.out.println("- " + m.getListaC().get(i));
                        }
                    } else
                        System.out.print("Nessun Partecipante Connesso Alla Chat, Riprova Più Tardi!");
                    break;
                // Per tenere lista aggiornata;
                case 202:
                    m1.listaC = m.listaC;
                    System.out.println("\n----------------------------------------");
                    if (m.messaggio.equals("connesso"))
                        System.out.println(m.getMittente() + " si è connesso ");
                    else
                        System.out.println(m.getMittente() + " si è disconnesso ");
                    System.out.println("------------------------------------------");
                    break;
                case 400:
                    System.out.println("Nome già utilizzato all'interno della chat, Premi 0 per reinserirlo");
                    break;
                case 401:
                    System.out.println("Nessun Utente connesso! Non puoi inviare alcun messaggio");
                    break;
                case 402:
                    System.out.println("Destinatario errato");
                    break;
                case 100:
                    System.out.println("\n----------------------------------------------");
                    System.out.println(m.getMittente().toUpperCase() + "\n");
                    System.out.print("(" + m.getMittente() + ") --> " + m.getMessaggio() + " ");
                    System.out.print(" Vuoi rispondere? [Y/N] ");
                    m.destinatario = m.getMittente();
                    // per identificare messaggio privato nel thread comunica
                    m1.indice = 6;
                    m1.destinatario = m.getMittente();
                    break;
                case 101:
                    System.out.println("\n----------------------------------------------");
                    System.out.println("Messaggio pubblico ricevuto da " + m.getMittente() + ":\n");
                    System.out.println("(" + m.getMittente() + ") --> " + m.getMessaggio() + " ");
                    System.out.println(" Vuoi rispondere? [Y/N] ");
                    // per identificare messaggio pubblico nel thread comunica
                    m1.indice = 3;
                    break;
                // gestisce le risposte
                case 102:
                    System.out.print("(" + m.getMittente() + ") --> " + m.getMessaggio() + " ");
                    System.out.print(" Vuoi rispondere? [Y/N] ");
                    m1.destinatario = m.getMittente();
                    m1.indice = 6;
                    break;
                case 500:
                    System.out.println("Disconnesso dalla Chat");
                    sock.close();
                    break;
            }
        }
    }

    public Messaggio DeSerializzazione(String in) {
        ObjectMapper objectMapper = new ObjectMapper();
        Messaggio m = new Messaggio();
        try {
            m = objectMapper.readValue(in, Messaggio.class);
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Errore durante la deserializzazione");
        }
        return m;
    }

    @Override
    public void run() {
        try {
            ricevi();
        } catch (Exception e) {
            e.getMessage();
        }

    }
}
