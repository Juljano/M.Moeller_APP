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
import android.widget.Toast;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.textfield.TextInputEditText;

public class SummaryFragment extends Fragment {

    private TextInputEditText etName, etAdress, etCity, etDate, etTaxNumber, etIban, etReceiver;
    private String name, adress, cityAndPlZ, date, taxNumber, receiver, iban;

    private Button btnFinish;

    private ImageButton btnSignature;

    private SignaturePad signaturePad;

    private Button clearButton, saveButton;

    private RecyclerView recyclerView;
    private SummaryAdapter summaryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        etName = view.findViewById(R.id.nameEditText);
        etAdress = view.findViewById(R.id.addressEditText);
        etCity = view.findViewById(R.id.cityEditText);
        etDate = view.findViewById(R.id.dateEditText);
        etReceiver = view.findViewById(R.id.receiverEditText);
        etIban = view.findViewById(R.id.ibanEditText);
        etTaxNumber = view.findViewById(R.id.taxNumberEditText);
        btnSignature = view.findViewById(R.id.imageButton);
        btnFinish = view.findViewById(R.id.btnFinish);
        recyclerView = view.findViewById(R.id.metallListRecyclerView);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        summaryAdapter = new SummaryAdapter();
        recyclerView.setAdapter(summaryAdapter);


        getCustomerData();

        setDataToEditText();


        //Button for the Signature
        btnSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignatureDialog();
            }
        });


        btnFinish.setOnClickListener(view1 -> {
            //creat pdf file
            CreatePDF createPDF = new CreatePDF(getActivity());
            createPDF.createPDFFile();
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

    private void setDataToEditText() {

        etName.setHint(name);
        etAdress.setHint(adress);
        etCity.setHint(cityAndPlZ);
        etDate.setHint(date);
        etTaxNumber.setHint(taxNumber);


        //if receiver and Iban is empty then is both edittexts gone
        if (receiver.isEmpty() && iban.isEmpty()) {
            etIban.setVisibility(View.GONE);
            etReceiver.setVisibility(View.GONE);
        }
        etReceiver.setHint(receiver);
        etIban.setHint(iban);


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
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();

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

                Toast.makeText(getActivity(), "Fehler beim speichern der Unterschrift!", Toast.LENGTH_SHORT).show();

            }
            btnSignature.setImageBitmap(signatureBitmap);

            dialog.dismiss();
        });

        dialog.show();
    }


}