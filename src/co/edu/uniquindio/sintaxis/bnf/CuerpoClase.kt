package co.edu.uniquindio.sintaxis.bnf

import javafx.scene.control.TreeItem

class CuerpoClase() {
    override fun toString(): String {
        return "Cuerpo de Clase"
    }

    fun getTreeItem(): TreeItem<String> {
        return TreeItem<String>(toString())
    }
}
