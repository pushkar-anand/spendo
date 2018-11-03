package me.pushkaranand.spendo.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import me.pushkaranand.spendo.db.entity.Transaction;
import me.pushkaranand.spendo.repository.TransactionRepository;

import java.util.ArrayList;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;
    private LiveData<ArrayList<Transaction>> allTransactions;

    public TransactionViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository(application);
        allTransactions = transactionRepository.getAllTransactions();
    }

    public LiveData<ArrayList<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }

}
