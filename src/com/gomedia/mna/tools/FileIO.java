package com.gomedia.mna.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import com.gomedia.mna.R;
import com.gomedia.mna.dummy.DataId;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

public class FileIO {

    public static String readFile(String fileDir) {
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            // File file = new File(ruta_sd.getAbsolutePath(), "/.mna/fk.txt");
            File file = new File(ruta_sd.getAbsolutePath() + fileDir);

            BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String texto = fin.readLine();

            fin.close();
            Log.e("Ficheros", "---> : " + texto);
            Log.e("Ficheros", "---> : " + file.getPath());
            return texto;
        } catch (Exception ex) {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }
        return "no found data!";
    }

    public static Uri readFileFromSD(String fileDir) {

        Uri media = Uri.parse(Environment.getExternalStorageDirectory().getPath() + fileDir);
        return media;
    }

    public static Bitmap readImageFromSD(String fileDir) {
        String imageInSD = Environment.getExternalStorageDirectory().getPath() + fileDir;
        // Log.e("fer", imageInSD);
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        return bitmap;
    }

    public static String readTextFromSD(String fileDir) {
        File dir = Environment.getExternalStorageDirectory();
        // File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");

        // Get the text file
        File file = new File(dir, fileDir);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
        }
        return text.toString();

    }

    public static List<String> readDirContentFromSD(String dirPath) {

        String dir = Environment.getExternalStorageDirectory().getPath() + dirPath;
        List<String> listFiles = new LinkedList<String>();
        File f = new File(dir);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            for (int i = 0; i < files.length; i++) {
                listFiles.add(files[i].getName());
            }
        }

        return listFiles;
    }

    public static boolean validateConnetDirfromSD(String dirPath, String[] exts, String filename) {
        int c = 0;

        List<String> list = readDirContentFromSD(dirPath);
        // Log.e("fer", list.toString());
        for (String l : list) {
            for (String e : exts) {
                if (l.equals(filename + e)) {
                    c++;
                }
            }
        }
        if (c == exts.length) {
            return true;
        } else {
            return false;
        }
    }

    // Media Data load
    public static Drawable loadDrawableFromFile(String path, String nameFile) {

        File file = new File(path + nameFile + ".png");
        Drawable drawable = null;
        if (file.exists()) {
            drawable = Drawable.createFromPath(file.getAbsolutePath());
            drawable.setBounds(10, 10, 10, 10);
            Log.e("fer", "file " + drawable);
        } else {
            Log.e("fer", "no file " + file.getPath());
        }
        return drawable;
    }

    public static void saveDatatoSD(List<DataId> code, Context context) {

        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File carpeta = new File(dir + "/.mna/banderasEle/");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        // copia banderas
        for (DataId dataId : code) {

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), dataId.getId());

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File file = new File(extStorageDirectory + "/.mna/banderasEle/", dataId.getName() + ".png");
            OutputStream outStream;
            try {
                outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
