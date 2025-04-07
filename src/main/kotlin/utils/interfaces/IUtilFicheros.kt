package utils.interfaces

import model.interfaces.IExportable

/*Cambiar para la creacion de cualquier cosa que trabaje con ficheros*/
interface IUtilFicheros {
    fun leerArchivo(ruta: String): List<String>
    fun agregarLinea(ruta: String, linea: String): Boolean
    fun <T : IExportable> escribirArchivo(ruta: String, elementos: List<T>): Boolean
    fun existeFichero(ruta: String): Boolean
    fun existeDirectorio(ruta: String): Boolean
}


