package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane
//TODO: Aqui puse solo identificador, pero hay que revisar si esta bien, o se pone las demas
class ValorNumerico(private val signo: Token?,private val identificador: Token?) : Sintaxis() {
    init {
        nombre = "Valor Numerico"
        estructura = "${signo?.lexema}${identificador?.lexema}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Signo", 0)
        agregarValor(signo?.lexema, 0)

        agregarAtributo("Valor", 1)
        agregarValor("${identificador?.lexema}", 1)

        return panel
    }
}