package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionLogica(private val logico: ExpresionLogica?, private val izquierda: ValorLogico?, private val operador: Token?, private val derecha: ValorLogico?) : Expresion() {

    init {
        nombre = "Expresion Cadena"
        estructura = " $izquierda $operador $logico $derecha"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(logico?.getTreeItem())
        treeItem.children.add(izquierda?.getTreeItem())
        treeItem.children.add(derecha?.getTreeItem())

        return treeItem
    }
    //TODO: es asi??
    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("valorLogico", 0)
        agregarValor(izquierda.toString(), 0)

        agregarAtributo("op", 1)
        agregarValor(operador.toString(), 1)

        agregarAtributo("ValorLogico", 2)
        agregarValor(derecha.toString(), 2)
        return panel
    }
}