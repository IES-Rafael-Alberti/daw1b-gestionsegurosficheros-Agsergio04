package data.clases

import data.interfaces.ICargarUsuariosIniciales
import model.usuarios.Usuario
import utils.clases.Ficheros

class RepoUsuariosFich(
    private val rutaArchivo: String,
    private val fich: Ficheros
) : RepoUsuariosMem(), ICargarUsuariosIniciales {

    override fun agregar(usuario: Usuario): Boolean {
        if (buscar(usuario.nombre) != null) return false
        if (fich.agregarLinea(rutaArchivo, usuario.serializar())) {
            return super.agregar(usuario)
        }
        return false
    }

    override fun eliminar(usuario: Usuario): Boolean {
        val usuariosActualizados = obtenerTodos().filter { it != usuario }
        if (fich.escribirArchivo(rutaArchivo, usuariosActualizados)) {
            return super.eliminar(usuario)
        }
        return false
    }

    override fun cambiarClave(usuario: Usuario, nuevaClave: String): Boolean {
        if (super.cambiarClave(usuario, nuevaClave)) {
            val usuariosActualizados = obtenerTodos()
            return fich.escribirArchivo(rutaArchivo, usuariosActualizados)
        }
        return false
    }

    override fun cargarUsuarios(): Boolean {
        val lineas = fich.leerArchivo(rutaArchivo) ?: return false
        lineas.forEach { linea ->
            val datos = linea.split(";")
            val usuario = Usuario.crearUsuario(datos)
            super.agregar(usuario)
        }
        return true
    }
}
