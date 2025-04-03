package data.clases

import data.interfaces.IRepoUsuarios
import model.enumerados.Perfil
import model.usuarios.Usuario

open class RepoUsuariosMem : IRepoUsuarios {

    protected val usuarios = mutableListOf<Usuario>()

    override fun agregar(usuario: Usuario): Boolean {
        var condicion = false

        if(buscar(usuario.nombre) == null){
            usuarios.add(usuario)
            condicion = true
        }

        return condicion
    }

    override fun buscar(nombreUsuario: String): Usuario? {
        return usuarios.find{ it.nombre == nombreUsuario }
    }

    override fun eliminar(usuario: Usuario): Boolean {
        return usuarios.remove(usuario)
    }

    override fun eliminar(nombreUsuario: String): Boolean {
        var persona = buscar(nombreUsuario)
        var condicion = false

        if(persona != null){
            eliminar(persona)
            condicion = true
        }

        return condicion
    }

    override fun obtenerTodos(): List<Usuario> {
        return usuarios
    }

    override fun obtener(perfil: Perfil): List<Usuario> {
        return usuarios.filter { it.perfil == perfil }
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        var usuarioClaveAntiguo = buscar(usuario.nombre)
        var condicion = false

        if(usuarioClaveAntiguo != null ){
            usuarioClaveAntiguo.cambiarClave(nuevaClave)
            condicion = true
        }

        return condicion
    }
}