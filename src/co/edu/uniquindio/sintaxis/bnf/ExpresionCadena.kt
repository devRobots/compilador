package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionCadena(private val cadena: Token, private val valor: Expresion?) : Expresion() {

    init {
        nombre = "Expresion Cadena"
        if(valor != null){
            estructura = " ${cadena.lexema} + ${valor.nombre}"
        }else{
            estructura = "${cadena.lexema}"
        }

    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)
        if (valor != null){
            treeItem.children.add(valor.getTreeItem())
        }

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("cadena", 0)
        agregarValor(estructura, 0)
        if (valor != null){
            agregarAtributo("Expresion", 1)
            agregarValor(valor.toString(), 1)
        }
        return panel
    }
}