package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSiNo(private val sentenciaSi : SentenciaSi?, private val sentenciaSiNo: SentenciaSiNo?) : SentenciaCondicional() {
    init {
        nombre = "Sentencia Si-No"
        estructura = "wi [ ... ] ¿ ... ? wo wi [ ... ] ¿ ... ? ... wo ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(sentenciaSi?.getTreeItem())
        treeItem.children.add(sentenciaSiNo?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Condición si", 0)
        agregarValor(sentenciaSi.toString(), 0)

        agregarAtributo("Sentencia Sino", 1)
        agregarValor(sentenciaSiNo.toString(), 1)

        return panel
    }
}