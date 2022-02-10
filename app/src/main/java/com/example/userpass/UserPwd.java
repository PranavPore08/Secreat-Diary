package com.example.userpass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserPwd extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseUser user;
    EditText txt_username,txt_other;
    Button btn_save;
    private TextInputEditText txt_password;
    String list;
    String outputString,inputPassword;
    String AES="AES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pwd);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user = auth.getCurrentUser();

        txt_username = findViewById(R.id.txt_User_Name);
        txt_password = findViewById(R.id.txt_pwd);
        txt_other = findViewById(R.id.txt_other);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            list = b.getString("ListItem");
        }

        DatabaseReference ref = database.getReference().child("User/").child(user.getUid() + "/List1/" + list);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String one=snapshot.child("password").getValue(String.class);
                one=Decode.decode(one);
                txt_password.setText(one);
                txt_username.setText(snapshot.child("username1").getValue(String.class));
                txt_other.setText(snapshot.child("other").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

            btn_save=findViewById(R.id.button_save);
            btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("User/").child(user.getUid()+"/List1/"+list).removeValue();
                User u = new User();
                u.setUsername1(txt_username.getText().toString());

                outputString=txt_password.getText().toString();
                inputPassword= Encode.encode(outputString);
                u.setPassword(inputPassword);
                u.setOther(txt_other.getText().toString());
                u.addList(list);
                database.getReference().child("User/").child(user.getUid()+"/List1/").child(list).setValue(u);
                Intent i=new Intent(UserPwd.this,Profile.class);
                startActivity(i);

            }
        });
    }

    private String decrypt(String outputString) throws Exception{
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,generateKey(outputString));
        byte[] decodedValue=Base64.decode(outputString,Base64.DEFAULT);
        byte[] decValue=c.doFinal(decodedValue);
        String decryptedValue=new String(decValue);
        return decryptedValue;
    }

    private String encrypt(String password) throws Exception{
        SecretKeySpec key=generateKey(password);
        Cipher c=Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(password.getBytes());
        String encryptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;

    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes=password.getBytes("UTF-8");
        digest.update(bytes,0, bytes.length);
        byte[] key=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
}