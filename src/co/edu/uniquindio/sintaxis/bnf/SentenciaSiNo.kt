package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSiNo(private val sentenciaSi : SentenciaSi?, private val sentenciaSiNo: SentenciaSiNo?) : SentenciaCondicional() {
    init {
        this.nombre = "Sentencia Si-No"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)

        return TreeItem(observable)
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("condición si", 0)
        agregarValor(sentenciaSi.toString(), 0)

        agregarAtributo("Sentencia Sino", 1)
        agregarValor(sentenciaSiNo.toString(), 1)

        return panel
    }
}