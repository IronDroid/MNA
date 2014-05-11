package com.gomedia.mna;

import java.util.List;

import com.gomedia.mna.dummy.MNA;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemMNA_Adapter extends BaseAdapter {

	private Context context;
	private List<MNA> mnas;

	public ItemMNA_Adapter(Context context, List<MNA> mnas) {
		super();
		this.context = context;
		this.mnas = mnas;
	}

	@Override
	public int getCount() {
		return mnas.size();
	}

	@Override
	public Object getItem(int position) {
		return mnas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mnas.get(position).hashCode();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		// View view = view;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_list_mna, null);
		}

		MNA mna = mnas.get(position);
		TextView textView_title_obra = (TextView) view.findViewById(R.id.textView_title_obra);
		textView_title_obra.setText(mna.getNameObra());

		TextView tRuta = (TextView) view.findViewById(R.id.textView_code_obra);
		tRuta.setText(context.getResources().getString(R.string.txt_code) + " " + mna.getCode());

		ImageView imageView = (ImageView) view.findViewById(R.id.imageView_obra);
		imageView.setImageBitmap(mna.getImage());
		// imageView.setImageResource(equipo.getImageEquipo());

		return view;
	}

}
