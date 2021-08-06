package gub.foysal.todaysmarkerprice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gub.foysal.todaysmarkerprice.Model.GpsTracker;
import gub.foysal.todaysmarkerprice.Prevelent.Prevelent;

public class AdminPanelActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private String catagoryName,pdescription,pname,pprice, currentDateSave, currentTimeSave,location;
    private Button addproduct;
    private EditText productname,productdescription,productprice;
    private ImageView camara;
    private static final int Galaryimage=1;
    private Uri imageUri;
    public String productRandomkey;
    private String downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference drf;
    private ProgressDialog loadingbar;
    double latitude,longtitude;
    private GpsTracker gpsTracker;
    public List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        catagoryName=getIntent().getExtras().get("catagory").toString();
        productImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        drf=FirebaseDatabase.getInstance().getReference().child("Products");

        camara=findViewById(R.id.camara_id);
        addproduct=findViewById(R.id.add_product_id);
        productname=findViewById(R.id.product_name_id);
        productdescription=findViewById(R.id.product_description_id);
        productprice=findViewById(R.id.product_price_id);

        loadingbar=new ProgressDialog(this);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalaryOpen();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValiditiDate();
            }
        });
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ValiditiDate() {
        pdescription=productdescription.getText().toString();
        pname=productname.getText().toString();
        pprice=productprice.getText().toString();
        if (imageUri==null){
            Toast.makeText(this, "Product image is mandatory..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pdescription)){
            Toast.makeText(this, "Please write product description..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pname)){
            Toast.makeText(this, "Please write product name..", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pprice)){
            Toast.makeText(this, "Please write product price..", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInfoToDatabase();
        }
    }

    private void GalaryOpen() {
        Intent galaryintent=new Intent();
        galaryintent.setAction(Intent.ACTION_GET_CONTENT);
        galaryintent.setType("image/*");
        startActivityForResult(galaryintent,Galaryimage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Galaryimage && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            camara.setImageURI(imageUri);
        }
    }
    private void StoreProductInfoToDatabase(){
        loadingbar.setTitle("Adding New Product");
        loadingbar.setMessage("Dear Admin Please Wait!..while we are adding the new Product...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        gpsTracker = new GpsTracker(AdminPanelActivity.this);
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
        SimpleDateFormat currentdate= new SimpleDateFormat("MMM dd, yyyy");
        currentDateSave=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime= new SimpleDateFormat("HH:mm:ss a");
        currentTimeSave=currenttime.format(calendar.getTime());

        productRandomkey =currentDateSave+currentTimeSave;
        final StorageReference filePath=productImageRef.child(imageUri.getLastPathSegment()+productRandomkey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String massage=e.toString();
                Toast.makeText(AdminPanelActivity.this, "Error: "+massage, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminPanelActivity.this, "Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> taskUri=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl= task.getResult().toString();
                            Toast.makeText(AdminPanelActivity.this, "Product image Url is save to Database Successfully..", Toast.LENGTH_SHORT).show();
                            SaveProductInfotoDatabaseFinally();
                        }
                    }
                });
            }
        });
    }


    private void SaveProductInfotoDatabaseFinally(){
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("prk", productRandomkey);
        productMap.put("date",currentDateSave);
        productMap.put("time",currentTimeSave);
        productMap.put("description",pdescription);
        productMap.put("image",downloadImageUrl);
        productMap.put("catagory",catagoryName);
        productMap.put("price",pprice);
        productMap.put("pname",pname);
        productMap.put("location",location);

        drf.child(productRandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(AdminPanelActivity.this,AdminCatagoryActivity.class);
                            startActivity(intent);
                            loadingbar.dismiss();
                            Toast.makeText(AdminPanelActivity.this, "Product is add Successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingbar.dismiss();
                            String massage=task.getException().toString();
                            Toast.makeText(AdminPanelActivity.this, "Error: "+massage, Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
