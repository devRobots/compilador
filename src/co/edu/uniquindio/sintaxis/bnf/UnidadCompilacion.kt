package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.semantica.Ambito
import co.edu.uniquindio.semantica.ErrorSemantico
import co.edu.uniquindio.semantica.TablaSimbolos
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis


import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 2.0
 *
 * Unidad de Compilacion
 */
class UnidadCompilacion(
        var paquete: Paquete?,
        var listaImportaciones: ArrayList<Importacion>,
        var clase: Clase?
) : Sintaxis("Unidad de Compilacion") {

    override fun getTreeItem(): TreeItem<SintaxisObservable> {
        val observable = SintaxisObservable(this)
        val treeItem = TreeItem(observable)

        treeItem.children.add(paquete?.getTreeItem())

        val listaObservable = SintaxisObservable(ListaSintactica("Importaciones"))
        val treeImportaciones = TreeItem(listaObservable)
        for (importacion in listaImportaciones) {
            treeImportaciones.children.add(importacion.getTreeItem())
        }
        treeItem.children.add(treeImportaciones)

        treeItem.children.add(clase?.getTreeItem())

        return treeItem
    }

    override fun getPropertiesPanel(): GridPane {
        agregarAtributo("Paquete")
        agregarValor(paquete.toString())

        agregarAtributo("Lista de Importaciones")
        agregarValor(listaImportaciones.toString())

        agregarAtributo("Clase")
        agregarValor(clase.toString())

        return panel
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>) {
        for (importacion in listaImportaciones) {
            importacion.llenarTablaSimbolos(tablaSimbolos)
        }
        clase?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, Ambito(nombre))
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<ErrorSemantico>) {
        clase?.analizarSemantica(tablaSimbolos, erroresSemanticos, Ambito(nombre))
    }
}