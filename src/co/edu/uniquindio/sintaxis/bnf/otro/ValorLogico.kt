package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionRelacional

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Valor Logico
 */
class ValorLogico(
        private val identificador: Token?,
        private val expresionRelacional: ExpresionRelacional?
) : Sintaxis("Valor Logico") {

    override fun toString(): String = expresionRelacional?.toString() ?: "${identificador?.lexema}"

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (expresionRelacional != null) {
            treeItem.children.add(expresionRelacional.getTreeItem())
        }
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Identificador")
        agregarValor(identificador?.lexema)

        agregarAtributo("Expresion Relacional")
        agregarValor(expresionRelacional.toString())

        return panel
    }
}