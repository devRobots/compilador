package co.edu.uniquindio.sintaxis.bnf.control

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.bnf.expresion.ExpresionLogica
import co.edu.uniquindio.sintaxis.bnf.sentencia.Sentencia
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class CicloWhile(private val expLogica : ExpresionLogica, private val listaSentencia: ArrayList<Sentencia>) : EstructuraControl(){
   init {
       nombre = "Sentencia while"
       estructura = "durante [ ... ] ¿ ... ?"
   }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(expLogica.getTreeItem())

        val listaSentenciasObservable = SintaxisObservable(ListaSintactica("Sentencias"))
        val treeSentencias = TreeItem(listaSentenciasObservable)
        for (sentencia in listaSentencia) {
            treeSentencias.children.add(sentencia.getTreeItem())
        }
        treeItem.children.add(treeSentencias)

        return treeItem

    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Expresion Logica", 0)
        agregarValor(expLogica.nombre, 0)

        agregarAtributo("Bloques de Sentencia", 1)
        agregarValor("Lista de Sentencias", 1)

        return panel
    }

}