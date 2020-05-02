package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ValorLogico(private val identificador: Token?, private val expresionRelacional: ExpresionRelacional?) : Valor() {

    init {
        nombre = "Valor Logico"
        estructura = " ${expresionRelacional}$identificador"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (expresionRelacional != null) {
            treeItem.children.add(expresionRelacional?.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo(nombre, 0)
        agregarValor(estructura, 0)
        return panel
    }
}