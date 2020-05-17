package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import co.edu.uniquindio.sintaxis.bnf.sentencia.IncrementoDecremento
import co.edu.uniquindio.sintaxis.bnf.sentencia.VariableLocal

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class CicloFor(private val decVariableLocal: VariableLocal?, private val expLogica: ExpresionLogica, private val asignacionCiclo: IncrementoDecremento, private val bloqueInstrucciones: ArrayList<Sentencia>) : EstructuraControl() {
    init {
        nombre = "Sentencia de ciclo For"
        estructura = "ciclo [ .. | ... | ... ] ¿ ... ?"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (decVariableLocal != null){
            treeItem.children.add(decVariableLocal.getTreeItem())
        }

        treeItem.children.add(expLogica.getTreeItem())
        treeItem.children.add(asignacionCiclo.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Bloques de Sentencias"))
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
        agregarAtributo("Declaracion",0)
        agregarValor(decVariableLocal?.nombre,0)

        agregarAtributo("Expresion Logica",1)
        agregarValor(expLogica.nombre,1)

        agregarAtributo("Asignacion",2)
        agregarValor(asignacionCiclo.nombre,2)

        agregarAtributo("Lista de sentencia",3)
        agregarValor("Lista de Sentencias",3)

        return panel
    }
}