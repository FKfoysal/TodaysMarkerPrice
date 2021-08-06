package gub.foysal.todaysmarkerprice.RecylerViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gub.foysal.todaysmarkerprice.R;
import gub.foysal.todaysmarkerprice.interfaces.itemClickLiseaner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cartProductName,cartProductQuantity,cartProductPrice;
    private itemClickLiseaner liseaner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProductName=itemView.findViewById(R.id.cart_product_name_id);
        cartProductPrice=itemView.findViewById(R.id.cart_product_price_id);
        cartProductQuantity=itemView.findViewById(R.id.cart_product_quantity_id);
    }

    public void setLiseaner(itemClickLiseaner liseaner) {
        this.liseaner = liseaner;
    }

    @Override
    public void onClick(View view) {
        liseaner.onClick(view, getAdapterPosition(),false);
    }
}
