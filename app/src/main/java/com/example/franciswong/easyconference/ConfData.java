package com.example.franciswong.easyconference;

/**
 * Created by gfh346 on 11/9/2015.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ConfData implements Serializable {

    //private String confInfo;
    private ArrayList<String> confInfo;

    void ConfData( )
    {
        confInfo = new ArrayList<String>();
        confInfo.add("Add New:::");
        WriteFile();

    }

    Boolean addInfo(String confName, String phoneNum, String pin, String notes)
    {
        confInfo.add(0, confName+":"+phoneNum + ":" + pin+ ":"+notes);
        WriteFile();
        return true;
    }


    // write text to file
    public void WriteFile()
    {
        // add-write text into file
        try {

            ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream("file.srl"));
            fileOut.writeObject(confInfo);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read text from file
    public void ReadFile() {

        //reading text from file
        try {
            ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream("file.srl"));
            confInfo = (ArrayList) fileIn.readObject();
            fileIn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
