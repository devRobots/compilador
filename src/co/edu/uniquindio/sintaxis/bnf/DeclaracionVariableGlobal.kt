package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class DeclaracionVariableGlobal(private val modificador:Token?, private val tipoDato:TipoDato,  val identificador:Token,private val expresion:Expresion) : Sintaxis() {
    init {
        this.nombre = "Variable Glocal"
    }
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipoDato.getTreeItem())
        treeItem.children.add(expresion.getTreeItem())
        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Tipo Dato", 0)
        agregarValor(tipoDato.toString(), 0)

        agregarAtributo("valor", 1)
        agregarValor(expresion.toString(), 1)

        return panel
    }

}