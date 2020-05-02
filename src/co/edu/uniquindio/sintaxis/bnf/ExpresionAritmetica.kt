package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionAritmetica(private val izquierda: ExpresionAritmetica?, private val derecho: ExpresionAritmetica?, private val operador: Token?, private val valor: ValorNumerico?) : Expresion() {
    init {
        nombre = "Expresion Aritmetica"
        estructura = if (valor != null) {
            valor?.estructura
        } else {
            izquierda?.estructura
        }
        if (operador != null) {
            estructura += "${operador.lexema}$derecho"
        }
    }

    constructor(valor: ValorNumerico?, operador: Token, derecho: ExpresionAritmetica?) : this(null, derecho, operador, valor)
    constructor(izquierda: ExpresionAritmetica?, operador: Token, derecho: ExpresionAritmetica?) : this(izquierda, derecho, operador, null)
    constructor(expresionAritmetica: ExpresionAritmetica) : this(expresionAritmetica, null, null, null)
    constructor(valor: ValorNumerico?) : this(null , null, null, valor)

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (izquierda != null) {
            treeItem.children.add(izquierda?.getTreeItem())
        }
        if (derecho != null) {
            treeItem.children.add(derecho?.getTreeItem())
        }
        if (valor != null) {
            treeItem.children.add(valor.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Valor", 0)
        agregarValor(valor?.nombre, 0)

        agregarAtributo("Izquierda", 1)
        agregarValor(izquierda?.estructura, 1)

        agregarAtributo("Derecha", 2)
        agregarValor(derecho?.estructura, 2)

        agregarAtributo("Operador", 3)
        agregarValor(operador?.lexema, 3)

        return panel
    }
}