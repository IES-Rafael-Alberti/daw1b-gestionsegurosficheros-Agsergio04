package service.clases

import data.interfaces.IRepoSeguros
import model.enumerados.Auto
import model.enumerados.Cobertura
import model.enumerados.Riesgo
import model.seguros.Seguro
import model.seguros.SeguroAuto
import model.seguros.SeguroHogar
import model.seguros.SeguroVida
import service.interfaces.IServSeguros
import java.time.LocalDate


class GestorSeguros(
    private val repoSeguros: IRepoSeguros
) : IServSeguros {

    // Contratar un seguro de hogar
    override fun contratarSeguroHogar(
        dniTitular: String,
        importe: Double,
        metrosCuadrados: Int,
        valorContenido: Double,
        direccion: String,
        anioConstruccion: Int
    ): Boolean {

        return repoSeguros.agregar(SeguroHogar(
            dniTitular = dniTitular,
            importe = importe,
            metrosCuadrados = metrosCuadrados,
            valorContenido = valorContenido,
            direccion = direccion,
            anioConstruccion = anioConstruccion))

    }

    // Contratar un seguro de auto
    override fun contratarSeguroAuto(
        dniTitular: String,
        importe: Double,
        descripcion: String,
        combustible: Int,
        tipoAuto: Auto,
        cobertura: Cobertura,
        asistenciaCarretera: Boolean,
        numPartes: Int
    ): Boolean {
        return repoSeguros.agregar(SeguroAuto(
            dniTitular = dniTitular,
            importe = importe,
            descripcion = descripcion,
            combustible = combustible,
            tipoAuto = tipoAuto,
            cobertura = cobertura,
            asistenciaCarretera = asistenciaCarretera,
            numPartes = numPartes
        ))
    }

    // Contratar un seguro de vida
    override fun contratarSeguroVida(
        dniTitular: String,
        importe: Double,
        fechaNacimiento: LocalDate,
        nivelRiesgo: Riesgo,
        indemnizacion: Double
    ): Boolean {
        return repoSeguros.agregar(
            SeguroVida(
                dniTitular = dniTitular,
                importe = importe,
                fechaNam = fechaNacimiento,
                nivelRiesgo = nivelRiesgo,
                indemnizacion = indemnizacion
            )
        )
    }


    override fun eliminarSeguro(numPoliza: Int): Boolean {
        return repoSeguros.eliminar(numPoliza)
    }


    override fun consultarTodos(): List<Seguro> {
        return repoSeguros.obtenerTodos()
    }


    override fun consultarPorTipo(tipoSeguro: String): List<Seguro> {
        return repoSeguros.obtener(tipoSeguro)
    }
}