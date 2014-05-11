package com.gomedia.mna.dummy;

import java.util.LinkedList;
import java.util.List;

import android.R.string;
import android.graphics.Bitmap;
import android.util.Log;

import com.gomedia.mna.tools.FileIO;

public class MNA_Helper extends MNA {

	public MNA_Helper() {
		// TODO Auto-generated constructor stub
	}

	public MNA_Helper(Bitmap image, String nameObra, String autor, String code) {
		super(image, nameObra, autor, code);
	}

	public List<MNA> getlistTest() {
		List<MNA> list = new LinkedList<MNA>();

		for (int i = 0; i < 17; i++) {
			list.add(new MNA(null, "fer [fk]", "fcc", "777"));
		}
		return list;
	}

	// ******************
	public void getDataList() {

	}

	public List<MNA> getListDirMNA(String path, String idLang) {

		String contentFile = FileIO.readTextFromSD(path + idLang);
		String s = contentFile.trim().substring(0, contentFile.length() - 3);
		List<MNA> list = new LinkedList<MNA>();
		for (String datas : s.split(";;")) {
			//Log.e("fer", FileIO.readImageFromSD("/.mna/" + datas.split(";")[0] + ".jpg") + "");
			list.add(new MNA_Helper(
					FileIO.readImageFromSD("/.mna/" + datas.split(";")[0] + ".jpg"),
					datas.split(";")[1],
					"",
					datas.split(";")[0]));
		}
		// Log.e("fer", contentFile);
		// List<String> l = FileIO.readDirContentFromSD(path);

		// Log.e("fer", idLang + " - " + l.toString());
		return list;

	}
}
