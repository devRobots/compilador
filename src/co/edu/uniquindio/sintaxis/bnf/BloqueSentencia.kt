package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

abstract class BloqueSentencia(var listaSentencia: ArrayList<Sentencia>) : Sintaxis(){
    init {
        this.nombre = "Bloque Sentencia"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Sentencias"))
        val treeSentencias = TreeItem(listaObservable)
        if (listaSentencia != null){
            for (sentencia in listaSentencia) {
                treeSentencias.children.add(sentencia.getTreeItem())
            }
        }

        treeItem.children.add(treeSentencias)
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Lista de Sentencias", 0)
        agregarValor(listaSentencia.toString(), 0)
        return panel
    }
}