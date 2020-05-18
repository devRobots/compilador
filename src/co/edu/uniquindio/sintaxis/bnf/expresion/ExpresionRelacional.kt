package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Expresion Relacional
 */
class ExpresionRelacional(
        private val izquierda: ExpresionAritmetica?,
        private val operacion: Token
        , private val derecho: ExpresionAritmetica?
) : Expresion("Expresion Relacional") {

    override fun toString(): String {
        return "$izquierda ${operacion.lexema} $derecho"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (izquierda != null) {
            treeItem.children.add(izquierda?.getTreeItem())
        }
        if (derecho != null) {
            treeItem.children.add(derecho?.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if (izquierda != null) {
            agregarAtributo("Izquierda")
            agregarValor(izquierda.toString())
        }
        if (izquierda != null) {
            agregarAtributo("Operador")
            agregarValor(operacion.lexema)
        }
        if (derecho != null) {
            agregarAtributo("Derecha")
            agregarValor(derecho.toString())
        }
        return panel
    }
}