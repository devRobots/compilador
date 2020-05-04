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

        if(sentenciaSi != null){
            treeItem.children.add(sentenciaSi.getTreeItem())
        }
        if(sentenciaSino != null){
            treeItem.children.add(sentenciaSino.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if(sentenciaSi != null){
            agregarAtributo("sentencia Si", 0)
            agregarValor(sentenciaSi.toString(), 0)
        }
        if(sentenciaSino != null){
            agregarAtributo("Sentencia sino",1)
            agregarValor(sentenciaSino?.toString(),1)
        }
        return panel
    }

}