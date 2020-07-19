package it.unicam.cs.pa.jbudget105057;

import java.io.IOException;


/**
 *
 * Interfaccia che si occupa della gestione della 'View' con i relativi metodi per lo start del programma
 * e la relativa terminazione di esso.
 */
public interface View {

    void open() throws IOException, InvalidInputException;
    void close();
}
