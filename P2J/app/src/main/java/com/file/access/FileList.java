package com.file.access;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Prashanth on 6/26/2015.
 */
public class FileList {


    public ArrayList<String> getFiles(String path){
        ArrayList<String> array_list = new ArrayList<String>();
        File directory = new File(path);
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if(file.isDirectory()) {
                    array_list.add(file.getName());
                }
            }
            for(File file : files){

                if(file.isFile()){
                    if(file.getName().endsWith(".pdf")||file.getName().endsWith(".jpg")) {
                        array_list.add(file.getName());
                    }
                }
            }
            return array_list;
        }else{
            return null;
        }

    }

    public String[] toArray(ArrayList<String> list){
        String[] array = new String[list.size()];
        for(int i =0;i<list.size();i++){
            array[i]=list.get(i);
        }
        return array;
    }
}
