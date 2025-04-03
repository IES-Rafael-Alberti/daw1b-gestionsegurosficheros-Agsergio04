package model.enumerados

enum class Cobertura(val desc : String) {
    TERCEROS("Terceros"),
    TERCEROS_AMPLIADO("Terceros +"),
    FRANQUICIA_200("Todo Riesgo con Franquicia de 200€"),
    FRANQUICIA_300("Todo Riesgo con Franquicia de 300€"),
    FRANQUICIA_400("Todo Riesgo con Franquicia de 400€"),
    FRANQUICIA_500("Todo Riesgo con Franquicia de 500€"),
    TODO_RIESGO("Todo Riesgo");

    companion object{
        fun getCobertura(valor : String) : Cobertura {
            return when(valor.lowercase() ){
                "terceros + " -> Cobertura.TERCEROS_AMPLIADO
                "todo riesgo con franquicia de 200€" -> Cobertura.FRANQUICIA_200
                "todo riesgo con franquicia de 300€" -> Cobertura.FRANQUICIA_300
                "todo riesgo con franquicia de 400€" -> Cobertura.FRANQUICIA_400
                "todo riesgo con franquicia de 500€" -> Cobertura.FRANQUICIA_500
                "todo riesgo" -> Cobertura.TODO_RIESGO
                else -> Cobertura.TERCEROS
            }
        }
    }
}