package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class CuerpoClase() : Sintaxis() {
    init {
        this.nombre = "Cuerpo de Clase"
        this.estructura = ""
    }

    override fun toString(): String {
        return "$nombre: $estructura"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        TODO("Not yet implemented")
    }
}
