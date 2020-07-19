package it.unicam.cs.pa.jbudget105057;

import javafx.application.Application;


public class App  {


    private final View view;


    public App(View view) {

        this.view = view;
    }


    public static void main(String[] args){

        try {

            if (args.length == 0) {
                new App(new ConsoleView(new Ledger(new BudgetManagement()))).startGUI();
            } else {
                new App(new ConsoleView(new Ledger(new BudgetManagement()))).startConsole();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void startConsole() throws Exception {

        view.open();
    }


    private void startGUI() {

        Application.launch(GUIView.class);
    }

}
