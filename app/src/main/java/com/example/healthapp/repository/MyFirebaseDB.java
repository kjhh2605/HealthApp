package com.example.healthapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseDB {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("GYMPT");
    public static DatabaseReference getDB() {
        return mDatabase;
    }
}
