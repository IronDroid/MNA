package com.gomedia.mna.dummy;

import android.graphics.drawable.Drawable;

public class Pais {

	private Drawable drawable;
	private String name;
	private String code;
	private Boolean bool;

	public Pais() {
	}

	public Pais(Drawable drawable, String name, String code, Boolean bool) {
		super();
		this.drawable = drawable;
		this.name = name;
		this.code = code;
		this.bool = bool;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getBool() {
		return bool;
	}

	public void setBool(Boolean bool) {
		this.bool = bool;
	}

}
