package connectfour

fun main() {
    println("Connect Four")

    println("First player's name:")
    val firstPlayer = readLine()!!

    println("Second player's name:")
    val secondPlayer = readLine()!!

    val (row, col) = getValidDimensions()
    
    val numberOfGames = inputNumberOfGames()

    println("$firstPlayer VS $secondPlayer")
    println("$row X $col board")

    var board = Array(row) { Array(col) { " " } }

    var firstPlayerScores = 0
    var secondPlayerScores = 0

    if (numberOfGames == 1) {
        println("Single game")
        singleGame(board, firstPlayer, secondPlayer, row, col, 1)
    } else {
        println("Total $numberOfGames games")
        var currentNumberOfGames = 1

        while (currentNumberOfGames <= numberOfGames) {
            println("Game #$currentNumberOfGames")
            val res = singleGame(board, firstPlayer, secondPlayer, row, col, currentNumberOfGames)
            when (res) {
                "draw" -> {
                    firstPlayerScores++
                    secondPlayerScores++
                }
                "first" -> firstPlayerScores += 2
                "second" -> secondPlayerScores += 2
            }
            currentNumberOfGames++
            println("Score")
            println("$firstPlayer: $firstPlayerScores $secondPlayer: $secondPlayerScores")
            board = Array(row) { Array(col) { " " } }
        }
    }

    println("Game over!")
}

fun getValidDimensions(): Pair<Int, Int> {
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")

    // e.g. "  6  x    3  ", "5X5" or "  2X  4"
    val regex = """\s*(\d)+\s*[xX]\s*(\d)+\s*""".toRegex()
    val userEntryForDimensions = readLine()!!

    val (row, col) = when {
        regex.matches(userEntryForDimensions) -> regex.find(userEntryForDimensions)!!.destructured.toList().map { s -> s.toInt() }
        userEntryForDimensions.isEmpty() -> listOf(6, 7)
        else -> {
            println("Invalid input")
            return getValidDimensions()
        }
    }

    if (row !in 5..9) {
        println("Board rows should be from 5 to 9")
        return getValidDimensions()
    }

    if (col !in 5..9) {
        println("Board columns should be from 5 to 9")
        return getValidDimensions()
    }

    return Pair(row, col)
}

fun printBoard(board: Array<Array<String>>, row: Int, col: Int) {
    for (i in 1..col) {
        print(" $i")
    }
    println()

    for (i in 0 until row) {
        for (j in 0 until col) {
            print("║${board[i][j]}")
        }
        print("║")
        println()
    }

    print("╚═")
    for (i in 1 until col) {
        print("╩═")
    }
    print("╝")
    println()
}

fun checkWin(board: Array<Array<String>>, row: Int, col: Int): Boolean {
    val player = board[row][col]
    val directions = arrayOf(arrayOf(-1, -1), arrayOf(-1, 0), arrayOf(-1, 1), arrayOf(0, -1), arrayOf(0, 1), arrayOf(1, -1), arrayOf(1, 0), arrayOf(1, 1))

    for (direction in directions) {
        val dx = direction[0]
        val dy = direction[1]

        var count = 0
        var x = row
        var y = col

        while (x in 0 until board.size && y in 0 until board[x].size && board[x][y] == player && count < 4) {
            count++
            x += dx
            y += dy
        }

        if (count == 4) {
            return true
        }
    }

    return false
}

fun inputNumberOfGames(): Int {
    while (true) {
        println("""Do you want to play single or multiple games?
For a single game, input 1 or press Enter
Input a number of games:""")
        val input = readLine()!!
        if (input.isEmpty()) {
            return 1
        }

        if (!isNumber(input)) {
            println("Invalid input")
            continue
        }

        val number = input.toInt()
        if (number < 1) {
            println("Invalid input")
            continue
        }

        return number
    }
}

fun isNumber(s: String): Boolean {
    return Regex("\\d+").matches(s)
}

fun singleGame(board: Array<Array<String>>, firstPlayer: String, secondPlayer: String,
               row: Int, col: Int, currentNumber: Int): String {
    printBoard(board, row, col)

    var currentPlayer = if (currentNumber % 2 != 0) firstPlayer else secondPlayer
    var movesCount = 0
    var statement = ""

    while (true) {
        println("$currentPlayer's turn:")
        var input = readLine()!!

        if (input == "end") {
            break
        }

        val column = input.toIntOrNull()

        if (column == null) {
            println("Incorrect column number")
            continue
        }

        if (column !in 1..col) {
            println("The column number is out of range (1 - $col)")
            continue
        }

        if (board[0][column - 1] != " ") {
            println("Column $column is full")
            continue
        }

        var rowToPlaceDisc = row - 1

        while (board[rowToPlaceDisc][column - 1] != " ") {
            rowToPlaceDisc--
        }

        board[rowToPlaceDisc][column - 1] = if (currentPlayer == firstPlayer) "o" else "*"

        printBoard(board, row, col)

        movesCount++

        if (checkWin(board, rowToPlaceDisc, column - 1)) {
            println("Player $currentPlayer won")
            statement = if (currentPlayer == firstPlayer) "first" else "second"
            break
        } else if (movesCount == row * col) {
            println("It is a draw")
            statement = "draw"
            break
        }

        currentPlayer = if (currentPlayer == firstPlayer) secondPlayer else firstPlayer

    }
    return statement
}