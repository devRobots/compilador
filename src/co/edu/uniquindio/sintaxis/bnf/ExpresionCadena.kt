package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionCadena(private val concatenar: Token?, private val valor: Expresion?) : Expresion() {

    init {
        nombre = "Expresion Cadena"
        estructura = " $concatenar [+ $valor]"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)
        treeItem.children.add(valor?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("cadena", 0)
        agregarValor(estructura, 0)
        return panel
    }
}