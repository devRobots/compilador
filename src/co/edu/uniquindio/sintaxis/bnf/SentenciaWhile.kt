package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class SentenciaWhile(private val expLogica = ExpresionLogica, private val bloqueInstrucciones: ArrayList<Sentencia>) :Sentencia(){
   init {
       nombre = "Sentencia while"
       estructura = "durante [ ... ] ¿ ... ?"
   }

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        TODO("Not yet implemented")
    }

    override fun getPropertiesPanel(): GridPane {
        TODO("Not yet implemented")
    }

}