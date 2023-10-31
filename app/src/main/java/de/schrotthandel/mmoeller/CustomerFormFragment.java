package de.schrotthandel.mmoeller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

public class CustomerFormFragment extends Fragment {


    private TextInputEditText etName, etAdress, etCity, etDate, etTaxNumber, etReceiverName, etIban;
    private RadioButton radioButtonPrivate, radioButtonBusiness, radioButtonCashPayment, radioButtonPerBank;
    private RadioGroup radioGroupType, radioGroupPayment;

    private boolean isPrivate, isBusiness, isBankTransfer, isCashPayment = false;
    private Button btn;
    private Spinner customerSpinner;
    private LinearLayout linearLayoutBank;


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


        setupRadioGroups();


        btn.setOnClickListener(view1 -> {


            //set the Values from Customer
            setCustomerData();


            //Switches to next Fragment "fragment_selection"
            SelectionFragment selectionFragment = new SelectionFragment();
            FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.mainactivity_container, selectionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });


        return view;
    }

    private void setCustomerData() {

        CustomerData.getInstance().setName(etName.getText().toString());
        CustomerData.getInstance().setAddress(etAdress.getText().toString());
        CustomerData.getInstance().setCityAndPLZ(etCity.getText().toString());
        CustomerData.getInstance().setDate(etDate.getText().toString());
        CustomerData.getInstance().setTaxNumber(etTaxNumber.getText().toString());
        CustomerData.getInstance().isPrivate(isPrivate);
        CustomerData.getInstance().isBusiness(isBusiness);
        CustomerData.getInstance().isCashPayment(isCashPayment);
        CustomerData.getInstance().isBankTransfer(isBankTransfer);
        CustomerData.getInstance().setReceiverName(etReceiverName.getText().toString());
        CustomerData.getInstance().setiBan(etIban.getText().toString());

    }

    private void setupRadioGroups() {

        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {

            if (radioButtonBusiness.isChecked()) {

                isBusiness = true;

            } else if (radioButtonPrivate.isChecked()) {

                isPrivate = true;
            }
        });

        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {

            if (radioButtonCashPayment.isChecked()) {


                //if linearLayoutBank is gone, then will be set etIban and etReceiver on null
                linearLayoutBank.setVisibility(View.GONE);
                etIban.setText("");
                etReceiverName.setText("");

                isCashPayment = true;


            } else if (radioButtonPerBank.isChecked()) {

                linearLayoutBank.setVisibility(View.VISIBLE);

                isBankTransfer = true;


            }
        });


    }

}