package com.ego.avatar4bmob;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ego.avatar4bmob.R;

import com.ego.avatar4bmob.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/24/024.
 */

public class insect extends BaseActivity {
    //获取图片
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private ImageView picture;
    private Uri imageUri;
    //图像处理控件
    private Button btntst;
    private Bitmap bmp;
    Bitmap resultImg;
    int w,h;
    int img_rows=20,ims_cols=20;
    boolean ableToJudge=true;
    int number; //昆虫数量
    int[] pixels,pixels2;
    String fileDir,model_bin_file,model_txt_file,labels_txt_file;
    TextView result;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    //opencv函数
    public static native String GoogleNet(int[] pixels, int w, int h,String model_bin_file,String model_txt_file,String labels_txt_file);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect);

        //获取图像按钮
        Button takephoto=(Button) findViewById(R.id.take_photo);
        Button chooseFromAlbum = (Button)findViewById(R.id.choose_from_album);
        //图像处理控件
        btntst=(Button) findViewById(R.id.btn_test);
        picture = (ImageView) findViewById(R.id.picture);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.nc3);
        picture.setImageBitmap(bmp);
        result=(TextView)findViewById(R.id.result);

        //获取图像按钮
        takephoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(insect.this,"com.ego.avatar4bmob.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
                //adapter.notifyDataSetChanged();
            }
        });
        chooseFromAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ContextCompat.checkSelfPermission(insect.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(insect.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                //adapter.notifyDataSetChanged();
            }
        });
        //识别图像按钮
        btntst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bmp =((BitmapDrawable) ((ImageView) picture).getDrawable()).getBitmap();
                w = bmp.getWidth();
                h = bmp.getHeight();
                double bl=(double)w/h;
                double bl2=(double)h/w;
                if(w>h)
                {
                    w=600;
                    h=(int)((double)w*bl2);
                    bmp =Bitmap.createScaledBitmap(bmp,w,h,true);
                }
                else
                {
                    h=600;
                    w=(int)((double)h*bl);
                    bmp =Bitmap.createScaledBitmap(bmp,w,h,true);
                }
                pixels = new int[w*h];
                //pixels2 = new int[w*h];
                bmp.getPixels(pixels, 0, w, 0, 0, w, h);
                //bmp.getPixels(pixels2, 0, w, 0, 0, w, h);
                File sdcard = Environment.getExternalStorageDirectory();

                model_bin_file = sdcard.getAbsolutePath() + "/InsectScan/caffe_train_iter_5000.caffemodel";
                model_txt_file = sdcard.getAbsolutePath() + "/InsectScan/deploy.prototxt";
                labels_txt_file = sdcard.getAbsolutePath() + "/InsectScan/label.txt";
                String google_res=GoogleNet(pixels, w, h,model_bin_file,model_txt_file,labels_txt_file);
                //Toast.makeText(MainActivity.this,google_res,Toast.LENGTH_SHORT).show();;
                result.setText(google_res);
            }
        });

    }
    //获取图像方法
    private void openAlbum(){
        //   Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //    intent.setType("image/*");
        //   startActivityForResult(intent,CHOOSE_PHOTO);
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap); //设置图像
                        ableToJudge=true;
                        //insectList.clear();
                        //initInsect();
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)//API等级
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri 则通过id进行解析处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出数字格式id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                        "content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equals(uri.getScheme())){
            //如果不是document类型的uri，则使用普通的方式处理
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap); //设置图像
            ableToJudge=true;
            //insectList.clear();
            //initInsect();
        }else{
            Toast.makeText(this,"图片获取失败",Toast.LENGTH_SHORT).show();
        }
    }
}
