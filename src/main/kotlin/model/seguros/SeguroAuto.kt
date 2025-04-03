package model.seguros

import model.enumerados.Auto
import model.enumerados.Auto.Companion.getAuto
import model.enumerados.Cobertura
import model.enumerados.Cobertura.Companion.getCobertura

class SeguroAuto : Seguro {

    private var descripcion : String = ""
    private var combustible : Int = 0
    private var tipoAuto : Auto = Auto.COCHE
    private var cobertura: Cobertura = Cobertura.TERCEROS
    private var asistenciaCarretera : Boolean = true
    private var numPartes : Int = 2

    constructor(dniTitular: String, importe: Double,descripcion : String,combustible : Int,tipoAuto : Auto,cobertura: Cobertura,asistenciaCarretera : Boolean,numPartes : Int) :
            super(incremetarId(), dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.cobertura = cobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    constructor(numPoliza : Int,dniTitular: String, importe: Double,descripcion : String,combustible : Int,tipoAuto : Auto,cobertura: Cobertura,asistenciaCarretera : Boolean,numPartes : Int) :
            super(numPoliza, dniTitular, importe) {
        this.descripcion = descripcion
        this.combustible = combustible
        this.tipoAuto = tipoAuto
        this.cobertura = cobertura
        this.asistenciaCarretera = asistenciaCarretera
        this.numPartes = numPartes
    }

    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        return interes + (1 + (interes + numPartes * PORCENTAJE_INCREMENTO_PARTES) / 100)
    }

    override fun serializar(separador: String): String {
        return super.serializar(separador) +
                "$separador$descripcion" +
                "$separador$combustible$separador$tipoAuto" +
                "$separador$cobertura$separador$asistenciaCarretera" +
                "$separador$numPartes$separador${tipoSeguro()}"
    }

    override fun toString(): String { // hacerlo con replace
        return super.toString() + "descripcion = $descripcion,combustible = $combustible,tipoAuto = $tipoAuto" +
                "cobertura = $cobertura,asistenciaCarretera = $asistenciaCarretera,numPartes = $numPartes "
    }



    companion object{
        var numPolizaAuto = 399999

        private val PORCENTAJE_INCREMENTO_PARTES = 2

        fun incremetarId() : Int{

            return if(numPolizaAuto < 799998){
                numPolizaAuto + 1
            } else {
                399999
            }
        }

        override fun toString(): String {
            return super.toString()
        }

        //parsear la fecha
        fun crearSeguro(datos: List<String>): SeguroAuto{
            return SeguroAuto(
                numPoliza = datos[0].toInt(),
                dniTitular = datos[1],
                importe = datos[2].toDouble(),
                descripcion = datos[3],
                combustible = datos[4].toInt(),
                tipoAuto = getAuto(datos[5]),
                cobertura = getCobertura(datos[6]),
                asistenciaCarretera = datos[7].toBoolean(),
                numPartes = datos[8].toInt()
            )
        }
    }
}