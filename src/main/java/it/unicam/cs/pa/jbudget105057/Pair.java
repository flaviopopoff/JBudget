package it.unicam.cs.pa.jbudget105057;


/**
 * Classe che rappresenta una coppia di due tipi di variabili, dichiarata final con lo scopo di essere
 * non modificabile.
 *
 * @param <T> primo tipo della classe.
 * @param <S> secondo tipo della classe.
 */
public final class Pair<T, S> {

    private T first;
    private S second;


    public Pair(T first, S second) {
        this.setFirst(first);
        this.setSecond(second);
    }


    public T getFirst() {

        return first;
    }

    private void setFirst(T first) {

        this.first = first;
    }


    public S getSecond() {

        return second;
    }

    private void setSecond(S second) {

        this.second = second;
    }


    @Override
    public String toString() {

        return first + ", " + second;
    }

}
