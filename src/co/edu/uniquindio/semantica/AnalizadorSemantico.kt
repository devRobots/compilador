package co.edu.uniquindio.semantica

import co.edu.uniquindio.sintaxis.bnf.UnidadCompilacion

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 3.0
 *
 * Analizador Semantico
 */
class AnalizadorSemantico(private var uc: UnidadCompilacion) {
    /**
     * Tabla de Simbolos y errores generados por el analizador semantico
     */
    var erroresSemanticos: ArrayList<ErrorSemantico> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(erroresSemanticos)

    /**
     * Metodo que realiza el analisis semantico
     */
    fun analizar() {
        llenarTablaSimbolos()
        analizarSemantica()
    }

    /**
     * Metodo que llena la tabla de simbolos
     */
    private fun llenarTablaSimbolos() {
        uc.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos)
    }

    /**
     * Metodo que realiza el analisis en base a la tabla previamente armada
     */
    private fun analizarSemantica() {
        uc.analizarSemantica(tablaSimbolos, erroresSemanticos)
    }
}