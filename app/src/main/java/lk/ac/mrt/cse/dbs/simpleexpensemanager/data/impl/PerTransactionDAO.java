package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by user on 11/19/2016.
 */
public class PerTransactionDAO implements TransactionDAO {


    private SQLiteDatabase database;

    public PerTransactionDAO(SQLiteDatabase dbase){
        this.database = dbase;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        //prepared statements
        String sql = "INSERT INTO TransactionLog (Account_no,Type,Amt,Log_date) VALUES (?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }



    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor results = database.rawQuery("SELECT * FROM TransactionLog",null);


        List<Transaction> transactions = new ArrayList<Transaction>();

        if(results.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(results.getLong(results.getColumnIndex("Log_date"))),
                        results.getString(results.getColumnIndex("Account_no")),
                        (results.getInt(results.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        results.getDouble(results.getColumnIndex("Amt")));
                transactions.add(t);
            }while (results.moveToNext());
        }
        return transactions;
    }



    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {


        Cursor resultSet = database.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("Amt")));
                transactions.add(t);
            } while (resultSet.moveToNext());
        }

        return transactions;


    }
}
