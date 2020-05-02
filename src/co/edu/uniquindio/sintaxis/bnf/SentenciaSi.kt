package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSi(private val expLogica: ExpresionLogica?, private val instruccion: Bloque?): SentenciaCondicional() {
    init {
        this.nombre = "Sentencia Si"
        this.estructura = "wi [ ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())
        treeItem.children.add(instruccion.getTreeItem())
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("sentencia", 0)
        agregarValor(expLogica.toString(), 0)

        agregarAtributo("Instrucciones", 1)
        agregarValor(instruccion.toString(), 1)

        return panel
    }

}