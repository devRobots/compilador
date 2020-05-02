package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionCadena(private val cadena: ExpresionCadena?, private val concatenar: Token?, private val valor: Expresion?) : Expresion() {

    init {
        nombre = "Expresion Cadena"
        estructura = " Cadena de Caracteres [+ Expresiones]"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(cadena?.getTreeItem())

        treeItem.children.add(valor?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda", 0)
        agregarValor(cadena.toString(), 0)

        agregarAtributo("Derecha", 1)
        agregarValor(valor.toString(), 1)
        return panel
    }
}