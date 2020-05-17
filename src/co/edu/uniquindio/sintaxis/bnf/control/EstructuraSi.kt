package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class EstructuraSi(private val expLogica: ExpresionLogica, private val bloqueInstrucciones: ArrayList<Sentencia>): Sintaxis() {
    init {
        this.nombre = "Sentencia Si"
        this.estructura = "wi [ Expresion Logica ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Lista de Sentencias"))
        val treeBloqueInstrucciones = TreeItem(listaObservable)
        for (bloque in bloqueInstrucciones) {
            treeBloqueInstrucciones.children.add(bloque.getTreeItem())
        }
        treeItem.children.add(treeBloqueInstrucciones)

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("expresion Logica", 0)
        agregarValor(estructura, 0)

        agregarAtributo("Instrucciones", 1)
        agregarValor(bloqueInstrucciones.toString(), 1)

        return panel
    }

}