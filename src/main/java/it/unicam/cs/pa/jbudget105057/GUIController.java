package it.unicam.cs.pa.jbudget105057;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 *
 * Classe che funge da controller per la GUI.
 */
public class GUIController implements Initializable {

    private final Ledger ledger;


    public GUIController() {

        ledger = new Ledger(new BudgetManagement());
    }



    @FXML
    private ChoiceBox<MovementType> typeTransaction;

    @FXML
    private TextField userTransaction;

    @FXML
    private ImageView image;

    @FXML
    private TextField moneyTransaction;

    @FXML
    private DatePicker dateTransaction;

    @FXML
    private TextField tagTransaction;

    @FXML
    public void addTransaction(ActionEvent actionEvent) {

        if (controlError()) {

            if (this.typeTransaction.getValue().equals(MovementType.EXPENSE) && !this.moneyTransaction.getText().startsWith("-")) {
                this.moneyTransaction.setText("-" + this.moneyTransaction.getText());
            } else if (this.typeTransaction.getValue().equals(MovementType.EARNING) && this.moneyTransaction.getText().startsWith("-")) {
                this.moneyTransaction.setText(this.moneyTransaction.getText().replace("-", ""));
            }

            Transaction transaction = new Transaction(this.typeTransaction.getValue(), this.userTransaction.getText().toUpperCase(),
                    Double.parseDouble(this.moneyTransaction.getText()), this.dateTransaction.getValue(), this.tagTransaction.getText().toUpperCase());
            this.clearInput();
            this.ledger.write(transaction);
            this.ledger.addTransaction(transaction);
        }
    }

    /**
     *
     * Metodo che procede a controllare eventuali errori di input dell'utente che
     * sono presenti nei textField.
     *
     * @return un valore booleano che indica se sono stati riscontrati errori o meno.
     */
    private boolean controlError() {
        if (this.typeTransaction.getValue() == null) {
            error(Alert.AlertType.WARNING, null, "Type must be selected!");
            return false;
        } else if (!this.moneyTransaction.getText().matches("[\\-\\+]?[0-9]*(\\.[0-9]+)?")) {
            error(Alert.AlertType.ERROR, "If the transaction is an expense need always positive number.", "Money must be insert!");
            return false;
        } else if (this.moneyTransaction.getText().isEmpty()) {
            this.moneyTransaction.setText("0.0");
            return true;
        } else if (this.dateTransaction.getValue() == null) {
            error(Alert.AlertType.INFORMATION, "If date isn't present, today's day is taken.", "Date of transaction today!");
            this.dateTransaction.setValue(LocalDate.now());
            return true;
        } else {
            return true;
        }
    }

    /**
     *
     * Metodo scritto con lo scopo di renderlo piu 'estendibile' possibile per essere usato
     * in ogni occasione, personalizzando tipo e testo.
     *
     * @param type tipo dell'errore (warning, error, confirm..).
     * @param headerText testo di informazione.
     * @param contentText messaggio di errore.
     */
    private void error(Alert.AlertType type, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    /**
     *
     * Metodo che mi ripulisce i TextFiled dopo aver premuto il bottone.
     */
    private void clearInput() {
        this.userTransaction.clear();
        this.moneyTransaction.clear();
        this.tagTransaction.clear();
    }



    @FXML
    private Label balance;

    @FXML
    public void updateBalance(ActionEvent actionEvent) {
        this.balance.setText(Double.toString(this.ledger.getBalance()));
    }



    @FXML
    private Label balanceForRange;

    @FXML
    private DatePicker dateStartForRange;

    @FXML
    private DatePicker dateEndForRange;

    @FXML
    private ChoiceBox<MovementType> choiceForRange;

    @FXML
    public void updateBalanceForRange(ActionEvent actionEvent) {

        if (this.dateStartForRange.getValue() == null) {
            error(Alert.AlertType.ERROR, null, "Date start must be selected!");
            return;
        }

        this.balanceForRange.setText(Double.toString(this.ledger.balanceForDates(choiceForRange.getValue(),
                this.dateStartForRange.getValue(), this.dateEndForRange.getValue())));
    }



    @FXML
    private Label balanceTrend;

    @FXML
    private DatePicker dateStartForTrend;

    @FXML
    private DatePicker dateEndForTrend;

    @FXML
    public void updateBalanceTrend(ActionEvent actionEvent) {

        if (this.dateStartForTrend.getValue() == null) {
            error(Alert.AlertType.ERROR, null, "Date start must be selected!");
            return;
        }

        this.balanceTrend.setText(this.ledger.trendBalance(this.dateStartForTrend.getValue(),
                this.dateEndForTrend.getValue()).apply(this.ledger.getTransaction()).toString());
    }



    @FXML
    private TableView<ITransaction> transactionTable;

    @FXML
    private TableColumn<Transaction, MovementType> typeColumnTransactionTable;

    @FXML
    private TableColumn<Transaction, String> userColumnTransactionTable;

    @FXML
    private TableColumn<Transaction, Double> moneyColumnTransactionTable;

    @FXML
    private TableColumn<Transaction, LocalDate> dateColumnTransactionTable;

    @FXML
    private TableColumn<Transaction, String> tagColumnTransactionTable;
    
    @FXML
    private ChoiceBox<MovementType> typeTransactionTable;

    @FXML
    public void showTypeTransactionTable(ActionEvent actionEvent) {
        this.transactionTable.setItems(this.returnTypeTransactionTable(this.typeTransactionTable.getValue()));
    }

    private ObservableList<ITransaction> returnTypeTransactionTable(MovementType type) {
        return FXCollections.observableList(this.ledger.getTransaction()
                .stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList()));
    }



    @FXML
    private TableView<Map.Entry<String, Double>> tagTable;

    @FXML
    private TableColumn<Map.Entry<String, Double>, String> tagColumnTagTable;

    @FXML
    private TableColumn<Map.Entry<String, Double>, String> moneyColumnTagTable;

    @FXML
    private ChoiceBox<MovementType> choiceTypeForEachTag;

    @FXML
    public void showTypeTagTable(ActionEvent actionEvent) {
        HashMap<String, Double> hashMap = ledger.balanceForEachTag(choiceTypeForEachTag.getValue());

        this.tagColumnTagTable.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        this.moneyColumnTagTable.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        ObservableList<Map.Entry<String, Double>> items = FXCollections.observableArrayList(hashMap.entrySet());
        this.tagTable.setItems(items);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.ledger.read();
        this.initializeChoice();

        this.typeColumnTransactionTable.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.userColumnTransactionTable.setCellValueFactory(new PropertyValueFactory<>("user"));
        this. moneyColumnTransactionTable.setCellValueFactory(new PropertyValueFactory<>("money"));
        this.dateColumnTransactionTable.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.tagColumnTransactionTable.setCellValueFactory(new PropertyValueFactory<>("tag"));

        this.image.setImage(new Image(new File("src/main/resources/jbudget.jpg").toURI().toString()));
    }

    private void initializeChoice() {
        this.typeTransaction.getItems().add(MovementType.EARNING);
        this.typeTransaction.getItems().add(MovementType.EXPENSE);

        this.choiceForRange.getItems().add(MovementType.EARNING);
        this.choiceForRange.getItems().add(MovementType.EXPENSE);

        this.choiceTypeForEachTag.getItems().add(MovementType.EARNING);
        this.choiceTypeForEachTag.getItems().add(MovementType.EXPENSE);

        this.typeTransactionTable.getItems().add(MovementType.EARNING);
        this.typeTransactionTable.getItems().add(MovementType.EXPENSE);
    }

}
