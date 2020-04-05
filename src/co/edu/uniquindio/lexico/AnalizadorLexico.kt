package co.edu.uniquindio.lexico

import kotlin.collections.ArrayList

/**
 * @author Samara Rincon
 * @author Yesid Rosas Toro
 * @author Cristian Camilo Quiceno
 *
 * @version 1.1
 *
 * Analizador Lexico
 */
class AnalizadorLexico(private val codigoFuente: String) {
    /**
     * Lista de tokens y errores generados por el analizador lexico
     */
    val listaTokens: ArrayList<Token> = ArrayList()
    val listaErrores: ArrayList<String> = ArrayList()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var caracterActual: Char
    private var posicionActual = 0
    private var filaActual = 0
    private var columnaActual = 0
    private val finCodigo = 0.toChar()

    init {
        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * Funcion principal
     *
     * Analiza el codigo fuente
     *
     * @param codigoFuente El codigo que se analizara
     *
     * @return tokens La lista de tokens
     * @return errores La rutina de errores
     */
    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSgteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esReal()) continue
            if (esHexadecimal()) continue
            if (esBooleano()) continue
//TODO: Falta caracter especial
            if (esIdentificador()) continue
            if (esPalabraReservada()) continue

            if (esCadena()) continue
            if (esCaracter()) continue

            if (esOperadorAsignacion()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorIncreDecre()) continue

            if (esOperadorRelacional()) continue
            if (esOperadorLogico()) continue

            if (esParentesisLlave()) continue
            if (esTerminal()) continue
            if (esSeparador()) continue

            if (esComentario()) continue
            if (esPunto()) continue

