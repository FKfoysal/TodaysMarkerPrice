package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText name,phone,password;
    private ProgressDialog loadingbar;
    private DatabaseReference rootref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.user_name_reg_id);
        phone=findViewById(R.id.phone_reg_Id);
        password=findViewById(R.id.pass_reg_Id);
        loadingbar=new ProgressDialog(this);
    }

    public void RegistrationCreateAccount(View view) {
        CreateAccount();
    }

    private void CreateAccount() {
        String inputName=name.getText().toString();
        String inputPhone=phone.getText().toString();
        String inputPassword=password.getText().toString();
        if (TextUtils.isEmpty(inputName)){
            Toast.makeText(getApplicationContext(),"Please Wite Your Name.....",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(inputPhone)){
            Toast.makeText(getApplicationContext(),"Please Wite Your Phone Number.....",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(inputPassword)){
            Toast.makeText(getApplicationContext(),"Please Wite Your Password.....",Toast.LENGTH_LONG).show();
        }
        else {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please Wite!..while we are checking the credentials.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidtoPhoneNumber(inputName,inputPhone,inputPassword);
        }
    }

    private void ValidtoPhoneNumber(final String inputName, final String inputPhone, final String inputPassword) {
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Users").child(inputPhone).exists()){
                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",inputPhone);
                    userdatamap.put("password",inputPassword);
                    userdatamap.put("name",inputName);
                    rootref.child("Users").child(inputPhone).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "Congratulation! your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(Register.this,Login.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingbar.dismiss();
                                        Toast.makeText(Register.this, "Netword Error! please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),"This"+inputPhone+" already exists.",Toast.LENGTH_LONG).show();
                    loadingbar.dismiss();
                    Toast.makeText(Register.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register.this,MainActivity.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
