package io.vorbind.chat.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserStorageHelper {
    private DatabaseReference usersRef;

    public FirebaseUserStorageHelper() {
        // Initialize the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
    }

    public void storeUserData(String phoneNumber, String name, String about) {
        // Create a new child node under the "Users" node with the user ID as the key
        DatabaseReference userRef = usersRef.child(phoneNumber);

        // Set the user data in the child node
        userRef.child("Name").setValue(name);
        userRef.child("About").setValue(about);
    }
}
