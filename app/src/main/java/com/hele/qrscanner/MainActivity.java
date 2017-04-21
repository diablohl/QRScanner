package com.hele.qrscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button mScan;
    public TextView mResult;
    public EditText mInput;
    public Button mshengcheng;
    public Button mSave;
    public ImageView mimageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScan=(Button)findViewById(R.id.btn_scan);
        mResult=(TextView)findViewById(R.id.tv_result);
        mshengcheng=(Button)findViewById(R.id.btn_sc);
        mimageView=(ImageView)findViewById(R.id.imageview);
        mInput=(EditText)findViewById(R.id.edittext);




    }
    public void scan(View view){
        //运行时权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }else {
            startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
        }

    }

    public void make(View view){
        String input=mInput.getText().toString();
        if (input.equals("")){
            Toast.makeText(MainActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
        }else {
            Bitmap bitmap= EncodingUtils.createQRCode(input,500,500,null);
            mimageView.setImageBitmap(bitmap);

        }

    }

    public void save(View view){

        mimageView.buildDrawingCache(true);
        mimageView.buildDrawingCache();
        Bitmap bitmap = mimageView.getDrawingCache();
        saveBitmapFile(bitmap);
        mimageView.setDrawingCacheEnabled(false);
        Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        systemtime();
    }

    public void saveBitmapFile(Bitmap bitmap){

        File temp = new File("/sdcard/QRCode/");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }

        String s=systemtime();

        //保存时，不会覆盖原同名图片
        File file=new File("/sdcard/QRCode/"+s+".jpg");//将要保存图片的路径和图片名称

        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取当前系统时间作为保存图片的文件名
    public String systemtime(){
        SimpleDateFormat timesdf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
        String FileTime =timesdf.format(new Date()).toString();//获取系统时间
        return FileTime;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            String result=bundle.getString("result");
            mResult.setText(result);

        }
    }

}
