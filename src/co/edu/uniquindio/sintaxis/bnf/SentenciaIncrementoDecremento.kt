package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaIncrementoDecremento(private val identificador: Token, private val operacion: Token) : Sentencia(){
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
