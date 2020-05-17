package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class EstructuraSiNo(private val bloqueInstrucciones : ArrayList<Sentencia>, private val estructuraSi : EstructuraSi?, private val estructuraSiNo: EstructuraSiNo?) : Sintaxis() {
    init {
        nombre = "Sentencia Si-No"
        estructura = if (estructuraSi != null){
            "wo ${estructuraSi.estructura} "
        }else{
            "wo ¿ .. ? "
        }
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (estructuraSi != null){
            treeItem.children.add(estructuraSi?.getTreeItem())
        }else{
            val listaObservable = SintaxisObservable(ListaSintactica("Lista de Sentencias"))
            val treeBloqueInstrucciones = TreeItem(listaObservable)
            for (bloque in bloqueInstrucciones) {
                treeBloqueInstrucciones.children.add(bloque.getTreeItem())
            }
            treeItem.children.add(treeBloqueInstrucciones)
        }
        if (estructuraSiNo != null){
            treeItem.children.add(estructuraSiNo?.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if (estructuraSi != null){
            agregarAtributo("Condición si", 0)
            agregarValor(estructuraSi.toString(), 0)
        }else{
            agregarAtributo("Instrucciones",0)
            agregarValor(bloqueInstrucciones.toString(),0)
        }
        if (estructuraSiNo != null){
            agregarAtributo("Sentencia Sino", 1)
            agregarValor(estructuraSiNo.toString(), 1)
        }

        return panel
    }
}