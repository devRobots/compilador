package co.edu.uniquindio.sintaxis

import kotlin.collections.ArrayList

import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.lexico.Categoria

import co.edu.uniquindio.sintaxis.bnf.*

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Analizador Sintactico
 */
class AnalizadorSintactico(private val tokens: ArrayList<Token>) {
    /**
     * Arbol sintactico y lista de errores por el analizador sintactico
     */
    private val listaErrores = ArrayList<ErrorSintactico>()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var posicionActual = 0
    private var tokenActual = tokens[0]

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    fun siguienteToken(){
        posicionActual++;
        if( posicionActual < tokens.size ) {
            tokenActual = tokens[posicionActual]
        }
    }

    /**
     * Metodo que reporta un error y lo agrega
     * a la lista de errores
     */
    fun reportarError(mensaje: String) {
        var error = ErrorSintactico(mensaje)
        listaErrores.add(error)
    }

    /**
     * Metodo que avanza al siguiente token
     *
     * Configura la posicion actual
     * Verifica que no se desborde
     */
    fun esUnidadDeCompilacion(): UnidadCompilacion? {
        // Paquete
        val paquete: Paquete? = esPaquete()
        // Lista de importaciones
        val listaImportaciones: ArrayList<Importacion> = esListaImportaciones()
        // Clase
        val clase = esClase()

        return if (clase != null) {
            UnidadCompilacion(paquete, listaImportaciones, clase)
        }
        else {
            null
        }
    }

    private fun esPaquete(): Paquete? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "caja") {
            siguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val paquete = tokenActual
                siguienteToken()
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    siguienteToken()
                    return Paquete(paquete)
                }
            }
        }
        return null
    }

    private fun esListaImportaciones(): ArrayList<Importacion> {
        var lista: ArrayList<Importacion> = ArrayList<Importacion>()
        var importacion: Importacion? = esImportacion()
        while (importacion != null) {
            lista.add(importacion)
            importacion = esImportacion()
        }
        return lista
    }

    private fun esImportacion(): Importacion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == "meter") {
            siguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val importacion = tokenActual
                siguienteToken()
                if (tokenActual.categoria == Categoria.FIN_SENTENCIA) {
                    siguienteToken()
                    return Importacion(importacion)
                }
            }
        }
        return null
    }

    private fun esClase(): Clase? {
        return null
    }
}