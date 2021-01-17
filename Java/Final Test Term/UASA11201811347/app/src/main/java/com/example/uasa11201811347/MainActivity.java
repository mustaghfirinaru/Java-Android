package com.example.uasa11201811347;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private String s_user,s_pass;
    private Adapter adapter;
    private String key,t_user,t_pass;
    private Item getitem;
    private Button btn_submit,btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        //View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_submit = findViewById(R.id.submit);
        btn_update = findViewById(R.id.update);
        user= findViewById(R.id.field1);
        pass= findViewById(R.id.field2);
        btn_submit.setOnClickListener(v -> {
            boolean kosong = false;
            if (user.getText().toString().equalsIgnoreCase("")) {
                user.setError("Empty");
                kosong = true;
            }
            if (pass.getText().toString().equalsIgnoreCase("")) {
                pass.setError("Empty");
                kosong = true;
            }
            if (!kosong) {
                s_user=user.getText().toString();
                s_pass=pass.getText().toString();
                Item itemuser = new Item(s_user,s_pass);
                myRef.child("users").push().setValue(itemuser);
                user.setText("");
                pass.setText("");
            }
        });
        btn_update.setEnabled(false);
        btn_update.setOnClickListener(v -> {
            boolean kosong = false;
            if (user.getText().toString().equalsIgnoreCase("")) {
                user.setError("Empty");
                kosong = true;
            }
            if (pass.getText().toString().equalsIgnoreCase("")) {
                pass.setError("Empty");
                kosong = true;
            }
            if (!kosong) {
                s_user = user.getText().toString();
                s_pass = pass.getText().toString();
                Item itemuser = new Item(s_user,s_pass);
                myRef.child("users").child(key).setValue(itemuser);
                btn_submit.setEnabled(true);
                btn_update.setEnabled(false);
                user.setText("");
                pass.setText("");
            }
        });
        RecyclerView rv = findViewById(R.id.items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("message").child("users"), Item.class)
                        .build();

        adapter=new Adapter(options);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                TraditionallistDialog(documentSnapshot);
            }
        });
    }
    public void TraditionallistDialog(final DataSnapshot documentSnapshot)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Option");
        String[] options = {"Edit", "Delete"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            String id;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        FirebaseDatabase.getInstance().getReference("message").child("users").child(documentSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                btn_submit.setEnabled(false);
                                btn_update.setEnabled(true);
                                key=snapshot.getKey();
                                getitem= (Item) snapshot.getValue(Item.class);
                                user.setText(getitem.getUsername());
                                pass.setText(getitem.getPass());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    case 1:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Hapus")
                                .setMessage("Yakin hapus?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        FirebaseDatabase.getInstance().getReference("message").child("users").child(documentSnapshot.getKey()).removeValue();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}