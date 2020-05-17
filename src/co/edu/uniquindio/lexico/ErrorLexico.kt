package co.edu.uniquindio.lexico

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Error Lexico
 */
class ErrorLexico(private var lexema: String, private var fila: Int, private var columna: Int): Error("No se reconoce el lexema '$lexema' en $fila:$columna")