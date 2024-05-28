package de.schrotthandel.mmoeller;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class CreatePDF {

    private Document document;
    private final Font fontSize = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD);
    private final Font fontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    private final Font fontAmount = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD);
    private ArrayList<Double> sumList;
    private final CustomerData customerData = CustomerData.getInstance();

    public void getCustomerData(Bitmap bitmap, Context context) throws DocumentException {

        if (customerData != null) {
            createPDFFile(customerData, context);
            createTitle(customerData);
            createHeader(customerData);
            createTable();
            getBankInformation(customerData);
            setSignature(bitmap);
            createFooter();
            backToMainActivity(context);
        }


    }

    public void createPDFFile(CustomerData customerData, Context context) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, customerData.getName() + "-" + createAccountNumber() + " -Abrechnung.pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/PDF-Moeller/Abrechnung/");
            Uri collection = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            }
            Uri fileUri = contentResolver.insert(collection, contentValues);
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(fileUri));
            Objects.requireNonNull(outputStream);

            outputStream = context.getContentResolver().openOutputStream(fileUri);


            document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();


        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void createTitle(CustomerData customerData) throws DocumentException {

        Chunk title = new Chunk("Schrotthandel Möller", fontSize);
        Paragraph paragraph = new Paragraph(title);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(paragraph);

        if (customerData.getCashPayment()) {

            Log.d("CreateTitle", "Bargutschrift");
            Chunk underline = new Chunk("Bargutschrift", fontSize);
            underline.setUnderline(0.1f, -2f);
            document.add(underline);

        }

        if (customerData.getBankTransfer()) {
            Log.d("CreateTitle", "Gutschrift");
            Chunk underline = new Chunk("Gutschrift", fontSize);
            underline.setUnderline(0.1f, -2f);
            document.add(underline);

        }

    }


    public void createHeader(CustomerData customerData) throws DocumentException {


        if (customerData.getPrivate()){

            Paragraph titleCompany = new Paragraph("Privat", fontHeader);
            Paragraph  titleCitizen = new Paragraph("Privat  ", fontHeader);
            Paragraph infoAboutCitizen = new Paragraph("Name " + customerData.getName() + "\n" + "Anschrift: " + customerData.getAddress() + "\n" + "Postleitzahl:" + customerData.getCityAndPLZ() + "\n" + "Pers.-Ausweiß Nr: " + customerData.getTaxNumber() + "\n" + "Rechnungsnummer: " + createAccountNumber(), fontHeader);
            titleCompany.setAlignment(Element.ALIGN_TOP);

            document.add(titleCitizen);
            document.add(infoAboutCitizen);


        }else if (customerData.getBusiness()){

             Paragraph titleCompany = new Paragraph("Gewerblich", fontHeader);
             Paragraph infoAboutCompany = new Paragraph("Name: " + customerData.getName() + "\n" + "Anschrift: " + customerData.getAddress() + "\n" + "Postleitzahl:" + customerData.getCityAndPLZ() + "\n" + "Steuernummer: " + customerData.getTaxNumber() + "\n" + "Rechnungsnummer: " + createAccountNumber(), fontHeader);
             titleCompany.setAlignment(Element.ALIGN_TOP);

            document.add(titleCompany);
            document.add(infoAboutCompany);

        }

        Paragraph line = new Paragraph(Chunk.NEWLINE);

        Paragraph paragraphline = new Paragraph(Chunk.NEWLINE);

        PdfPTable tableheader = new PdfPTable(2);
        tableheader.setWidthPercentage(100);
        tableheader.addCell(getCell("Mario Möller\n" + "Schaumburger Weg 1b \n" + "32423 Minden \n" + "Tel:0571/3983607 \n" + "Mobil:017678103103\n" + "UST-NR.335/5151/1209", PdfPCell.ALIGN_LEFT));

        //Date
        Chunk date = new Chunk(customerData.getDate(), fontHeader);
        Paragraph paragraph = new Paragraph(date);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);


        PdfPTable pdfPTable = new PdfPTable(2);
        tableheader.setWidthPercentage(100);
        tableheader.setSpacingAfter(10f);


            document.add(pdfPTable);
            document.add(line);
            document.add(paragraphline);
            document.add(tableheader);
            document.add(paragraph);


    }


    public void createTable() throws DocumentException {

        MetallTypeData metallTypeData = MetallTypeData.getInstance();

        Paragraph newline = new Paragraph(Chunk.NEWLINE);


        PdfPTable table = new PdfPTable(4); // 4 columns.
        table.setSpacingBefore(5f);
        table.setSpacingAfter(10f);
        table.setWidthPercentage(100);


        float[] columnWidths = new float[]{10f, 30f, 10f, 10f};
        table.setWidths(columnWidths);


        //Header from Table
        table.addCell(new PdfPCell(new Paragraph("Gewicht in Kg")));
        table.addCell(new PdfPCell(new Paragraph("Bezeichnung")));
        table.addCell(new PdfPCell(new Paragraph("Preis/kg")));
        table.addCell(new PdfPCell(new Paragraph("Betrag in Euro")));

        sumList = new ArrayList<>();


        for (int i = 0; i < metallTypeData.getWeightList().size(); i++) {
            String weight = metallTypeData.getWeightList().get(i).toString();
            String description = metallTypeData.getMetalTypeList().get(i);
            String pricePerKg = metallTypeData.getPriceList().get(i).toString();
            String amount = metallTypeData.getSumList().get(i).toString();

            // Fügen Sie die Daten in die Zellen der Tabelle ein
            table.addCell(new PdfPCell(new Paragraph(weight)));
            table.addCell(new PdfPCell(new Paragraph(description)));
            table.addCell(new PdfPCell(new Paragraph(pricePerKg)));
            table.addCell(new PdfPCell(new Paragraph(amount)));

            sumList.add(Double.parseDouble(amount));

        }


        Log.d("Amount", sumList.toString());

        document.add(newline);

        String totalAmount = "Gesamtbetrag in Euro:    " + getAmount();
        PdfPCell totalCell = new PdfPCell(new Phrase(totalAmount, fontAmount));
        totalCell.setColspan(4);
        table.addCell(totalCell);

        document.add(newline);

        document.add(table);

    }



    private String getAmount() {
        double sum = 0;
        Locale locale = Locale.GERMANY;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        if (!sumList.isEmpty()) {
            for (Double s : sumList) {
                sum += s;
            }

        }
        return numberFormat.format(sum) ;
    }




    private void getBankInformation(CustomerData customerData) throws DocumentException {
        if (!customerData.getiBan().isEmpty() && !customerData.getReceiverName().isEmpty()){

            Chunk messageBankInformation = new Chunk("Das Gutschrift wird auf folgendes Konto überwiesen:");

            PdfPTable table = new PdfPTable(2); // 2 columns.
            table.setSpacingBefore(5f);
            table.setSpacingAfter(10f);
            table.setWidthPercentage(100);

            float[] columnWidths = new float[]{10f, 10f};
            table.setWidths(columnWidths);

            table.addCell(new PdfPCell(new Paragraph("Empfänger")));
            table.addCell(new PdfPCell(new Paragraph("IBAN")));

            table.addCell(new PdfPCell(new Paragraph(customerData.getReceiverName())));
            table.addCell(new PdfPCell(new Paragraph(customerData.getiBan())));

            document.add(messageBankInformation);
            document.add(table);

        }



    }
    public void setSignature(Bitmap bitmap) throws DocumentException {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image signatureImage;
            try {
                signatureImage = Image.getInstance(stream.toByteArray());
                signatureImage.setAbsolutePosition(500f, 100f);
                signatureImage.scaleToFit(200, 50);
                document.add(signatureImage);
            } catch (IOException e) {
                Log.e("SignatureError", "Failure by loading the signature");
            }
        } else {
            Log.e("SignatureError", "The signature is not available");
        }
    }



    public void createFooter() throws DocumentException {

            PdfPTable tableInfo = new PdfPTable(1);
            tableInfo.setWidthPercentage(100);
            tableInfo.setSpacingAfter(10f);
            tableInfo.addCell(getCell("Der Rechnungsbetrag enthält keine Umsatzsteuer!", PdfPCell.ALIGN_LEFT));
            tableInfo.addCell(getCell("Die Steuer wird gemäß § 12b Abs2. Nr7. UStG von Leitungsempfänger geschuldet!", PdfPCell.ALIGN_LEFT));
            tableInfo.addCell(getCell("Ich versichere, dass die von  mir abgelieferte Ware mein Eigentum ist.\nWir weisen sie drauf hin,das jede nachaltige  Tätigkeit zur Erzielung von Einnahmen zur Umsatzsteuer- und Einkommenssteuerpflicht führen kann.\nKlären sie  dies mit  ihren Finanzamt oder Steuerberater.", PdfPCell.ALIGN_LEFT));

            document.add(tableInfo);

        document.close();


    }



    //when the pdf is complete, then will switch to the MainActivity
    private void backToMainActivity(Context context){

        MetallTypeData metallTypeData = MetallTypeData.getInstance();
        metallTypeData.clearData();

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();

    }

    //Create account Number of the Customer and Tax office
    private String createAccountNumber() {

        Random random = new Random();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Log.d("accountNumber ", dateTimeFormatter.format(localDateTime) + "-" + random.nextInt());

        return dateTimeFormatter.format(localDateTime) + "-" + random.nextInt();
    }

    private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }


}
