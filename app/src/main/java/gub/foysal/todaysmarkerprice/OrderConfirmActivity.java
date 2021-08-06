package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;

public class OrderConfirmActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private Button confirmOrder;
    private TextView viewtotalAmount;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);

        nameEditText=findViewById(R.id.order_name_id);
        phoneEditText=findViewById(R.id.order_number_id);
        addressEditText=findViewById(R.id.order_address_id);
        cityEditText=findViewById(R.id.order_city_id);
        viewtotalAmount=findViewById(R.id.order_totalAmount_id);
        confirmOrder=findViewById(R.id.order_btn_id);

        totalAmount=getIntent().getStringExtra("Total Prices");
        viewtotalAmount.setText("Total Amount: "+totalAmount+"Tk");

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checked();
            }
        });
    }

    private void Checked() {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Put Your Full Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Put Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Put Your Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Put Your City Name", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmOrdered();
        }
    }

    private void ConfirmOrdered() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM DD, yyyy");
        String saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss, a");
        String saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevelent.currentOnlineusers.getPhone());

        HashMap<String, Object> orderMap=new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("phone",phoneEditText.getText().toString());
        orderMap.put("address",addressEditText.getText().toString());
        orderMap.put("city",cityEditText.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("state","not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevelent.currentOnlineusers.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(OrderConfirmActivity.this, "Your Order has been Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(OrderConfirmActivity.this,HomePageActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
