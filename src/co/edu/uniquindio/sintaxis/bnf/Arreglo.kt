package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Arreglo (private val listArgumento: ArrayList<Argumento>, private val tipoDato : TipoDato?, private val expresion : Expresion? ): Sintaxis() {
    init {
        nombre = "Declarar arreglo"
        estructura = "{ ... } o ent [ ... ]"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expresion?.getTreeItem())
        treeItem.children.add(tipoDato?.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Lista de argumentos"))
        val treeListaArgumentos = TreeItem(listaObservable)
        for (lista in listArgumento) {
            treeListaArgumentos.children.add(lista.getTreeItem())
        }
        treeItem.children.add(treeListaArgumentos)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Lista de argumentos", 0)
        agregarValor(listArgumento.toString(),0)

        agregarAtributo("Expresion",0)
        agregarValor(tipoDato.toString(),0)
        agregarValor(expresion.toString(),0)

        return panel
    }

}