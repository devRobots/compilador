package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

open class SentenciaCondicional(private val sentenciaSi : SentenciaSi, private val sentenciaSino : SentenciaSiNo?) : Sentencia(){
    init {
        nombre = "sentencia condicional"
        estructura = " wi[ ... ] ¿ ... ? wo ¿ ... ?"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(sentenciaSi.getTreeItem())
        treeItem.children.add(sentenciaSino?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("sentencia Si", 0)
        agregarValor(sentenciaSi.toString(), 0)

        agregarAtributo("Sentencia sino",1)
        agregarValor(sentenciaSino?.toString(),1)

        return panel
    }

}