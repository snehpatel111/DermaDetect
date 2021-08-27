package com.example.android.dermadetect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.android.dermadetect.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Upload_Image extends AppCompatActivity {

    private static final int PICK_IMAGE = 100 ;
    private static final int REQUEST_IMAGE_CAPTURE =100 ;

    Uri imageuri;
    ImageView image;
     Bitmap photo;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__image);

        image = (ImageView)findViewById(R.id.image1);
         editText= (EditText) findViewById(R.id.result);
        Button upload = (Button)findViewById(R.id.upload);
        Button predict = (Button)findViewById(R.id.predict);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent.createChooser(intent,"Select Photo"),100);
            }

        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo = Bitmap.createScaledBitmap(photo,100,100,true);
                try {
                    Model model = Model.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 100, 100, 1}, DataType.FLOAT32);
                    TensorImage tensorImage =  new TensorImage(DataType.FLOAT32);
                    tensorImage.load(photo);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();
                    editText.setText(outputFeature0.getFloatArray()[0]+ "\n"+outputFeature0.getFloatArray()[1]);
                } catch ( IOException e) {
                    // TODO Handle the exception
                }
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK & null != data) {
                    imageuri = data.getData();
                    image.setImageURI(imageuri);
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageuri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}