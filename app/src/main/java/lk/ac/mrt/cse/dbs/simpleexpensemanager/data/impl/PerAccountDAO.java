package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by user on 11/19/2016.
 */
public class PerAccountDAO  implements AccountDAO {

    private SQLiteDatabase database;


    public PerAccountDAO(SQLiteDatabase dbase){
        this.database = dbase;
    }
    @Override


    public List<String> getAccountNumbersList() {

        //need a cursor object to loop through records in the db table
        Cursor result = database.rawQuery("SELECT Account_no FROM Account",null);


        //Initialize a list to to store data retrieved from the database table
        List<String> accounts = new ArrayList<String>();

        //Loop through the records  and add them to the List using cursor object
        //cursor object originally should point  to the first record
        if(result.moveToFirst()) {

            do {
                accounts.add(result.getString(result.getColumnIndex("Account_no")));
            }

            while (result.moveToNext());
        }

        return accounts;
        //return list
    }

    @Override
    public List<Account> getAccountsList() {

        //similar to the previous method but this time not only acc no but the rest is also has to be retrieved
        Cursor results = database.rawQuery("SELECT * FROM Account",null);
        List<Account> accounts = new ArrayList<Account>();

        if(results.moveToFirst()) {
            do {
                Account account = new Account(results.getString(results.getColumnIndex("Account_no")),
                        results.getString(results.getColumnIndex("Bank")),
                        results.getString(results.getColumnIndex("Holder")),
                        results.getDouble(results.getColumnIndex("Initial_amt")));
                accounts.add(account);
            } while (results.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor results = database.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(results.moveToFirst()) {
            do {
                account = new Account(results.getString(results.getColumnIndex("Account_no")),
                        results.getString(results.getColumnIndex("Bank")),
                        results.getString(results.getColumnIndex("Holder")),
                        results.getDouble(results.getColumnIndex("Initial_amt")));
            } while (results.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        //  use prepared statements here

        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Initial_amt) VALUES (?,?,?,?)";

        SQLiteStatement pstatement = database.compileStatement(sql);



        pstatement.bindString(1, account.getAccountNo());
        pstatement.bindString(2, account.getBankName());
        pstatement.bindString(3, account.getAccountHolderName());
        pstatement.bindDouble(4, account.getBalance());

        //Execute the prepared statement
        pstatement.executeInsert();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement pstatement = database.compileStatement(sql);

        pstatement.bindString(1,accountNo);

        pstatement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amt = Initial_amt + ?";
        SQLiteStatement pstatement = database.compileStatement(sql);

        if(expenseType == ExpenseType.EXPENSE){
            pstatement.bindDouble(1,-amount);
        } else{
            pstatement.bindDouble(1,amount);
        }

        pstatement.executeUpdateDelete();
    }








}
