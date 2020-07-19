package it.unicam.cs.pa.jbudget105057;


/**
 *
 * Classe che gestisce le eccezioni di input che non sono presenti nel menu.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {

        super(message);
    }

}
