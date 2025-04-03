package data.interfaces

import model.usuarios.Usuario

interface IUtilFichero {
    fun escribirArchivo(string: kotlin.String, usuarios: kotlin.collections.List<model.usuarios.Usuario>): kotlin.Boolean
    fun leerArchivo(string: String)
}