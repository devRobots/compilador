package co.edu.uniquindio.sintaxis.bnf.sentencia

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
 * Incremento o Decremento
 */
class IncrementoDecremento(
        private val identificador: Token,
        private val operacion: Token
) : Sentencia("Incremento/Decremento") {

    override fun toString(): String {
        return "${identificador.lexema}${operacion.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Identificador: ")
        agregarValor(identificador.lexema)

        agregarAtributo("Operacion: ")
        agregarValor(operacion.lexema)

        return panel
    }
}
