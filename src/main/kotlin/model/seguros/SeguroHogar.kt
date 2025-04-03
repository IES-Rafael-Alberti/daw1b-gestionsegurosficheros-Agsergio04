package model.seguros

class SeguroHogar  : Seguro {
    var metrosCuadrados : Int
    var valorContenido : Double
    var direccion : String
    var anioConstruccion : Int


    constructor(dniTitular: String, importe: Double, metrosCuadrados: Int, valorContenido: Double, direccion: String, anioConstruccion: Int) :
            super(incremetarId(), dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.anioConstruccion = anioConstruccion
    }

    private constructor(numPoliza: Int, dniTitular: String, importe: Double, metrosCuadrados: Int, valorContenido: Double, direccion: String, anioConstruccion: Int) :
            super(numPoliza, dniTitular, importe) {
        this.metrosCuadrados = metrosCuadrados
        this.valorContenido = valorContenido
        this.direccion = direccion
        this.anioConstruccion = anioConstruccion
    }



    override fun calcularImporteAnioSiguiente(interes: Double): Double {
        return importe + ((interes / CICLO_ANIOS_INCREMENTO) * PORCENTAJE_INCREMENTO_ANIOS)
    }

    override fun serializar(separador: String): String {
        return super.serializar(separador) +
                "$separador$metrosCuadrados" +
                "$separador$valorContenido$separador$direccion" +
                "$separador$anioConstruccion$separador${tipoSeguro()}"
    }

    override fun toString(): String {
        return super.toString() + "metrosCuadrados = $metrosCuadrados,valorContenido = $valorContenido,direccion = $direccion" +
                "anioConstruccion = $anioConstruccion"
    }



    companion object{
        var numPolizaHogar = 99999

        private val PORCENTAJE_INCREMENTO_ANIOS = 0.02
        private val CICLO_ANIOS_INCREMENTO = 5

        fun incremetarId() : Int{

            return if(numPolizaHogar < 399998){
                numPolizaHogar + 1
            } else {
                100000
            }
        }


        fun crearSeguro(datos: List<String>): SeguroHogar{
            return SeguroHogar(
                numPoliza = datos[0].toInt(),
                dniTitular = datos[1],
                importe = datos[2].toDouble(),
                metrosCuadrados = datos[3].toInt(),
                valorContenido = datos[4].toDouble(),
                direccion = datos[5],
                anioConstruccion = datos[6].toInt(),//Parsear fecha

            )
        }
    }
}