package com.example.userpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class Profile extends AppCompatActivity implements List_Dialog.EDialog {
    private Button btn,btn_logout;
     ListView list;
    private ImageView ig;
    private ArrayAdapter<String> myadapter;
    private ArrayList<String> arrayList;
    TextView name;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    Task<Void> reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Welcome");

        btn=findViewById(R.id.btn_add);
        list=findViewById(R.id.list_item);
        arrayList=new ArrayList<String>();
        name=findViewById(R.id.txt_name);
        btn_logout=findViewById(R.id.btn_logout);
        ig=findViewById(R.id.imageView3);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("List");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    User info=snap.getValue(User.class);
                    String txt_list=" "+info.getList();
                    arrayList.add(txt_list);
                }
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getInstance().signOut();
                finish();
                Toast.makeText(Profile.this, "Logout successful", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        GoogleSignInAccount signin= GoogleSignIn.getLastSignedInAccount(this);
        if(signin!=null){
            name.setText(signin.getDisplayName());
            Glide.with(this).load(signin.getPhotoUrl()).into(ig);
        }
        myadapter=new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,arrayList);
        list.setAdapter(myadapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List_Dialog l=new List_Dialog();
                l.show(getSupportFragmentManager(),"List Dialog");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String selectedFromList = (String) (list.getItemAtPosition(i));
                User u=new User();
                u.listItem(selectedFromList);
                Bundle b=new Bundle();
                b.putString("ListItem",selectedFromList);
                Intent intent=new Intent(getApplicationContext(),UserPwd.class);
                intent.putExtras(b);
                startActivity(intent);

            }
        });
    }


    @Override
    public void applyText(String L_Name) {
        User u=new User();
        u.addList(L_Name);
        reference=database.getReference().child("User").child(user.getUid()).child("List").child(u.list).setValue(u);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }
}