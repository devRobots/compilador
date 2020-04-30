package co.edu.uniquindio.sintaxis.bnf

import co.edu.uniquindio.lexico.Token
import javafx.scene.control.TreeItem

    class Paquete(var paquete: Token) {
        override fun toString(): String {
            return "Paquete"
        }

        fun getTreeItem(): TreeItem<String> {
            return TreeItem<String>(toString())
        }
    }