package co.edu.uniquindio.semantica

class ErrorSemantico(private var mensaje: String) {
    override fun toString(): String {
        return "Error Semantico: $mensaje"
    }
}