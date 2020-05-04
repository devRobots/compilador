package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSi(private val expLogica: ExpresionLogica, private val bloqueInstrucciones: ArrayList<Sentencia>):Sintaxis() {
    init {
        this.nombre = "Sentencia Si"
        this.estructura = "wi [${expLogica}] ¿ ... ?"
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
        agregarAtributo("sentencia", 0)
        agregarValor(expLogica.toString(), 0)

        agregarAtributo("Instrucciones", 1)
        agregarValor(bloqueInstrucciones.toString(), 1)

        return panel
    }

}