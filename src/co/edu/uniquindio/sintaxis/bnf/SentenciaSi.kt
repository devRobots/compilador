package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaSi(private val expLogica: ExpresionLogica, private val listaBloqueInstrucciones: ArrayList<BloqueInstrucciones>): SentenciaCondicional() {
    init {
        this.nombre = "Sentencia Si"
        this.estructura = "wi [ ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Intrucciones"))
        val treeBloqueInstrucciones = TreeItem(listaObservable)
        for (bloque in listaBloqueInstrucciones) {
            treeBloqueInstrucciones.children.add(bloque.getTreeItem())
        }
        treeItem.children.add(treeBloqueInstrucciones)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("sentencia", 0)
        agregarValor(expLogica.toString(), 0)

        agregarAtributo("Instrucciones", 1)
        agregarValor("Lista de Bloques de Intrucciones", 1)

        return panel
    }

}