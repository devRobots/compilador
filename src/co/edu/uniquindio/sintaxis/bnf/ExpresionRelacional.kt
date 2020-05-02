package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionRelacional(private val izquierda: ExpresionAritmetica?, private val derecho: ExpresionAritmetica?) : Expresion() {

    init {
        nombre = "Expresion Relacional"
        estructura = " @a << [*10 / #2] "
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(izquierda?.getTreeItem())

        treeItem.children.add(derecho?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda", 0)
        agregarValor(izquierda.toString(), 0)

        agregarAtributo("Derecha", 1)
        agregarValor(derecho.toString(), 1)
        return panel
    }
}