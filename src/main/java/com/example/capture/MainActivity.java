package com.example.capture;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    int Sex;
    Interpreter interpreter;
    Interpreter seginterpreter;
    String outputStr="";
    boolean isFinish = false;
    TextView head ;
    TextView shoulder ;
    TextView arm ;
    TextView torso;
    TextView chest ;
    TextView pelvis ;
    TextView leg ;
    TextView thigh ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplicationContext(),openActivity.class);
        startActivityForResult(intent,0);
        TextView head = findViewById(R.id.head_text);
        TextView shoulder = findViewById(R.id.shoulder_text);
        TextView arm = findViewById(R.id.arm_text);
        TextView torso = findViewById(R.id.torso_text);
        TextView chest = findViewById(R.id.chect_text);
        TextView pelvis = findViewById(R.id.pelvis_text);
        TextView leg = findViewById(R.id.leg_text);
        TextView thigh = findViewById(R.id.thigh_text);
        Button ButtonM = findViewById(R.id.mybody);

        head.setVisibility(View.GONE);
        shoulder.setVisibility(View.GONE);
        arm.setVisibility(View.GONE);
        torso.setVisibility(View.GONE);
        chest.setVisibility(View.GONE);
        pelvis.setVisibility(View.GONE);
        leg.setVisibility(View.GONE);
        thigh.setVisibility(View.GONE);
        ButtonM.setVisibility(View.GONE);
        Button gallery = findViewById(R.id.Gallery);
        Button camera = findViewById(R.id.camera);
        ImageView result_image = findViewById(R.id.result_image);
        result_image.setVisibility(View.GONE);
        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                startActivityForResult(intent,2);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override //이미지 불러오기기(갤러리 접근)
            public void onClick(View v) {
                Intent img = new Intent();
                img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(img, 1); //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {

            return;
        }
        if(requestCode == 0) {
            Sex = data.getIntExtra("Sex",3);
        }
        if(requestCode == 1) {

            @SuppressLint("WrongViewCast") TextView tf_output = findViewById(R.id.ConstraintLayout);
            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap tmpimg = BitmapFactory.decodeStream(in);

                tmpimg = getSegment(tmpimg);

                tmpimg = Bitmap.createScaledBitmap(tmpimg,200,200,true);


                if(Sex == 0) {
                    interpreter = getTfliteInterpreter("f_model.tflite");
                }
                if(Sex == 1) {
                    interpreter = getTfliteInterpreter("m_model.tflite");
                }


                float[][][][] input = new float[1][200][200][1];
                float[][] output = new float[1][16];

                for (int i = 0; i < tmpimg.getWidth(); i++)
                {
                    for (int j = 0; j < tmpimg.getHeight(); j++) {
                        int rgb = tmpimg.getPixel(i,j);
                        int R = (rgb >> 16) & 0xff;
                        int G = (rgb >>  8) & 0xff;
                        int B = (rgb      ) & 0xff;
                        double tmp = 0.299 * R + 0.587 * G + 0.114 * B;
                        input[0][j][i][0] = (float) tmp/255;
                    }
                }
                String[] label = {"chest circ","waist circ","pelvis circ","neck circ","bicep circ","thigh circ","knee circ","arm length","leg length","calf length","head circ","wrist circ","arm span","shoulders width","torso length","inner leg"};

                interpreter.run(input, output);



                Button gallery = findViewById(R.id.Gallery);
                Button camera = findViewById(R.id.camera);
                Button buttonM = findViewById(R.id.mybody);
                TextView result_text = findViewById(R.id.ConstraintLayout);
                ImageView result_image = findViewById(R.id.result_image);

                camera.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
                buttonM.setVisibility(View.VISIBLE);
                TextView head = findViewById(R.id.head_text);
                TextView shoulder = findViewById(R.id.shoulder_text);
                TextView arm = findViewById(R.id.arm_text);
                TextView torso = findViewById(R.id.torso_text);
                TextView chest = findViewById(R.id.chect_text);
                TextView pelvis = findViewById(R.id.pelvis_text);
                TextView leg = findViewById(R.id.leg_text);
                TextView thigh = findViewById(R.id.thigh_text);
                head.setVisibility(View.VISIBLE);
                shoulder.setVisibility(View.VISIBLE);
                arm.setVisibility(View.VISIBLE);
                torso.setVisibility(View.VISIBLE);
                chest.setVisibility(View.VISIBLE);
                pelvis.setVisibility(View.VISIBLE);
                leg.setVisibility(View.VISIBLE);
                thigh.setVisibility(View.VISIBLE);

                result_text.setVisibility(View.GONE);
                result_image.setVisibility(View.VISIBLE);


                head.setText("머리 둘레\n \n"+String.valueOf(output[0][10]));
                shoulder.setText("어깨 너비\n \n"+String.valueOf(output[0][13]));
                arm.setText("팔 길이\n \n"+String.valueOf(output[0][7]));
                torso.setText("상체 길이\n \n"+String.valueOf(output[0][14]));
                chest.setText("가슴 둘레\n \n"+String.valueOf(output[0][0]));
                pelvis.setText("골반 둘레\n \n"+String.valueOf(output[0][2]));
                leg.setText("하체 길이\n \n"+String.valueOf(output[0][8]));
                thigh.setText("허벅지 둘레\n \n"+String.valueOf(output[0][5]));

                result_image.setImageBitmap(tmpimg);
                result_image.setClipToOutline(true);

                buttonM.setOnClickListener(new View.OnClickListener() {
                    @Override //이미지 불러오기기(갤러리 접근)
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        startActivity(intent);
                    }
                });
                isFinish = true;
                in.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public Bitmap getSegment(Bitmap inputbitmap) {

        seginterpreter = getTfliteInterpreter("lite-model_deeplabv3_1_metadata_2.tflite");

        Bitmap resizeinput = Bitmap.createScaledBitmap(inputbitmap, 257,257, true);

        float[][][][] input = new float[1][257][257][3];
        float[][][][] output = new float[1][257][257][21];

        int[][] grayinput = new int[257][257];

        for (int i = 0; i < resizeinput.getWidth(); i++)
        {
            for (int j = 0; j < resizeinput.getHeight(); j++) {
                int rgb = resizeinput.getPixel(i,j);
                int R = (rgb >> 16) & 0xff;
                int G = (rgb >>  8) & 0xff;
                int B = (rgb      ) & 0xff;

                input[0][j][i][0] = (float) R/255;
                input[0][j][i][1] = (float) G/255;
                input[0][j][i][2] = (float) B/255;

                double tmp = 0.299 * R + 0.587 * G + 0.114 * B;

                grayinput[j][i] = (int)tmp;
            }
        }

        seginterpreter.run(input, output);

        int[][] mSegmentBits =  new int[257][257];
        int[][] outputBitmap = new int[257][257];

        for( int y =0; y<257 ; y++){
            for( int x=0; x<257;x++){
                float maxVal = 0;
                mSegmentBits[x][y]=0;

                for(int c =0; c<21;c++){
                    float value = output[0][y][x][c];
                    if(c==0 || value > maxVal){
                        maxVal = value;
                        mSegmentBits[y][x]=c;
                    }
                }

                if(mSegmentBits[y][x]==15){
                    outputBitmap[y][x]=grayinput[y][x];
                }
                else {
                    outputBitmap[y][x]=0;

                }
                //System.out.println(outputBitmap[y][x]);
            }

        }




        Bitmap outputimg = bitmapFromArray(outputBitmap);

        return outputimg;
    }


    public static Bitmap bitmapFromArray(int[][] pixels2d){
        int width = pixels2d.length;
        int height = pixels2d[0].length;
        Bitmap output = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width * height];
        int pixelsIndex = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixels[pixelsIndex] = pixels2d[i][j];
                pixelsIndex ++;
            }
        }

        for(int k=0;k<width * height;k++){
            System.out.println(pixels[k]);
        }

        output.setPixels(pixels,0,width,0,0,width,height);

        //return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return output;
    }



    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}

