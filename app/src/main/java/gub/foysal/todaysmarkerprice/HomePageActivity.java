package gub.foysal.todaysmarkerprice;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gub.foysal.todaysmarkerprice.Model.Products;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.ProductViewHolder;
import io.paperdb.Paper;


public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userProName;
    private CircleImageView userProImage;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null) {
            type = getIntent().getExtras().get("Admin").toString();
        }

        productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")){
                    Intent intent=new Intent(HomePageActivity.this,CartActivity.class);
                    startActivity(intent);
                }

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        userProName= (TextView) headerView.findViewById(R.id.profilename_show_id);
        userProImage=headerView.findViewById(R.id.profile_image_id);
        try {
            if (!type.equals("Admin")) {
                userProName.setText(Prevelent.currentOnlineusers.getName().trim());
                Picasso.get().load(Prevelent.currentOnlineusers.getImage()).placeholder(R.drawable.profile).into(userProImage);
            }
        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        recyclerView=findViewById(R.id.recyler_view_id);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef, Products.class)
                .build();

         adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int positon, @NonNull final Products products) {
                holder.txtProductName.setText(products.getPname());
                holder.textProductDec.setText(products.getDescription());
                holder.texProductLocation.setText(products.getLocation());
                holder.txtProductPrice.setText("Price = "+products.getPrice()+"TK");
                Picasso.get().load(products.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type.equals("Admin")){
                            Intent intent=new Intent(HomePageActivity.this,AdminMaintainActivity.class);
                            intent.putExtra("prk",products.getPrk());
                            startActivity(intent);
                        }
                        else {
                            Intent intent=new Intent(HomePageActivity.this,ProductDetailsActivity.class);
                            intent.putExtra("prk",products.getPrk());
                            startActivity(intent);
                        }
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
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(HomePageActivity.this);

        alertdialog.setTitle(R.string.dilog_mass_name);
        alertdialog.setIcon(R.drawable.ic_help_outline_black_24dp);
        alertdialog.setCancelable(true);

        alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=alertdialog.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
        MenuItem searchItem=menu.findItem(R.id.search);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomePageActivity.this,SearchProductActivity.class);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart_id) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomePageActivity.this,CartActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_catagory_id) {

        } else if (id == R.id.nav_search_id) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomePageActivity.this,SearchProductActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_setting_id) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomePageActivity.this,SettingActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_logout_id) {
            if (!type.equals("Admin")){
                Paper.book().destroy();
                Intent intent =new Intent(HomePageActivity.this,MainActivity.class);
                startActivity(intent);
            }


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
