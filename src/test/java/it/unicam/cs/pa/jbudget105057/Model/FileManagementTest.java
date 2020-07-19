package it.unicam.cs.pa.jbudget105057.Model;


import it.unicam.cs.pa.jbudget105057.FileManagement;
import it.unicam.cs.pa.jbudget105057.ITransaction;
import it.unicam.cs.pa.jbudget105057.MovementType;
import it.unicam.cs.pa.jbudget105057.Transaction;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class FileManagementTest {

    @Test
    void read() throws IOException {
        FileManagement fileManager = new FileManagement();
        ArrayList<ITransaction> transactions = fileManager.read();

        Transaction t1 = new Transaction(MovementType.EARNING, "A", 10, LocalDate.now(), "");
        Transaction t2 = new Transaction(MovementType.EXPENSE, "B", 15, LocalDate.now(), "");
        Transaction t3 = new Transaction(MovementType.EARNING, "C", 20, LocalDate.now(), "");
        fileManager.write(t1);
        fileManager.write(t2);
        fileManager.write(t3);
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);

        assertEquals(transactions.toString(), fileManager.read().toString());


        Transaction t4 = new Transaction(MovementType.EARNING, "C", 30, LocalDate.now(), "");
        Transaction t5 = new Transaction(MovementType.EXPENSE, "A", 45, LocalDate.now(), "");
        fileManager.write(t4);
        fileManager.write(t5);
        transactions.add(t4);
        transactions.add(t5);

        assertEquals(transactions.toString(), fileManager.read().toString());


        Transaction t6 = new Transaction(MovementType.EARNING, "B", 50, LocalDate.now(), "");
        fileManager.write(t6);
        transactions.add(t6);

        assertEquals(transactions.toString(), fileManager.read().toString());
    }

}
