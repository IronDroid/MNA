package com.gomedia.mna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.gomedia.mna.dummy.DataId;
import com.gomedia.mna.dummy.Pais;
import com.gomedia.mna.tools.FileIO;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Language_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        setTitle(R.string.title_activity_language);

        saveDatatoSD();

        createRadioButtonLanguage(loadPaisFromFile("/.mna/idiomas.txt", "/.mna/idiomasAgregar.txt"));
        // ******************
        // readFile("");
        getImagesIdentifiers();

    }
    String[] code2 = {"ca", "bo"};

    private void getImagesIdentifiers() {

        int resID = 0;
        ArrayList<DataId> images = new ArrayList<DataId>();
        for (String c : code2) {

            resID = getResources().getIdentifier("banele_" + c, "drawable", getPackageName());
            Log.e("fer", c + "   " + resID);
            if (resID != 0) {
                images.add(new DataId(resID, c));
            }
        }
        Log.e("fer", "" + images.toString());
        FileIO.saveDatatoSD(images, this);
    }
    String[] code = {"ca.png", "zh-hant.png", "ar.png", "bg.png", "bo.png", "cs.png", "da.png", "de.png", "du.png", "el.png", "en.png", "eo.png", "es.png", "et.png", "eu.png", "fa.png", "fi.png", "fo.png", "fr.png", "ga.png", "gl.png", "he.png", "hi.png", "hr.png", "hu.png", "id.png", "is.png",
        "it.png", "ja.png", "km.png", "ko.png", "lb.png", "lt.png", "lv.png", "mn.png", "nb.png", "nl.png", "nn.png", "pl.png", "pt.png", "pt-br.png", "pt-pt.png", "ro.png", "ru.png", "sco.png", "se.png", "sk.png", "sl.png", "sq.png", "sr.png", "sv.png", "tg.png", "th.png", "tl.png", "tr.png",
        "uk.png", "vi.png", "xx.png", "zh-hans.png"};

    private void saveDatatoSD() {

        String[] ban = {"bo.png", "de.png", "en.png", "es.png", "fr.png", "it.png", "pt.png", "us.png"};
        int[] banDraw = {R.drawable.ban_bo, R.drawable.ban_de, R.drawable.ban_en, R.drawable.ban_es, R.drawable.ban_fr, R.drawable.ban_it, R.drawable.ban_pt, R.drawable.ban_us};

        File carpeta = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.mna/banderas/");
        if (!carpeta.exists()) {
            carpeta.mkdirs();

            copyFilesToSdCard();
            // copia banderas
            for (int i = 0; i < banDraw.length; i++) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), banDraw[i]);
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

                Log.e("fer", "copy banderas" + extStorageDirectory);

                File file = new File(extStorageDirectory + "/.mna/banderas/", ban[i]);
                OutputStream outStream;
                try {
                    outStream = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch ������block
                    e.printStackTrace();
                }
            }

        }

    }
    // ****************************
    final static String TARGET_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.mna/";

    private void copyFilesToSdCard() {
        copyFileOrDir("idiomas.txt");
        copyFileOrDir("idiomasAgregar.txt");
        copyFileOrDir("SS501_UR_Man.mp3");
    }

    private void copyFileOrDir(String path) {

        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            Log.i("tag", "copyFileOrDir() " + path);
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = TARGET_BASE_PATH + path;
                Log.e("tag", "path=" + fullPath);
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit")) {
                    if (!dir.mkdirs())
						;
                }
                Log.e("tag", "could not create dir " + fullPath);
                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (path.equals("")) {
                        p = "";
                    } else {
                        p = path + "/";
                    }

                    if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit")) {
                        copyFileOrDir(p + assets[i]);
                    }
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        String newFileName = null;
        try {
            Log.i("tag", "copyFile() " + filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid
            // compression
            // on APK file
            {
                newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length() - 4);
            } else {
                newFileName = TARGET_BASE_PATH + filename;
            }
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", "Exception in copyFile() of " + newFileName);
            Log.e("tag", "Exception in copyFile() " + e.toString());
        }

    }
    // ****************************
    String idLAN = "";
    View view;

    void cambiarIdioma(String idioma) {
        Log.e("Ficheros", idioma);
        CharSequence codIdioma = idioma;

        Configuration idConf = new Configuration();
        Locale appLoc = new Locale(codIdioma.toString());
        Locale.setDefault(appLoc);
        idConf.locale = appLoc;
        getBaseContext().getResources().updateConfiguration(idConf, getBaseContext().getResources().getDisplayMetrics());

        Resources res = this.getResources();
        res.updateConfiguration(idConf, null);

    }

    String readFile(String fileDir) {
        try {
            File ruta_sd = Environment.getExternalStorageDirectory();
            // File file = new File(ruta_sd.getAbsolutePath(), "/.mna/fk.txt");
            File file = new File(ruta_sd.getAbsolutePath(), fileDir);

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
    String[] lanId = {"es", "ay", "du", "en", "es", "fr", "it", "pt", "us", "qu"};
    String[] lanImg = {"bo", "bo", "du", "en", "es", "fr", "it", "pt", "us", "bo"};
    String[] languages = {"Espa��ol", "Aymara", "Germany", "Ingles", "Spain", "France", "Italy", "Portuges", "Usa", "Quechua"};

    String loadLanguages(String s) {
        for (int i = 0; i < lanId.length; i++) {
            if (s.equals(lanId[i])) {
                return languages[i];
            }
        }
        return "file no found";
    }

    Drawable loadDrawableImage(String path, String s) {
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        for (int i = 0; i < lanId.length; i++) {
            if (s.equals(lanId[i])) {
                return FileIO.loadDrawableFromFile(dir + path, lanImg[i]);
            }
        }
        return FileIO.loadDrawableFromFile(dir + path, s);
    }

    List<Pais> loadPaisFromFile(String fileDir, String fileDir2) {

        String s = readFile(fileDir);
        // s += readFile(fileDir2);
        String[] ss = s.split(";;");
        List<Pais> listPais = new LinkedList<Pais>();
        for (String string : ss) {
            listPais.add(new Pais(loadDrawableImage("/.mna/banderas/", string.split("=")[0]), loadLanguages(string.split("=")[0]), string.split("=")[0], (string.split("=")[1]).equals("1") ? true : false));
        }

        // esto agrega el 2��� file
        s = readFile(fileDir2);
        ss = s.split(";;");
        for (String string : ss) {
            if (!((string.split(";")[0]).split("=")[1]).equals("--")) {
                Log.e("Ficheros", "-> " + string.split(";")[1]);
                listPais.add(new Pais(loadDrawableImage("/.mna/banderasEle/", string.split(";")[1]), string.split(";")[2], (string.split("=")[1]).split(";")[0], true));
            }
        }
        return listPais;
    }

    // ******************************crea bottones
    String getLanguge(String lans) {
        for (int i = 0; i < lanId.length; i++) {
            if (lans.trim().equals(languages[i])) {
                return lanId[i];
            }
        }
        return lanId[3];
    }

    void createRadioButtonLanguage(List<Pais> paisList) {

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;
                // radioButton.setTextColor(Color.rgb(242, 242, 242));
                idLAN = getLanguge(radioButton.getText().toString());
                Log.e("ficheros", "ok text: " + radioButton.getText() + "  " + idLAN);
                GlobalDataApplication application = (GlobalDataApplication) getApplication();
                application.idLang = idLAN;
                startActivity(new Intent(getApplicationContext(), Menu2MNA_Activity.class));
            }
        };

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 3, 3, 3);

        for (int i = 0; i < paisList.size(); i++) {

            Pais pais = paisList.get(i);
            if (pais.getBool()) {
                Log.e("Ficheros", "ok: " + pais.getName() + " - " + pais.getCode());

                RadioButton radioButton = new RadioButton(this);
                radioButton.setButtonDrawable(R.drawable.selector_radio_check_mna);
                radioButton.setBackgroundResource(R.drawable.selector_radio_mna);
                radioButton.setTextColor(R.drawable.selector_textview_standar);
                radioButton.setTextSize(25);

                radioButton.setLayoutParams(layoutParams);

                radioButton.setText(" " + pais.getName());
                radioButton.setId(i);

                // radioButton.setCompoundDrawables(null, null, drawable, null);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(pais.getDrawable(), null, null, null);
                // radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                // R.drawable.ic_bandera_bol, 0);
                radioButton.setOnClickListener(onClickListener);

                // radioButton.setPadding(50, 0, 0, 0);

                // radioButton.setHeight(80);

                radioGroup.addView(radioButton);
            }

        }

    }

    @Override
    public void onBackPressed() {
    }
}
