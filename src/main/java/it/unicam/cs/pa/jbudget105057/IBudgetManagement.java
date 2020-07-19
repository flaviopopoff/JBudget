package it.unicam.cs.pa.jbudget105057;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;


/**
 *
 * Interfaccia che gestisce le operazioni legate al budget attraverso i relativi metodi.
 */
public interface IBudgetManagement {

    double getBalance();

    void insert(ITransaction transaction);

    Function<ArrayList<ITransaction>, Pair<Boolean, Double>> trendBalance(LocalDate dateStart, LocalDate dateEnd);

    double balanceForTag(MovementType type, String tag);
    double balanceForDates(MovementType type, LocalDate dateStart, LocalDate dateEnd);

    HashMap<String, Double> balanceForEachTag(MovementType type);

    ArrayList<ITransaction> getList();
    void setList(ArrayList<ITransaction> list);
}
