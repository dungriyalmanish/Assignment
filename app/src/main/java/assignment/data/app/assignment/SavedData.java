package assignment.data.app.assignment;

/*
 * I Have created this class, just to show the saved content.
 * This do no fall under the MVP pattern -->  It is just for details*/

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SavedData extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data);
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //reading files in main thread....
        //[START]
        arrayAdapter.clear();
        File f = getFilesDir();
        FileInputStream fin;
        byte[] b;
        for (File f1 : f.listFiles()) {
            try {
                fin = openFileInput(f1.getName());
                b = new byte[fin.available()];
                fin.read(b);
                arrayAdapter.add(new String(b));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //[END]
    }
}
