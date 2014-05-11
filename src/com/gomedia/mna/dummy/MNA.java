package com.gomedia.mna.dummy;

import android.graphics.Bitmap;

public class MNA {

	private Bitmap image;
	private String nameObra;
	private String autor;
	private String code;

	public MNA() {
		// TODO Auto-generated constructor stub
	}

	public MNA(Bitmap image, String nameObra, String autor, String code) {
		super();
		this.image = image;
		this.nameObra = nameObra;
		this.autor = autor;
		this.code = code;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getNameObra() {
		return nameObra;
	}

	public void setNameObra(String nameObra) {
		this.nameObra = nameObra;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
