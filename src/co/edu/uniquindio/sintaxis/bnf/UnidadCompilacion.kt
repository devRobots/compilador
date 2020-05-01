package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.app.SintaxisObservable
import co.edu.uniquindio.sintaxis.ListaSintactica
import co.edu.uniquindio.sintaxis.Sintaxis

import javafx.scene.control.TreeItem
import javafx.scene.layout.GridPane

class UnidadCompilacion(var paquete: Paquete?, var listaImportaciones: ArrayList<Importacion>, var clase: Clase) : Sintaxis() {
    init {
        this.nombre = "Unidad de Compilacion"
    }

    override fun toString(): String {
        return "$nombre"
    }

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
        agregarAtributo("Paquete", 0)
        agregarValor(paquete.toString(), 0)

        agregarAtributo("Lista de Importaciones", 1)
        agregarValor(listaImportaciones.toString(), 1)

        agregarAtributo("Clase", 2)
        agregarValor(clase.toString(), 2)

        return panel
    }
}