package com.myolin.cameraapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class APIFragment extends Fragment {

    Button apiButton;
    MyPrefs myPrefs;
    ReadAsset asset;
    StringBuffer sb = new StringBuffer();;

    int idcolumn = 3;
    int start = 4;
    int end = 285;

    public APIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        asset = bundle.getParcelable("Asset");
        myPrefs = bundle.getParcelable("myPrefs");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_api, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiButton = (Button)getView().findViewById(R.id.apiButton);

        final String fileName = "OptimiserData.txt";

        apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraApp.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        /*apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fuel = myPrefs.retrieveString("AA");
                //boolean fuel = myPrefs.retrieveBoolean("BuildingAddressSame");
                //Toast.makeText(getContext(), fuel, Toast.LENGTH_SHORT).show();
                for(int i=start; i<end; i++){
                    String id = asset.getStringDataCell(idcolumn,i);
                    String value = myPrefs.retrieveString(id);
                    sb.append(id + "-" + value + "\n");
                }
                generateTextFileOnSD(fileName);
                //Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
            }
        });*/
    }


    public void generateTextFileOnSD(String sFileName){
        try{
            File root = new File(Environment.getExternalStorageDirectory(), "OptimiserData");
            if(!root.exists()){
                root.mkdirs();
            }
            File gpxFile = new File(root, sFileName);

            FileWriter writer = new FileWriter(gpxFile, true);
            //writer.append(sb.toString());
            writer.append("HELLO MY FRIEND");
            writer.flush();
            writer.close();
            Toast.makeText(getContext(), "Data has been written", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void generateTextFileOnSD2(String sFileName){
        try {
            File myFile = new File("/sdcard/mysdfile.txt");
                   myFile.createNewFile();
                   FileOutputStream fOut = new FileOutputStream(myFile);
                   OutputStreamWriter myOutWriter =
                           new OutputStreamWriter(fOut);
                   myOutWriter.append("HELLO MY FRIEND 2");
                   myOutWriter.close();
                   fOut.close();
                   Toast.makeText(getContext(),
                           "Done writing SD 'mysdfile.txt'",
                           Toast.LENGTH_SHORT).show();
               } catch (Exception e) {
                   Toast.makeText(getContext(), e.getMessage(),
                           Toast.LENGTH_SHORT).show();
               }
    }

}
