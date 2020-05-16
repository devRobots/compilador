package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSiNo(private val bloqueInstrucciones : ArrayList<Sentencia>, private val sentenciaSi : SentenciaSi?, private val sentenciaSiNo: SentenciaSiNo?) : Sintaxis() {
    init {
        nombre = "Sentencia Si-No"
        if (sentenciaSi != null){
            estructura = "wo ${sentenciaSi.estructura} "
        }else{
            estructura = "wo ¿ .. ? "
        }
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (sentenciaSi != null){
            treeItem.children.add(sentenciaSi?.getTreeItem())
        }else{
            val listaObservable = SintaxisObservable(ListaSintactica("Lista de Sentencias"))
            val treeBloqueInstrucciones = TreeItem(listaObservable)
            for (bloque in bloqueInstrucciones) {
                treeBloqueInstrucciones.children.add(bloque.getTreeItem())
            }
            treeItem.children.add(treeBloqueInstrucciones)
        }
        if (sentenciaSiNo != null){
            treeItem.children.add(sentenciaSiNo?.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if (sentenciaSi != null){
            agregarAtributo("Condición si", 0)
            agregarValor(sentenciaSi.toString(), 0)
        }else{
            agregarAtributo("Instrucciones",0)
            agregarValor(bloqueInstrucciones.toString(),0)
        }
        if (sentenciaSiNo != null){
            agregarAtributo("Sentencia Sino", 1)
            agregarValor(sentenciaSiNo.toString(), 1)
        }

        return panel
    }
}