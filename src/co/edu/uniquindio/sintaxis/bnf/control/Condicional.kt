package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Condicional(private val estructuraSi : EstructuraSi, private val estructuraSino : EstructuraSiNo?) : EstructuraControl(){
    init {
        nombre = "sentencia condicional"
        if (estructuraSino != null){
            estructura = " wi[Expresion Logica] ¿ ... ? wo ¿ ... ?"
        }
        estructura = " $estructuraSi"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if(estructuraSi != null){
            treeItem.children.add(estructuraSi.getTreeItem())
        }
        if(estructuraSino != null){
            treeItem.children.add(estructuraSino.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if(estructuraSi != null){
            agregarAtributo("sentencia Si", 0)
            agregarValor(estructuraSi.toString(), 0)
        }
        if(estructuraSino != null){
            agregarAtributo("Sentencia sino",1)
            agregarValor(estructuraSino?.toString(),1)
        }
        return panel
    }

}