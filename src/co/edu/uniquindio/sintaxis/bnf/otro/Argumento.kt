package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Argumento(private val expresion: Expresion) : Sintaxis() {
    init {
        nombre = "Parametro"
        estructura = expresion.estructura
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expresion.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Parametro", 0)
        agregarValor(expresion.nombre, 0)

        return panel
    }
}
