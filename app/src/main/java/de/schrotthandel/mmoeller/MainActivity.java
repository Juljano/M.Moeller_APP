package de.schrotthandel.mmoeller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;

    private final CustomerFormFragment customerFormFragment = new CustomerFormFragment();
    private final ContactsFragment contactsFragment = new ContactsFragment();
    private final CheckFragment checkFragment = new CheckFragment();
    private static final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.menuCustomerForm) {
                setFragment(customerFormFragment);
                return true;

            } else if (id == R.id.menuCheck) {
                setFragment(checkFragment); // Ändere diese Zeile
                return true;

            } else if (id == R.id.menuContacts) {
                setFragment(contactsFragment);
                return true;
            }
            return false;
        }

        private void setFragment(Fragment fragment) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainactivity_container, fragment);
            fragmentTransaction.commit();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainactivity_container, new CustomerFormFragment())
                    .commit();
        }

    }


    @Override
    protected void onStart() {

        super.onStart();

        //APKDownloader apkDownloader = new APKDownloader(this);
        //String fileUrl = "https://www.techsmith.de/blog/wp-content/uploads/2020/11/TechSmith-Blog-JPGvsPNG-DE.png";
        //apkDownloader.downlaodAPK(fileUrl);

    }

}