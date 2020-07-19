package it.unicam.cs.pa.jbudget105057;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Interfaccia che si occupa dell'intera gestione del 'database' scrivendo su di esso
 * e leggendolo.
 */
public interface IFileManagement {

    void write(ITransaction transaction);
    ArrayList<ITransaction> read() throws IOException;
}