            if (caracterActual != finCodigo) {
                listaTokens.add(Token("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual))
                obtenerSgteCaracter()
            }
        }

        val lista = listaTokens.clone() as ArrayList<Token>

        for (token in lista) {
            if (token.categoria == Categoria.DESCONOCIDO) {
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

    /**
     * Verifica si la palabra actual es un numero entero
     *
     * @return esEntero retorna true si es entero
     */
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
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna)) //TODO: debe retornar a la posicion inicial
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un identificador
     *
     * @return esIdentificador retorna true si es identificador
     */
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
                // Transicion
                while (Character.isLetter(caracterActual) || caracterActual == '_' || Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                if (!palabra.endsWith("_")) {
                    listaTokens.add(Token(palabra, Categoria.IDENTIFICADOR, fila, columna))
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un numero real
     *
     * @return esReal retorna true si es real
     */
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
                        //TODO: debe retornar a la posicion inicial
                    }
                } else {
                    listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                    //TODO: debe retornar a la posicion inicial
                }
            } else {
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
                //TODO: debe retornar a la posicion inicial
            }
            return true
        }
        return false
    }


    /**
     * Verifica si la palabra actual es un operador aritmetico
     *
     * @return esOperadorAritmetico retorna true si es un operador aritmetico
     */
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

    /**
     * Verifica si el caracter corresponde a un operador aritmetico
     *
     * @param caracter El caracter a verificar
     *
     * @return boolean retorna true si es un operador aritmetico valido
     */
    private fun evaluarOperadorAritmetico(caracter: Char): Boolean {
        return caracter == '+' || caracter == '-' || caracter == 'x' || caracter == '%' || caracter == '/'
    }//TODO: debe verificar que no sea de incremento. Bueno investigar despues
    /**
     * Verifica si la palabra actual es un operador de asignacion
     *
     * @return esOperadorAsignacion retorna true si es un operador de asignacion
     */
    private fun esOperadorAsignacion(): Boolean {
        //TODO: Faltan operadores de asignacion
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual.toString() + "" == "=") {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ASIGNACION, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es operador de incremento o decremento
     *
     * @return esOperadorIncreDecre retorna true si es operador de incremento o decremento
     */
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

    /**
     * Verifica si la palabra actual es un operador logico
     *
     * @return esOperadorLogico retorna true si es un operador logico
     */
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

    /**
     * Verifica si la palabra actual es parentesis o llave
     *
     * @return esParentesisLlaves retorna true si es parentesis o llave
     */
    private fun esParentesisLlave(): Boolean {
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

    /**
     * Verifica si la palabra actual es terminal
     *
     * @return esTerminal retorna true si es terminal
     */
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

    /**
     * Verifica si la palabra actual es un separador
     *
     * @return esSeparador retorna true si es separador
     */
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

    /**
     * Verifica si la palabra actual es un operador relacional
     *
     * @return esOperadorRelacional retorna true si es operador relacional
     */
    private fun esOperadorRelacional(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        var pal = ""
        if (caracterActual == '<') {
            pal += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-' || caracterActual == '<') {
                pal += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == '>' && codigoFuente[posicionActual-1] == '-') {
                    pal += caracterActual
                    obtenerSgteCaracter()
                }
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
            } else {
                listaTokens.add(Token(pal, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        if (caracterActual == '>') {
            pal += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-' || caracterActual == '>') {
                pal += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
            } else {
                listaTokens.add(Token(pal, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
            }
            return true
        }
        if (caracterActual == '¬') {
            pal += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == '-') {
                pal += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(pal, Categoria.OPERADOR_RELACIONAL, fila, columna))
                return true
            } else {
                posicionActual -= 2
                obtenerSgteCaracter()
                return false
            }
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un numero hexadecimal
     *
     * @return esHexadecimal retorna true si es hexadecimal
     */
    private fun esHexadecimal(): Boolean {
        //TODO: no hace falta pero bueno :v, autodestruir mensaje despues
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

    /**
     * Verifica si el caracter es una letra hexadecimal
     *
     * @param caracter El caracter a verificar
     *
     * @return boolean retorna true si es una letra hexadecimal
     */
    private fun esLetraHex(caracter: Char): Boolean {
        return when (caracter) {
            'A', 'B', 'C', 'D', 'E', 'F' -> true
            else -> false
        }
    }

    /**
     * Verifica si la palabra actual es un Booleano
     *
     * Palabras Booleanas:
     * .true , .false
     *
     * @return esBooleano retorna true si es una palabra Booleana
     */
    private fun esBooleano(): Boolean { // palabra reservada
        if (caracterActual == '.') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 't') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'r') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'u') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'e'){
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(palabra, Categoria.BOOLEANO, fila, columna))
                            return true
                        }
                    }
                }
            }
            if (caracterActual == 'f') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'a') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'l') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 's'){
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            if (caracterActual == 'e'){
                                palabra += caracterActual
                                obtenerSgteCaracter()
                                listaTokens.add(Token(palabra, Categoria.BOOLEANO, fila, columna))
                                return true
                            }
                        }
                    }
                }
            }

            if (palabra.isNotEmpty()) {
                while (Character.isLetter(caracterActual)) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }

                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))//TODO: debe retornar a la posicion inicial
                return true
            }
        }
        return false
    }


    /**
     * Verifica si la palabra actual es una cadena de caracteres
     *
     * @return esCadena retorna true si es cadena
     */
    private fun esCadena(): Boolean { // TODO: corregir, validar si encuentra caraccter especial
        if (caracterActual == '(') {
            var palabra = ""
            val fila = filaActual
            val columna = columnaActual
            // Transici�n
            palabra += caracterActual
            obtenerSgteCaracter()
            while (caracterActual != ')' && caracterActual !='\n' && caracterActual != finCodigo) {
                palabra += caracterActual
                obtenerSgteCaracter()
            }
            palabra += caracterActual
            obtenerSgteCaracter()
            if (palabra.endsWith(")")) {
                listaTokens.add(Token(palabra, Categoria.CADENA_CARACTERES, fila, columna))
            }else{
                listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            }
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un caracter
     *
     * @return esCaracter retorna true si es un caracter
     */
    private fun esCaracter(): Boolean {
        //TODO: validar si tiene caracter especial
        if (posicionActual+2 <= codigoFuente.length && caracterActual == '"') {
            if (codigoFuente[posicionActual+2] == '"') {
                var palabra = ""
                val fila = filaActual
                val columna = columnaActual
                // obtiene el caracter
                palabra += caracterActual
                obtenerSgteCaracter()
                palabra += caracterActual
                obtenerSgteCaracter()
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(palabra, Categoria.CARACTER, fila, columna))
                return true
            }
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un comentario
     *
     * @return esComentario retorna true si es comentario
     */
    private fun esComentario(): Boolean {
        //TODO: verificar
        if (caracterActual == ':') {
            var palabra = ""
            var terminal = '\n'
            var category = Categoria.COMENTARIO_LINEA
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            obtenerSgteCaracter()
            // Verifica si es un comentario de bloque o de linea
            if (caracterActual == '/'){
                category = Categoria.COMENTARIO_BLOQUE
                palabra += caracterActual
                obtenerSgteCaracter()
                while (caracterActual != finCodigo && !esTerminalBloque()) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
                palabra += caracterActual
                obtenerSgteCaracter()
            } else{
                while (caracterActual != finCodigo && caracterActual != terminal) {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                }
            }
            palabra += caracterActual
            obtenerSgteCaracter()
            listaTokens.add(Token(palabra, category, fila, columna))
            return true
        }
        return false
    }

    /**
     * Metodo para validar si es un terminal de Comentario de bloque
     */
    private fun esTerminalBloque(): Boolean{
        if (posicionActual+1 <= codigoFuente.length && caracterActual == '/' && codigoFuente[posicionActual+1] == ':'){
            return true;
        }
        return false;
    }

    /**
     * Verifica si hay dos punto o un punto
     *
     * @return esPunto retorna true si es dos punto o un punto
    */
    private fun esPunto(): Boolean{
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '|') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.DOS_PUNTOS, fila, columna))
            obtenerSgteCaracter()
            return true
        }else if (caracterActual == ';'){
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PUNTO, fila, columna))
            obtenerSgteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es una palabra reservada
     *
     * Palabras Reservadas:
     * cosa, caja, choose, ciclo, ent, estrato1, estrato6, dec, devolver, durante, meter, saltar, pal, bip, wi, wo, bit
     *
     * @return esPalabraReservada retorna true si es palabra reservada
     */
    private fun esPalabraReservada(): Boolean { // palabra reservada
        var palabra = ""
        val fila = filaActual
        val columna = columnaActual

        if (caracterActual == 'c') {
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
            }else if (caracterActual == 'h'){
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'o') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'o') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 's') {
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
            }else if(caracterActual == 'i'){
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'c') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    if (caracterActual == 'l') {
                        palabra += caracterActual
                        obtenerSgteCaracter()
                        if (caracterActual == 'o') {
                            palabra += caracterActual
                            obtenerSgteCaracter()
                            listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                            return true
                        }
                    }
                }
            }
        }
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

        if (caracterActual == 'p') {
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'a') {
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'l') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            }
        }

        if (caracterActual == 'w'){
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'i'){
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                return true
            }else if(caracterActual == 'o'){
                palabra += caracterActual
                obtenerSgteCaracter()
                listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                return true
            }
        }

        if (caracterActual == 'b'){
            palabra += caracterActual
            obtenerSgteCaracter()
            if (caracterActual == 'i'){
                palabra += caracterActual
                obtenerSgteCaracter()
                if (caracterActual == 'p' || caracterActual == 't') {
                    palabra += caracterActual
                    obtenerSgteCaracter()
                    listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
                    return true
                }
            }
        }

        if (palabra.isNotEmpty()) {
            while (Character.isLetter(caracterActual)) {
                palabra += caracterActual
                obtenerSgteCaracter()
            }

            listaTokens.add(Token(palabra, Categoria.DESCONOCIDO, fila, columna))
            return true
        }
        return false
    }
}