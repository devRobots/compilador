package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion
 */
abstract class Expresion(nombre: String) : Sintaxis(nombre) {
    abstract fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito)
    abstract fun obtenerTipo(): String
}
