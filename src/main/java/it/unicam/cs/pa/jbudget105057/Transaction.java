package it.unicam.cs.pa.jbudget105057;

import java.time.LocalDate;


/**
 *
 * Classe che rappresenta la transazione.
 */
public class Transaction implements ITransaction {

    private MovementType type;
    private String user;
    private double money;
    private LocalDate date;
    private String tag;


    /**
     *
     * Costruttore che crea la transazione.
     *
     * @param type entrata o uscita.
     * @param user che effettua il movimento.
     * @param money che riguardano il movimento.
     * @param date in cui viene fatto il movimento.
     * @param tag per cui è stato effettuato il movimento.
     */
    public Transaction(MovementType type, String user, double money, LocalDate date, String tag) {
        this.setType(type);
        this.setUser(user);
        this.setMoney(money);
        this.setDate(date);
        this.setTag(tag);
    }

    public Transaction() {
    }


    @Override
    public MovementType getType() {

        return type;
    }


    @Override
    public void setType(MovementType type) {

        this.type = type;
    }


    @Override
    public String getUser() {

        return user;
    }


    @Override
    public void setUser(String user) {

        this.user = user;
    }


    @Override
    public double getMoney() {

        return money;
    }


    @Override
    public void setMoney(double money) {

        this.money = money;
    }


    @Override
    public String getTag() {

        return tag;
    }


    @Override
    public void setTag(String tag) {

        this.tag = tag;
    }


    @Override
    public LocalDate getDate() {

        return date;
    }


    @Override
    public void setDate(LocalDate date) {

        this.date = date;
    }



    /**
     *
     * @return la rappresentazione del toString() della transazione per facilitare
     * la stampa in maniera tabellare.
     */
    @Override
    public String toString() {
        return  this.getType()  + this.spaceWord(this.getType())  + "| " +
                this.getUser()  + this.spaceWord(this.getUser())  + "| " +
                this.getMoney() + this.spaceWord(this.getMoney()) + "| " +
                this.getDate()  + this.spaceWord(this.getDate())  + "| " +
                this.getTag()   + this.spaceWord(this.getTag());
    }

}
