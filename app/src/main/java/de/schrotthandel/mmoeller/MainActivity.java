package de.schrotthandel.mmoeller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;

    private final CustomerFormFragment customerFormFragment = new CustomerFormFragment();
    private final ContactsFragment contactsFragment = new ContactsFragment();
    private final CheckFragment checkFragment = new CheckFragment();

    private RelativeLayout relativeLayout;
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
        //For the Snackbar-Messagebox
        relativeLayout = findViewById(R.id.mainactivity_container);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_container, new CustomerFormFragment()).commit();
        }

    }

    @Override
    protected void onStart() {

        super.onStart();
        // Check if Update is available

        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());

                CheckUpdate.getVersionNumber();

                boolean isUpdateAvailable = CheckUpdate.checkVersion();

                Log.d("isUpdateAvailable", String.valueOf(isUpdateAvailable));
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (isUpdateAvailable) {
                            Snackbar.make(relativeLayout, "Es ist ein Update verfügbar!", Snackbar.LENGTH_LONG).setAction("Download", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://www.schrotthandel-moeller.de/Android-Update/Version.txt"));
                                    startActivity(intent);
                                }
                            }).show();
                        }
                    }
                });
            }
        }).start();


    }

}