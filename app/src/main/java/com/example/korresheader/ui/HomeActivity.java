package com.example.korresheader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.korresheader.R;
import com.example.korresheader.databinding.ActivityMainBinding;
import com.example.korresheader.util.Contact;
import com.example.korresheader.viewmodel.HomeActivityViewModel;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    //View binding
    private ActivityMainBinding binding;

    //Static value for the Intent when opening a contact
    public static final String SELECTED_CONTACT = "selected_contact";

    //ViewModel
    private HomeActivityViewModel viewModel;

    //The adapter for the RecyclerView
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        initList();
        observeModel();
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.addNewContactButton.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), ContactView.class)));
    }

    /**
     * This method initialize the RecyclerView and all it's necessary components
     */
    private void initList() {
        contactAdapter = new ContactAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.contactList.setLayoutManager(linearLayoutManager);
        binding.contactList.setAdapter(contactAdapter);
        binding.contactList.addItemDecoration(new StickyRecyclerHeadersDecoration(contactAdapter));
        binding.contactList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),binding.contactList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        continueWithContact(viewModel.getContacts().getValue().get(position));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //We don't do anything
                    }
                })
        );
    }

    /**
     * Method that invokes ContactView activity with the selected contact
     * @param contact The selected contact
     */
    private void continueWithContact(Contact contact) {
        Intent intent = new Intent(this, ContactView.class);
        intent.putExtra(SELECTED_CONTACT, contact);
        startActivity(intent);
    }

    /**
     * This method calls for observer the model
     */
    private void observeModel() {
        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        viewModel.getContacts().observe(this, contactAdapter::setContactList);
    }
}