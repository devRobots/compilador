package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class ExpresionLogica(private val logico: ExpresionLogica?, private val izquierda: ValorLogico?, private val operador: Token?) : Expresion() {

    init {
        nombre = "Expresion Logica"
        if(operador != null){
            if(operador?.lexema == "¬"){
                estructura = "$operador [ $izquierda ]"
            }else{
                estructura = "$izquierda $operador $logico"
            }
        }
        estructura = " $izquierda"
    }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        if (logico != null){
            treeItem.children.add(logico.getTreeItem())
        }
        if (izquierda != null){
            treeItem.children.add(izquierda.getTreeItem())
        }

        return treeItem
    }
    //TODO: es asi??
    override fun getPropertiesPanel(): GridPane {
        if (operador != null){
            agregarAtributo("op", 0)
            agregarValor(operador.toString(), 0)
        }
        if (izquierda != null){
            agregarAtributo("valorLogico", 1)
            agregarValor(izquierda.toString(), 1)
        }
        return panel
    }
}