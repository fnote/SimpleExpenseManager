package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PerAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PerTransactionDAO;

/**
 * Created by user on 11/19/2016.
 */
public class PersistentExpenseManager extends ExpenseManager {


    private Context cont;
    public PersistentExpenseManager(Context cont){

        this.cont = cont;
        setup();
    }


    @Override
    public void setup() {
        //open an existing database or create new one
        SQLiteDatabase mydatabase = cont.openOrCreateDatabase("140623", cont.MODE_PRIVATE, null);

        //creating account table in the mydatabase
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Holder VARCHAR," +
                "Initial_amt REAL" +
                " );");


        //creating table TransactionLog in mydatabase and connecting it with Accounts table
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");



        //These two functions will hold our DAO instances in memory till the program exists
        PerAccountDAO accountDAO = new PerAccountDAO(mydatabase);
        //accountDAO.addAccount(new Account("Account12567","commercial bank","sithija",1500));

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PerTransactionDAO(mydatabase));
    }
}
