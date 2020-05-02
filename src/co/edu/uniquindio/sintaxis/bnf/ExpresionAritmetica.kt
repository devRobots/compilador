package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
import kotlin.system.exitProcess

class ExpresionAritmetica() : Expresion() {

    var izquierda :ExpresionAritmetica? = null
    var derecho: ExpresionAritmetica? = null
    var operador: Token? = null
    var valor: ValorNumerico? = null

    init {
        nombre = "Expresion Aritmetica"
    }

    constructor(izquierda: ExpresionAritmetica?, operador: Token?,derecho: ExpresionAritmetica?) : this() {
        this.izquierda = izquierda
        this.derecho = derecho
        this.operador = operador
    }
    constructor(expresionAritmetica: ExpresionAritmetica) : this() {
        this.izquierda = expresionAritmetica
    }
    constructor(valor: ValorNumerico?) : this() {
        this.valor = valor
    }

    // TODO: Revisar
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(izquierda?.getTreeItem())

        treeItem.children.add(derecho?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Izquierda", 0)
        agregarValor(izquierda.toString(), 0)

        agregarAtributo("Derecha", 1)
        agregarValor(derecho.toString(), 1)
        return panel
    }
}