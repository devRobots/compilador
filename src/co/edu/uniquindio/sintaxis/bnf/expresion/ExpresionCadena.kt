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
 * Expresion Cadena
 */
class ExpresionCadena(
        private val cadena: Token,
        private val valor: Expresion?
) : Expresion("Expresion Cadena") {

    override fun toString(): String {
        return if (valor != null) {
            "${cadena.lexema} + ${valor.nombre}"
        } else {
            cadena.lexema
        }
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)
        if (valor != null) {
            treeItem.children.add(valor.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Cadena")
        agregarValor(cadena.lexema)

        agregarAtributo("Expresion")
        agregarValor(valor.toString())

        return panel
    }
}