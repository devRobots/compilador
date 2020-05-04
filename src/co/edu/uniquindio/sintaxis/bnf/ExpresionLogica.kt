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

        if (logico != null){
            treeItem.children.add(logico.getTreeItem())
        }
        if (izquierda != null){
            treeItem.children.add(izquierda.getTreeItem())
        }
        if (derecha != null){
            treeItem.children.add(derecha.getTreeItem())
        }

        return treeItem
    }
    //TODO: es asi??
    override fun getPropertiesPanel(): GridPane {
        if (operador != null){
            agregarAtributo("op", 0)
            agregarValor(operador.toString(), 0)
        }
        if (izquierda != null){
            agregarAtributo("valorLogico", 1)
            agregarValor(izquierda.toString(), 1)
        }
        if (derecha != null){
            agregarAtributo("ValorLogico", 2)
            agregarValor(derecha.toString(), 2)
        }
        return panel
    }
}