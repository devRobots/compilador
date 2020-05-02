package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionAritmetica(private val izquierda: ExpresionAritmetica?,private val derecho: ExpresionAritmetica?,private val operador: Token?,private val valor: ValorNumerico?) : Expresion() {
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        TODO("Not yet implemented")
    }

    override fun getPropertiesPanel(): GridPane {
        TODO("Not yet implemented")
    }
}