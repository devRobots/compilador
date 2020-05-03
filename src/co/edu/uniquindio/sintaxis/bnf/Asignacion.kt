package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Asignacion(private val identificador: Token, private val expresion: Expresion): Sentencia() {
    init {
        nombre = "Asignacion"
        estructura = "${identificador.lexema} = ${expresion.estructura}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expresion.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Identificador", 0)
        agregarValor(identificador.lexema, 0)

        agregarAtributo("Expresion", 1)
        agregarValor(expresion.nombre, 1)

        return panel
    }
}
