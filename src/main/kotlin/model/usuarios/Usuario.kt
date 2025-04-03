package model.usuarios

import model.enumerados.Perfil
import model.enumerados.Perfil.Companion.getPerfil
import model.interfaces.IExportable

class Usuario(
    val nombre : String,
    clave : String,
    var perfil : Perfil
) : IExportable {

    var clave : String = ""
        private set


    fun cambiarClave(nuevaClaveEncriptada : String){
        clave = nuevaClaveEncriptada
    }

    override fun serializar(separador: String): String {
        return "$nombre$separador$clave$separador$perfil"
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun equals(other: Any?): Boolean {
        if(this == other) return true
        if(other !is Usuario) return false
        return clave == other.clave

    }

    companion object{
        fun crearUsuario(datos : List<String>) : Usuario{
            return Usuario(
                nombre = datos[0],
                clave = datos[1],
                perfil = getPerfil(datos[2])
            )
        }
    }
}