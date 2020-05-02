package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionLogica(private val cadena: ExpresionLogica?, private val operacion: Token?, private val valor: ExpresionRelacional?) : Expresion() {

    init {
        nombre = "Expresion Cadena"
        estructura = " <Expresion Logica> OpLogico <Expresion Logica>, ¬<Expresion Logica>, Expresion Relacional"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(cadena?.getTreeItem())

        treeItem.children.add(valor?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda", 0)
        agregarValor(cadena.toString(), 0)

        agregarAtributo("Derecha", 1)
        agregarValor(valor.toString(), 1)
        return panel
    }
}