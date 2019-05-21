package inc.alfaleon.pruebarv;

public class Utilidades {

    public static final String NOMBRE_TABLA = "monstruo";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_DESCRIPCION_LARGA = "descripcion_larga";


    public static final String CREAR_TABLA_MONSTRUO = "CREATE TABLE " +
            ""+NOMBRE_TABLA+" ("+CAMPO_NOMBRE+" TEXT, "+CAMPO_DESCRIPCION+" TEXT,"+CAMPO_DESCRIPCION_LARGA+" TEXT)";

}
