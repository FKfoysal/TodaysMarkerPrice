package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainActivity extends AppCompatActivity {
    private EditText adminEditName,adminEditPrice,adminEditDec;
    private Button applyToChengeBtn, deleteProduct;
    private ImageView adminEditImage;
    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain);
        adminEditName=findViewById(R.id.admin_edit_product_name_id);
        adminEditPrice=findViewById(R.id.admin_edit_product_price_id);
        adminEditDec=findViewById(R.id.admin_edit_product_description_id);
        deleteProduct=findViewById(R.id.delete_this_product_id);

        adminEditImage=findViewById(R.id.admin_edit_product_image_id);

        applyToChengeBtn=findViewById(R.id.apply_change_id);

        productID = getIntent().getStringExtra("prk");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        DisplayEditProductInfo();
        applyToChengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyChanges();
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteThisProduct();
            }
        });
    }

    private void DeleteThisProduct() {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainActivity.this, "This Product is deleted Sucessfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AdminMaintainActivity.this,AdminCatagoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ApplyChanges() {
        String pname=adminEditName.getText().toString();
        String pprice=adminEditPrice.getText().toString();
        String pdec=adminEditDec.getText().toString();
        if (pname.equals("")){
            Toast.makeText(this, "Write Down Product Name", Toast.LENGTH_SHORT).show();
        }
        else if (pprice.equals("")){
            Toast.makeText(this, "Write Down Product Price", Toast.LENGTH_SHORT).show();
        }
        else if (pdec.equals("")){
            Toast.makeText(this, "Write Down Product Decription", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> productMap=new HashMap<>();
            productMap.put("prk", productID);
            productMap.put("description",pdec);
            productMap.put("price",pprice);
            productMap.put("pname",pname);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainActivity.this, "Changes is Successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainActivity.this,AdminCatagoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void DisplayEditProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name=dataSnapshot.child("pname").getValue().toString();
                    String price=dataSnapshot.child("price").getValue().toString();
                    String dec=dataSnapshot.child("description").getValue().toString();
                    String image=dataSnapshot.child("image").getValue().toString();

                    adminEditName.setText(name);
                    adminEditPrice.setText(price);
                    adminEditDec.setText(dec);
                    Picasso.get().load(image).into(adminEditImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
