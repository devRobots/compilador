package co.edu.uniquindio.sintaxis.bnf.sentencia

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.otro.Parametro
import co.edu.uniquindio.sintaxis.bnf.otro.TipoDato
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class Arreglo (private val tipoDato : TipoDato, private val identificador : Token, private val listParametros: ArrayList<Parametro>): Sentencia() {
    init {
        nombre = "Declarar arreglo"
        estructura = "${tipoDato}{} ${identificador} = {List}"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(tipoDato.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Lista de parametros"))
        val treeListaParametros = TreeItem(listaObservable)
        for (lista in listParametros) {
            treeListaParametros.children.add(lista.getTreeItem())
        }
        treeItem.children.add(treeListaParametros)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Declaración de arreglo",0)
        agregarValor(estructura,0)

        agregarAtributo("Lista de Parametros",1)
        agregarValor(listParametros.toString(),1)

        return panel
    }

}