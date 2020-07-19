package it.unicam.cs.pa.jbudget105057;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 *
 * Classe che si occupa di gestire la console.
 */
public class ConsoleView implements View {

    private final Scanner scanner = new Scanner(System.in);
    private boolean flag = true;

    private final Ledger ledger;


    public ConsoleView(Ledger ledger) {

        this.ledger = ledger;
    }


    /**
     *
     * Metodo che stampa le scelte del menu.
     */
    public void menu() {
        System.out.println("-----------------------------------------------");
        System.out.println("| [0] Exit.                                   |");
        System.out.println("| [1] Add movement [earning/expense].         |");
        System.out.println("| [2] Print movement list [earning/expense].  |");
        System.out.println("| [3] Return balance.                         |");
        System.out.println("| [4] [Expense/Earning] by 'time'.            |");
        System.out.println("| [5] [Expense/Earning] by 'tag'.             |");
        System.out.println("| [6] Budget trend in the time.               |");
        System.out.println("| [7] [Expense/Earning] foreach tag.          |");
        System.out.println("-----------------------------------------------");
    }



    /**
     *
     * @param option input passato dall'utente che corrisponde alla relativa scelta del menu.
     */
    public void choice(String option) throws InvalidInputException {

        switch (option) {

            case "0":
                exit();
                break;

            case "1":
                addTransaction();
                break;

            case "2":
                printTransactionsList();
                break;

            case "3":
                System.out.println("Balance: € " + this.ledger.getBalance() + "\n");
                break;

            case "4":
                transactionByDate();
                break;

            case "5":
                transactionByTag();
                break;

            case "6":
                transactionTrend();
                break;

            case "7":
                transactionForEachTag();
                break;

            default:
                throw new InvalidInputException("Option not found in the menu.");
        }

        System.out.println("\n-----------------------------------------------\n");
    }


    private void exit() {
        System.out.println("Goodbye!.\n");
        close();
    }



    private void addTransaction() {
        System.out.println("[1] Earning, [2] Expense: ");
        MovementType typeOfTransaction = (scanner.nextLine().equals("1")) ? MovementType.EARNING : MovementType.EXPENSE;
        System.out.println();

        System.out.println("User that make the movement: ");
        String nameOfTransaction = scanner.nextLine().toUpperCase();
        System.out.println();


        System.out.println("Money: ");
        String moneyOfTransaction = scanner.nextLine();

        if (typeOfTransaction.equals(MovementType.EXPENSE) && !moneyOfTransaction.startsWith("-")) {
            moneyOfTransaction = "-" + moneyOfTransaction;
        }

        System.out.println();


        System.out.println("Date: [yyyy-MM-dd]\n" +
                "If the date is not present, press [enter] and today's day is taken.");
        String temporaryDate = scanner.nextLine();
        LocalDate dateOfTransaction = temporaryDate.isEmpty() ? LocalDate.now() : LocalDate.parse(temporaryDate);
        System.out.println();

        System.out.println("Tag: ");
        String tagOfTransaction = scanner.nextLine().toUpperCase();
        System.out.println();

        Transaction transaction = new Transaction(typeOfTransaction, nameOfTransaction,
                Double.parseDouble(moneyOfTransaction), dateOfTransaction, tagOfTransaction);

        this.ledger.write(transaction);
        this.ledger.addTransaction(transaction);
        System.out.println("Movement added successfully.\n\n");
    }


    /**
     *
     * Metodo che ritorna una tabella dei movimenti.
     */
    private void printTransactionsList() {
        System.out.println("[1] Earning list, [2] Expense list.");
        MovementType typeOfListToReturn = (scanner.nextLine().equals("1")) ? MovementType.EARNING : MovementType.EXPENSE;

        System.out.println("Type            | User            | Money           | Date            | Tag");
        System.out.println("------------------------------------------------------------------------------------");

        forTable(typeOfListToReturn).accept(this.ledger.getTransaction());
    }

