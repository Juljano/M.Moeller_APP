package de.schrotthandel.mmoeller;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

public class CreatePDF {

    private final Context c;


    public CreatePDF(Context context){
        this.c = context;
    }



    public void getSignature(Bitmap signature){

    }
    public void createPDFFile(){

        Toast.makeText(c, "PDF wurde erstellt!", Toast.LENGTH_LONG).show();


    }
}
