package gub.foysal.todaysmarkerprice.RecylerViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gub.foysal.todaysmarkerprice.R;
import gub.foysal.todaysmarkerprice.interfaces.itemClickLiseaner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, textProductDec,txtProductPrice,texProductLocation;
    public ImageView imageView;
    private itemClickLiseaner listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.sample_product_name_id);
        textProductDec=itemView.findViewById(R.id.sample_product_description_id);
        txtProductPrice=itemView.findViewById(R.id.sample_product_price_id);
        imageView=itemView.findViewById(R.id.sample_product_image_id);
        texProductLocation=itemView.findViewById(R.id.sample_product_location_id);
    }

    public void setListener(itemClickLiseaner listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(),false);
    }
}
