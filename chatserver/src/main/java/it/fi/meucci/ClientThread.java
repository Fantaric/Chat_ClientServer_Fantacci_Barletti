package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientThread extends Thread {

    ServerSocket portaServer;
    Socket clientSock;
    static Messaggio m = new Messaggio();
    HashMap<String, Socket> lista;

    public ClientThread(Socket client, ServerSocket server, HashMap<String, Socket> lista) {
        this.clientSock = client;
        this.portaServer = server;
        this.lista = lista;
    }

    public void comunica() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
        DataOutputStream out = new DataOutputStream(clientSock.getOutputStream());

        for (;;) {

            String strRicevuta = in.readLine();
            m = DeSerializzazione(strRicevuta);

            switch (m.getIndice()) {

                case 1:
                    if (lista != null) {
                        for (String key : lista.keySet()) {
                            if (key.equals(m.getMittente())) {
                                // indice 400 errore durante la connessione
                                m.indice = 400;
                                out.writeBytes(Serializzazione(m) + "\n");
                            }
                        }
                    }
                    if (m.indice != 400) {
                        lista.put(m.getMittente(), clientSock);
                        // connessione OK!
                        m.indice = 200;
                        out.writeBytes(Serializzazione(m) + "\n");
                        UpdatedList(m.getMittente(), "connesso");
                    }

                    break;
                case 2:
                    if (ControlList().size() != 0) {
                        for (String key : lista.keySet()) {
                            if (key.equals(m.getDestinatario())) {
                                DataOutputStream out1 = new DataOutputStream(lista.get(key).getOutputStream());
                                m.indice = 100;
                                out1.writeBytes(Serializzazione(m) + "\n");
                                break;
                            }
                        }
                        // brutto da cambiare!!!
                        if (m.indice != 100)
                            m.indice = 402;
                        else
                            break;
                    } else {
                        m.indice = 401;
                    }
                    out.writeBytes(Serializzazione(m) + "\n");
                    break;
                case 3:
                    for (String key : lista.keySet()) {
                        if (!key.equals(m.getMittente())) {
                            DataOutputStream out1 = new DataOutputStream(lista.get(key).getOutputStream());
                            m.indice = 101;
                            out1.writeBytes(Serializzazione(m) + "\n");
                        }
                    }
                    break;
                case 4:
                    m.listaC = ControlList();
                    m.indice = 201;
                    out.writeBytes(Serializzazione(m) + "\n");
                    break;
                case 5:
                    lista.remove(m.getMittente());
                    m.indice = 500;
                    out.writeBytes(Serializzazione(m) + "\n");
                    UpdatedList(m.getMittente(), "disconnesso");
                    break;
                // Risporta al messaggio privato
                case 6:
                    for (String key : lista.keySet()) {
                        if (key.equals(m.getDestinatario())) {
                            DataOutputStream out1 = new DataOutputStream(lista.get(key).getOutputStream());
                            m.indice = 102;
                            out1.writeBytes(Serializzazione(m) + "\n");
                        }
                    }
                    break;
            }
        }

    }

    public void UpdatedList(String nome, String azione) throws IOException {
        m.mittente = null;
        for (String key : lista.keySet()) {
            DataOutputStream out1 = new DataOutputStream(lista.get(key).getOutputStream());
            m.listaC = ControlList();
            m.indice = 202;
            m.mittente = nome;
            m.messaggio = azione;
            out1.writeBytes(Serializzazione(m) + "\n");
        }
    }

    public ArrayList<String> ControlList() {
        ArrayList<String> l = new ArrayList<>();
        for (String key : lista.keySet()) {
            if (!key.equals(m.getMittente())) {
                l.add(key);
            }
        }
        return l;
    }

    public Messaggio DeSerializzazione(String in) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            m = objectMapper.readValue(in, Messaggio.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la deserializzazione");
        }
        return m;
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

    public void run() {
        try {
            comunica();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
