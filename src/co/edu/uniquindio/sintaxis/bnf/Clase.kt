package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Clase(private val modificadorAcceso: Token?,private val identificador: Token,private val cuerpoClase: CuerpoClase?) : Sintaxis() {
    init {
        this.nombre = "Clase"
        this.estructura = "${modificadorAcceso?.lexema} cosa ${identificador.lexema}"
    }

    override fun toString(): String {
        return "$nombre: $estructura"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(cuerpoClase?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Modificador de Acceso", 0)
        agregarValor(modificadorAcceso?.lexema, 0)

        agregarAtributo("Identificador", 1)
        agregarValor(identificador.lexema, 1)

        agregarAtributo("Cuerpo de Clase", 2)
        agregarValor(cuerpoClase.toString(), 2)

        return panel
    }
}