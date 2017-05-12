package com.example.zisis.sdy61_ge5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zisis on 274//17.
 */
public class MenuScreen  extends AppCompatActivity  {

    //Field exist in MenuScreen activity
    Button btnNew,btnSave,btnOpen;
    EditText editText;
    ScrollView mScrollView;
    TextView choice1,choise2,choise3;
    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        choice1 = (TextView)findViewById(R.id.textView);
        choise2 = (TextView)findViewById(R.id.textView2);
        choise3 = (TextView)findViewById(R.id.textView3);
        btnNew = (Button)findViewById(R.id.btnNew);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnOpen = (Button)findViewById(R.id.btnOpen);
        editText = (EditText)findViewById(R.id.editText);
        //mScrollView = (ScrollView) findViewById(R.id.scroller);
       // editText.setMovementMethod(new ScrollingMovementMethod());//scrolling

    }  //end of onCreate method


 /* Three methods that called in button listeners
  * openFile opens the file that the user select in
  * storage picker,saveFile saves the text that user
  * write in editText place and newFile cretaes a
  * new file in storage. */

    public void openFile(View v)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public void saveFile(View v)
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }

    public void newFile(View v)
    {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt");

        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    //Function that save the content of the editText in a File that selected in the storage picker
    public void writeFileContent(Uri uri)
    {
        try{
            ParcelFileDescriptor pfd =
                    this.getContentResolver().
                            openFileDescriptor(uri, "w");

           FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());

            String textContent =
                    editText.getText().toString();

            fileOutputStream.write(textContent.getBytes());

            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Function that reads the content from a file that user select in the
     * storage picker and displays it in the editText field. */

    public String readFileContent(Uri uri) throws IOException {

        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");

        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String currentline;
        while ((currentline = reader.readLine()) != null) {
            stringBuilder.append(currentline + "\n");
        }
        inputStream.close();
        parcelFileDescriptor.close();
        return stringBuilder.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri currenturi = null;
        if (resultCode == Activity.RESULT_OK){

            if (requestCode == CREATE_REQUEST_CODE){
                if (data !=null){
                    editText.setText("");

                }
            }
            else if (requestCode == SAVE_REQUEST_CODE){
                if (data != null){
                    currenturi = data.getData();
                    writeFileContent(currenturi);
                    //editText.setText("");
                    Toast.makeText(this, "File saved successfully!",
                            Toast.LENGTH_SHORT).show();

                }

            }
            else if (requestCode == OPEN_REQUEST_CODE){

                if (data != null){
                    currenturi = data.getData();
                    try {
                        String content = readFileContent(currenturi);
                        editText = (EditText) findViewById(R.id.editText);
                        editText.setText(content);

                    } catch (IOException e){
                        Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    } //end of onActivityResult
}
