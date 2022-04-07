package com.example.thecollectivediet;

import android.os.Environment;

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String Pictures = ROOT_DIR + "/Pictures";
    public String Camera = ROOT_DIR + "/DCIM/camera";
}
