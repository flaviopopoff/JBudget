package it.unicam.cs.pa.jbudget105057;

import java.time.LocalDate;


/**
 *
 * Interfaccia che caratterizza una singola transazione.
 */
public interface ITransaction {

    String getUser();
    void setUser(String user);

    String getTag();
    void setTag(String tag);

    double getMoney();
    void setMoney(double money);

    LocalDate getDate();
    void setDate(LocalDate date);

    MovementType getType();
    void setType(MovementType type);


    /**
     *
     * Metodo che misura la lunghezza del parametro formale e ritorna una spaziatura.
     *
     * @param <T> tipo che viene passo come parametro formale.
     * @return una spaziatura cosi da avere un corretto allineamento nelle tabelle e nel file.
     */
    default <T> String spaceWord(T obj) {

        return " ".repeat(Math.max(0, 15 - obj.toString().length() + 1));
    }

}
