package model.enumerados

enum class Riesgo(val interesAplicado : Double) {
    BAJO(2.0), MEDIO(5.0), ALTO(10.0);

    companion object{
        fun getRiesgo(valor: Double) : Riesgo {
            return when(valor){
                5.0-> Riesgo.MEDIO
                10.0 -> Riesgo.ALTO
                else -> Riesgo.BAJO
            }
        }
    }
}