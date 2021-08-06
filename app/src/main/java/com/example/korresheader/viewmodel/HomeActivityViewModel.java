package com.example.korresheader.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.korresheader.Contact;
import com.example.korresheader.ContactRepository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.Disposable;

public class HomeActivityViewModel extends ViewModel {

    //The LiveData for the contacts
    private final MutableLiveData<List<Contact>> contacts = new MutableLiveData<>();

    //The repository
    private ContactRepository contactRepository;

    //The disposable for the Contacts
    private Disposable contactsDisposable;

    /**
     * Constructor
     */
    public HomeActivityViewModel() {

    }

    /**
     * This method updates the Contacts when the ContactRepository list is changed
     */
    private void updateContacts() {
        HashMap<String, Contact> hashMap = contactRepository
                .getContactMap().blockingLatest().iterator().next();
        // We add the Contact to the local contacts value
        List<Contact> listItems = hashMap.values().stream()
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());
        contacts.postValue(listItems);
    }

    /**
     * Returns a LiveData with the current list of quickset items to show to the user.
     */
    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    /**
     * Set the context for the Repository
     * @param context The context
     */
    public void setContext(Context context) {
        contactRepository = new ContactRepository(context);
        startDisposable();
    }

    /**
     * This method starts the disposable
     */
    private void startDisposable() {
        contactsDisposable = contactRepository.getContactMap().subscribe(o -> updateContacts());
    }
}