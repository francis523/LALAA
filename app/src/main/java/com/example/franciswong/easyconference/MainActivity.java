package com.example.franciswong.easyconference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1, spinner2;
    private Button buttonAdd, buttonCall, buttonDel;
    private EditText editTextPhone, editTextPine, editTextNotes;
    final Context context = this;
    private String newConfName;

    ConfData conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conf = new ConfData(this);
        conf.ReadFile();
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

        editTextPhone = (EditText) findViewById(R.id.editPhoneNum);
        editTextPine = (EditText) findViewById(R.id.editConfPin);
        editTextNotes = (EditText) findViewById(R.id.editNotes);

        FloatingActionButton fabCall = (FloatingActionButton) findViewById(R.id.call);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with Call action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if (userInput.getText().length() != 0)
                                        {
                                            addNewConfInfo(userInput.getText().toString());
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        FloatingActionButton fabDel = (FloatingActionButton) findViewById(R.id.delete);
        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with Delete action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        spinner2 = (Spinner) findViewById(R.id.spinnerDescription);
        List<String> list = new ArrayList<String>();
        list = conf.getConfName();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinnerDescription);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                System.out.println("addListenerOnSpinnerItemSelection onItemSelected get called");

                updateContent(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
    });

    }

    private void updateContent(int pos)
    {

        String retriveConf = conf.retriveSelectedConf(pos);

        System.out.println("pos: "+ pos+ "info:" + retriveConf);

    }

    void addNewConfInfo(String confName)
    {


        System.out.println("phone: "+ editTextPhone.getText().toString());
        System.out.println("pin: "+ editTextPine.getText().toString());
        System.out.println("note: "+ editTextNotes.getText().toString());

        conf.addInfo(confName, editTextPhone.getText().toString(), editTextPine.getText().toString(), editTextNotes.getText().toString());
        conf.ReadFile();
        addItemsOnSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
