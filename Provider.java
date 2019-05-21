package inc.alfaleon.pruebarv;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class Provider extends ContentProvider {
    //URI
    private static final String uri = "content://inc.alfaleon.pruebarv/monstruo";


    public static final Uri CONTENT_URI = Uri.parse(uri);
    private static final int MONSTRUO = 1;
    private static final int MONSTRUO_ID = 2;
    private static final UriMatcher uriMatcher;
    //Clase para declarar las constantes de la columna de la tabla

    public static final class Monstruo implements BaseColumns{
        private Monstruo(){


        }

        public static final String COL_NOMBRE = "nombre";
        public static final String COL_DESCRIPCION = "descripcion";
        public static final String COL_DESC_LARGA = "descripcion_larga";
    }

    //Base de datos
    private Conexion conn;
    SQLiteDatabase db;
    private static final String BD_NOMBRE = "yokai";
    private static final int BD_VERSION = 1;
    private static final String TABLA_MONSTRUO = "monstruo";


    //iniciamos el urimatcher

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("inc.alfaleon.pruebarv", "monstruo", MONSTRUO);
        uriMatcher.addURI("inc.alfaleon.pruebarv", "monstruo/#", MONSTRUO_ID);

    }


    @Override
    public boolean onCreate() {
        conn = new Conexion(getContext(), BD_NOMBRE, null, BD_VERSION);
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        //Por si solicitamos un monstruo en concreto por su id (nombre)
        String where = selection;
        if(uriMatcher.match(uri)==MONSTRUO_ID){
            where = "nombre=" + uri.getLastPathSegment();
        }
         db = conn.getWritableDatabase();

        Cursor c = db.query(TABLA_MONSTRUO, projection, where, selectionArgs, null, null, sortOrder);
        return c;
    }


    @Override
    public String getType( Uri uri) {
        //identifica el tipo de datos que devuelve el content provider
        int match = uriMatcher.match(uri);
        switch(match){
            case MONSTRUO:
                return "vnd.android.cursor.dir/vnd.pruebarv.monstruo";
            case MONSTRUO_ID:
                return "vnd.android.cursor.item/vnd.pruebarv.monstruo";
            default:
                return null;
        }

    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        //devuelve la uri que hace referencia al registro insertado. Obtenemos el nuevo id y construimos una nueva uri en base al id obtenido.
        long regId = 1;
        SQLiteDatabase db = conn.getWritableDatabase();
        regId = db.insert(TABLA_MONSTRUO, null, values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int cont;
        String where = selection;
        if(uriMatcher.match(uri)==MONSTRUO_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = conn.getWritableDatabase();
        cont = db.delete(TABLA_MONSTRUO, where, selectionArgs);
        return cont;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int cont;
        String where = selection;
        if(uriMatcher.match(uri)==MONSTRUO_ID){
            where = "nombre=" + uri.getLastPathSegment();
        }
        SQLiteDatabase db = conn.getWritableDatabase();
        cont = db.update(TABLA_MONSTRUO, values, where, selectionArgs);
        return cont;
    }
}
