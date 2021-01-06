package com.mobileapps.uas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.model.Car;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddCarActivity extends AppCompatActivity {
    String url = "";
    Button btnSave, btnDelete;
    boolean isImgChanged = false ;
    ProgressBar loader;
    ImageView img;
    EditText etCarName, etPrice;
    Car car;
    private static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        
        initWidget();
        setUp();
        initClickListener();

    }

    private void setUp() {
        if(Util.isFirst){

            btnDelete.setVisibility(View.GONE);
            Glide.with(img)
                    .load(R.drawable.ic_upload)
                    .into(img);

        } else {
            car = getIntent().getParcelableExtra(PlaceOrderActivity.EXTRA_CAR);
            etCarName.setText(car.getName());
            etPrice.setText(""+car.getCost());
            btnSave.setText("Simpan");

            Glide.with(img)
                    .load(car.getImgUrl())
                    .into(img);

        }
    }

    private void initClickListener() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               showDeleteDialog();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);

                if(isImgChanged){
                    uploadImage();
                } else {
                    save();
                }
            }
        });
    }

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title

        alertDialogBuilder.setTitle("Hapus dari daftar mobil");

        // set dialog message
        alertDialogBuilder.setMessage("Apakah anda yakin ingin menghapus ?");
        alertDialogBuilder
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        delete();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void save() {

        String carId = "";
        if(Util.isFirst){
            carId = "car"+ Util.generateId();
        } else {
            carId = car.getId();
            if(!isImgChanged){
                url = car.getImgUrl();
            }

        }
        String name = etCarName.getText().toString();

        int price = Integer.parseInt(etPrice.getText().toString());


        Car car = new Car(carId,name, 0,price, url,true);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CARS");
        mDatabase.child(carId).setValue(car)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loader.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),
                                    "Berhasil", Toast.LENGTH_SHORT).show();
                            if(Util.isFirst){
                                startActivity(new Intent(AddCarActivity.this, CarsListActivity.class));
                                finish();
                            } else {
                                finish();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        startActivity(new Intent(AddCarActivity.this, CarsListActivity.class));
        finish();
    }

    private void delete() {
        FirebaseDatabase.getInstance().getReference("CARS").child(car.getId()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Berhasil", Toast.LENGTH_SHORT).show();
                            finish();


                        }
                    }
                });
        finish();
    }

    private void initWidget() {
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        etCarName = findViewById(R.id.etCarName);
        etPrice = findViewById(R.id.etPrice);
        img = findViewById(R.id.img);
        loader = findViewById(R.id.progressBar);
    }

    private void uploadImage(){
        // Create a storage reference from our app


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference carImgRef = storageRef.child("images/img"+Util.generateId());


        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = carImgRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("statusupload", "gagal"+exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("statusupload", "sukses"+carImgRef.getDownloadUrl().toString());

                carImgRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                //Do what you want with the url
                                url = downloadUrl.toString();
                                Log.d("statusupload", "suksesbos"+url);
                                save();
                            }

                        });

            }
        });


    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Glide.with(img)
                        .load(selectedImage)
                        .into(img);
                isImgChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}