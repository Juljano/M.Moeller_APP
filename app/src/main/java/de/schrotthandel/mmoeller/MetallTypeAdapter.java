package de.schrotthandel.mmoeller;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MetallTypeAdapter extends RecyclerView.Adapter<MetallTypeAdapter.MetallViewHolder> {

    private final List<Metall> metallList;

    private final MetallTypeData metallTypeData = MetallTypeData.getInstance();


    public MetallTypeAdapter(List<Metall> metallList) {
        this.metallList = metallList;
    }

    @NonNull
    @Override
    public MetallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metal_product, parent, false);
        return new MetallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetallViewHolder holder, int position) {
        Metall metall = metallList.get(position);

        holder.metallNameTextView.setText(metall.getName());

        //get Sort of Metall
       // metallTypeData.getMetalTypeList().add(metall.getName());


        // Set the TextWatcher für pricePerKgEditText
        holder.pricePerKgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateAndDisplayResult(holder);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        holder.weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateAndDisplayResult(holder);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }


    @Override
    public int getItemCount() {
        return metallList.size();
    }

    // ViewHolder-Klasse
    public class MetallViewHolder extends RecyclerView.ViewHolder {
        TextView metallNameTextView;
        CardView cardView;
        EditText weightEditText, pricePerKgEditText, sumEditText;

        public MetallViewHolder(View itemView) {
            super(itemView);
            metallNameTextView = itemView.findViewById(R.id.metallNameTextView);
            weightEditText = itemView.findViewById(R.id.weightEditText);
            pricePerKgEditText = itemView.findViewById(R.id.pricePerKgEditText);
            sumEditText = itemView.findViewById(R.id.sumEditText);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    private void calculateAndDisplayResult(MetallViewHolder holder) {
        try {
            double kgValue = Double.parseDouble(holder.weightEditText.getText().toString());
            double kgPerEuroValue = Double.parseDouble(holder.pricePerKgEditText.getText().toString());

            double result = kgPerEuroValue * kgValue;

            holder.sumEditText.setText(String.valueOf(result));


            if (!holder.weightEditText.getText().toString().isEmpty() && !holder.sumEditText.getText().toString().isEmpty() && !holder.pricePerKgEditText.getText().toString().isEmpty()) {
                metallTypeData.getMetalTypeList().add(holder.metallNameTextView.getText().toString());
                metallTypeData.getWeightList().add(kgValue);
                metallTypeData.getPriceList().add(kgPerEuroValue);
                metallTypeData.getSumList().add(result);

            }


        } catch (NumberFormatException e) {
            // Fehlerhafte Eingabe verhindern
            holder.sumEditText.setText("Ungültiger Wert");
        }
    }

}