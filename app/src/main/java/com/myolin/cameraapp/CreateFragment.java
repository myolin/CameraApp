package com.myolin.cameraapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CreateFragment extends Fragment implements Parcelable {

    View parent_View;

    //CustomLayout customLayout;
    ScrollLayout scrollLayout;

    private static int TAKE_PICTURE = 1;
    ImageView imageView;

    ReadAsset asset;
    MyPrefs myPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int rows = bundle.getInt("Rows");
        int start = bundle.getInt("Start");
        int end = bundle.getInt("End");
        asset = bundle.getParcelable("Asset");
        myPrefs = bundle.getParcelable("myPrefs");
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        scrollLayout = new ScrollLayout(getContext(), width, rows, start, end, asset, myPrefs);
        parent_View = scrollLayout;
        imageView = scrollLayout.getImageView();
        //init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return parent_View;
    }

    /*public void init(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, TAKE_PICTURE);
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bitmap);
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<ReadAsset> CREATOR = new Creator<ReadAsset>() {
        @Override
        public ReadAsset createFromParcel(Parcel in) {
            return new ReadAsset(in);
        }

        @Override
        public ReadAsset[] newArray(int size) {
            return new ReadAsset[size];
        }
    };
}
