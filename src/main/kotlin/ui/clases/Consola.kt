package ui.clases

import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder
import ui.intefaces.IEntradaSalida

class Consola: IEntradaSalida {
    override fun mostrar(msj: String, salto: Boolean, pausa: Boolean) {
        println("$msj${if (salto) "\n" else ""} ")
        if(pausa) pausar()
    }

    override fun mostrarError(msj: String, pausa: Boolean) {
        val mensaje = if(msj.startsWith("ERROR - ")) "ERROR - $msj" else msj
        mostrar(mensaje,pausa = pausa)
    }

    override fun pedirInfo(msj: String): String {
        if(msj.trim() != ""){
            mostrar(msj,false)
        }
        return readln().trim()
    }

    override fun pedirInfo(
        msj: String,
        error: String,
        debeCumplir: (String) -> Boolean
    ): String {
        val mensaje = pedirInfo(msj)
        require(debeCumplir(mensaje)){ error }
        return mensaje
    }

    override fun pedirDouble(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Double) -> Boolean
    ): Double {
        var valor : Double? = null

        do {
            try {
                val input = pedirInfo(prompt).replace(',', '.')
                if (debeCumplir(input.toDouble())){
                    mostrarError(error)
                } else {
                    valor = input.toDouble()
                }

            } catch (e: NumberFormatException) {
                mostrarError(errorConv)
            }
        } while(valor == null)

        return valor
    }

    override fun pedirEntero(
        prompt: String,
        error: String,
        errorConv: String,
        debeCumplir: (Int) -> Boolean
    ): Int {
        var valor : Int? = null

        do {
            try {
                val input = pedirInfo(prompt).replace(',', '.')
                if (debeCumplir(input.toInt())){
                    mostrarError(error)
                } else {
                    valor = input.toInt()
                }

            } catch (e: NumberFormatException) {
                mostrarError(errorConv)
            }
        } while(valor == null)

        return valor
    }

    override fun pedirInfoOculta(prompt: String): String {
        return try {
            val terminal = TerminalBuilder.builder()
                .dumb(true) // Para entornos no interactivos como IDEs
                .build()

            val reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build()

            reader.readLine(prompt, '*') // Oculta la contraseña con '*'
        } catch (e: UserInterruptException) {
            mostrarError("Entrada cancelada por el usuario (Ctrl + C).", pausa = false)
            ""
        } catch (e: EndOfFileException) {
            mostrarError("Se alcanzó el final del archivo (EOF ó Ctrl+D).", pausa = false)
            ""
        } catch (e: Exception) {
            mostrarError("Problema al leer la contraseña: ${e.message}", pausa = false)
            ""
        }
    }


    override fun pausar(msj: String) {
        pedirInfo("Presiona ENTER para continuar ")
    }

    override fun limpiarPantalla(numSaltos: Int) {
        if (System.console() != null) {
            mostrar("\u001b[H\u001b[2J", false)
            System.out.flush()
        } else {
            repeat(numSaltos) {
                mostrar("")
            }
        }
    }

    override fun preguntar(mensaje: String): Boolean {
        var respuesta : String? = null
        do{
            respuesta = pedirInfo(mensaje + "(s/n)").lowercase()
            if(respuesta != null){
                mostrarError("Respuesta incorrecta")
            }

        }while(respuesta.toString() !in listOf("n","s"))

        return respuesta == "s"
    }

    fun preguntar2(mensaje: String): Boolean {
        var respuesta : String? = null
        do{
            respuesta = try{
                pedirInfo("$mensaje + (s/n):","Respuesta Incorrecta. Intentalo de nuevo..."){
                    it.lowercase() in listOf<String>("s","n")
                }


            }catch (e : IllegalArgumentException){
                mostrarError(e.message.toString())
                ""
            }

        } while(respuesta.isEmpty())

        return respuesta == "s"
    }

    /*fun preguntar3(mensaje: String): Boolean {
        var respuesta: String?

        do {
            respuesta = try {

                pedirInfo("$mensaje (s/n):", "Respuesta incorrecta. Inténtalo de nuevo...", ::validarSiNo)
            } catch (e: IllegalArgumentException) {
                mostrarError(e.message.toString())
                null
            }
        } while (respuesta.isNullOrEmpty())

        return respuesta.lowercase() == "s"
    }

    fun pausa(msj : String){

    }*/
}