package de.schrotthandel.mmoeller;

import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class CheckUpdate {

    private static String newVersion;
    static void getVersionNumber() {

        try {
            //Call the website and check if update is available.
            //The Url from Update-Server
            final String updateUrl = "https://www.schrotthandel-moeller.de/Android-Update/Version.txt";
            URL url = new URL(updateUrl);
            Scanner getVersion = new Scanner(url.openStream());

            while (getVersion.hasNextLine()) {

                newVersion = getVersion.next();
                checkVersion();
            }

            getVersion.close();
        } catch (IOException e) {

            Log.d("CheckUpdate-error", e.toString());

        }

    }


    //Check the currentVersion with the new Version
    static boolean checkVersion(){
        final String currentVersion = "1.1";

        return newVersion != null && !currentVersion.equals(newVersion);
    }
}
