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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gub.foysal.todaysmarkerprice.Model.AdminNewOrders;
import gub.foysal.todaysmarkerprice.Model.Cart;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.CartViewHolder;

public class AdminUsersCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartLisPtRef;
    private String userId ="";

    private TextView cartListViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_cart);

        cartListViewName=findViewById(R.id.admin_cart_userName_id);
        recyclerView=findViewById(R.id.admin_product_list_recyler_id);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userId=getIntent().getStringExtra("Uid");
        String userNameId=getIntent().getStringExtra("Uname");
        cartListViewName.setText(userNameId+" Cart List");

        cartLisPtRef= FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(userId)
                .child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartLisPtRef,Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull final Cart cart) {
                holder.cartProductName.setText(cart.getPname());
                holder.cartProductPrice.setText("Price "+cart.getPrice()+"Tk");
                holder.cartProductQuantity.setText("Quantity = "+cart.getQuentity());
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
}
