package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gub.foysal.todaysmarkerprice.Model.Comments;
import gub.foysal.todaysmarkerprice.Model.GpsTracker;
import gub.foysal.todaysmarkerprice.Model.Products;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.CommentViewHolder;
import gub.foysal.todaysmarkerprice.RecylerViewHolder.ProductViewHolder;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productdetailsImage;
    private TextView detailName, detailprice, detailDescription;
    private FloatingActionButton floatingActionButton;
    private ElegantNumberButton numberButton;
    private String productID = "", state="normal";

    private EditText commentText;
    private Button commentBtn,addToCart;
    private String checker = "";
    double latitude, longtitude;
    private GpsTracker gpsTracker;
    private String location, commentTextDoc, currentDateSave, currentTimeSave;
    public String commentRandomKey;
    public List<Address> addresses;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference commentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productdetailsImage = findViewById(R.id.product_detail_image_id);
        detailName = findViewById(R.id.product_detail_name_id);
        detailprice = findViewById(R.id.product_detail_price_id);
        detailDescription = findViewById(R.id.product_detail_description_id);
        addToCart = findViewById(R.id.fab_add_product_id);
        numberButton = findViewById(R.id.elegant_number_btn_id);

        commentText = findViewById(R.id.comment_id);
        commentBtn = findViewById(R.id.comment_btn_id);

        commentRef = FirebaseDatabase.getInstance().getReference()
                .child("Comments")
                .child(Prevelent.currentOnlineusers.getPhone());

        recyclerView = findViewById(R.id.recyler_view_id);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValiditiCommentInfo();
            }
        });

        productID = getIntent().getStringExtra("prk");

        ProductDetails(productID);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (state.equals("Order Placed")||state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailsActivity.this, "very soon your order is shipped or confirmed, " +
                            "then you can purchase more products.", Toast.LENGTH_LONG).show();
                }
                else {
                    addingTOCartList();
                }
            }
        });
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ValiditiCommentInfo() {
        commentTextDoc = commentText.getText().toString();
        if (TextUtils.isEmpty(commentTextDoc)) {
            Toast.makeText(this, "Comment Box is Emply", Toast.LENGTH_SHORT).show();
        } else {
            UpdateOnlyCommentsInfo();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderedStateMsg();

        FirebaseRecyclerOptions<Comments> options=new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(commentRef, Comments.class)
                .build();

        FirebaseRecyclerAdapter<Comments, CommentViewHolder> adapter
                = new FirebaseRecyclerAdapter<Comments, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int positon, @NonNull final Comments comments) {
                holder.textComment.setText(""+comments.getComment());
                holder.textAddress.setText("Address: "+comments.getLocation());
                holder.textTime.setText("Time: "+comments.getPrk());
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comment_list,parent,false);
                CommentViewHolder holder=new CommentViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void UpdateOnlyCommentsInfo()
    {
        gpsTracker = new GpsTracker(ProductDetailsActivity.this);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            if(gpsTracker.canGetLocation()){
                latitude = gpsTracker.getLatitude();
                longtitude = gpsTracker.getLongitude();
                addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                location = addresses.get(0).getAddressLine(0);

            }else{
                gpsTracker.showSettingsAlert();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        currentDateSave=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        currentTimeSave=currentTime.format(calendar.getTime());

        commentRandomKey=currentDateSave+currentTimeSave;
        final DatabaseReference commentRef=FirebaseDatabase.getInstance().getReference()
                .child("Comments")
                .child(Prevelent.currentOnlineusers.getPhone());

        HashMap<String,Object> commentMap=new HashMap<>();
        commentMap.put("prk",commentRandomKey);
        commentMap.put("comment",commentTextDoc);
        commentMap.put("date",currentDateSave);
        commentMap.put("time",currentTimeSave);
        commentMap.put("location",location);
        commentRef.child(commentRandomKey).updateChildren(commentMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(ProductDetailsActivity.this, "Comment Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }


    private void addingTOCartList() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM DD, yyyy");
        String saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss, a");
        String saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference listRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap=new HashMap<>();
        cartMap.put("prk",productID);
        cartMap.put("pname",detailName.getText().toString());
        cartMap.put("price",detailprice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quentity",numberButton.getNumber());
        cartMap.put("discount","");

        listRef.child("User View").child(Prevelent.currentOnlineusers.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            listRef.child("Admin View").child(Prevelent.currentOnlineusers.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ProductDetailsActivity.this,HomePageActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void ProductDetails(String productID) {
        final DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);
                    detailName.setText(products.getPname());
                    detailprice.setText(products.getPrice());
                    detailDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productdetailsImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                    if (shipedState.equals("shipped"))
                    {
                        state="Order Shipped";
                    }
                    else if (shipedState.equals("not shipped"))
                    {
                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
