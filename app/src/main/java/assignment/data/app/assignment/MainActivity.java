package assignment.data.app.assignment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import assignment.data.app.assignment.model.CardData;
import assignment.data.app.assignment.presenter.MainConnector;
import assignment.data.app.assignment.view.IView;

public class MainActivity extends AppCompatActivity implements IView.IMainView {

    private static final String TAG = "MainActivity";
    RecyclerView _r_listView;
    ItemsAdapter itemsAdapter;
    MainConnector presenter;
    int imageIdPosition;
    //CardData mCardData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _r_listView = findViewById(R.id._r_view);
        itemsAdapter = new ItemsAdapter(this);
        presenter = new MainConnector(this);
        _r_listView.setHasFixedSize(true);
        _r_listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _r_listView.setAdapter(itemsAdapter);
        itemsAdapter.clearAll();
        presenter.readData();
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void checkPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1002:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "PERMISSIONS REQUIRED!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void updateCard(CardData cardData) {
        itemsAdapter.addCard(cardData);
    }

    @Override
    public void captureImage(int pos) {
        imageIdPosition = pos;
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            try {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                File f = new File(Environment.getExternalStorageDirectory(), "image" + imageIdPosition + ".png");
                FileOutputStream fout = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fout);
                fout.flush();
                fout.close();
                Log.v(TAG, "Results updated");
                itemsAdapter.updateImageCard(f.getAbsolutePath(), imageIdPosition);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu._menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id._save:
                presenter.logCurrentData(itemsAdapter.getAllData());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showSavedLogs() {
        startActivity(new Intent(this, SavedData.class));
    }

    @Override
    public void showImage(String loc) {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.photo_viewer);
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView imageView = d.findViewById(R.id._image_view);
        imageView.setImageDrawable(Drawable.createFromPath(loc));
        d.show();
    }
}
