package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionAritmetica(private val izquierda: ExpresionAritmetica?, private val derecho: ExpresionAritmetica?,private val operador: Token?, private val valor: ValorNumerico?) : Expresion() {
    init {
        nombre = "Expresion Aritmetica"
        estructura = " Cadena de Caracteres [+ Expresiones]"
    }

    constructor(izquierda: ExpresionAritmetica?, operador: Token?,derecho: ExpresionAritmetica?) : this(izquierda, derecho, operador, null)
    constructor(expresionAritmetica: ExpresionAritmetica) : this(expresionAritmetica, null, null, null)
    constructor(valor: ValorNumerico?) : this(null, null, null, valor)

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(izquierda?.getTreeItem())
        treeItem.children.add(derecho?.getTreeItem())

        treeItem.children.add(valor?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda", 0)
        agregarValor(izquierda.toString(), 0)

        agregarAtributo("Derecha", 1)
        agregarValor(derecho.toString(), 1)

        agregarAtributo("Valor", 2)
        agregarValor(valor.toString(), 2)

        return panel
    }
}