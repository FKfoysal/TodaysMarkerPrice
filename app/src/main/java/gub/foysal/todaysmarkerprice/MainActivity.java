package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gub.foysal.todaysmarkerprice.Model.Users;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private String userphonePrimaryKey,userPasswordKey;
    private DatabaseReference rootref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        loadingbar=new ProgressDialog(this);
        userphonePrimaryKey=Paper.book().read(Prevelent.UserPhoneKey);
        userPasswordKey=Paper.book().read(Prevelent.UserPasswordKey);
        if (userphonePrimaryKey!=""&& userPasswordKey!=""){
            if (!TextUtils.isEmpty(userphonePrimaryKey)&& !TextUtils.isEmpty(userPasswordKey)){
                Allowaccess(userphonePrimaryKey,userPasswordKey);

                loadingbar.setTitle("Already Logged in");
                loadingbar.setMessage("Please wite....");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
            }
        }

    }

    private void Allowaccess(final String phone, final String password) {
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    Users userdata=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (userdata.getPhone().equals(phone))
                    {
                        if (userdata.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Login Successfully..", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent=new Intent(MainActivity.this,HomePageActivity.class);
                            Prevelent.currentOnlineusers=userdata;
                            startActivity(intent);
                        }
                        else {
                            loadingbar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account with this,"+phone+" number do not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void LoginClick(View view) {
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }

    public void RegistrationClick(View view) {
        Intent intent=new Intent(MainActivity.this,Register.class);
        startActivity(intent);
    }

}
