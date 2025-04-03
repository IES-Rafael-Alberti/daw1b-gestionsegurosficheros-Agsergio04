package data.clases

import data.interfaces.ICargarSegurosIniciales
import model.seguros.Seguro
import model.seguros.SeguroAuto
import model.seguros.SeguroHogar
import model.seguros.SeguroVida
import model.usuarios.Usuario
import utils.clases.Ficheros

class RepoSegurosFich(
    private val rutaArchivo : String,
    private val fich :  Ficheros
) : ICargarSegurosIniciales, RepoSegurosMem() {

    override fun cargarSeguros(mapa: Map<String, (List<String>) -> Seguro>): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo) ?: return false

        val segurosCargados = lineas.mapNotNull { linea ->
            val datos = linea.split(";")  // Asumiendo que los datos están separados por ";"
            mapa[datos[0]]?.invoke(datos) // Usa la función de creación adecuada
        }

        seguros.clear()
        seguros.addAll(segurosCargados)

        actualizarContadores(segurosCargados)
        return true
    }

    override fun agregar(seguro: Seguro): Boolean {
        if (buscar(seguro.numPoliza) != null) return false
        if (fich.agregarLinea(rutaArchivo, seguro.serializar())) {
            return super.agregar(seguro)
        }
        return false
    }


    override fun eliminar(seguro: Seguro): Boolean {
        if (fich.escribirArchivo(rutaArchivo, seguros.filter { it != seguro })) {
            return super.eliminar(seguro)
        }
        return false
    }

    private fun actualizarContadores(seguros : List<Seguro>){
        val maxHogar = seguros.filter{it.tipoSeguro() == "SeguroHogar"}.maxOfOrNull{it.numPoliza}
        val maxAuto = seguros.filter{it.tipoSeguro() == "SeguroAuto"}.maxOfOrNull{it.numPoliza}
        val maxVida = seguros.filter{it.tipoSeguro() == "SeguroVida"}.maxOfOrNull{it.numPoliza}

        if(maxHogar != null) SeguroHogar.numPolizaHogar = maxHogar
        if(maxAuto != null) SeguroAuto.numPolizaAuto = maxAuto
        if(maxVida != null) SeguroVida.numPolizaVida = maxVida
    }
}