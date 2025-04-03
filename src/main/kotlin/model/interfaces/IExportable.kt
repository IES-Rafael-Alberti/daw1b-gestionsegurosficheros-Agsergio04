package model.interfaces

interface IExportable {
    fun serializar(separador : String = ";") : String
}