package co.edu.uniquindio.app

import co.edu.uniquindio.lexico.AnalizadorLexico
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.util.Callback

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Controlador de la Ventana Principal
 */
class VentanaPrincipalController {
    /**
     * Codigo fuente
     */
    @FXML lateinit var texto: TextArea

    /**
     * Elementos de Analizador Lexico
     */
    @FXML lateinit var salidaLexico: TableView<TokenObservable>
    @FXML lateinit var palabra: TableColumn<TokenObservable, String?>
    @FXML lateinit var categoria: TableColumn<TokenObservable, String?>
    @FXML lateinit var fila: TableColumn<TokenObservable, String?>
    @FXML lateinit var columna: TableColumn<TokenObservable, String?>

    /**
     * Elementos de Rutina de errores
     */
    @FXML lateinit var erroresLexico: ListView<String>

    /**
     * Metodo initialize de JavaFX
     *
     * Inicializa las configuraciones basicas de los TableView
     */
    @FXML
    fun initialize() {
        palabra.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.palabra }
        categoria.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.categoria }
        fila.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.fila }
        columna.cellValueFactory = Callback { token: TableColumn.CellDataFeatures<TokenObservable, String?> -> token.value.columna }
    }

    /**
     * Obtiene el codigo fuente y lo envia a los analizadores
     * lexico, sintactio y semantico para imprimir sus respectivas salidas
     */
    @FXML
    fun analizar() {
        val codigoFuente = texto.text

        // Analizador Lexico
        val al = AnalizadorLexico(codigoFuente)
        al.analizar()

        val tokens = al.listaTokens
        val errores = al.listaErrores

        val tokensObservables: ObservableList<TokenObservable> = FXCollections.observableArrayList()
        for (token in tokens) {
            val observable = TokenObservable(token)
            tokensObservables.add(observable)
        }

        salidaLexico.items = tokensObservables
        salidaLexico.refresh()

        val erroresObservables: ObservableList<String> = FXCollections.observableArrayList()
        for (error in errores) {
            erroresObservables.add(error)
        }

        erroresLexico.items = erroresObservables
        erroresLexico.refresh()

        // Analizador Sintactico
    }
}