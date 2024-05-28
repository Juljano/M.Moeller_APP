package de.schrotthandel.mmoeller;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class SelectionFragment extends Fragment{

    public static FloatingActionButton buttonSelection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        buttonSelection = view.findViewById(R.id.buttonSelection);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        MetallTypeAdapter metallTypeAdapter = new MetallTypeAdapter(getMetallType(), getContext());
        recyclerView.setAdapter(metallTypeAdapter);






        buttonSelection.setOnClickListener(view1 -> {

            MetallTypeData metallTypeData = MetallTypeData.getInstance();


            int minSize = Math.min(metallTypeAdapter.getMetallSort().size(), metallTypeAdapter.getTempKgPerEuroValues().size());

            for (int i = 0; i < minSize; i++) {
                Log.d("SummaryFragment-Size", String.valueOf(minSize));
                Log.d("SummaryFragment-Sort", metallTypeAdapter.getMetallSort().get(i));
                Log.d("SummaryFragment-kg", String.valueOf(metallTypeAdapter.getTempKgValues().get(i)));
                Log.d("SummaryFragment-Price/kg", String.valueOf(metallTypeAdapter.getTempKgPerEuroValues().get(i)));

                metallTypeData.getMetalTypeList().add(metallTypeAdapter.getMetallSort().get(i));
                metallTypeData.getWeightList().add(metallTypeAdapter.getTempKgValues().get(i));
                metallTypeData.getPriceList().add(metallTypeAdapter.getTempKgPerEuroValues().get(i));
                metallTypeData.getSumList().add(metallTypeAdapter.getTempKgValues().get(i) * metallTypeAdapter.getTempKgPerEuroValues().get(i));
            }


            SummaryFragment summaryFragment = new SummaryFragment();
            FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainactivity_container, summaryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    private List<Metall> getMetallType() {

        //All sorts of  Metall
        List<Metall> metallList = new ArrayList<>();
        metallList.add(new Metall("Schrott, Stanzabfälle"));
        metallList.add(new Metall("E-Motore"));
        metallList.add(new Metall("Sperrschrott"));
        metallList.add(new Metall("Schredder-Vormaterial"));
        metallList.add(new Metall("Guss"));
        metallList.add(new Metall("Aluminium, profile"));  //Blank, Farbe, ISO  //Sort after metallType
        metallList.add(new Metall("Messing, Spähne"));
        metallList.add(new Metall("Aluminium, neu"));
        metallList.add(new Metall("Zink"));
        metallList.add(new Metall("Blei"));
        metallList.add(new Metall("VA"));
        metallList.add(new Metall("Kabel"));
        metallList.add(new Metall("Kupfer"));

        return metallList;
    }

}