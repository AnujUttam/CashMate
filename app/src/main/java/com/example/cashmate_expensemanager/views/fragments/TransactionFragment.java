package com.example.cashmate_expensemanager.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cashmate_expensemanager.R;
import com.example.cashmate_expensemanager.adapter.TransactionsAdapter;
import com.example.cashmate_expensemanager.databinding.ActivityMainBinding;
import com.example.cashmate_expensemanager.databinding.FragmentTransactionBinding;
import com.example.cashmate_expensemanager.models.Transaction;
import com.example.cashmate_expensemanager.utils.Constants;
import com.example.cashmate_expensemanager.utils.Helper;
import com.example.cashmate_expensemanager.viewmodels.MainViewModel;
import com.example.cashmate_expensemanager.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;

public class TransactionFragment extends Fragment {

    public static TransactionFragment newInstance(String param1, String param2) {

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    FragmentTransactionBinding binding;
    //ActivityMainBinding binding;
    Calendar calendar;
    // Realm realm;
    public MainViewModel viewModel;
    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentTransactionBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        calendar = Calendar.getInstance();
        updateDate();

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTransactionFragment().show(getParentFragmentManager(), null);
            }
        });

        binding.nextbtn.setOnClickListener(v -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            }
            else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
            //viewModel.getTransactions(calendar);
        });
        binding.previousbtn.setOnClickListener(v -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            }
            else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
            //viewModel.getTransactions(calendar);
        });
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }else if(tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //ArrayList<Transaction> transactions = new ArrayList<>();

        binding.transactionList.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.transactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(getActivity(),transactions);
                binding.transactionList.setAdapter(transactionsAdapter);
                if (transactions.size() > 0){
                    binding.empty.setVisibility(View.GONE);
                }
                else {
                    binding.empty.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.IncomeValue.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseValue.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {

                binding.totalValue.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);
        return binding.getRoot();

    }

    void updateDate(){
        if (Constants.SELECTED_TAB == Constants.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }
        else if((Constants.SELECTED_TAB == Constants.MONTHLY)){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }


}