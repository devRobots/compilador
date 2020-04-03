package co.edu.uniquindio.sintaxis

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
    private val listaErrores = ArrayList<String>()

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
}