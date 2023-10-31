package de.schrotthandel.mmoeller;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class APKDownloader implements Downloader {

    private final Context context;

    public APKDownloader(Context context) {
        this.context = context;
    }

    public void downlaodAPK(String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Update.jpg");

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
