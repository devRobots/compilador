package co.edu.uniquindio.app.observable

import co.edu.uniquindio.semantica.simbolo.Funcion
import javafx.beans.property.SimpleStringProperty

class FuncionObservable(funcion: Funcion) {
    val acceso: SimpleStringProperty = SimpleStringProperty(funcion.modificadorAcceso)
    val tipo: SimpleStringProperty = SimpleStringProperty(funcion.tipoRetorno)
    val nombre: SimpleStringProperty = SimpleStringProperty(funcion.nombre)
    val parametros: SimpleStringProperty = SimpleStringProperty(funcion.tiposParametros.toString())
}