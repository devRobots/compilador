package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionRelacional
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ValorLogico(private val identificador: Token?, private val expresionRelacional: ExpresionRelacional?) : Sintaxis() {

    init {
        nombre = "Valor Logico"
        estructura = if (expresionRelacional != null){
            " $expresionRelacional"
        }else{
            " ${identificador?.lexema}"
        }
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (expresionRelacional != null) {
            treeItem.children.add(expresionRelacional?.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo(nombre, 0)
        agregarValor(estructura, 0)
        return panel
    }
}