    private Consumer<ArrayList<ITransaction>> forTable(MovementType type) {
        return list -> list.stream().filter(t -> t.getType().equals(type)).forEach(System.out::println);
    }


    private void transactionByDate() {
        System.out.println("[1] Earning by time, [2] Expense by time.\n" +
                "Format response: [1/2, TIME_START [yyyy-MM-dd], TIME_END [yyyy-MM-dd]]");

        String[] splitTimeInput = scanner.nextLine().split(",");
        MovementType typeForBalanceByRange = splitTimeInput[0].trim().equals("1") ? MovementType.EARNING : MovementType.EXPENSE;

        System.out.println(typeForBalanceByRange + " in the range [" + splitTimeInput[1].trim() + ", " + splitTimeInput[2].trim() + "]: € " +
                this.ledger.balanceForDates(typeForBalanceByRange, LocalDate.parse(splitTimeInput[1].trim()),
                        LocalDate.parse(splitTimeInput[2].trim())));
 
    }



    private void transactionByTag() {
        System.out.println("[1] Earning by tag, [2] Expense by tag.\n" +
                "Format response: [1/2, Tag] ");

        String[] splitTagInput = scanner.nextLine().split(",");

        MovementType typeForBalanceByTag = splitTagInput[0].equals("1") ? MovementType.EARNING : MovementType.EXPENSE;
        System.out.println("Total " + typeForBalanceByTag + " for Tag [ " + splitTagInput[1].trim().toUpperCase() + " ]: € " +
                this.ledger.balanceForTag(typeForBalanceByTag, splitTagInput[1].trim().toUpperCase()));
    }



    private void transactionTrend() {
        System.out.println("Format response: [DATE_START [yyyy-MM-dd], DATE_END [yyyy-MM-dd]]\n" +
                "'DATE START' must be specified, 'DATE END' can be omitted with [enter] -> 2020-06-10.");

        String[] splitDates = scanner.nextLine().split(",");
        System.out.println(splitDates.length);
        LocalDate date = (splitDates.length == 1) ? null : LocalDate.parse(splitDates[1].trim());

        Pair<Boolean, Double> pair = this.ledger.trendBalance(LocalDate.parse(splitDates[0].trim()), date)
                .apply(this.ledger.getTransaction());
        System.out.println("Date: [" + splitDates[0].trim() + "] - [" + (date == null ? LocalDate.now() : date) + "]" +
                "\nState: " + pair.getFirst() + "\nBalance: € " + pair.getSecond());
    }



    private void transactionForEachTag() {
        System.out.println("[1] Earning foreach tag, [2] Expense foreach tag.");
        MovementType typeForEachTagList = (scanner.nextLine().equals("1")) ? MovementType.EARNING : MovementType.EXPENSE;
        System.out.println("Tag         | Money\n---------------------\n" +
                toStringHashMap(this.ledger.balanceForEachTag(typeForEachTagList)));
    }



    /**
     *
     * Metodo che ritorna una rappresentazione in stringa dell'HashMap passato.
     *
     * @param map HashMap passato.
     * @param <K> tipo della chiave.
     * @param <V> tipo dei valori.
     * @return l'HashMap in formato String.
     */
    private <K, V> String toStringHashMap(HashMap<K, V> map) {
        return map.entrySet().stream()
                .map(e -> (e.getKey() + " ".repeat(Math.max(0, 12 - e.getKey().toString().length()))) + "| " + e.getValue())
                .collect(Collectors.joining("\n"));
    }



    /**
     *
     * Metodo che rappresenta il punto di inizio del programma, dove inizialmente carica la lista
     * delle transazioni gia effettuate e prende gli input dell'utente.
     */
    @Override
    public void open() {

        this.ledger.read();

        while (flag) {

            menu();

            System.out.println("Option: ");
            String option = scanner.nextLine();

            try {
                choice(option);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    /**
     *
     * Metodo che termina il programma portando la flag a 'false' e ritornare uno status di uscita '0'
     * che indica che il programma è terminato senza problemi.
     */
    @Override
    public void close() {
        flag = false;
        System.exit(0);
    }

}
