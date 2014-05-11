package com.gomedia.mna.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;

/**
 *
 * @author Jhon_Li
 */
public class DBAdapter {

    private static final String DB_NAME = "mna";
    private static final String TABLE_POSICION = "Posicion";
    private static final String TABLE_OBRA_VISTA = "ObraVista";
    private static final String TABLE_OBRA_MENSAJE = "ObraMensaje";
    private static final String IMEI = "imei";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String TIPO = "tipo";
    private static final String MENSAJE = "mensaje";
    private static final String COD_OBRA = "codObra";
    private static final String ESTADO = "estado";
    private static final int DB_VERSION = 1;
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBAdapter(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Posicion (id_p INTEGER PRIMARY KEY AUTOINCREMENT, imei TEXT, lat TEXT, lon TEXT, estado INTEGER)");
            db.execSQL("CREATE TABLE ObraVista (id_ov INTEGER PRIMARY KEY AUTOINCREMENT, imei TEXT, tipo INTEGER, codObra INTEGER, estado INTEGER)");
            db.execSQL("CREATE TABLE ObraMensaje (id_om INTEGER PRIMARY KEY AUTOINCREMENT, imei TEXT, codObra INTEGER, mensaje TEXT, estado INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void insertPos(String imei, String lat, String lon, boolean estado) {
        ContentValues cv = new ContentValues();
        cv.put(IMEI, imei);
        cv.put(LAT, lat);
        cv.put(LON, lon);
        cv.put(ESTADO, estado);
        db.insert(TABLE_POSICION, null, cv);
    }

    public void insertObraVista(String imei, int tipo, String codObra, boolean estado) {
        ContentValues cv = new ContentValues();
        cv.put(IMEI, imei);
        cv.put(TIPO, tipo);
        cv.put(COD_OBRA, codObra);
        cv.put(ESTADO, estado);
        db.insert(TABLE_OBRA_VISTA, null, cv);
    }

    public void insertObraMensaje(String imei, int codObra, String mensaje, boolean estado) {
        ContentValues cv = new ContentValues();
        cv.put(IMEI, imei);
        cv.put(COD_OBRA, codObra);
        cv.put(MENSAJE, mensaje);
        cv.put(ESTADO, estado);
        db.insert(TABLE_OBRA_MENSAJE, null, cv);
    }

    public boolean updatePos(int id) {
        Cursor res = db.query(TABLE_POSICION, new String[]{IMEI, LAT, LON, ESTADO}, "id_p = " + id, null, null, null, null, null);
        res.moveToNext();
        ContentValues cv = new ContentValues();
        cv.put(IMEI, res.getString(0));
        cv.put(LAT, res.getString(1));
        cv.put(LON, res.getString(2));
        cv.put(ESTADO, 1);
        db.update(TABLE_POSICION, cv, "id_p = " + id, null);
        return true;
    }

    public boolean updateObraVista(int id) {
        Cursor res = db.query(TABLE_OBRA_VISTA, new String[]{IMEI, TIPO, COD_OBRA, ESTADO}, "id_ov = " + id, null, null, null, null, null);
        res.moveToNext();
        ContentValues cv = new ContentValues();
        cv.put(IMEI, res.getString(0));
        cv.put(TIPO, res.getString(1));
        cv.put(COD_OBRA, res.getString(2));
        cv.put(ESTADO, 1);
        db.update(TABLE_OBRA_VISTA, cv, "id_ov = " + id, null);
        return true;
    }

    public boolean updateObraMensaje(int id) {
        Cursor res = db.query(TABLE_OBRA_MENSAJE, new String[]{IMEI, COD_OBRA, MENSAJE, ESTADO}, "id_om = " + id, null, null, null, null, null);
        res.moveToNext();
        ContentValues cv = new ContentValues();
        cv.put(IMEI, res.getString(0));
        cv.put(COD_OBRA, res.getString(1));
        cv.put(MENSAJE, res.getString(2));
        cv.put(ESTADO, 1);
        db.update(TABLE_OBRA_MENSAJE, cv, "id_om = " + id, null);
        return true;
    }

    public Cursor getPos() {
        Cursor res = db.query(TABLE_POSICION, new String[]{"id_p", IMEI, LAT, LON, ESTADO}, ESTADO + " = " + 0, null, null, null, null, null);
        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }
    public Cursor getObraVista() {
        Cursor res = db.query(TABLE_OBRA_VISTA, new String[]{"id_ov", IMEI, TIPO, COD_OBRA, ESTADO}, ESTADO + " = " + 0, null, null, null, null, null);
        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }
    public Cursor getObraMensaje() {
        Cursor res = db.query(TABLE_OBRA_MENSAJE, new String[]{"id_om", IMEI, COD_OBRA, MENSAJE, ESTADO}, ESTADO + " = " + 0, null, null, null, null, null);
        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }

    public DBAdapter open() {
        db = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }
}
