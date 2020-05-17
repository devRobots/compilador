package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Importacion(private val importacion: Token?) : Sintaxis() {
    init {
        this.nombre = "Importacion"
        this.estructura = "meter ${importacion?.lexema}!"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre de la importacion", 0)
        agregarValor(importacion?.lexema, 0)

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos) {
        tablaSimbolos.agregarImportacion(importacion!!.lexema, importacion.fila, importacion.columna)
    }
}