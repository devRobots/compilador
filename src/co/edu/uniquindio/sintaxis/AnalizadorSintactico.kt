package co.edu.uniquindio.sintaxis

import co.edu.uniquindio.lexico.Categoria
import co.edu.uniquindio.lexico.Token
import java.util.*
import kotlin.collections.ArrayList

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
    private val arbolSintactico = TreeSet<Token>()
    private val listaErrores = ArrayList<ErrorSintactico>()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var posicionActual = 0
    private var tokenActual = tokens[0]

    /**
     * Funcion principal
     *
     * Analiza la lista de tokens
     *
     * @param listaTokens La lista de tokens que se analizara
     *
     * @return arbol El arbol sintactico
     * @return errores La rutina de errores
     */
    fun analizar() {

    }

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
        val listaFunciones: ArrayList<Funcion> = esListaFunciones()
        return if (listaFunciones.size > 0) {
            UnidadCompilacion(listaFunciones)
        } else null
    }

    /**
     * Metodo que revisa si es una lista de funciones
     */
    fun esListaFunciones(): ArrayList<Funcion> {
        var lista: ArrayList<Funcion> = ArrayList<Funcion>()
        var f: Funcion? = esFuncion()
        while (f != null) {
            lista.add(f)
            f = esFuncion()
        }
        return lista
    }

    /**
     * Metodo que revisa si el token actual es una
     * una funcion
     */
    fun esFuncion(): Funcion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA &&
                tokenActual.lexema == "def") {
            siguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val nombre = tokenActual
                siguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    siguienteToken()
                    val parametros: ArrayList<Parametro> = esListaParametros()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        siguienteToken()
                        var tipoRetorno: Token? = null
                        if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                            siguienteToken()
                            tipoRetorno = esTipoRetorno()
                            siguienteToken()
                            if (tipoRetorno == null) {
                                reportarError("Falta el tipo de retorno de la función")
                            }
                        }
                        val bloqueSentencias: ArrayList<Sentencia> = esBloqueSentencias()
                        if (bloqueSentencias != null) {
                            return Funcion(nombre, parametros, tipoRetorno, bloqueSentencias)
                        } else {
                            reportarError("Faltó el bloque de sentencias en la función")
                        }
                    } else {
                        reportarError("Falta paréntesis derecho")
                    }
                } else {
                    reportarError("Falta paréntesis izquierdo")
                }
            } else {
                reportarError("Falta el nombre de la función")
            }
        }
        return null
    }
}