package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Paquete(private val paquete: Token?) : Sintaxis() {
    init {
        this.nombre = "Paquete"
        this.estructura = "caja ${paquete?.lexema}!"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre del paquete", 0)
        agregarValor(paquete?.lexema, 0)

        return panel
    }
}