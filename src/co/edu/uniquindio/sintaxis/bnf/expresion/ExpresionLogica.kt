package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.bnf.otro.ValorLogico
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Logica
 */
class ExpresionLogica(
        private val logico: ExpresionLogica?,
        private val izquierda: ValorLogico?,
        private val operador: Token?
) : Expresion("Expresion Logica") {

    override fun toString(): String = if (operador == null) {
        "$izquierda"
    } else {
        if (operador?.lexema == "¬") {
            "$operador [ $izquierda ]"
        } else {
            "$izquierda $operador $logico"
        }
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (logico != null) {
            treeItem.children.add(logico.getTreeItem())
        }
        if (izquierda != null) {
            treeItem.children.add(izquierda.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda")
        agregarValor(izquierda.toString())

        agregarAtributo("Operador")
        agregarValor(operador.toString())

        agregarAtributo("Logico")
        agregarValor(logico.toString())

        return panel
    }
}