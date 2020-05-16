package co.edu.uniquindio.sintaxis.bnf.expresion

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.bnf.expresion.Expresion
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionAritmetica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionRelacional(private val izquierda: ExpresionAritmetica?, private val operacion:Token, private val derecho: ExpresionAritmetica?) : Expresion() {

    init {
        nombre = "Expresion Relacional"
        estructura = " $izquierda ${operacion.lexema} $derecho"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (izquierda != null){
            treeItem.children.add(izquierda?.getTreeItem())
        }
        if (derecho != null){
            treeItem.children.add(derecho?.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        if (izquierda != null){
            agregarAtributo("Izquierda", 0)
            agregarValor(izquierda.toString(), 0)
        }
        if (izquierda != null){
            agregarAtributo("operador", 1)
            agregarValor(operacion.lexema, 1)
        }
        if (derecho != null){
            agregarAtributo("Derecha", 2)
            agregarValor(derecho.toString(), 2)
        }
        return panel
    }
}