package de.schrotthandel.mmoeller;

import java.io.IOException;

public interface Downloader {

    void onDownloadComplete();

    void onDownloadFailed();


    void checkUpdate() throws IOException;
}

