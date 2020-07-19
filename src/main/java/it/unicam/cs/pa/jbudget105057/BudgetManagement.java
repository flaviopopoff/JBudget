package it.unicam.cs.pa.jbudget105057;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 *
 * Classe che fa da model e gestisce tutte le azioni legate alla transazione.
 */
public class BudgetManagement implements IBudgetManagement {

    private ArrayList<ITransaction> transactionsList = new ArrayList<>();
    private final Transaction transaction;


    public BudgetManagement() {

        this.transaction = new Transaction();
    }


    @Override
    public String toString() {

        return this.transaction.toString();
    }


    /**
     *
     * @return il bilancio.
     */
    @Override
    public double getBalance() {

        return this.getList().stream().mapToDouble(ITransaction::getMoney).sum();
    }


    /**
     *
     * @param transaction indica la transazione da inserire nell'ArrayList di transazioni.
     */
    @Override
    public void insert(ITransaction transaction) {

        this.getList().add(transaction);
    }


    /**
     *
     * Metodo che analizza l'andamento del bilancio in un dato range di tempo, se 'dateEnd' è 'null'
     * il range inizia da 'dateStart' fino a 'LocalDate.now()' (giorno corrente).
     *
     * @param dateStart data di inizio.
     * @param dateEnd   data di fine.
     * @return un Pair<>, coppia di valori data da una Function, che rappresentano l'andamento
     * del bilancio ed il bilancio stesso.
     */
    @Override
    public Function<ArrayList<ITransaction>, Pair<Boolean, Double>> trendBalance(LocalDate dateStart, LocalDate dateEnd) {
        LocalDate endDate = (dateEnd != null) ? dateEnd : LocalDate.now();

        return list -> {
            double trend = list.stream().filter(forDates(null, dateStart, endDate))
                    .mapToDouble(ITransaction::getMoney).sum();

            return new Pair<>(trend > 0, trend);
        };
    }


    /**
     * Metodo che prende la lista dei movimenti e la filtra per un dato 'tag', cosi da sapere
     * la somma delle spese/entrate per quel 'tag'.
     *
     * @param type entrata o uscita.
     * @param tag  di cui si vuole sapere il bilancio.
     * @return la somma di tutte le uscite/entrate effettuate con quel tag.
     */
    @Override
    public double balanceForTag(MovementType type, String tag) {

        return this.getList().stream().filter(t -> t.getTag().equals(tag) && t.getType().equals(type))
                .mapToDouble(ITransaction::getMoney).sum();
    }


    /**
     * Metodo che prende la lista dei movimenti e la filtra per un dato range di date, cosi da sapere
     * la somma delle spese/entrate in quel range.
     *
     * @param type      entrata o uscita.
     * @param dateStart data di inizio.
     * @param dateEnd   data di fine.
     * @return la somma di tutte le entrate/uscite effettuate in quel lasso di tempo.
     */
    @Override
    public double balanceForDates(MovementType type, LocalDate dateStart, LocalDate dateEnd) {

        LocalDate endDate = (dateEnd != null) ? dateEnd : LocalDate.now();

        return this.getList().stream().filter(forDates(type, dateStart, endDate))
                .mapToDouble(ITransaction::getMoney).sum();
    }


    /**
     * Metodo che ritorna un HashMap che ha un 'tag' come chiave e la somma come valore corrispondente.
     *
     * @return un HashMap formato solo da 'tag' non duplicati, e la somma legata ad esso.
     */
    @Override
    public HashMap<String, Double> balanceForEachTag(MovementType type) {

        HashMap<String, Double> map = new HashMap<>();

        tagNotDuplicate().forEach(tag -> {
            if (!(this.balanceForTag(type, tag) == 0)) {
                map.put(tag, this.balanceForTag(type, tag));
            }
        });

        return map;
    }

    /**
     *
     * @return una lista di String che contiene tutti i tag non duplicati presenti
     * nell'ArrayList delle transazioni.
     */
    private ArrayList<String> tagNotDuplicate() {
        return this.getList().stream().map(ITransaction::getTag).distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     *
     * @return la lista delle transazioni.
     */
    @Override
    public ArrayList<ITransaction> getList() {

        return this.transactionsList;
    }


    /**
     *
     * Metodo che setta la lista dei movimenti ad ogni restart del programma.
     * Se la lista passata è 'null' ritorno la lista dei movimenti istanziata in precedenza.
     *
     * @param list lista delle transazioni che garantisce 'persistenza'.
     */
    @Override
    public void setList(ArrayList<ITransaction> list) {

        if (list == null) {
            return;
        }

        this.transactionsList = list;
    }


    /**
     *
     * Metodo che viene utilizzato in due punti della classe, se type è null si procede
     * a tenere conto solo delle date, altrimenti anche dei tipo del movimento.
     *
     * @param type tipo per cui bisogna applicare il Predicate.
     * @param dates dates per cui bisogna 'filtrare'
     * @return un predicate da passare al filter dello stream cosi da non avere un eccessivo
     * numero di righe.
     */
    private Predicate<ITransaction> forDates(MovementType type, LocalDate... dates) {

        if (type != null) {
            return t -> dates[0].compareTo(t.getDate()) * t.getDate().compareTo(dates[1]) >= 0 && t.getType().equals(type);
        }

        return t -> dates[0].compareTo(t.getDate()) * t.getDate().compareTo(dates[1]) >= 0;
    }

}
