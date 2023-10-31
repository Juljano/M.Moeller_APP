package de.schrotthandel.mmoeller;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {
    private final MetallTypeData metallTypeData = MetallTypeData.getInstance();



    @NonNull
    @Override
    public SummaryAdapter.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metal_product, parent, false);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.SummaryViewHolder holder, int position) {

        holder.metallNameTextView.setText(metallTypeData.getMetalTypeList().get(position));
        holder.sumEditText.setHint(String.valueOf(metallTypeData.getSumList().get(position)));
        holder.weightEditText.setHint(String.valueOf(metallTypeData.getWeightList().get(position)));
        holder.pricePerKgEditText.setHint(String.valueOf(metallTypeData.getPriceList().get(position)));


    }

    @Override
    public int getItemCount() {
       return metallTypeData.getMetalTypeList().size();
    }


    public class SummaryViewHolder extends RecyclerView.ViewHolder {

        TextView metallNameTextView;
        CardView cardView;
        EditText weightEditText, pricePerKgEditText, sumEditText;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            metallNameTextView = itemView.findViewById(R.id.metallNameTextView);
            weightEditText = itemView.findViewById(R.id.weightEditText);
            pricePerKgEditText = itemView.findViewById(R.id.pricePerKgEditText);
            sumEditText = itemView.findViewById(R.id.sumEditText);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
