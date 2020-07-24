package it.unicam.cs.pa.jbudget105057;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;


/**
 *
 * Classe che fa da controller e gestisce tutta la logica dell'applicazione.
 */
public class Ledger {

    private final IFileManagement fileManagement = new FileManagement();
    private final IBudgetManagement budgetManagement;


    public Ledger(IBudgetManagement budgetManagement) {

        this.budgetManagement = budgetManagement;
    }



    public double getBalance() {

        return this.budgetManagement.getBalance();
    }


    public void addTransaction(ITransaction transaction) {

        this.budgetManagement.insert(transaction);
    }


    public Function<ArrayList<ITransaction>, Pair<Boolean, Double>> trendBalance(LocalDate dateStart, LocalDate dateEnd) {
        return this.budgetManagement.trendBalance(dateStart, dateEnd);
    }


    public double balanceForTag(MovementType type, String tag) {

        return this.budgetManagement.balanceForTag(type, tag);
    }


    public double balanceForDates(MovementType type, LocalDate dateStart, LocalDate dateEnd) {
        return this.budgetManagement.balanceForDates(type, dateStart, dateEnd);
    }


    public HashMap<String, Double> balanceForEachTag(MovementType type) {

        return this.budgetManagement.balanceForEachTag(type);
    }


    public void setList(ArrayList<ITransaction> list) {

        this.budgetManagement.setList(list);
    }


    public ArrayList<ITransaction> getTransaction() {

        return budgetManagement.getList();
    }


    /**
     *
     * @param transaction transazione da scrivere sul file.
     */
    public void write(ITransaction transaction) {

        this.fileManagement.write(transaction);
    }


    public void read() {

        try {
            this.setList(this.fileManagement.read());

        } catch (IOException e) { e.printStackTrace(); }

    }

}
