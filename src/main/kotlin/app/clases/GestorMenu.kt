package app.clases

/**
 * Clase encargada de gestionar el flujo de menús y opciones de la aplicación,
 * mostrando las acciones disponibles según el perfil del usuario autenticado.
 *
 * @property nombreUsuario Nombre del usuario que ha iniciado sesión.
 * @property perfilUsuario Perfil del usuario: admin, gestion o consulta.
 * @property ui Interfaz de usuario.
 * @property gestorUsuarios Servicio de operaciones sobre usuarios.
 * @property gestorSeguros Servicio de operaciones sobre seguros.
 */

import model.enumerados.Auto
import model.enumerados.Cobertura
import model.enumerados.Perfil
import model.enumerados.Riesgo
import service.interfaces.IServSeguros
import service.interfaces.IServUsuarios
import ui.intefaces.IEntradaSalida
import java.time.LocalDate

class GestorMenu(
    private var nombreUsuario: String,
    private var perfilUsuario: Perfil,
    private val ui: IEntradaSalida,
    private val gestorUsuarios: IServUsuarios,
    private val gestorSeguros: IServSeguros)
{

    /**
     * Inicia un menú según el índice correspondiente al perfil actual.
     *
     * @param indice Índice del menú que se desea mostrar (0 = principal).
     */
    fun iniciarMenu(indice: Int = 0) {
        val (opciones, acciones) = ConfiguracionesApp.obtenerMenuYAcciones(perfilUsuario.toString(), indice)
        ejecutarMenu(opciones, acciones)
    }

    /**
     * Formatea el menú en forma numerada.
     */
    private fun formatearMenu(opciones: List<String>): String {
        var cadena = ""
        opciones.forEachIndexed { index, opcion ->
            cadena += "${index + 1}. $opcion\n"
        }
        return cadena
    }

    /**
     * Muestra el menú limpiando pantalla y mostrando las opciones numeradas.
     */
    private fun mostrarMenu(opciones: List<String>) {
        ui.limpiarPantalla()
        ui.mostrar(formatearMenu(opciones), salto = false)
    }

    /**
     * Ejecuta el menú interactivo.
     *
     * @param opciones Lista de opciones que se mostrarán al usuario.
     * @param ejecutar Mapa de funciones por número de opción.
     */
    private fun ejecutarMenu(opciones: List<String>, ejecutar: Map<Int, (GestorMenu) -> Boolean>) {
        do {
            mostrarMenu(opciones)
            val opcion = ui.pedirInfo("Elige opción > ").toIntOrNull()
            if (opcion != null && opcion in 1..opciones.size) {
                // Buscar en el mapa las acciones a ejecutar en la opción de menú seleccionada
                val accion = ejecutar[opcion]
                // Si la accion ejecutada del menú retorna true, debe salir del menú
                if (accion != null && accion(this)) return
            }
            else {
                ui.mostrarError("Opción no válida!")
            }
        } while (true)
    }

    /** Crea un nuevo usuario solicitando los datos necesarios al usuario */
    fun nuevoUsuario() {
        nombreUsuario = ui.pedirInfo("Introduce tu nombre")
        var clave = ui.pedirInfo("Introduce tu contraseña")

        gestorUsuarios.agregarUsuario(nombreUsuario,clave,Perfil.CONSULTA)
    }

    /** Elimina un usuario si existe */
    fun eliminarUsuario() {
        if(gestorUsuarios.buscarUsuario(nombreUsuario) != null){
            gestorUsuarios.eliminarUsuario(nombreUsuario)
        } else {
            ui.mostrarError("No se ha encontrado al usuario para borrar ! ! !")
        }

    }

    /** Cambia la contraseña del usuario actual */
    fun cambiarClaveUsuario() {
        var usuario = gestorUsuarios.buscarUsuario(nombreUsuario)

        var nuevaContrasenia = ui.pedirInfo("Introduce la nueva contraseña")
        usuario?.cambiarClave(nuevaContrasenia)

    }

    /**
     * Mostrar la lista de usuarios (Todos o filstrados por un perfil)
     */
    fun consultarUsuarios() {
        var buscarPerfil = ui.pedirInfo("¿Que tipo de usuario deseas buscar?\n(admin,gestion,otro)")

        for(usuario in gestorUsuarios.consultarPorPerfil(Perfil.getPerfil(buscarPerfil))){
            ui.mostrar(usuario.toString())
        }
    }

    /**
     * Solicita al usuario un DNI y verifica que tenga el formato correcto: 8 dígitos seguidos de una letra.
     *
     * @return El DNI introducido en mayúsculas.
     */
    private fun pedirDni(): String {
        val dni = ui.pedirInfo(
            "Introduce el DNI (8 dígitos seguidos de una letra):",
            "Formato incorrecto. Debe tener 9 caracteres: 8 dígitos y 1 letra al final."
        ) {
            it.length == 9 && it.substring(0, 8).all { it.isDigit() } && it.last().isLetter()
        }
        return dni.toUpperCase()
    }


    /**
     * Solicita al usuario un importe positivo, usado para los seguros.
     *
     * @return El valor introducido como `Double` si es válido.
     */
    private fun pedirImporte() : Double {
        var importe: Double? = null

        do {
            val input = ui.pedirInfo("Introduce un importe positivo:")
            try {
                val valor = input.toDouble()
                if (valor > 0) {
                    importe = valor
                } else {
                    ui.mostrarError("El importe debe ser mayor a 0.")
                }
            } catch (e: NumberFormatException) {
                ui.mostrarError("Valor numérico no valido.")
            }
        } while (importe == null)
        return importe
    }

    /** Crea un nuevo seguro de hogar solicitando los datos al usuario */
    fun contratarSeguroHogar() {
        val domicilio = ui.pedirInfo("Introduce la dirección del hogar:")
        val importe = pedirImporte()
        val metrosCuadrados = ui.pedirEntero("Introduce los metros cuadrados:","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        val valorContenido = ui.pedirDouble("Introduce el valor del contenido:",
            "Introduce un numero valido","No se puede convertir", { it <= 0 } )
        val anioConstruccion = ui.pedirEntero("Introduce el año de construccion:","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        if (gestorSeguros.contratarSeguroHogar(pedirDni(), importe, metrosCuadrados, valorContenido, domicilio, anioConstruccion)) {
            ui.mostrar("Seguro de hogar contratado.")
        } else {
            ui.mostrarError("Error al contratar seguro de hogar.")
        }
    }


    /** Crea un nuevo seguro de auto solicitando los datos al usuario */
    fun contratarSeguroAuto() {
        val descripcion = ui.pedirInfo("Introduce la descripción del auto:")
        val combustible = ui.pedirEntero("Introduce el combustible:","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        val tipoAuto = ui.pedirInfo("Introduce el tipo de auto (coche, moto, camion):")
        val Auto = Auto.getAuto(tipoAuto)
        val coberturaTipo = ui.pedirInfo("Introduce la cobertura (terceros, terceros_ampliado, franquicia_200, franquicia_300, franquicia_400, franquicia_500, todo_riesgo):")
        val cobertura = Cobertura.getCobertura(coberturaTipo)
        val asistenciaCarretera = ui.preguntar("¿Desea asistencia en carretera? (s/n)")
        val numPartes = ui.pedirEntero("Introduce el numero de partes:","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        val importe = pedirImporte()
        if (gestorSeguros.contratarSeguroAuto(pedirDni(), importe, descripcion, combustible, Auto, cobertura, asistenciaCarretera, numPartes)) {
            ui.mostrar("Seguro de auto contratado.")
        } else {
            ui.mostrarError("Error al contratar seguro de auto.")
        }
    }

    /** Crea un nuevo seguro de vida solicitando los datos al usuario */
    fun contratarSeguroVida() {
        val fechaNacimientoInput = ui.pedirInfo("Introduce tu fecha de nacimiento (YYYY-MM-DD):")
        val fechaNacimiento = LocalDate.parse(fechaNacimientoInput)
        val nivelRiesgoCadena = ui.pedirDouble("Introduce el nivel de riesgo\n(alto 10.0/medio 5.0/ bajo 0.0) :","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        val nivelRiesgo = Riesgo.getRiesgo(nivelRiesgoCadena)
        val indemnizacion = ui.pedirDouble("Introduce la indemnización:","Introduce un numero valido",
            "No se puede convertir", { it <= 0 } )
        val importe = pedirImporte()
        if (gestorSeguros.contratarSeguroVida(pedirDni(), importe, fechaNacimiento, nivelRiesgo, indemnizacion)) {
            ui.mostrar("Seguro de vida contratado.")
        } else {
            ui.mostrarError("Error al contratar seguro de vida.")
        }
    }


    /** Elimina un seguro si existe por su número de póliza */
    fun eliminarSeguro() {
        val numPoliza = ui.pedirInfo("Introduce el numero de poliza del seguro a eliminar:").toIntOrNull()
        if (numPoliza != null) {
            if (gestorSeguros.eliminarSeguro(numPoliza)) {
                ui.mostrar("Seguro eliminado exitosamente.")
            } else {
                ui.mostrarError("No se encontro un seguro con ese numero de poliza.")
            }
        } else {
            ui.mostrarError("Numero de poliza inválido.")
        }
    }

    /** Muestra todos los seguros existentes */
    fun consultarSeguros() {
        val seguros = gestorSeguros.consultarTodos()
        if (seguros.isEmpty()) {
            ui.mostrar("No hay seguros registrados.")
        } else {
            seguros.forEach { seguro ->
                ui.mostrar(seguro.toString())
            }
        }
    }

    /** Muestra todos los seguros de tipo hogar */
    fun consultarSegurosHogar() {
        val seguros = gestorSeguros.consultarPorTipo("SeguroHogar")
        if (seguros.isEmpty()) {
            ui.mostrar("No hay seguros de hogar registrados.")
        } else {
            seguros.forEach { seguro ->
                ui.mostrar(seguro.toString())
            }
        }
    }

    /** Muestra todos los seguros de tipo auto */
    fun consultarSegurosAuto() {
        val seguros = gestorSeguros.consultarPorTipo("SeguroAuto")
        if (seguros.isEmpty()) {
            ui.mostrar("No hay seguros de auto registrados.")
        } else {
            seguros.forEach { seguro ->
                ui.mostrar(seguro.toString())
            }
        }
    }

    /** Muestra todos los seguros de tipo vida */
    fun consultarSegurosVida() {
        val seguros = gestorSeguros.consultarPorTipo("SeguroVida")
        if (seguros.isEmpty()) {
            ui.mostrar("No hay seguros de vida registrados.")
        } else {
            seguros.forEach { seguro ->
                ui.mostrar(seguro.toString())
            }
        }
    }

}