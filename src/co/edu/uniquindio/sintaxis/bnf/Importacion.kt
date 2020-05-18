package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Importacion
 */
class Importacion(private val importacion: Token?) : Sintaxis("Importacion") {

    override fun toString(): String {
        return "meter ${importacion?.lexema}!"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre de la importacion")
        agregarValor(importacion?.lexema)

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos) {
        tablaSimbolos.agregarImportacion(importacion!!.lexema, importacion.fila, importacion.columna)
    }
}