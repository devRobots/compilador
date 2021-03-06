package co.edu.uniquindio.sintaxis.bnf.sentencia

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
 * Sentencia
 */
abstract class Sentencia(nombre: String) : Sintaxis(nombre) {
    open fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        return
    }

    open fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>, ambito: Ambito) {
        return
    }

}