package co.edu.uniquindio.lexico

import java.util.*

class AnalizadorLexico(private val codigoFuente: String) {
    val listaTokens: ArrayList<Token> = ArrayList()
    val listaErrores: ArrayList<String> = ArrayList()

    private var caracterActual: Char
    private var posicionActual = 0
    private var filaActual = 0
    private var columnaActual = 0
    private val finCodigo: Char

    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSgteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esReal()) continue
            if (esHexadecimal()) continue

            if (esIdentificador()) continue
            if (esPalabraReservada()) continue

            if (esCadena()) continue

            if (esOperadorAsignacion()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorIncreDecre()) continue

            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue

            if (esParentesisLlaves()) continue
            if (esTerminal()) continue
            if (esSeparador()) continue

            if (esComentario()) continue
            if (caracterActual != finCodigo) {
                listaTokens.add(Token("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual))
                obtenerSgteCaracter()
            }
        }

        val lista  = listaTokens.clone() as ArrayList<Token>

        for (token in lista) {
            if(token.categoria == Categoria.DESCONOCIDO) {
                listaErrores.add("No se reconoce la palabra '" + token.palabra + "' en " + token.fila + ":" + token.columna)
                // TODO: Por determinar -> listaTokens.remove(token)
            }
        }
    }

    /**
     * Metodo que obtiene el siguiente caracter
     *
     * Configura las filas y columnas
     * Verifica que no se desborde
     */
    private fun obtenerSgteCaracter() {
        posicionActual++
        if (posicionActual < codigoFuente.length) {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            caracterActual = codigoFuente[posicionActual]
        } else {
            caracterActual = finCodigo
        }
    }

    private fun esEntero(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '#') {
            var palabra = "" + caracterActual
            obtenerSgteCaracter()
            if (Character.isDigit(caracterActual)) { // Transicion
                palabra += caracterActual
                obtenerSgteCaracter()
                while (Character.isDigit(caracterActual)) { // Transicion
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(palabra, Categoria.ENTERO, fila, columna))
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    private fun esIdentificador(): Boolean {
        if (caracterActual == '@') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            // Transicion
            palabra += caracterActual
            obtenerSgteCaracter()
            if (Character.isLetter(caracterActual)) {
                palabra += caracterActual
                obtenerSgteCaracter()
                while (Character.isLetter(caracterActual) || caracterActual == '_') { // Transicion
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                if (!palabra.endsWith("_")) {
                    listaTokens.add(Token(palabra, Categoria.IDENTIFICADOR, fila, columna))
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    private fun esReal(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '*') {
            var palabra = "" + caracterActual
            obtenerSgteCaracter()
            if (Character.isDigit(caracterActual)) { // Transicion
                palabra += caracterActual
                obtenerSgteCaracter()
                while (Character.isDigit(caracterActual)) { // Transicion
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                if (caracterActual == '.') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (Character.isDigit(caracterActual)) {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        while (Character.isDigit(caracterActual)) { // Transicion
                            palabra += caracterActual
                            obtenerSgteCaracter()
                        }
                        listaTokens.add(Token(palabra, Categoria.REAL, fila, columna))
                        obtenerSgteCaracter()
                    } else {
                        listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                    }
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es un operador aritmetico
    private fun esOperadorAritmetico(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        return if (evaluarOperadorAritmetico(caracterActual)) {
            if (caracterActual != '-') {
                listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ARTIMETICO, fila, columna))
                obtenerSgteCaracter()
                true
            } else {
                obtenerSgteCaracter()
                if (caracterActual != '>') {
                    listaTokens.add(Token("-", Categoria.OPERADOR_ARTIMETICO, fila, columna))
                    obtenerSgteCaracter()
                    true
                } else {
                    listaTokens.add(Token("->", Categoria.OPERADOR_RELACIONAL, fila, columna))
                    obtenerSgteCaracter()
                    true
                }
            }
        } else false
    }

    private fun evaluarOperadorAritmetico(caracter: Char): Boolean {
        return caracter == '+' || caracter == '-' || caracter == 'x' || caracter == '%' || caracter == '/'
    }

    //Metodo que verifica si la palabra es un operador de asignacion
    private fun esOperadorAsignacion(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual.toString() + "" == "=") {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ASIGNACION, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es un operador de incremento y decremento
    private fun esOperadorIncreDecre(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '^') {
            var pal = caracterActual.toString() + ""
            obtenerSgteCaracter()

            when (caracterActual) {
                '+' -> {
                    pal += caracterActual
                    listaTokens.add(Token(pal, Categoria.OPERADOR_INCREMENTO, fila, columna))
                    obtenerSgteCaracter()
                }
                '-' -> {
                    pal += caracterActual
                    listaTokens.add(Token(pal, Categoria.OPERADOR_DECREMENTO, fila, columna))
                    obtenerSgteCaracter()
                }
                else -> {
                    listaTokens.add(Token("^", Categoria.OPERADOR_LOGICO, fila, columna))
                }
            }
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es un operador logico
    private fun esOperadorLogico(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '^' || caracterActual == '~' || caracterActual == '¬') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_LOGICO, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es un parentesis
    private fun esParentesisLlaves(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '{' || caracterActual == '}') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PARENTESIS, fila, columna))
            obtenerSgteCaracter()
            return true
        } else if (caracterActual == '¿' || caracterActual == '?') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.LLAVES, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es terminal
    private fun esTerminal(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '!') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.FIN_SENTENCIA, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es separador
    private fun esSeparador(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '°') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.SEPARADOR, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    //Metodo que verifica si la palabra es un operador Relacional
    private fun esOperadorRelacional(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        var pal = ""
        if (caracterActual == '<') {
            pal += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-') {
                pal += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == '>') {
                    pal += caracterActual
                }
                obtenerSgteCaracter()
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
            } else {
                listaTokens.add(Token(pal, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        } else if (caracterActual == '-') {
            pal += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '>') {
                pal += caracterActual
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
                return true
            }
        }
        return false
    }

    //Metodo que verifica si la palabra es operador hexadecimal
    private fun esHexadecimal(): Boolean {
        if (caracterActual == '$') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (Character.isDigit(caracterActual) || esLetraHex(caracterActual)) {
                do {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                } while (Character.isDigit(caracterActual) || esLetraHex(caracterActual))
                listaTokens.add(Token(palabra, Categoria.HEXADECIMAL, fila, columna))
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    //Metodo que verifica si la letra es hexadecimal
    private fun esLetraHex(caracter: Char): Boolean {
        return when (caracter) {
            'A', 'B', 'C', 'D', 'E', 'F' -> true
            else -> false
        }
    }

    // metodo que reconoce una cadena de caracteres
    private fun esCadena(): Boolean {
        if (caracterActual == '(') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            while (caracterActual != ')') {
                palabra += caracterActual
                obtenerSgteCaracter()
            }
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(palabra, Categoria.CADENA_CARACTERES, fila, columna))
            return true
        }
        return false
    }

    // metodo que reconoce los comentarios del lenguaje
    private fun esComentario(): Boolean {
        if (caracterActual == ':') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            obtenerSgteCaracter()
            // Transici�n
            while (caracterActual != '\n') {
                palabra += caracterActual
                obtenerSgteCaracter()
            }
            listaTokens.add(Token(palabra, Categoria.COMENTARIOS, fila, columna))
            return true
        }
        return false
    }

    //verifica si la palabra reservada
    private fun esPalabraReservada(): Boolean { // palabra reservada
        var palabra = ""
        val fila = filaActual
        val columna = columnaActual

        if (caracterActual == 'c') {
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'o') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 's') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                        return true
                    }
                }
            } else if (caracterActual == 'a') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'j') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                        return true
                    }
                }
            }
        }
        // palabra reservada
        if (caracterActual == 'e') {
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'n') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            } else if (caracterActual == 's') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'r') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'a') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            if (caracterActual == 't') {
                                palabra += caracterActual
                                obtenerSgteCaracter()
                                if (caracterActual == 'o') {
                                    palabra += caracterActual
                                    obtenerSgteCaracter()
                                    if (caracterActual == '1') {
                                        palabra += caracterActual
                                        obtenerSgteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    } else if (caracterActual == '6') {
                                        palabra += caracterActual
                                        obtenerSgteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Palabra Reservada
        if (caracterActual == 'd') {
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'e') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'c') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                } else if (caracterActual == 'v') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'o') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'l') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            if (caracterActual == 'v') {
                                palabra += caracterActual
                                obtenerSgteCaracter()
                                if (caracterActual == 'e') {
                                    palabra += caracterActual
                                    obtenerSgteCaracter()
                                    if (caracterActual == 'r') {
                                        palabra += caracterActual
                                        obtenerSgteCaracter()
                                        listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (caracterActual == 'u') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'r') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'a') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'n') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            if (caracterActual == 't') {
                                palabra += caracterActual
                                obtenerSgteCaracter()
                                if (caracterActual == 'e') {
                                    palabra += caracterActual
                                    obtenerSgteCaracter()
                                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                    return true
                                }
                            }
                        }
                    }
                }
            }
        }
        // Palabra Reservada
        if (caracterActual == 'm') {
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'e') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 't') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'e') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'r') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                            return true
                        }
                    }
                }
            }
        }
        // Palabra Reservada
        if (caracterActual == 's') {
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'a') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'l') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 't') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'a') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            if (caracterActual == 'r') {
                                palabra += caracterActual
                                obtenerSgteCaracter()
                                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                                return true
                            }
                        }
                    }
                }
            }
        }

        if(palabra.isNotEmpty()) {
            while(Character.isLetter(caracterActual)){
                palabra += caracterActual
                obtenerSgteCaracter()
            }

            listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            return true
        }
        return false
    }

    init {
        caracterActual = codigoFuente[posicionActual]
        finCodigo = 0.toChar()
    }
}