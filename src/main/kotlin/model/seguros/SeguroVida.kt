package model.seguros

import model.enumerados.Riesgo
import model.enumerados.Riesgo.Companion.getRiesgo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeguroVida : Seguro {
    private var fechaNam : LocalDate = LocalDate.now()
    private var nivelRiesgo : Riesgo = Riesgo.BAJO
    private var indemnizacion : Double = 0.0


    constructor(dniTitular: String, importe: Double,fechaNam : LocalDate,nivelRiesgo : Riesgo,indemnizacion : Double) :
            super(incremetarId(), dniTitular, importe) {

                this.fechaNam = fechaNam
                this.nivelRiesgo = nivelRiesgo
                this.indemnizacion = indemnizacion

    }

    constructor(numPoliza : Int,dniTitular: String, importe: Double,fechaNam : LocalDate,nivelRiesgo : Riesgo,indemnizacion : Double) :
            super(numPoliza, dniTitular, importe) {

        this.fechaNam = fechaNam
        this.nivelRiesgo = nivelRiesgo
        this.indemnizacion = indemnizacion


    }

    override fun serializar(separador: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        return super.serializar(separador) +
                "$separador${fechaNam.format(formatter)}" +
                "$separador$nivelRiesgo$separador$indemnizacion" +
                "$separador${tipoSeguro()}"
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        subirRiesgo()
        return super.calcularImporteAnioSiguiente(interes)
    }

    fun subirRiesgo(){
        if (nivelRiesgo == Riesgo.BAJO ) {
            nivelRiesgo = Riesgo.MEDIO
        } else if(nivelRiesgo == Riesgo.MEDIO ){
            nivelRiesgo == Riesgo.ALTO
        } else {
            nivelRiesgo == Riesgo.BAJO
        }
    }

    override fun toString(): String {
        return super.toString() + "fechaNam = $fechaNam,nivelRiesgo = $nivelRiesgo,indemnizacion = $indemnizacion"
    }

    companion object{
        var numPolizaVida = 799999

        fun incremetarId() : Int{

            return numPolizaVida + 1

        }


        fun crearSeguro(datos: List<String>): SeguroVida {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return SeguroVida(
                numPoliza = datos[0].toInt(),
                dniTitular = datos[1],
                importe = datos[2].toDouble(),
                fechaNam = LocalDate.parse(datos[3], formatter),
                nivelRiesgo = getRiesgo(datos[4].toDouble()),
                indemnizacion = datos[5].toDouble()
            )
        }

    }
}