package de.schrotthandel.mmoeller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class SelectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private MetallTypeAdapter metallTypeAdapter;

    private FloatingActionButton btnNext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selection, container, false);

        btnNext = view.findViewById(R.id.faNext);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        metallTypeAdapter = new MetallTypeAdapter(getMetallType());
        recyclerView.setAdapter(metallTypeAdapter);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SummaryFragment summaryFragment = new SummaryFragment();
                FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainactivity_container, summaryFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
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
        metallList.add(new Metall("Späne Aluminium kehrreste (Verschmutzt)"));
        metallList.add(new Metall("Messing, schwer, leicht, Spähne"));
        metallList.add(new Metall("Aluminium, neu, Profile"));
        metallList.add(new Metall("Aluminium Guß"));
        metallList.add(new Metall("Alu-Schredder"));
        metallList.add(new Metall("Zink"));
        metallList.add(new Metall("Blei"));
        metallList.add(new Metall("VA"));
        metallList.add(new Metall("Kupfer"));

        return metallList;
    }
}