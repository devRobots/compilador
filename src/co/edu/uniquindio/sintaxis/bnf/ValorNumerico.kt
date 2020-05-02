package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ValorNumerico(private val signo: Token?,private val entero: Token?,private val real: Token?,private val identificador: Token?) : Sintaxis() {
    init {
        nombre = "Valor Numerico"
        estructura = "$signo$entero$real$identificador"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Signo", 0)
        agregarValor(signo?.lexema, 0)

        agregarAtributo("Valor", 0)
        agregarValor("${entero?.lexema}${real?.lexema}${identificador?.lexema}", 0)

        return panel
    }
}