package com.example.franciswong.easyconference;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    final String REGEX = ":";
    ConfData conf;
    private Spinner spinner1, spinner2;
    private Button buttonAdd, buttonCall, buttonDel;
    private EditText editTextPhone, editTextPin, editTextNotes;
    private CheckBox checkBoxDelay, checkBoxAddPound;
    private String newConfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        conf = new ConfData(this, REGEX);
        conf.ReadFile();
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

        editTextPhone = (EditText) findViewById(R.id.editPhoneNum);
        editTextPin = (EditText) findViewById(R.id.editConfPin);
        editTextNotes = (EditText) findViewById(R.id.editNotes);

        checkBoxDelay = (CheckBox) findViewById(R.id.checkBoxDelaySend);
        checkBoxAddPound = (CheckBox) findViewById(R.id.checkBoxAddPound);

        FloatingActionButton fabCall = (FloatingActionButton) findViewById(R.id.call);
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make sure there is a number to call
                if (editTextPhone.getText().toString() != "") {

                    // Format the phone number to call
                    // , -> 1 second delay
                    // ; -> request user manually enter
                    String uri = "tel:" + editTextPhone.getText().toString();
                    if (checkBoxDelay.isChecked()) {
                        uri = uri + ",,,";
                    } else {
                        uri = uri + ";";
                    }
                    uri = uri + editTextPin.getText().toString();
                    if (checkBoxAddPound.isChecked()) {
                        uri = uri + "#";
                    }

                    // Try to make the phone call
                    try {

                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                        startActivity(callIntent);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Your call has failed...",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } else {
                    Snackbar.make(view, "Please enter phone number first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if (userInput.getText().length() != 0) {
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

        FloatingActionButton fabClear = (FloatingActionButton) findViewById(R.id.clear);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContent(0);
                Snackbar.make(view, "Replace with Clear action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabDel = (FloatingActionButton) findViewById(R.id.delete);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinner1.getSelectedItemPosition()!=0) {
                    conf.deleteConf(spinner1.getSelectedItemPosition());
                    updateContent(0);
                }else {
                    Snackbar.make(view, "You can't delete this item", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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

    private void updateContent(int pos) {
        editTextNotes.setText("");
        editTextPin.setText("");
        editTextPhone.setText("");
        checkBoxDelay.setChecked(false);
        checkBoxAddPound.setChecked(false);

        String retriveConf = conf.retriveSelectedConf(pos);
        String[] tmpString = retriveConf.split(REGEX);

//        System.out.println("tmpString size: " + tmpString.length);

        switch (tmpString.length) {

            case 4:
                editTextNotes.setText(tmpString[3]);
                System.out.println("4");
            case 3:
                editTextPin.setText(tmpString[2]);
                System.out.println("3");
                if (tmpString[2].contains(",")) {
                    checkBoxDelay.setChecked(true);
                }
                if (tmpString[2].contains("#")) {
                    checkBoxAddPound.setChecked(true);
                }

            case 2:
                editTextPhone.setText(tmpString[1]);
                System.out.println("2");
                //           case 1:
                //               System.out.println("1");
        }

        //
        //
        //

        //   System.out.println("pos: "+ pos+ "tmpString:" + tmpString[0]+ tmpString[1]+ tmpString[2]);

    }

    void addNewConfInfo(String confName) {


        System.out.println("phone: " + editTextPhone.getText().toString());
        System.out.println("pin: " + editTextPin.getText().toString());
        System.out.println("note: " + editTextNotes.getText().toString());

        conf.addInfo(confName, editTextPhone.getText().toString(), editTextPin.getText().toString(), editTextNotes.getText().toString());
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
