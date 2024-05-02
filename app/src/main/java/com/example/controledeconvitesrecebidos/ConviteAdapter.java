package com.example.controledeconvitesrecebidos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ConviteAdapter extends BaseAdapter {
    private Context context;
    private List<Convite> convites;

    private static class ConviteHolder {
        public ImageView imgConvite;
        public TextView txtTitulo;
        public TextView txtLocal;
        public TextView txtData;
    }

    public ConviteAdapter(Context context, List<Convite> convites) {
        this.context = context;
        this.convites = convites;
    }

    @Override
    public int getCount() {
        return convites.size();
    }

    @Override
    public Object getItem(int i) {
        return convites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ConviteHolder holder;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_convites, viewGroup, false);

            holder = new ConviteHolder();

            holder.imgConvite = view.findViewById(R.id.imageConvite);
            holder.txtData = view.findViewById(R.id.txtData);
            holder.txtLocal = view.findViewById(R.id.txtLocal);
            holder.txtTitulo = view.findViewById(R.id.txtTitulo);

            view.setTag(holder);
        }
        else {
            holder = (ConviteHolder) view.getTag();
        }

        holder.txtTitulo.setText(convites.get(i).getTitulo());
        holder.txtLocal.setText(convites.get(i).getLocal());
        holder.txtData.setText(convites.get(i).getData());

        holder.imgConvite.setImageDrawable(
                new BitmapDrawable(BitmapFactory.decodeByteArray(
                                                                    convites.get(i).getImgConvite(),
                                                                0,
                                                                    convites.get(i).getImgConvite().length)));

        return view;
    }
}
