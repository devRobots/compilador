package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaWhile(private val expLogica : ExpresionLogica, private val bloqueInstrucciones: ArrayList<Sentencia>) :Sentencia(){
   init {
       nombre = "Sentencia while"
       estructura = "durante [ ... ] ¿ ... ?"
   }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Intrucciones"))
        val treeBloqueInstrucciones = TreeItem(listaObservable)
        for (bloque in bloqueInstrucciones) {
            treeBloqueInstrucciones.children.add(bloque.getTreeItem())
        }

        treeItem.children.add(treeBloqueInstrucciones)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Sentencia", 0)
        agregarValor(expLogica.toString(), 0)

        agregarAtributo("Instrucciones", 1)
        agregarValor("Lista de Bloques de Intrucciones", 1)

        return panel
    }

}