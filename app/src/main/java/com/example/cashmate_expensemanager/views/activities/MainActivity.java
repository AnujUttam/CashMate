package com.example.cashmate_expensemanager.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cashmate_expensemanager.adapter.TransactionsAdapter;
import com.example.cashmate_expensemanager.models.Transaction;
import com.example.cashmate_expensemanager.utils.Constants;
import com.example.cashmate_expensemanager.utils.Helper;
import com.example.cashmate_expensemanager.viewmodels.MainViewModel;
import com.example.cashmate_expensemanager.views.fragments.AddTransactionFragment;
import com.example.cashmate_expensemanager.R;
import com.example.cashmate_expensemanager.databinding.ActivityMainBinding;
import com.example.cashmate_expensemanager.views.fragments.StatsFragment;
import com.example.cashmate_expensemanager.views.fragments.TransactionFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //setupDatabase();

        Constants.setCategories();
        calendar = Calendar.getInstance();
        //updateDate();

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new TransactionFragment());
        transaction.commit();

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(item.getItemId() == R.id.transactions) {
                    getSupportFragmentManager().popBackStack();
                } else if(item.getItemId() == R.id.stats){
                    transaction.replace(R.id.content, new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }

}
