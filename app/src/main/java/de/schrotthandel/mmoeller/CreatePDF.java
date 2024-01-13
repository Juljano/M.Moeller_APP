package de.schrotthandel.mmoeller;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Objects;

public class CreatePDF {

    private Document document;
    private final Font fontSize = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD);
    private final Font fontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);



    public void getCustomerData(Context context) throws DocumentException {
        CustomerData customerData = CustomerData.getInstance();

        if (customerData != null) {
            createPDFFile(customerData, context);
            createTitle(customerData);
            createHeader(customerData);
            createTable();

        }
    }


    public Bitmap getSignature(Bitmap signature) {

        //Toast.makeText(context,signature.getHeight(), Toast.LENGTH_LONG).show();

        setSignature(signature);

        return signature;

    }


    public void createPDFFile(CustomerData customerData, Context context) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, customerData.getName() + " -Abrechnung.pdf");
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

        public void createTitle (CustomerData customerData) throws DocumentException {

            Log.d("CreateTitle", customerData.getCashPayment().toString());

            Chunk title = new Chunk("Schrotthandel Möller", fontSize);
            Paragraph paragraph = new Paragraph(title);
            paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(title);

            if (customerData.getCashPayment() != null && customerData.getCashPayment()) {

                Log.d("CreateTitle", "Bat");

                Chunk underline = new Chunk("Bargutschrift", fontSize);
                underline.setUnderline(0.1f, -2f);
                document.add(underline);

            }

            if (customerData.getBankTransfer() != null && customerData.getBankTransfer()) {
                Log.d("CreateTitle", "Gutschrift");
                Chunk underline = new Chunk("Gutschrift", fontSize);
                underline.setUnderline(0.1f, -2f);
                document.add(underline);

            }

        }


        public void createHeader (CustomerData customerData) throws DocumentException {

            Paragraph titleCompany = new Paragraph("Firma", fontHeader);
            Paragraph info = new Paragraph("Name: " + customerData.getName() + "\n" +
                    "Anschrift: " + customerData.getAddress() + "\n" +
                    "Postleitzahl:" + customerData.getCityAndPLZ() + "\n" +
                    "Steuernummer: " + customerData.getTaxNumber(), fontHeader);


            Paragraph line = new Paragraph(Chunk.NEWLINE);

            titleCompany.setAlignment(Element.ALIGN_TOP);

            Paragraph paragraphline = new Paragraph(Chunk.NEWLINE);

            PdfPTable tableheader = new PdfPTable(2);
            tableheader.setWidthPercentage(100);
            tableheader.addCell(getCell("Der Rechnungsbetrag enthält keine\nUmsatzsteuer! Die Steuer wird\ngemäß § 12b Abs2. Nr7. UStG vom\nLeitungsempfänger geschuldet!", PdfPCell.ALIGN_LEFT));

            tableheader.addCell(getCell("Mario Möller\n" + "Schaumburger Weg 1b \n" +
                    "32423 Minden \n" +
                    "Tel:0571/3983607 \n" +
                    "Mobil:017678103103\n" +
                    "UST-NR.335/5151/1209", PdfPCell.ALIGN_RIGHT));


            Paragraph titleCitizen = new Paragraph("Privat  ", fontHeader);
            Paragraph infoAboutCitizen = new Paragraph("Name " + customerData.getName() + "\n" +
                    "Anschrift: " + customerData.getAddress() + "\n" +
                    "Postleitzahl:" + customerData.getCityAndPLZ() + "\n" +
                    "Pers.-Ausweiß Nr: " + customerData.getTaxNumber(), fontHeader);


            PdfPTable pdfPTable = new PdfPTable(2);
            tableheader.setWidthPercentage(100);
            tableheader.setSpacingAfter(10f);

            tableheader.addCell(getCell(" ", PdfPCell.ALIGN_LEFT));
            tableheader.addCell(getCell("\n" + customerData.getDate(), PdfPCell.ALIGN_RIGHT));


            if (!info.isEmpty()) {
                document.add(titleCompany);
                document.add(info);
            } else {
                document.add(pdfPTable);
                document.add(titleCitizen);
                document.add(infoAboutCitizen);
            }

            document.add(line);
            document.add(paragraphline);
            document.add(tableheader);


        }


        public void createTable () throws DocumentException {

            MetallTypeData metallTypeData = MetallTypeData.getInstance();

            Paragraph newline = new Paragraph(Chunk.NEWLINE);


            PdfPTable table = new PdfPTable(4); // 4 columns.
            table.setSpacingBefore(5f);
            table.setSpacingAfter(10f);
            table.setWidthPercentage(100);

            float[] columnWidths = new float[]{10f, 30f, 10f, 10f};
            table.setWidths(columnWidths);

            String[] categories = {
                    "Schrott", "E-Motore", "Sperrschrott", "Schredder-Vormaterial",
                    "Guss", "Späne Aluminium Kehrreste", "Kabel", "Kupfer,leicht,Draht,schwer",
                    "Messing", "Aluminium Guß", "Aluminium,neu,Profile", "Aluminium-Schredder",
                    "Zink", "Blei", "VA", "Kupfer"
            };

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
            }

            document.add(newline);
            document.add(table);

            String totalAmount = "Gesamtbetrag in Euro: " + "€";
            PdfPCell totalCell = new PdfPCell(new Phrase(totalAmount, fontSize));
            totalCell.setColspan(4);
            table.addCell(totalCell);

            document.add(newline);
            document.add(table);

            document.close();


        }


        public void setSignature (Bitmap bitmap){

        }


        public void createFooter () {

        }


        public void setDataToPdf () {

           // Toast.makeText(, "PDF wurde erstellt!", Toast.LENGTH_LONG).show();


        }

        private PdfPCell getCell (String text,int alignment){
            PdfPCell cell = new PdfPCell(new Phrase(text));
            cell.setPadding(0);
            cell.setHorizontalAlignment(alignment);
            cell.setBorder(PdfPCell.NO_BORDER);
            return cell;
        }


    }
