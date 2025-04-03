package model.enumerados

enum class Auto {
    COCHE, MOTO, CAMION;

    companion object{
        fun getAuto(valor : String) : Auto {
            return when(valor.lowercase() ){
                "camion" -> Auto.CAMION
                "moto" -> Auto.MOTO
                else -> Auto.COCHE
            }
        }
    }
}