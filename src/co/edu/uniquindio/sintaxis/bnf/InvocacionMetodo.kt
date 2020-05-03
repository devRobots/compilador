package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import co.edu.uniquindio.sintaxis.ListaSintactica
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class InvocacionMetodo(private val identificador: Token, private val listaParametros: ArrayList<Parametro>) : Sentencia() {
    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        val listaObservable = SintaxisObservable(ListaSintactica("Parametros"))
        val treeImportaciones = TreeItem(listaObservable)
        for (parametro in listaParametros) {
            treeImportaciones.children.add(parametro.getTreeItem())
        }
        treeItem.children.add(treeImportaciones)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Nombre metodo", 0)
        agregarValor(identificador.lexema, 0)

        agregarAtributo("Lista de Parametros", 1)
        agregarValor(listaParametros.toString(), 1)

        return panel
    }

}
