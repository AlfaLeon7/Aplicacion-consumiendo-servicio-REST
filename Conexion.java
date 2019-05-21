package inc.alfaleon.pruebarv;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexion extends SQLiteOpenHelper {


    public Conexion(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(Utilidades.CREAR_TABLA_MONSTRUO);
        for(int i=1; i<=15; i++)
        {
            //Generamos los datos de muestra
            String nombre = "Monstruo " + i;
            String descripcion = "Monstruo base " + i;
            String descripcion_larga = "No hace demasiado";

            //Insertamos los datos en la tabla Clientes
            db.execSQL("INSERT INTO  "+Utilidades.NOMBRE_TABLA+"  (nombre, descripcion, descripcion_larga) " +
                    "VALUES ('" + nombre + "', '" + descripcion +"', '" + descripcion_larga + "')");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS monstruo");
        onCreate(db);
            }
}
