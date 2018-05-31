package cn.yin.com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by 79859 on 2018/5/31.
 */

public class PictureUtils {
    public static final String TAG = "PictureUtils";
    public static final int SAVE_DONE_TOAST = 666;
    public static final String IMAGE_SAVE_PATH = "/Gank/";


    public static String getSDCardPath(){
        File SDCardDir;
        // check whether SD card exist
        boolean sdcardExist= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(sdcardExist){
            SDCardDir=Environment.getExternalStorageDirectory();
        }else {
            SDCardDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        return SDCardDir.toString();
    }

    public static boolean saveBitmapFromUrl(Context context,String url) throws ExecutionException, InterruptedException {
        Bitmap bitmap = Glide.with(context)
                .load(url).asBitmap()
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
        PictureUtils.saveBitmapToSDCard(bitmap, url,context);
        return true;
    }

    private static File saveBitmapToSDCard(Bitmap bitmap, String url, Context context) {
        String SavePath = getSDCardPath() + IMAGE_SAVE_PATH;
        try {
            File path = new File(SavePath);
            if (!path.exists())
                path.mkdirs();

            String filepath = SavePath + getLastStringFromUrl(url);
            File file = new File(filepath);
            if (!file.exists())
                file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                Log.i(TAG, "saveBitmapToSDCard: " + filepath);
                ToastUtil.showToast(context,"图片保存在"+ filepath);
                return file;
            }
        } catch (Exception e) {
            String text=e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    public static String getLastStringFromUrl(String url) {
        String[] splitStrs = url.split("\\/");
        return splitStrs[splitStrs.length - 1];
    }

    public static String getImgPathFromUrl(String url) {
        return getSDCardPath() + IMAGE_SAVE_PATH + getLastStringFromUrl(url);
    }
}
