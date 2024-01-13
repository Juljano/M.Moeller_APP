package de.schrotthandel.mmoeller;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class APKDownloader implements Downloader {

    private final Context context;

    public APKDownloader(Context context) {
        this.context = context;
    }


    @Override
    public void checkUpdate() throws IOException {

        //Call the website and check if update is available.
        //The Url from Update-Server
        final String updateUrl = "https://www.schrotthandel-moeller.de/Android-Update/Version.txt";
        URL url = new URL(updateUrl);
        Scanner newVersion = new Scanner(url.openStream());
        String getAppVersion = newVersion.toString();
        Log.d("checkUpdate", getAppVersion);

      /*  if (!getAppVersion.isEmpty()){

            downlaodAPK(updateUrl);
        }*/

    }

    public void downlaodAPK(String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Update.apk");

    }

    @Override
    public void onDownloadComplete() {

        Toast.makeText(context.getApplicationContext(), "Erfolgreich heruntergeladen, siehe im Download-Ordner!", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onDownloadFailed() {

        Toast.makeText(context.getApplicationContext(), "Es gab ein Fehler beim Herunterladen!", Toast.LENGTH_LONG).show();

    }

}
