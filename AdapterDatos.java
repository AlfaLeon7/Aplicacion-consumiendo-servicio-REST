package inc.alfaleon.pruebarv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolder> implements View.OnClickListener {


    private ArrayList<Monstruo> listDatos;
    private View.OnClickListener listener;


    public AdapterDatos(ArrayList<Monstruo> listDatos) {

        this.listDatos = listDatos;
    }




    @NonNull
    @Override
    //Para conectar items.xml con el adaptador
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()
        ).inflate(R.layout.items, null, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {


        viewHolder.nombre.setText(listDatos.get(i).getNombre());
        viewHolder.descripcion.setText(listDatos.get(i).getDescripcion());
        viewHolder.descripcionLarga.setText(listDatos.get(i).getDescripcionLarga());
        viewHolder.foto.setImageResource(listDatos.get(i).getFoto());

        viewHolder.setOnClickListener();




    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
    if(listener!=null){
        listener.onClick(v);
}
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        Context context;
        TextView nombre, descripcion, descripcionLarga;
        ImageView foto;

        public ViewHolder( View itemView) {

            super(itemView);
          context =   itemView.getContext();
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            descripcion = (TextView) itemView.findViewById(R.id.idInfo);
            descripcionLarga = (TextView) itemView.findViewById(R.id.idInfoLarga);
           foto = (ImageView) itemView.findViewById(R.id.idImagen);


        }
        void setOnClickListener(){
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onClick(v);
        }
    }

}

public void updateList(ArrayList<Monstruo> listaNueva){
        listDatos = new ArrayList<>();
        listDatos.addAll(listaNueva);
        notifyDataSetChanged();
}



}
