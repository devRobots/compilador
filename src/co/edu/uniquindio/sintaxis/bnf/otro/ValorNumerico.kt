package co.edu.uniquindio.sintaxis.bnf.otro

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Valor Numerico
 */
class ValorNumerico(private val signo: Token?, private val identificador: Token) : Sintaxis("Valor Numerico") {

    override fun toString(): String {
        return "${signo?.lexema ?: ""}${identificador.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Signo")
        agregarValor(signo?.lexema)

        agregarAtributo("Valor")
        agregarValor(identificador.lexema)

        return panel
    }
}