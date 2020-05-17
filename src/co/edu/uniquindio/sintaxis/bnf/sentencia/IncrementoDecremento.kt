package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class IncrementoDecremento(private val identificador: Token, private val operacion: Token) : Sentencia(){
    init {
        nombre = "Sentencia de Incremento/Decremento"
        estructura = "${identificador.lexema}${operacion.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Identificador: ", 0)
        agregarValor(identificador.lexema, 0)

        agregarAtributo("Operacion: ", 1)
        agregarValor(operacion.lexema, 1)

        return panel
    }
}
