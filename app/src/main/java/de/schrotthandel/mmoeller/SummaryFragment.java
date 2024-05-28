package de.schrotthandel.mmoeller;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.DocumentException;

public class SummaryFragment extends Fragment {

    private TextView tvName, tvAdress, tvCity, tvDate, tvTaxNumber, tvIban, tvReceiver, tvTitle;
    private String name, adress, cityAndPlZ, date, taxNumber, receiver, iban;

    private ImageButton btnSignature;

    private SignaturePad signaturePad;

    private Button clearButton, saveButton;

    private Bitmap signatureBitmap;
    Bitmap bitmap;
    private CreatePDF createPDF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        tvName = view.findViewById(R.id.nameTextView);
        tvAdress = view.findViewById(R.id.addressTextView);
        tvCity = view.findViewById(R.id.cityTextView);
        tvDate = view.findViewById(R.id.dateTextView);
        tvReceiver = view.findViewById(R.id.receiverTextView);
        tvIban = view.findViewById(R.id.ibanTextView);
        tvTaxNumber = view.findViewById(R.id.taxNumberTextView);
        btnSignature = view.findViewById(R.id.imageButton);
        tvTitle = view.findViewById(R.id.tvTitle);
        Button btnFinish = view.findViewById(R.id.btnFinish);
        RecyclerView recyclerView = view.findViewById(R.id.metallListRecyclerView);
        createPDF = new CreatePDF();


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        SummaryAdapter summaryAdapter = new SummaryAdapter();
        recyclerView.setAdapter(summaryAdapter);


        getCustomerData();

        setCustomerData();


        //Button for the Signature
        btnSignature.setOnClickListener(view12 -> showSignatureDialog());


        btnFinish.setOnClickListener(view1 -> {
            //create pdf file
            try {
                createPDF.getCustomerData(signatureBitmap, getContext());
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });


        return view;
    }

    private void getCustomerData() {

        CustomerData customerData = CustomerData.getInstance();
        name = customerData.getName();
        adress = customerData.getAddress();
        cityAndPlZ = customerData.getCityAndPLZ();
        date = customerData.getDate();
        taxNumber = customerData.getTaxNumber();
        receiver = customerData.getReceiverName();
        iban = customerData.getiBan();

    }

    private void setCustomerData() {
        tvName.setText(name);
        tvAdress.setText(adress);
        tvCity.setText(cityAndPlZ);
        tvDate.setText(date);
        tvTaxNumber.setText(taxNumber);


        //if receiver and Iban is empty,  then are the Textviews gone
        if (receiver.isEmpty() && iban.isEmpty()) {
            tvIban.setVisibility(View.GONE);
            tvReceiver.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
            tvReceiver.setText(receiver);
            tvIban.setText(iban);


    }

    private void showSignatureDialog() {
        // Erstelle einen Dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.signature_input_dialog);

        clearButton = dialog.findViewById(R.id.clear_button);
        saveButton = dialog.findViewById(R.id.save_button);
        signaturePad = dialog.findViewById(R.id.signaturePad);


        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            window.setAttributes(layoutParams);
        }

        clearButton.setOnClickListener(v -> signaturePad.clear());

        saveButton.setOnClickListener(v -> {
        signatureBitmap = signaturePad.getSignatureBitmap();

            if (signatureBitmap != null) {

                btnSignature.setImageBitmap(signatureBitmap);

                signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                    @Override
                    public void onStartSigning() {
                    }

                    @Override
                    public void onSigned() {
                        saveButton.setEnabled(true);
                        clearButton.setEnabled(true);
                    }

                    @Override
                    public void onClear() {
                        saveButton.setEnabled(false);
                        clearButton.setEnabled(true);
                    }
                });

            } else {

                Toast.makeText(getActivity(), "Fehler beim erstellen der Unterschrift!", Toast.LENGTH_SHORT).show();

            }

            dialog.dismiss();
        });

        dialog.show();
    }

}

