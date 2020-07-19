package it.unicam.cs.pa.jbudget105057.Model;

import it.unicam.cs.pa.jbudget105057.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class BudgetManagementTest {

    @Test
    void getBalance() {
        BudgetManagement controller = new BudgetManagement();

        Transaction t1 = new Transaction(MovementType.EARNING, "A", 10, LocalDate.now(), "S");
        Transaction t2 = new Transaction(MovementType.EXPENSE, "B", -100, LocalDate.now(), "P");
        Transaction t3 = new Transaction(MovementType.EARNING, "C", 150, LocalDate.now(), "A");
        controller.insert(t1);
        controller.insert(t2);
        controller.insert(t3);
        assertEquals(60.0, controller.getBalance());

        Transaction t4 = new Transaction(MovementType.EXPENSE, "B", -150, LocalDate.now(), "S");
        Transaction t5 = new Transaction(MovementType.EARNING, "C", 275, LocalDate.now(), "A");
        controller.insert(t4);
        controller.insert(t5);
        assertEquals(185.0, controller.getBalance());

        Transaction t6 = new Transaction(MovementType.EARNING, "A", 80, LocalDate.now(), "S");
        controller.insert(t6);
        assertEquals(265.0, controller.getBalance());

        Transaction t7 = new Transaction(MovementType.EARNING, "B", 60, LocalDate.now(), "P");
        controller.insert(t7);
        assertEquals(325.0, controller.getBalance());

        Transaction t8 = new Transaction(MovementType.EXPENSE, "C", -125, LocalDate.now(), "A");
        Transaction t9 = new Transaction(MovementType.EXPENSE, "A", -99, LocalDate.now(), "S");
        controller.insert(t8);
        controller.insert(t9);
        assertEquals(101.0, controller.getBalance());
    }


    @Test
    void trendBalance() {
        BudgetManagement controller = new BudgetManagement();

        Transaction t1 = new Transaction(MovementType.EARNING, "B", 50, LocalDate.parse("2020-02-01"), "S");
        Transaction t2 = new Transaction(MovementType.EARNING, "C", 100, LocalDate.parse("2020-02-15"), "A");
        controller.insert(t1);
        controller.insert(t2);
        Pair<Boolean, Double> p1 = controller.trendBalance(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-03-31")).apply(controller.getList());
        assertTrue(p1.getFirst());

        Transaction t3 = new Transaction(MovementType.EXPENSE, "A", -1000, LocalDate.parse("2020-06-15"), "S");
        Transaction t4 = new Transaction(MovementType.EARNING, "A", 765, LocalDate.parse("2020-06-16"), "P");
        Transaction t5 = new Transaction(MovementType.EARNING, "B", 75, LocalDate.parse("2020-06-14"), "S");
        controller.insert(t3);
        controller.insert(t4);
        controller.insert(t5);
        Pair<Boolean, Double> p2 = controller.trendBalance(LocalDate.parse("2020-06-10"), LocalDate.parse("2020-06-20")).apply(controller.getList());
        Pair<Boolean, Double> p3 = controller.trendBalance(LocalDate.parse("2020-06-10"), LocalDate.parse("2020-06-20")).apply(controller.getList());
        assertFalse(p2.getFirst());
        assertEquals(-160, p3.getSecond());

        Transaction t6 = new Transaction(MovementType.EARNING, "A", 650, LocalDate.parse("2020-12-31"), "S");
        controller.insert(t6);
        Pair<Boolean, Double> p4 = controller.trendBalance(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-12-31")).apply(controller.getList());
        assertTrue(p4.getFirst());
    }


    @Test
    void balanceForTag() {
        BudgetManagement controller = new BudgetManagement();

        Transaction t1 = new Transaction(MovementType.EXPENSE, "A", -50, LocalDate.parse("2020-06-13"), "S");
        Transaction t2 = new Transaction(MovementType.EARNING, "B", 100, LocalDate.parse("2020-06-15"), "S");
        controller.insert(t1);
        controller.insert(t2);
        assertEquals(-50.0, controller.balanceForTag(MovementType.EXPENSE, "S"));

        Transaction t3 = new Transaction(MovementType.EARNING, "C", 200, LocalDate.parse("2020-06-16"), "A");
        controller.insert(t3);
        assertEquals(100.0, controller.balanceForTag(MovementType.EARNING, "S"));
        assertEquals(200.0, controller.balanceForTag(MovementType.EARNING, "A"));

        Transaction t4 = new Transaction(MovementType.EARNING, "C", 400, LocalDate.parse("2020-06-14"), "P");
        Transaction t5 = new Transaction(MovementType.EARNING, "A", 15, LocalDate.parse("2020-06-15"), "S");
        Transaction t6 = new Transaction(MovementType.EXPENSE, "A", -60, LocalDate.parse("2020-06-17"), "S");
        controller.insert(t4);
        controller.insert(t5);
        controller.insert(t6);
        assertEquals(115.0, controller.balanceForTag(MovementType.EARNING, "S"));
        assertEquals(-110.0, controller.balanceForTag(MovementType.EXPENSE, "S"));
    }


    @Test
    void balanceForDates() {
        BudgetManagement controller = new BudgetManagement();

        Transaction t1 = new Transaction(MovementType.EARNING, "A", 100, LocalDate.parse("2020-06-16"), "S");
        Transaction t2 = new Transaction(MovementType.EXPENSE, "A", -100, LocalDate.parse("2020-06-16"), "S");
        Transaction t3 = new Transaction(MovementType.EARNING, "B", 100, LocalDate.parse("2020-05-31"), "S");
        controller.insert(t1);
        controller.insert(t2);
        controller.insert(t3);
        assertEquals(100.0, controller.balanceForDates(MovementType.EARNING,
                LocalDate.parse("2020-06-01"), LocalDate.parse("2020-06-30")));
        assertEquals(-100.0, controller.balanceForDates(MovementType.EXPENSE,
                LocalDate.parse("2020-06-01"), LocalDate.parse("2020-06-30")));

        Transaction t4 = new Transaction(MovementType.EXPENSE, "C", -500, LocalDate.parse("2020-05-15"), "S");
        Transaction t5 = new Transaction(MovementType.EARNING, "A", 100, LocalDate.parse("2020-05-24"), "S");
        controller.insert(t4);
        controller.insert(t5);
        assertEquals(200.0, controller.balanceForDates(MovementType.EARNING,
                LocalDate.parse("2020-05-01"), LocalDate.parse("2020-05-31")));
        assertEquals(-600.0, controller.balanceForDates(MovementType.EXPENSE,
                LocalDate.parse("2020-04-12"), LocalDate.parse("2020-07-28")));

        Transaction t6 = new Transaction(MovementType.EXPENSE, "B", -100, LocalDate.parse("2020-06-16"), "S");
        controller.insert(t6);
        assertEquals(-700.0, controller.balanceForDates(MovementType.EXPENSE,
                LocalDate.parse("2020-01-01"), null));

        Transaction t7 = new Transaction(MovementType.EARNING, "B", 1000, LocalDate.parse("2020-06-01"), "S");
        Transaction t8 = new Transaction(MovementType.EARNING, "C", 100, LocalDate.parse("2020-06-16"), "S");
        Transaction t9 = new Transaction(MovementType.EARNING, "C", 100, LocalDate.parse("2020-06-16"), "S");
        Transaction t10 = new Transaction(MovementType.EXPENSE, "C", -100, LocalDate.parse("2020-06-16"), "S");
        controller.insert(t7);
        controller.insert(t8);
        controller.insert(t9);
        controller.insert(t10);
        assertEquals(300.0, controller.balanceForDates(MovementType.EARNING,
                LocalDate.parse("2020-06-16"), LocalDate.parse("2020-06-16")));
        assertEquals(-300.0, controller.balanceForDates(MovementType.EXPENSE,
                LocalDate.parse("2020-06-16"), LocalDate.parse("2020-06-16")));
        assertEquals(100.0, controller.balanceForDates(MovementType.EARNING,
                LocalDate.parse("2020-04-01"), LocalDate.parse("2020-05-30")));
        assertEquals(1500.0, controller.balanceForDates(MovementType.EARNING,
                LocalDate.parse("2020-01-01"), LocalDate.parse("2020-12-31")));
    }


    @Test
    void setList() {
        BudgetManagement controller = new BudgetManagement();

        controller.setList(null);
        assertTrue(controller.getList().isEmpty());

        ArrayList<ITransaction> t1 = new ArrayList<>();
        t1.add(new Transaction(MovementType.EARNING, "",0, LocalDate.now(), ""));
        controller.setList(t1);
        assertFalse(controller.getList().isEmpty());
    }


    @Test
    void movementForEachTag() {
        BudgetManagement controller = new BudgetManagement();
        HashMap<String, Double> hashMap = new HashMap<>();

        Transaction t1 = new Transaction(MovementType.EARNING, "ME", 100, LocalDate.now(), "S");
        Transaction t2 = new Transaction(MovementType.EARNING, "ME", 200, LocalDate.now(), "T");
        Transaction t3 = new Transaction(MovementType.EARNING, "ME", 300, LocalDate.now(), "T");

        controller.insert(t1);
        controller.insert(t2);
        controller.insert(t3);

        hashMap.put("S", 100.0);
        hashMap.put("T", 500.0);

        assertEquals(controller.balanceForEachTag(MovementType.EARNING), hashMap);
        hashMap.clear();


        Transaction t4 = new Transaction(MovementType.EXPENSE, "ME", -150, LocalDate.now(), "S");
        Transaction t5 = new Transaction(MovementType.EXPENSE, "ME", -50, LocalDate.now(), "A");

        controller.insert(t4);
        controller.insert(t5);

        hashMap.put("S", -150.0);
        hashMap.put("A", -50.0);
        assertEquals(controller.balanceForEachTag(MovementType.EXPENSE), hashMap);
    }

}