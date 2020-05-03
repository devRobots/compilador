package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaFor(private val decVariableLocal:DeclaracionVariableLocal, private val expLogica: ExpresionLogica, private val asignacionCiclo: SentenciaIncrementoDecremento, private val bloqueInstrucciones: ArrayList<Sentencia>?) :Sintaxis() {
    init {
        nombre = "Sentencia de ciclo For"
        estructura = "ciclo [... | ... | ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(decVariableLocal.getTreeItem())
        treeItem.children.add(expLogica.getTreeItem())
        treeItem.children.add(asignacionCiclo.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Intrucciones"))
        val treeBloqueInstrucciones = TreeItem(listaObservable)
        if(bloqueInstrucciones != null){
            for (bloque in bloqueInstrucciones) {
                treeBloqueInstrucciones.children.add(bloque.getTreeItem())
            }
        }
        treeItem.children.add(treeBloqueInstrucciones)


        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Sentencia de ciclo for",0)
        agregarValor(decVariableLocal.toString(),0)
        agregarValor(expLogica.toString(),0)
        agregarValor(asignacionCiclo.toString(),0)

        agregarAtributo("lista de sentencia",1)
        agregarValor(bloqueInstrucciones?.toString(),1)

        return panel
    }
}