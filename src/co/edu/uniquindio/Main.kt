package co.edu.uniquindio

import co.edu.uniquindio.app.VentanaPrincipalController

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(VentanaPrincipalController::class.java.getResource("VentanaPrincipal.fxml"))
        val parent : Parent = loader.load()

        val scene = Scene(parent)

        primaryStage?.scene = scene
        primaryStage?.title = "Compilador"
        primaryStage?.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch( Main::class.java )
        }
    }
}