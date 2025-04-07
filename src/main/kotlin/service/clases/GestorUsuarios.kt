package service.clases

import data.interfaces.IRepoSeguros
import data.interfaces.IRepoUsuarios
import model.enumerados.Perfil
import model.usuarios.Usuario
import service.interfaces.IServUsuarios
import utils.interfaces.IUtilSeguridad

class GestorUsuarios(
    private val repoUsuarios: IRepoUsuarios,
    private val seguridad : IUtilSeguridad
) : IServUsuarios {

    override fun iniciarSesion(nombre: String, clave: String): Perfil? {
        val usuario = buscarUsuario(nombre)

        return if(usuario != null && seguridad.verificarClave(clave,usuario.clave)){
            usuario.perfil
        } else {
            null
        }
    }

    override fun agregarUsuario(
        nombre: String,
        clave: String,
        perfil: Perfil

    ): Boolean {
        var condicion = false

        if(buscarUsuario(nombre) == null){
            condicion = true
            repoUsuarios.agregar(Usuario(nombre,seguridad.encriptarClave(clave),perfil))
        }

        return condicion
    }

    override fun eliminarUsuario(nombre: String): Boolean {
        return repoUsuarios.eliminar(nombre)
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        var condicion = false

        if(buscarUsuario(usuario.nombre) != null){
            condicion = true
            val usuarioBuscado = buscarUsuario(usuario.nombre)
            usuarioBuscado?.cambiarClave(seguridad.encriptarClave(nuevaClave))
        }
        return condicion

        //repoUsuarios.cambiarClave(Usuario,seguridad.encriptarClave(nuevaClave))
    }

    override fun buscarUsuario(nombre: String): Usuario? {
        return repoUsuarios.buscar(nombre)
    }

    override fun consultarTodos(): List<Usuario> {
        return repoUsuarios.obtenerTodos()
    }

    override fun consultarPorPerfil(perfil: Perfil): List<Usuario> {
        val listaUsuariosMismoPerfil = mutableListOf<Usuario>()

        for(usuario in consultarTodos()){
            if(usuario.perfil == perfil) listaUsuariosMismoPerfil.add(usuario)
        }

        return listaUsuariosMismoPerfil
    }

}