package de.schrotthandel.mmoeller;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MetallTypeAdapter extends RecyclerView.Adapter<MetallTypeAdapter.MetallViewHolder> {

    private final List<Metall> metallList;
    private final List<Double> tempKgValues = new ArrayList<>();
    private final List<Double> tempKgPerEuroValues = new ArrayList<>();
    private final List <String> metallSort =new ArrayList<>();
    private final Context context;


    public MetallTypeAdapter(List<Metall> metallList, Context context) {
        this.metallList = metallList;
        this.context = context;
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

        //set MetallType in Textview
        holder.metallNameTextView.setText(metall.getName());


        // Set the TextWatcher for pricePerKgEditText
        holder.pricePerKgEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {

                    calculateAndDisplayResult(holder);
                    setMetallSort(holder);


                } catch (NumberFormatException e) {

                    Toast.makeText(context, "Bitte nur Zahlen eingeben!" + position, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        // Set the TextWatcher for weightEditText
        holder.weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                try {

                    calculateAndDisplayResult(holder);

                } catch (NumberFormatException e) {

                    Toast.makeText(context, "Bitte nur Zahlen eingeben!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }


    public List<String> getMetallSort() {
        return metallSort;
    }

    public List<Double> getTempKgValues() {
        return tempKgValues;
    }

    public List<Double> getTempKgPerEuroValues() {
        return tempKgPerEuroValues;
    }

    @Override
    public int getItemCount() {
        return metallList.size();
    }

    // ViewHolder-class
    public static class MetallViewHolder extends RecyclerView.ViewHolder {
        TextView metallNameTextView;
        CardView cardView;
        EditText weightEditText, pricePerKgEditText, sumEditText;
        Button button;

        public MetallViewHolder(View itemView) {
            super(itemView);
            metallNameTextView = itemView.findViewById(R.id.metallNameTextView);
            weightEditText = itemView.findViewById(R.id.weightEditText);
            pricePerKgEditText = itemView.findViewById(R.id.pricePerKgEditText);
            sumEditText = itemView.findViewById(R.id.sumEditText);
            cardView = itemView.findViewById(R.id.cardview);
            button = itemView.findViewById(R.id.buttonSelection);

        }
    }


    private void setMetallSort(MetallViewHolder holder){

        if (!metallSort.contains(holder.metallNameTextView.getText().toString())){

            metallSort.add(holder.metallNameTextView.getText().toString());

        }
    }

    private void calculateAndDisplayResult(MetallViewHolder holder) {

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.GERMAN);

        try {

            double kgValue = Double.parseDouble(holder.weightEditText.getText().toString());
            double kgPerEuroValue = Double.parseDouble(holder.pricePerKgEditText.getText().toString());

            tempKgValues.add(Double.parseDouble(holder.weightEditText.getText().toString()));
            tempKgPerEuroValues.add(Double.parseDouble(holder.pricePerKgEditText.getText().toString()));

            double result = kgPerEuroValue * kgValue;

            holder.sumEditText.setText(numberFormat.format(result));

        } catch (NumberFormatException e) {

            // Fehlerhafte Eingabe verhindern
            holder.sumEditText.setText("Ungültiger Wert");


        }

    }
}