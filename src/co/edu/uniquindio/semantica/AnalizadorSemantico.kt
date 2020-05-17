package co.edu.uniquindio.semantica

import co.edu.uniquindio.sintaxis.bnf.UnidadCompilacion

class AnalizadorSemantico(private var uc: UnidadCompilacion) {

    var erroresSemanticos: ArrayList<ErrorSemantico> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(erroresSemanticos)

    fun llenarTablaSimbolos() {
        uc.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos)
    }

    fun analizarSemantica() {
        uc.analizarSemantica(tablaSimbolos, erroresSemanticos)
    }
}