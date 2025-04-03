package app.clases

/**
 * Clase encargada de cargar los datos iniciales de usuarios y seguros desde ficheros,
 * necesarios para el funcionamiento del sistema en modo persistente.
 *
 * @param ui Interfaz de entrada/salida para mostrar mensajes de error.
 * @param repoUsuarios Repositorio que permite cargar usuarios desde un fichero.
 * @param repoSeguros Repositorio que permite cargar seguros desde un fichero.
 */

import data.interfaces.ICargarSegurosIniciales
import data.interfaces.ICargarUsuariosIniciales
import ui.intefaces.IEntradaSalida

class CargadorInicial(
    private val ui: IEntradaSalida,
    private val repoUsuarios: ICargarUsuariosIniciales,
    private val repoSeguros: ICargarSegurosIniciales
) {
    /**
     * Carga los usuarios desde el archivo configurado en el repositorio.
     * Muestra errores si ocurre un problema en la lectura o conversión de datos.
     */
    fun cargarUsuarios() {
        try {
            if (!repoUsuarios.cargarUsuarios()) {
                ui.mostrarError("No se encontraron usuarios iniciales")
            }
        } catch (e: Exception) {
            ui.mostrarError("Error crítico cargando usuarios: ${e.message}")
        }
    }

    /**
     * Carga los seguros desde el archivo configurado en el repositorio.
     * Utiliza el mapa de funciones de creación definido en la configuración de la aplicación
     * (ConfiguracionesApp.mapaCrearSeguros).
     * Muestra errores si ocurre un problema en la lectura o conversión de datos.
     */
    fun cargarSeguros() {
        try {
            repoSeguros.cargarSeguros(ConfiguracionesApp.mapaCrearSeguros)
        } catch (e: Exception) {
            ui.mostrarError("Error crítico cargando seguros: ${e.message}")
        }
    }
}