package model.seguros

import model.interfaces.IExportable

abstract class Seguro(
    val numPoliza : Int,
    private var dniTitular : String,
    protected var importe : Double

) : IExportable {
     open fun calcularImporteAnioSiguiente(interes: Double): Double{
        return importe * interes
    }

    fun tipoSeguro() : String{
        return this::class.simpleName?: "Desconocido"
    }

    override fun serializar(separador: String): String {
        return "$numPoliza$separador$dniTitular$separador$importe"
    }

    override fun toString(): String {
        return "Seguro(numPoliza=$numPoliza, dniTitular=$dniTitular, importe=${"%.2f".format(importe)})"
    }

    override fun equals(other: Any?): Boolean {
        if(this == other) return true
        if(other !is Seguro) return false
        return numPoliza == other.numPoliza
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}