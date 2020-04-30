package co.edu.uniquindio.sintaxis.bnf

import javafx.event.EventType
import javafx.scene.control.TreeItem

class UnidadCompilacion(var paquete: Paquete?, var listaImportaciones: ArrayList<Importacion>, var clase: Clase) {
    override fun toString(): String {
        return "Unidad de Compilacion"
    }

    fun getTreeItem(): TreeItem<String> {
        val treeItem = TreeItem<String>(toString())

        treeItem.children.add(paquete?.getTreeItem())

        val treeImportaciones = TreeItem<String>("Importaciones")
        for (importacion in listaImportaciones) {
            treeImportaciones.children.add(importacion.getTreeItem())
        }
        treeItem.children.add(treeImportaciones)

        treeItem.children.add(clase?.getTreeItem())

        return treeItem
    }
}