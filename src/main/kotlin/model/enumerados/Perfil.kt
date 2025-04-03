package model.enumerados

enum class Perfil {
    ADMIN,GESTION,CONSULTA;

    companion object{
        fun getPerfil(valor : String) : Perfil {
            return when(valor.lowercase() ){
                "admin" -> Perfil.ADMIN
                "gestion" -> Perfil.GESTION
                else -> Perfil.CONSULTA
            }
        }
    }
}