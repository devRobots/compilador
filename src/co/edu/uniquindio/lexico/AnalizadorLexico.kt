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
    val listaErrores: ArrayList<ErrorLexico> = ArrayList()

    /**
     * Elementos necesarios del analizador lexico
     */
    private var caracterActual: Char
    private var posicionActual = 0
    private var filaActual = 0
    private var columnaActual = 0
    private val finCodigo = 0.toChar()

    init {
        if (codigoFuente.isNotEmpty()) {
            caracterActual = codigoFuente[posicionActual]
        }else{
            caracterActual = finCodigo
        }
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
                siguienteCaracter()
                continue
            }
            if (esEntero()) continue
            if (esReal()) continue
            if (esBooleano()) continue

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
                siguienteCaracter()
            }
        }

        procesarErrores()
    }

    /**
     * Metodo que procesa los Tokens desconocidos
     * Y extrae una rutina de errores
     *
     * @return listaErrores La lista con los errores
     */
    private fun procesarErrores() {
        val caracteresDesconocidos = ArrayList<Token>()

        for (token in listaTokens) {
            if (token.categoria == Categoria.DESCONOCIDO) {
                caracteresDesconocidos.add(token)
                //listaTokens.remove(token)
            }
        }

        var ultTope = -1
        for (i in 0 until caracteresDesconocidos.size) {
            if (ultTope < i) {
                val actual = caracteresDesconocidos[i]
                var palabra = actual.palabra

                val tope = obtenerIndiceFinal(i, caracteresDesconocidos)

                for (j in i until tope + 1) {
                    if (j != i) {
                        palabra += caracteresDesconocidos[j].palabra
                    }
                }

                ultTope = tope

                listaErrores.add(ErrorLexico(palabra, actual.fila, actual.columna))
            }
        }
    }

    /**
     * Obtiene el indice del final de la palabra desconocida
     *
     * @param indice El indice en donde empieza la palabra
     * @param caracteres La lista de los caracteres desconocidos
     *
     * @return indice El tope de la palabra desconocida
     */
    private fun obtenerIndiceFinal(indice: Int, caracteres: ArrayList<Token>): Int {
        val fila = caracteres[indice].fila

        var colActual = caracteres[indice].columna
        var final = indice

        for (i in indice + 1 until caracteres.size) {
            if (fila == caracteres[i].fila) {
                if (caracteres[i].columna == colActual + 1) {
                    final = i
                    colActual++
                }
            }
        }

        return final
    }

    /**
     * Metodo que avanza al siguiente caracter
     *
     * Configura las filas y columnas
     * Verifica que no se desborde
     */
    private fun siguienteCaracter() {
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
     * Realiza backtracking para volver hasta una posicion anterior
     *
     * @param posicionInicial La posicion inicial
     * @param fila La fila inicial
     * @param columna La columna inicial
     */
    private fun backtracking(posicionInicial: Int, fila: Int, columna: Int) {
        posicionActual = posicionInicial
        filaActual = fila
        columnaActual = columna

        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * Verifica si la palabra actual es un numero entero
     *
     * @return esEntero retorna true si es entero
     */
    private fun esEntero(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '#') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()

                centinela = true

                while (Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }

                listaTokens.add(Token(palabra, Categoria.ENTERO, fila, columna))
            }

            if (!centinela) {
                backtracking(posicionInicial, fila, columna)
            }
        }

        return centinela
    }

    /**
     * Verifica si la palabra actual es un identificador
     *
     * @return esIdentificador retorna true si es identificador
     */
    private fun esIdentificador(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '@') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isLetter(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()

                while (Character.isLetter(caracterActual) || caracterActual == '_' || Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }

                if (!palabra.endsWith("_")) {
                    listaTokens.add(Token(palabra, Categoria.IDENTIFICADOR, fila, columna))
                    centinela = true
                }
            }

            if (!centinela) {
                backtracking(posicionInicial, fila, columna)
            }
        }
        return centinela
    }

    /**
     * Verifica si la palabra actual es un numero real
     *
     * @return esReal retorna true si es real
     */
    private fun esReal(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '*') {
            palabra += caracterActual
            siguienteCaracter()

            if (Character.isDigit(caracterActual)) {
                palabra += caracterActual
                siguienteCaracter()
                while (Character.isDigit(caracterActual)) {
                    palabra += caracterActual
                    siguienteCaracter()
                }
                if (caracterActual == '.') {
                    palabra += caracterActual
                    siguienteCaracter()

                    if (Character.isDigit(caracterActual)) {
                        palabra += caracterActual
                        siguienteCaracter()

                        centinela = true

                        while (Character.isDigit(caracterActual)) {
                            palabra += caracterActual
                            siguienteCaracter()
                        }

                        listaTokens.add(Token(palabra, Categoria.REAL, fila, columna))
                        siguienteCaracter()
                    }
                }
            }

            if (!centinela) {
                backtracking(posicionInicial, fila, columna)
            }
        }
        return centinela
    }


    /**
     * Verifica si la palabra actual es un operador aritmetico
     *
     * @return esOperadorAritmetico retorna true si es un operador aritmetico
     */
    private fun esOperadorAritmetico(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (evaluarOperadorAritmetico(caracterActual)) {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == '=') {
                backtracking(posicionInicial, fila, columna)
            } else {
                listaTokens.add(Token(palabra, Categoria.OPERADOR_ARITMETICO, fila, columna))
                centinela = true
            }
        }
        return centinela
    }

    /**
     * Verifica si el caracter corresponde a un operador aritmetico
     *
     * @param caracter El caracter a verificar
     *
     * @return boolean retorna true si es un operador aritmetico valido
     */
    private fun evaluarOperadorAritmetico(caracter: Char): Boolean {
        return caracter == '+' || caracter == '-' || caracter == '°' || caracter == '%' || caracter == '/'
    }

    /**
     * Verifica si el caracter corresponde a un caracter especial
     *
     * @param caracter El caracter a verificar
     *
     * @return boolean retorna true si es un caracter especial
     */
    private fun evaluarCaracterEspecial(caracter: Char): Boolean {
        return caracter == '$' || caracter == 'n' || caracter == 'u' || caracter == '"' || caracter == '('
                || caracter == ')' || caracter == 'r' || caracter == 'f' || caracter == 'b' || caracter == 't'
    }

    /**
     * Verifica si la palabra actual es un operador de asignacion
     *
     * @return esOperadorAsignacion retorna true si es un operador de asignacion
     */
    private fun esOperadorAsignacion(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false
        if (caracterActual == '=') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.OPERADOR_ASIGNACION, fila, columna))
            siguienteCaracter()
            centinela = true
        } else if (evaluarOperadorAritmetico(caracterActual)) {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == '=') {
                palabra += caracterActual
                listaTokens.add(Token(palabra, Categoria.OPERADOR_ASIGNACION, fila, columna))
                siguienteCaracter()
                centinela = true
            }
        }

        if (!centinela) {
            backtracking(posicionInicial, fila, columna)
        }

        return centinela
    }

    /**
     * Verifica si la palabra actual es operador de incremento o decremento
     *
     * @return esOperadorIncreDecre retorna true si es operador de incremento o decremento
     */
    private fun esOperadorIncreDecre(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '^') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == '+') {
                palabra += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(palabra, Categoria.OPERADOR_INCREMENTO, fila, columna))
                centinela = true
            } else if (caracterActual == '-') {
                palabra += caracterActual
                siguienteCaracter()
                listaTokens.add(Token(palabra, Categoria.OPERADOR_DECREMENTO, fila, columna))
                centinela = true
            }
        }

        if (!centinela) {
            backtracking(posicionInicial, fila, columna)
        }

        return centinela
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
            listaTokens.add(Token(caracterActual.toString(), Categoria.OPERADOR_LOGICO, fila, columna))
            siguienteCaracter()
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
        if (caracterActual == '[' || caracterActual == ']') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PARENTESIS, fila, columna))
            siguienteCaracter()
            return true
        } else if (caracterActual == '¿' || caracterActual == '?') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.LLAVES, fila, columna))
            siguienteCaracter()
            return true
        } else if (caracterActual == '{' || caracterActual == '}') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.CORCHETES, fila, columna))
            siguienteCaracter()
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
            siguienteCaracter()
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

        if (caracterActual == ',') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.SEPARADOR, fila, columna))
            siguienteCaracter()
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
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '<') {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == '-' || caracterActual == '<') {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == '>' && codigoFuente[posicionActual - 1] == '-') {
                    palabra += caracterActual
                    siguienteCaracter()
                }
                centinela = true
            }
        }
        if (caracterActual == '>') {
            palabra += caracterActual
            siguienteCaracter()
            if (caracterActual == '-' || caracterActual == '>') {
                palabra += caracterActual
                siguienteCaracter()
                centinela = true
            }
        }
        if (caracterActual == '¬') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == '-') {
                palabra += caracterActual
                siguienteCaracter()
                centinela = true
            }
        }

        if (centinela) {
            listaTokens.add(Token(palabra, Categoria.OPERADOR_RELACIONAL, fila, columna))
        } else {
            backtracking(posicionInicial, fila, columna)
        }
        return centinela
    }

    /**
     * Verifica si la palabra actual es un Booleano
     *
     * Palabras Booleanas:
     * .true , .false
     *
     * @return esBooleano retorna true si es una palabra Booleana
     */
    private fun esBooleano(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '.') {
            palabra += caracterActual
            siguienteCaracter()
            if (esComplementoPalabra("true")) {
                palabra += "true"
                centinela = true
            } else if (esComplementoPalabra("false")) {
                palabra += "false"
                centinela = true
            }
        }

        if (centinela) {
            listaTokens.add(Token(palabra, Categoria.BOOLEANO, fila, columna))
        } else {
            backtracking(posicionInicial, fila, columna)
        }
        return centinela
    }


    /**
     * Verifica si la palabra actual es una cadena de caracteres
     *
     * @return esCadena retorna true si es cadena
     */
    private fun esCadena(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == '(') {
            palabra += caracterActual
            siguienteCaracter()
            while (caracterActual != ')'&& caracterActual != '(' && caracterActual != finCodigo) {
                if (esCaracterEspecial()) {
                    palabra += caracterActual
                    siguienteCaracter()
                }else if(caracterActual == '$'){
                    break
                }
                palabra += caracterActual
                siguienteCaracter()
            }
            palabra += caracterActual
            siguienteCaracter()
            if (palabra.endsWith(")") && fila == filaActual){
                centinela = true
            }
        }
        if (centinela) {
            listaTokens.add(Token(palabra, Categoria.CADENA_CARACTERES, fila, columna))
        } else {
            backtracking(posicionInicial, fila, columna)
        }
        return centinela
    }

    /**
     * Verifica si la palabra actual es un caracter
     *
     * @return esCaracter retorna true si es un caracter
     */
    private fun esCaracter(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false
        if (caracterActual == '"') {
            palabra += caracterActual
            siguienteCaracter()
            if (esCaracterEspecial() && fila == filaActual ) {
                palabra += caracterActual
                siguienteCaracter()
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == '"' && fila == filaActual ) {
                    centinela = true
                }
            } else if (caracterActual != '"' && fila == filaActual) {
                palabra += caracterActual
                siguienteCaracter()
                if (caracterActual == '"' && fila == filaActual ) {
                    centinela = true
                }
            }
        }
        if (centinela) {
            palabra += caracterActual
            siguienteCaracter()
            listaTokens.add(Token(palabra, Categoria.CARACTER, fila, columna))
        } else {
            backtracking(posicionInicial, fila, columna)
        }
        return centinela
    }

    /**
     * Metodo para validar si es un caracter especial
     *
     * @return esCaracterEspecial retorna true si es caracter especial
     */
    private fun esCaracterEspecial(): Boolean {
        if (posicionActual + 1 <= codigoFuente.length && caracterActual == '$' && evaluarCaracterEspecial(codigoFuente[posicionActual + 1])) {
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es un comentario
     *
     * @return esComentario retorna true si es comentario
     */
    private fun esComentario(): Boolean {
        if (caracterActual == ':') {
            var palabra = ""
            var terminal = '\n'
            var category = Categoria.COMENTARIO_LINEA
            val fila = filaActual
            val columna = columnaActual
            palabra += caracterActual
            siguienteCaracter()
            // Verifica si es un comentario de bloque o de linea
            if (caracterActual == '/') {
                category = Categoria.COMENTARIO_BLOQUE
                palabra += caracterActual
                siguienteCaracter()
                while (caracterActual != finCodigo && !esTerminalBloque()) {
                    palabra += caracterActual
                    siguienteCaracter()
                }
                palabra += caracterActual
                siguienteCaracter()
            } else {
                while (caracterActual != finCodigo && caracterActual != terminal) {
                    palabra += caracterActual
                    siguienteCaracter()
                }
            }
            palabra += caracterActual
            siguienteCaracter()
            listaTokens.add(Token(palabra, category, fila, columna))
            return true
        }
        return false
    }

    /**
     * Metodo para validar si es un terminal de Comentario de bloque
     */
    private fun esTerminalBloque(): Boolean {
        if (posicionActual + 1 <= codigoFuente.length && caracterActual == '/' && codigoFuente[posicionActual + 1] == ':') {
            return true
        }
        return false
    }

    /**
     * Verifica si hay dos punto o un punto
     *
     * @return esPunto retorna true si es dos punto o un punto
     */
    private fun esPunto(): Boolean {
        val fila = filaActual
        val columna = columnaActual
        if (caracterActual == '|') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.DOS_PUNTOS, fila, columna))
            siguienteCaracter()
            return true
        } else if (caracterActual == ';') {
            listaTokens.add(Token(caracterActual.toString() + "", Categoria.PUNTO, fila, columna))
            siguienteCaracter()
            return true
        }
        return false
    }

    /**
     * Verifica si la palabra actual es una palabra reservada
     *
     * Palabras Reservadas:
     * bip, bit,
     * cosa, caja, control, ciclo,
     * ent, estrato1, estrato6,
     * dec, devolver, durante,
     * meter,
     * pal,
     * saltar,
     * wi, wo
     *
     * @return esPalabraReservada retorna true si es palabra reservada
     */
    private fun esPalabraReservada(): Boolean {
        val posicionInicial = posicionActual
        val fila = filaActual
        val columna = columnaActual

        var palabra = ""
        var centinela = false

        if (caracterActual == 'b') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == 'i') {
                palabra += caracterActual
                siguienteCaracter()

                if (caracterActual == 'p' || caracterActual == 't') {
                    palabra += caracterActual
                    siguienteCaracter()

                    centinela = true
                }
            }

        } else if (caracterActual == 'c') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == 'o') {
                palabra += caracterActual
                siguienteCaracter()

                if (esComplementoPalabra("sa")) {
                    palabra += "sa"
                    centinela = true
                } else if (esComplementoPalabra("ntrol")) {
                    palabra += "ntrol"
                    centinela = true
                }
            } else {
                if (esComplementoPalabra("aja")) {
                    palabra += "aja"
                    centinela = true
                } else if (esComplementoPalabra("iclo")) {
                    palabra += "iclo"
                    centinela = true
                }
            }
        } else if (caracterActual == 'e') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == 's') {
                palabra += caracterActual
                siguienteCaracter()

                if (esComplementoPalabra("trato")) {
                    palabra += "trato"
                    if (caracterActual == '1' || caracterActual == '6') {
                        palabra += caracterActual
                        siguienteCaracter()
                        centinela = true
                    }
                }
            } else if (esComplementoPalabra("nt")) {
                palabra += "nt"
                centinela = true
            }
        } else if (caracterActual == 'd') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == 'e') {
                palabra += caracterActual
                siguienteCaracter()

                if (esComplementoPalabra("volver")) {
                    palabra += "volver"
                    centinela = true
                } else if (esComplementoPalabra("c")) {
                    palabra += "c"
                    centinela = true
                }
            } else if (esComplementoPalabra("urante")) {
                palabra += "urante"
                centinela = true
            }
        } else if (caracterActual == 'm') {
            palabra += caracterActual
            siguienteCaracter()

            if (esComplementoPalabra("eter")) {
                palabra += "eter"
                centinela = true
            }
        } else if (caracterActual == 'p') {
            palabra += caracterActual
            siguienteCaracter()

            if (esComplementoPalabra("al")) {
                palabra += "al"

                centinela = true
            }
        } else if (caracterActual == 's') {
            palabra += caracterActual
            siguienteCaracter()

            if (esComplementoPalabra("altar")) {
                palabra += "altar"
                centinela = true
            }
        } else if (caracterActual == 'w') {
            palabra += caracterActual
            siguienteCaracter()

            if (caracterActual == 'i' || caracterActual == 'o') {
                palabra += caracterActual
                siguienteCaracter()
                centinela = true
            }
        }

        if (centinela) {
            listaTokens.add(Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna))
        } else if (palabra.isNotEmpty()) {
            backtracking(posicionInicial, fila, columna)
        }

        return centinela
    }

    /**
     * Verifica digito a digito que una cadena es parte de una palabra
     *
     * @param palabra La cadena a verificar
     *
     * @return retorna true si es la continuacion de la palabra
     */
    private fun esComplementoPalabra(palabra: String): Boolean {
        var centinela = true

        for (caracter in palabra) {
            if (caracterActual != caracter) {
                centinela = false
                break
            }
            siguienteCaracter()
        }

        return centinela
    }
}