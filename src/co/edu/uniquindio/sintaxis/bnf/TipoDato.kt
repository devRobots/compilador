package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class TipoDato(private val tipo: Token) : Sintaxis() {
    init {
        this.nombre = "Tipo de Dato"
        this.estructura = "${tipo.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo de dato", 0)
        agregarValor(tipo.lexema, 0)

        return panel
    }

}