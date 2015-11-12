package com.example.franciswong.easyconference;

/**
 * Created by gfh346 on 11/9/2015.
 */

import android.content.Context;

import java.io.File;
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
    Context m_Context;
    String m_Regex;

    public ConfData(Context context, String Regex)
    {
        m_Context = context;
        m_Regex =  Regex;
        confInfo = new ArrayList<String>();
        confInfo.add("Add New:::");
 //       WriteFile();
        ReadFile();

    }

    Boolean addInfo(String confName, String phoneNum, String pin, String notes)
    {

        if (confName.length()== 0)
        {
            System.out.println("ERROR!!! confName can't be empty");
            return false;
        }

        confInfo.add(confName+m_Regex+phoneNum + m_Regex + pin+ m_Regex+notes);
        WriteFile();
        return true;
    }


    // write text to file
    public void WriteFile()
    {
        // add-write text into file
        try {

            System.out.println("WriteFile: "+ confInfo.toString());

            ObjectOutputStream fileOut;
            fileOut = new ObjectOutputStream(new FileOutputStream(m_Context.getFilesDir()+"file.srl"));
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

            ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(m_Context.getFilesDir()+"file.srl"));
            confInfo = (ArrayList) fileIn.readObject();
            fileIn.close();
            System.out.println("ReadFile: " + confInfo.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getConfName()
    {

        ArrayList<String> confNameList = new ArrayList<String>();

        System.out.println(confInfo.toString());

        for (String tmpString :confInfo )
        {
            confNameList.add(tmpString.substring(0,tmpString.indexOf(":") ));
        }

        System.out.println(confNameList.toString());

        return confNameList;
    }

    public String retriveSelectedConf(int pos)
    {
        if (pos < confInfo.size())
            return confInfo.get(pos);
        else
            return "";
    }

    public Boolean deleteConf(int pos)
    {
        confInfo.remove(pos);
        WriteFile();
        return true;
    }

}
