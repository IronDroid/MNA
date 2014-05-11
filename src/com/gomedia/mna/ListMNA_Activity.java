package com.gomedia.mna;

import com.gomedia.mna.dummy.MNA_Helper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListMNA_Activity extends Activity implements OnItemClickListener {

    GlobalDataApplication global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mna);

        global = (GlobalDataApplication) getApplication();

        MNA_Helper helper = new MNA_Helper();

        try {
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setAdapter(new ItemMNA_Adapter(this, helper.getListDirMNA("/.mna/", global.idLang + ".txt")));
            listView.setOnItemClickListener(this);
        } catch (Exception e) {
            Toast.makeText(this, "Datos no disponibles", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // *********************************
    public void buttonback(View view) {
        onBackPressed();
    }

    // *********************************
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        TextView textView_code_obra = (TextView) view
                .findViewById(R.id.textView_code_obra);

        GlobalDataApplication global = (GlobalDataApplication) getApplication();
        // global.codeObra =
        global.codeObra = textView_code_obra.getText().toString().split(":")[1]
                .trim();
        // String s =
        // textView_code_obra.getText().toString().split(":")[1].trim();
        // Log.e("fer", s);
        startActivity(new Intent(this, PropertiesMNA_Activity.class).putExtra("tipo", 3));
    }
}
