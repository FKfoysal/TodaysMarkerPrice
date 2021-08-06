package gub.foysal.todaysmarkerprice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCatagoryActivity extends AppCompatActivity {
    private ImageView piaj,mosla,rosun,holud;
    private ImageView puishak,lalshak,kolmishak,palonshak;
    private ImageView cicinga,dundol,jali,kakrol;

    private Button logoutBtn,maintainBtn,checkOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catagory);
        piaj=findViewById(R.id.piaj_id);
        mosla=findViewById(R.id.mosla_id);
        rosun=findViewById(R.id.mosla_id);
        holud=findViewById(R.id.holud_id);

        puishak=findViewById(R.id.puishak_id);
        lalshak=findViewById(R.id.lalshak_id);
        kolmishak=findViewById(R.id.kolmishak_id);
        palonshak=findViewById(R.id.palonshak_id);

        cicinga=findViewById(R.id.cicinga_id);
        dundol=findViewById(R.id.dundol_id);
        jali=findViewById(R.id.jali_id);
        kakrol=findViewById(R.id.kakrol_id);

        logoutBtn=findViewById(R.id.logout_admin_id);
        maintainBtn=findViewById(R.id.maintain_product_id);
        checkOrderBtn=findViewById(R.id.check_new_order_product_id);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCatagoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCatagoryActivity.this,AdminNewOrderedActivity.class);
                startActivity(intent);
            }
        });
        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminCatagoryActivity.this,HomePageActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });
    }

    public void PeajClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","piaj");
        startActivity(intent);
    }

    public void MoslaClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","mosla");
        startActivity(intent);
    }

    public void HoludClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","holud");
        startActivity(intent);
    }

    public void RosunClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","rosun");
        startActivity(intent);
    }

    public void PushakClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","pushak");
        startActivity(intent);
    }

    public void LalshakClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","lalshak");
        startActivity(intent);
    }

    public void KolmishakClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","kolmishak");
        startActivity(intent);
    }

    public void PalonshakClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","palonshak");
        startActivity(intent);
    }

    public void CicingaClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","cicinga");
        startActivity(intent);
    }

    public void DundolClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","dundol");
        startActivity(intent);
    }

    public void JaliClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","jali");
        startActivity(intent);
    }

    public void KakrolClick(View view) {
        Intent intent=new Intent(AdminCatagoryActivity.this,AdminPanelActivity.class);
        intent.putExtra("catagory","kakrol");
        startActivity(intent);
    }
}
