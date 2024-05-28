package de.schrotthandel.mmoeller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class CustomerFormFragment extends Fragment {

    private TextInputEditText etName, etAdress, etCity, etDate, etTaxNumber, etReceiverName, etIban;
    private RadioButton radioButtonPrivate, radioButtonBusiness, radioButtonCashPayment, radioButtonPerBank;
    private RadioGroup radioGroupType, radioGroupPayment;
    private FloatingActionButton btn;
    private Spinner customerSpinner;

    private boolean isBankTransfer;
    private LinearLayout linearLayoutBank;
    private CustomerData customerData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_form, container, false);

        btn = view.findViewById(R.id.btnNext);
        etName = view.findViewById(R.id.etName);
        etAdress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etDate = view.findViewById(R.id.etDate);
        etTaxNumber = view.findViewById(R.id.etTaxNumber);
        customerSpinner = view.findViewById(R.id.spinnerCustomerType);
        radioGroupType = view.findViewById(R.id.radioGroupType);
        radioGroupPayment = view.findViewById(R.id.radioGroupPayments);
        radioButtonBusiness = view.findViewById(R.id.radioButtonBusiness);
        radioButtonCashPayment = view.findViewById(R.id.radioButtonCashPayment);
        radioButtonPrivate = view.findViewById(R.id.radioButtonPrivate);
        radioButtonPerBank = view.findViewById(R.id.radioButtonPerBank);
        etReceiverName = view.findViewById(R.id.etReceiverName);
        etIban = view.findViewById(R.id.etIban);
        linearLayoutBank = view.findViewById(R.id.linearLayoutBank);
        etIban = view.findViewById(R.id.etIban);
        etReceiverName = view.findViewById(R.id.etReceiverName);
        linearLayoutBank.setVisibility(View.GONE); //Standard Value
        customerData = CustomerData.getInstance();
        isBankTransfer = false;



        setupRadioGroups();


        btn.setOnClickListener(view1 -> {


            //checked that all Textinputfields are filled before you can switch to next Fragment
            if (areAllFieldsFilled()) {

                if (radioButtonPerBank.isChecked() || radioButtonCashPayment.isChecked()) {

                    if (radioButtonPrivate.isChecked() || radioButtonBusiness.isChecked()) {

                        //set the Values from Customer
                        setCustomerData();

                        //Switches to next Fragment "fragment_selection"
                        SelectionFragment selectionFragment = new SelectionFragment();
                        FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();

                        fragmentTransaction.replace(R.id.mainactivity_container, selectionFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                }

            }

            if (!areAllFieldsFilled() || !radioButtonPerBank.isChecked() || !radioButtonCashPayment.isChecked() || !radioButtonPrivate.isChecked() || !radioButtonBusiness.isChecked()){

                Toast.makeText(getActivity(), "Bitte alle Felder ausfüllen und Kunden-/Bezahltyp wählen!", Toast.LENGTH_LONG).show();

            }


        });


        return view;
    }


    private void setCustomerData() {

        customerData.setName(etName.getText().toString());
        customerData.setAddress(etAdress.getText().toString());
        customerData.setCityAndPLZ(etCity.getText().toString());
        customerData.setDate(etDate.getText().toString());
        customerData.setTaxNumber(etTaxNumber.getText().toString());
        customerData.setReceiverName(etReceiverName.getText().toString());
        customerData.setiBan(etIban.getText().toString());


        Log.d("CustomerFragment", customerData.getName());
        

    }

    private void setupRadioGroups() {

            radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {

                if (radioButtonBusiness.isChecked()) {
                    customerData.setBusiness(true);
                    customerData.setPrivate(false);

                } else if (radioButtonPrivate.isChecked()) {

                    customerData.setPrivate(true);
                    customerData.setBusiness(false);

                }
            });



        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {

            if (radioButtonCashPayment.isChecked()) {
                //if linearLayoutBank is gone, then will be set etIban and etReceiver on null
                linearLayoutBank.setVisibility(View.GONE);
                etIban.setText("");
                etReceiverName.setText("");
                isBankTransfer = false;
                customerData.setCashPayment(true);
                customerData.setBankTransfer(false);

            } else if (radioButtonPerBank.isChecked()) {

                linearLayoutBank.setVisibility(View.VISIBLE);
                isBankTransfer = true;
                customerData.setBankTransfer(true);
                customerData.setCashPayment(false);

            }
        });


    }



    //checked that all Textinputfields are filled
    private boolean areAllFieldsFilled() {

        if (TextUtils.isEmpty(etName.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(etAdress.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(etCity.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(etDate.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(etTaxNumber.getText())) {
            return false;
        }

        if (isBankTransfer) {

            return !TextUtils.isEmpty(etReceiverName.getText()) && !TextUtils.isEmpty(etIban.getText());
        }

        return true;
    }
}