package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gub.foysal.todaysmarkerprice.Model.Cart;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView totalPrice,textmasg1;
    private Button next;
    private RecyclerView.LayoutManager llm;
    private int overTotalPrices=0;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_list_recyler_id);
        totalPrice=findViewById(R.id.total_price_id);
        next=findViewById(R.id.next_btn_id);
        textmasg1=findViewById(R.id.order_confirm_massage_id);

        recyclerView.setHasFixedSize(true);
        llm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CartActivity.this,OrderConfirmActivity.class);
                intent.putExtra("Total Prices",String.valueOf(overTotalPrices));
                startActivity(intent);
              finish();
           }
        });
        totalPrice.setText("Total Prices = "+String.valueOf(overTotalPrices) + " Tk");
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderedStateMsg();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        final FirebaseRecyclerOptions<Cart> options
                =new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef
                        .child("User View")
                        .child(Prevelent.currentOnlineusers.getPhone())
                        .child("Products"),Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart cart) {
                holder.cartProductName.setText(cart.getPname());
                holder.cartProductPrice.setText("Price "+cart.getPrice()+"Tk");
                holder.cartProductQuantity.setText("Quantity = "+cart.getQuentity());
                try {
                    x=Integer.valueOf(cart.getPrice());
                    int perProductprice = ((Integer.valueOf(cart.getPrice())))* Integer.valueOf(cart.getQuentity());
                    overTotalPrices= overTotalPrices + perProductprice;
                }catch (NumberFormatException e)
                {
                    //Log.i(e.getMessage(), "MyClass.getView() — get item number " + i);
                    Toast.makeText(CartActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]=new CharSequence[]
                                {
                                  "Edit",
                                  "Delete"
                                };
                        AlertDialog.Builder builder1=new AlertDialog.Builder(CartActivity.this);
                        builder1.setTitle("Cart Options: ");
                        builder1.setItems(options,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("prk", cart.getPrk());
                                    startActivity(intent);
                                }
                                if (i == 1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevelent.currentOnlineusers.getPhone())
                                            .child("Products")
                                            .child(cart.getPrk())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(CartActivity.this, "Item Delete Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(CartActivity.this, HomePageActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            }
                        });
                        builder1.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cart_list_view,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderedStateMsg(){
        DatabaseReference checkOrderRef=FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevelent.currentOnlineusers.getPhone());
        checkOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String shipedState=dataSnapshot.child("state").getValue().toString();
                    String uname=dataSnapshot.child("name").getValue().toString();

                    if (shipedState.equals("shipped"))
                    {
                        totalPrice.setText("Dear "+ uname+"\n Your order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);
                        textmasg1.setVisibility(View.VISIBLE);
                        textmasg1.setText(R.string.order_confirmation_massage);

                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "very soon you will recived your first order, " +
                                "then you can purchase more products.", Toast.LENGTH_SHORT).show();
                    }
                    else if (shipedState.equals("not shipped"))
                    {
                        totalPrice.setText("Shipping state='Not Shipped' ");
                        recyclerView.setVisibility(View.GONE);
                        textmasg1.setVisibility(View.VISIBLE);
                        next.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "very soon you will recived your first order, " +
                                "then you can purchase more products.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
