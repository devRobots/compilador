package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class CuerpoClase(private val listaBloquesSentencia: ArrayList<BloqueSentencia>) : Sintaxis() {
    init {
        this.nombre = "Cuerpo de Clase"
        this.estructura = "¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Sentencia"))
        val treeBloquesSentencia = TreeItem(listaObservable)
        for (bloqueSentencia in listaBloquesSentencia) {
            treeBloquesSentencia.children.add(bloqueSentencia.getTreeItem())
        }
        treeItem.children.add(treeBloquesSentencia)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Lista de Bloques de Sentencia", 0)
        agregarValor(listaBloquesSentencia.toString(), 0)

        return panel
    }
}
