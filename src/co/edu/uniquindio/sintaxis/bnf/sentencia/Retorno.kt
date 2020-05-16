package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Retorno(private val expresion: Expresion) : Sentencia() {
    init {
        nombre = "Retorno"
        estructura = "devolver ${expresion.estructura} !"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expresion.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Expresion de retorno: ", 0)
        agregarValor(expresion.estructura, 0)

        return panel
    }
}