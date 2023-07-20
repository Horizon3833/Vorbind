package io.vorbind.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.vorbind.chat.adapters.UserItem;
import io.vorbind.chat.adapters.UserListAdapter;
import io.vorbind.chat.helpers.SharedPreferencesHelper;

public class Home extends AppCompatActivity {

    //Static Variables
    private final String SELECT_UNIVERSAL_USER_TEXT = "Select User";
    private final String SELECT_USER_TEXT = "Home";

    //ALl The Components in Activity
    public FloatingActionButton fab;
    public RecyclerView recyclerView;
    public List<UserItem> userList;
    public LinearLayout noChatFound;
    public TextView headingView;

    //Database and User Variables
    public FirebaseDatabase database;
    public DatabaseReference usersReference;
    public UserListAdapter adapter;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialization of view components
        recyclerView = findViewById(R.id.recyclerView);
        noChatFound = findViewById(R.id.noChatFound);
        headingView = findViewById(R.id.textView);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> pickContact());

        //Database Initialized
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");
        sharedPreferencesHelper = new SharedPreferencesHelper(Home.this);

        //Adding user list
        userList = new ArrayList<>();

        // Create and set the adapter and adding click events to adapter items
        adapter = new UserListAdapter(userList, user -> {
            // Handle item click here
            if (headingView.getText().equals(SELECT_UNIVERSAL_USER_TEXT)) {
                headingView.setText(SELECT_USER_TEXT);
                sharedPreferencesHelper.addUser(user);
                addAllUsersToList();
            } else {
                //TODO: Chat Screen will open with Intent in which User name and all other details will be given
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adding Users to Recycler
        if (sharedPreferencesHelper.getUsers() != null && !sharedPreferencesHelper.getUsers().isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noChatFound.setVisibility(View.GONE);
            addAllUsersToList();
        } else {
            recyclerView.setVisibility(View.GONE);
            noChatFound.setVisibility(View.VISIBLE);
        }
    }

    private void pickContact() {
        headingView.setText(SELECT_UNIVERSAL_USER_TEXT);
        noChatFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        final List<String> numberTagsList = new ArrayList<>();
        final List<String> namesList = new ArrayList<>();
        final List<String> aboutsList = new ArrayList<>();

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot numberSnapshot : dataSnapshot.getChildren()) {
                    String numberTag = numberSnapshot.getKey();
                    numberTagsList.add(numberTag);

                    String name = numberSnapshot.child("Name").getValue(String.class);
                    namesList.add(name);

                    String about = numberSnapshot.child("About").getValue(String.class);
                    aboutsList.add(about);
                }

                Toast.makeText(Home.this, "Data Captured", Toast.LENGTH_SHORT).show();

                // Now you have all the data in separate lists
                // You can use the lists as needed here
                for (int i = 0; i < numberTagsList.size(); i++) {
                    String name = namesList.get(i);
                    String about = aboutsList.get(i);

                    userList.add(new UserItem(name, "@drawable/unknown", about));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur during the operation
                Toast.makeText(Home.this, "Error: ".concat(databaseError.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addAllUsersToList() {
        List<UserItem> usersList = sharedPreferencesHelper.getUsers();
        userList.clear();
        userList.addAll(usersList);
        adapter.notifyDataSetChanged();
    }


}