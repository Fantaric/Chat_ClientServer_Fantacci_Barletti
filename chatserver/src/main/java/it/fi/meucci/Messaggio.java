package it.fi.meucci;

import java.util.ArrayList;

public class Messaggio {
    
    public int indice;
    public String mittente;
    public String destinatario;
    public String messaggio;
    public ArrayList<String> listaC;


    public Messaggio() {
    }

    public Messaggio(int indice, String mittente, String destinatario, String messaggio, ArrayList<String> listaC){
        this.indice = indice;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.messaggio = messaggio;
        this.listaC = listaC;
    }

    public int getIndice() {
        return this.indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getMittente() {
        return this.mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getDestinatario() {
        return this.destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getMessaggio() {
        return this.messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }


    public ArrayList<String> getListaC() {
        return this.listaC;
    }

    public void setListaC(ArrayList<String> listaC) {
        this.listaC = listaC;
    }
    


}
