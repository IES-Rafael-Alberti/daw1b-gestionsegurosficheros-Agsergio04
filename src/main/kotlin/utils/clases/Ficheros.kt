package utils.clases

import model.interfaces.IExportable
import ui.intefaces.IEntradaSalida
import utils.interfaces.IUtilFicheros
import java.io.File
import java.io.IOException

class Ficheros(private val entradaSalida: IEntradaSalida) : IUtilFicheros {


    override fun leerArchivo(ruta: String): List<String> {
        return try {
            File(ruta).readLines()
        } catch (e: IOException) {
            entradaSalida.mostrarError("Error al leer el archivo: ${e.message}")
            emptyList()
        }
    }


    override fun agregarLinea(ruta: String, linea: String): Boolean {
        return try {
            File(ruta).appendText("$linea\n")
            true
        } catch (e: IOException) {
            entradaSalida.mostrarError("Error al escribir en el archivo: ${e.message}")
            false
        }
    }


    override fun <T : IExportable> escribirArchivo(ruta: String, elementos: List<T>): Boolean {
        val archivo = File(ruta)

         try {
            var contenido = elementos.bufferedWriter().use { out ->
                 elementos.forEach { out.write(it.serializar() + "\n") }

            }

             contenido = elementos.joinToString("\n")

            archivo.writeText(contenido.toString())
            true
        } catch (e: IOException) {
            entradaSalida.mostrarError("Error al escribir en el archivo: ${e.message}")
            false
        }
    }


    override fun existeFichero(ruta: String): Boolean {
        return File(ruta).exists()
    }


    override fun existeDirectorio(ruta: String): Boolean {
        val archivo = File(ruta)
        return archivo.exists() && archivo.isDirectory
    }
}
