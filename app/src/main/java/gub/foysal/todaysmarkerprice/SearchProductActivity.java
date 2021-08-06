package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gub.foysal.todaysmarkerprice.Model.Products;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.ProductViewHolder;

public class SearchProductActivity extends AppCompatActivity {
    private EditText searchText;
    private Button searchBtn,searchlocation,searchprice;
    private RecyclerView searchList;
    private String searchInput;
    DatabaseReference searchRef;
    String checker="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        searchText=findViewById(R.id.search_product_name_id);
        searchBtn=findViewById(R.id.search_product_btn_id);
        searchList=findViewById(R.id.search_list_recyler_id);
        searchlocation=findViewById(R.id.search_product_location_btn_id);
        searchprice=findViewById(R.id.search_product_price_btn_id);

        searchRef= FirebaseDatabase.getInstance().getReference().child("Products");

        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="name";
                searchInput=searchText.getText().toString();
                onStart();
            }
        });
        searchlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="location";
                searchInput=searchText.getText().toString();
                onStart();
            }
        });
        searchprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker="price";
                searchInput=searchText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checker=="location"){
            searchLocalionize();
        }
        else if (checker=="price")
        {
            searchPricewise();
        }

        else if(checker=="name")
        {
            Query query1 = searchRef.orderByChild("pname").startAt(searchInput);
            final FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(query1, Products.class)
                    .build();

            final FirebaseRecyclerOptions<Products> options2=new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(query1, Products.class)
                    .build();

            FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int positon, @NonNull final Products products) {
                    holder.txtProductName.setText(products.getPname());
                    holder.textProductDec.setText(products.getDescription());
                    holder.texProductLocation.setText(products.getLocation());
                    holder.txtProductPrice.setText("Price = "+products.getPrice()+"TK");
                    Picasso.get().load(products.getImage()).into(holder.imageView);

                    //output.add(getItem(positon));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                            intent.putExtra("prk",products.getPrk());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.product_list_item,parent,false);
                    ProductViewHolder holder=new ProductViewHolder(view);
                    return holder;
                }
            };
            //getData(searchView.getQuery().toString());
            searchList.setAdapter(adapter);
            adapter.startListening();
        }
        else {
            Toast.makeText(this, "Please input for search", Toast.LENGTH_SHORT).show();
        }


    }

    private void searchPricewise() {
        Query query2 = searchRef.orderByChild("price").startAt(searchInput);
        final FirebaseRecyclerOptions<Products> options1=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query2, Products.class)
                .build();
        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int positon, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.textProductDec.setText(products.getDescription());
                holder.texProductLocation.setText(products.getLocation());
                holder.txtProductPrice.setText("Price = "+products.getPrice()+"TK");
                Picasso.get().load(products.getImage()).into(holder.imageView);

                //output.add(getItem(positon));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("prk",products.getPrk());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_list_item,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }

    public void searchLocalionize(){
        Query query3 = searchRef.orderByChild("location").startAt(searchInput).endAt(searchText + "\uf8ff");
        final FirebaseRecyclerOptions<Products> options2=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query3, Products.class)
                .build();
        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int positon, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.textProductDec.setText(products.getDescription());
                holder.texProductLocation.setText(products.getLocation());
                holder.txtProductPrice.setText("Price = "+products.getPrice()+"TK");
                Picasso.get().load(products.getImage()).into(holder.imageView);

                //output.add(getItem(positon));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("prk",products.getPrk());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_list_item,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
