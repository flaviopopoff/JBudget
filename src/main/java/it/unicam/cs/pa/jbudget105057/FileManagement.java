package it.unicam.cs.pa.jbudget105057;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;


/**
 *
 * Classe che si occupa dell'interazione con il file, attraverso i relativi metodi.
 */
public class FileManagement implements IFileManagement {

    private final File FILE = new File("Movement.json");


    /**
     *
     * Metodo che scrive la transazione sul file.
     *
     * @param transaction da scrivere sul file.
     */
    @Override
    public void write(ITransaction transaction) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(new Gson().toJson(transaction) + "\r\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * Metodo che ricarica la lista dal rispettivo file ad ogni avvio del programma.
     *
     * @return un ArrayList di tutte le transazioni contenute nel file.
     */
    @Override
    public ArrayList<ITransaction> read() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(FILE));
        ArrayList<ITransaction> transactions = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            transactions.add(new Gson().fromJson(line, Transaction.class));
        }

        return transactions;
    }

}
