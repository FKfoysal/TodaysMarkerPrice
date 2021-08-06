package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gub.foysal.todaysmarkerprice.Model.AdminNewOrders;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;

public class AdminNewOrderedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference newOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_ordered);

        newOrderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        recyclerView=findViewById(R.id.new_orders_recyler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminNewOrders> options=new FirebaseRecyclerOptions.Builder<AdminNewOrders>()
                .setQuery(newOrderRef,AdminNewOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminNewOrders,AdminNewOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminNewOrders, AdminNewOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminNewOrderViewHolder holder, final int position, @NonNull final AdminNewOrders adminNewOrders) {
                holder.uname.setText("Name: "+adminNewOrders.getName());
                holder.uphone.setText("Phone: "+adminNewOrders.getPhone());
                holder.utotalPrice.setText("Total Amount: "+adminNewOrders.getTotalAmount());
                holder.udateTime.setText("Date & Time: "+adminNewOrders.getDate()+" "+adminNewOrders.getTime());
                holder.uAddress.setText("Shipping Address: "+adminNewOrders.getAddress()+", "+adminNewOrders.getCity());

                holder.showProductBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Uid=getRef(position).getKey();

                        Intent intent=new Intent(AdminNewOrderedActivity.this,AdminUsersCartActivity.class);
                        intent.putExtra("Uid",Uid);
                        intent.putExtra("Uname",adminNewOrders.getName());
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]=new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder1=new AlertDialog.Builder(AdminNewOrderedActivity.this);
                        builder1.setTitle("Have you shipped this order products ?");
                        builder1.setItems(options,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    String Uid=getRef(position).getKey();
                                    RemovesOrdered(Uid);
                                }
                                if (i == 1)
                                {

                                }
                            }
                        });
                        builder1.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminNewOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_orders_layout,parent,false);
                return new AdminNewOrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemovesOrdered(String uid) {
        newOrderRef.child(uid).removeValue();
    }

    public static class AdminNewOrderViewHolder extends RecyclerView.ViewHolder
    {
        public TextView uname,uphone,utotalPrice,udateTime,uAddress;
        public Button showProductBtn;

        public AdminNewOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            uname=itemView.findViewById(R.id.new_ordere_user_name_id);
            uphone=itemView.findViewById(R.id.new_order_phone_number_id);
            utotalPrice=itemView.findViewById(R.id.new_order_total_price_id);
            udateTime=itemView.findViewById(R.id.new_order_date_time_id);
            uAddress=itemView.findViewById(R.id.new_order_address_city_id);
            showProductBtn=itemView.findViewById(R.id.new_order_show_btn_id);
        }
    }
}
