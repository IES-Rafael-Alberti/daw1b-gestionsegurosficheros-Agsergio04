package org.example.app

import app.clases.ControlAcceso
import app.clases.GestorMenu
import data.clases.RepoSegurosFich
import data.clases.RepoSegurosMem
import data.clases.RepoUsuariosFich
import data.clases.RepoUsuariosMem
import model.enumerados.Perfil
import service.clases.GestorSeguros
import service.clases.GestorUsuarios
import ui.clases.Consola
import utils.clases.Ficheros
import utils.clases.Seguridad

fun main() {

    // Crear dos variables con las rutas de los archivos de texto donde se almacenan los usuarios y seguros.
    // Estos ficheros se usarán solo si el programa se ejecuta en modo de almacenamiento persistente.

    val RUTA_USUARIOS = "C:\\Users\\sergi\\Desktop\\Trabajo\\fp\\Primero\\Programacion\\kotlin\\GestionSeguros\\src\\main\\kotlin\\data\\datos"
    val RUTA_SEGUROS = "C:\\Users\\sergi\\Desktop\\Trabajo\\fp\\Primero\\Programacion\\kotlin\\GestionSeguros\\src\\main\\kotlin\\data\\datos"

    // Instanciamos los componentes base del sistema: la interfaz de usuario, el gestor de ficheros y el módulo de seguridad.
    // Estos objetos serán inyectados en los diferentes servicios y utilidades a lo largo del programa.

    val ui = Consola()
    val ficheros = Ficheros(ui)
    val seguridad = Seguridad()


    // Limpiamos la pantalla antes de comenzar, para que la interfaz esté despejada al usuario.


    ui.limpiarPantalla()

    // Preguntamos al usuario si desea iniciar en modo simulación.
    // En modo simulación los datos no se guardarán en archivos, solo estarán en memoria durante la ejecución.

    val modo = ui.preguntar("¿Deseas guardan en memoria?")

    // Declaramos los repositorios de usuarios y seguros.
    // Se asignarán más abajo dependiendo del modo elegido por el usuario.

    val repoUsuarios : Any?
    val repoSeguros : Any?

    if(modo){
        repoSeguros = RepoSegurosMem()
        repoUsuarios = RepoUsuariosMem()
    } else {
        repoSeguros = RepoSegurosFich(RUTA_SEGUROS,ficheros)
        repoUsuarios = RepoUsuariosFich(RUTA_USUARIOS,ficheros)
    }
    // Si se ha elegido modo simulación, se usan repositorios en memoria.
    // Si se ha elegido almacenamiento persistente, se instancian los repositorios que usan ficheros.
    // Además, creamos una instancia del cargador inicial de información y lanzamos la carga desde los ficheros.


    // Se crean los servicios de lógica de negocio, inyectando los repositorios y el componente de seguridad.

    val gestorUsuarios = GestorUsuarios(repoUsuarios, seguridad)
    val gestorSeguros = GestorSeguros(repoSeguros)



    // Se inicia el proceso de autenticación. Se comprueba si hay usuarios en el sistema y se pide login.
    // Si no hay usuarios, se permite crear un usuario ADMIN inicial.
    val controlAcceso = ControlAcceso(RUTA_USUARIOS, gestorUsuarios, ui, ficheros)
    val credenciales = controlAcceso.autenticar()

    if (credenciales == null) {
        ui.mostrar("Acceso cancelado. Saliendo del sistema.")
    }

    val nombreUsuario = credenciales?.first

    // Si el login fue exitoso (no es null), se inicia el menú correspondiente al perfil del usuario autenticado.
    // Se lanza el menú principal, **iniciarMenu(0)**, pasándole toda la información necesaria.

    val gestorMenu = GestorMenu(nombreUsuario.toString(), Perfil.ADMIN, ui, gestorUsuarios, gestorSeguros)
    gestorMenu.iniciarMenu(0)



}