package data.clases

import data.interfaces.IRepoSeguros
import model.seguros.Seguro
import model.usuarios.Usuario

open class RepoSegurosMem : IRepoSeguros {

    protected val seguros = mutableListOf<Seguro>()

    override fun agregar(seguro: Seguro): Boolean {
        var condicion = false

        if(buscar(seguro.numPoliza) == null){
            seguros.add(seguro)
            condicion = true
        }

        return condicion
    }

    override fun buscar(numPoliza: Int): Seguro? {
        return seguros.find{ it.numPoliza == numPoliza }
    }

    override fun eliminar(seguro: Seguro): Boolean {
        return seguros.remove(seguro)
    }

    override fun eliminar(numPoliza: Int): Boolean {
        var seguro = buscar(numPoliza)
        var condicion = false

        if(seguro != null){
            eliminar(seguro)
            condicion = true
        }

        return condicion
    }

    override fun obtenerTodos(): List<Seguro> {
        return seguros
    }

    override fun obtener(tipoSeguro: String): List<Seguro> {
        return seguros.filter { it.tipoSeguro() == tipoSeguro }
    }
}