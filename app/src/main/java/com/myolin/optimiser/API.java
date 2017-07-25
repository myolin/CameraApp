package com.myolin.optimiser;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

public class API extends AppCompatActivity {

    String fileName = "OptimiserData.txt";

    MyPrefs myPrefs;
    ReadAsset asset;
    StringBuffer sb = new StringBuffer();
    StringBuffer sb2 = new StringBuffer();

    int idcolumn = 3;
    int start = 4;
    int end = 285;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        myPrefs = new MyPrefs(getApplicationContext());
        asset = new ReadAsset(getApplicationContext());

        Button createTextFile = (Button) findViewById(R.id.button_camera);
        createTextFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=start; i<end; i++){
                    String id = asset.getStringDataCell(idcolumn,i);
                    String value = myPrefs.retrieveString(id);
                    //sb.append(id + "=" + value + "\n");
                    //sb.append("&"+id+"="+value);
                    sb2.append(id + "=" + value + "\n");
                }
                generateNoteOnSD(fileName);
            }
        });

        for(int i=start; i<end; i++){
            String id = asset.getStringDataCell(idcolumn,i);
            String value = myPrefs.retrieveString(id);
            if(!value.equals("")) {
                sb.append("&" + id + "=" + value);
            }
        }

        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url;
                    HttpURLConnection conn;

                    try{
                        url = new URL("http://dev4.omaudits.com/sites/weather/uploaddata2015.php");
                        String param = "act=buildfile&program=5851&login=1K5P-N6L9-P24A&foruser=1J6D-CCVW-C2A5&filename=testfile.opt"+sb.toString();
                        conn = (HttpURLConnection)url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setFixedLengthStreamingMode(param.getBytes().length);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        PrintWriter out = new PrintWriter(conn.getOutputStream());
                        out.print(param);
                        out.close();

                        String response = "";
                        Scanner inStream = new Scanner(conn.getInputStream());
                        while (inStream.hasNextLine()){
                            response += (inStream.nextLine());
                        }
                        Log.d("RESPONSESSSSSSSSSSS", response);
                    }catch(MalformedURLException e){
                        Toast.makeText(API.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }catch(IOException e){
                        Toast.makeText(API.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button send = (Button)findViewById(R.id.sendbutton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.clearAll();
                thread.start();
                //Toast.makeText(getApplicationContext(), "Successfully Sent", Toast.LENGTH_SHORT).show();
                //saveExcelFile("EXCELTESTFILE.xls");
                //reduceImageSize();
            }
        });
    }

    //not used
    public void generateNoteOnSD(String sFileName){
        try{
            File root = new File(Environment.getExternalStorageDirectory(), "OptimiserData");
            //File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File gpxFile = new File(root, sFileName);

            FileWriter writer = new FileWriter(gpxFile, false);
            writer.append(sb2.toString());
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void saveExcelFile(String fileName) {

        FileOutputStream os = null;
        File file = null;
        try{
            //New Workbook
            Workbook wb = new HSSFWorkbook();

            Cell c = null;

            //New Sheet
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("My Sample Excel");

            //Cell cell = sheet1.createRow(2).createCell(1);

            Row row1 = sheet1.createRow(0);
            Row row2 = sheet1.createRow(1);
            Row row3 = sheet1.createRow(2);

            c = row1.createCell(0);
            c.setCellValue("Control");

            c = row1.createCell(1);
            c.setCellValue("FileName");

            c = row1.createCell(2);
            c.setCellValue("Photo");

            //row.setHeightInPoints((2 * sheet1.getDefaultRowHeightInPoints()));

            row2.setHeight((short)10000);
            row3.setHeight((short)10000);

            sheet1.setColumnWidth(0, (10 * 256));
            sheet1.setColumnWidth(1, (10 * 256));
            sheet1.setColumnWidth(2, (15 * 2000));

            String image1path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/IMG_20170511_230645.jpg";
            String image2path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/IMG_20170505_143005.jpg";

            Bitmap bitmap = BitmapFactory.decodeFile(image1path1);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            Bitmap bitmap2 = BitmapFactory.decodeFile(image2path1);
            ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 40, bytes2);

            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "photo1.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "photo2.jpg");
            try{
                f2.createNewFile();
                FileOutputStream fo = new FileOutputStream(f2);
                fo.write(bytes2.toByteArray());
                fo.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            String image1path2 = f.getAbsolutePath();
            String image2path2 = f2.getAbsolutePath();

            InputStream inputStream = new FileInputStream(image1path2);
            byte[] bytess = IOUtils.toByteArray(inputStream);
            int pictureIdx = wb.addPicture(bytess, Workbook.PICTURE_TYPE_JPEG);
            inputStream.close();

            InputStream inputStream2 = new FileInputStream(image2path2);
            byte[] bytess2 = IOUtils.toByteArray(inputStream2);
            int pictureIdx2 = wb.addPicture(bytess2, Workbook.PICTURE_TYPE_JPEG);
            inputStream2.close();

            CreationHelper helper = wb.getCreationHelper();
            Drawing drawing = sheet1.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
            //anchor.setCol1(1);
            //anchor.setRow1(2);
            //anchor.setCol2(2);
            //anchor.setRow2(3);
            anchor.setCol1(2);
            anchor.setRow1(1);
            anchor.setCol2(3);
            anchor.setRow2(2);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            ClientAnchor anchor2 = helper.createClientAnchor();
            //pict.resize();
            anchor2.setCol1(2);
            anchor2.setRow1(2);
            anchor2.setCol2(3);
            anchor2.setRow2(3);
            Picture pict2 = drawing.createPicture(anchor2, pictureIdx2);

            c = row2.createCell(1);
            c.setCellValue(image1path2.substring(image1path2.lastIndexOf("/")+1));

            c = row3.createCell(1);
            c.setCellValue(image2path2.substring(image2path2.lastIndexOf("/")+1));

            // Create a path where we will place our List of objects on external storage
            //File file = new File(context.getExternalFilesDir(null), fileName);
            File root = new File(Environment.getExternalStorageDirectory(), "OptimiserData");
            if(!root.exists()){
                root.mkdirs();
            }
            file = new File(root, fileName);

            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
    }

    public void reduceImageSize(){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera/IMG_20170505_143005.jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}