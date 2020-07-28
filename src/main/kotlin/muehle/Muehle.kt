package muehle

//import io.javalin.Javalin
//import io.javalin.core.PathSegment
//import io.javalin.http.Context
import kotlin.math.max
import kotlin.math.min
import java.io.File

class Muehle(val nodes: IntArray, val sel: Int, val turn: Int, val avl: Int,
             val phs: Int, val last: Muehle? = null) : MuehleInterface {
    //n[0...23] represents the nodes
    //sel is current sel
    //avl is number of remaining rounds of inserting(short for avl)
    //phs is the phase: 1 is insert, 0 is regular, -1 take out
    //last holds the previous game position
    companion object {
        val hm = HashMap<Long, Int>()
        var countMoves = 0  //calculated Moves in order for AI to make a single choice
        var start = System.currentTimeMillis()
        var calcDuration = 0L
        var maxDepth = 3
        val timeLimit = -1

        const val f1: Int = 0b000101010001010100010101
        const val f2: Int = 0b010000000100000001000000
        const val f3: Int = 0b000000000000000010101010

        val paths = arrayOf(mapOf(2 to 7, 1 to 1), mapOf(3 to 0, 2 to 9, 1 to 2), mapOf(3 to 1, 2 to 3),
                mapOf(0 to 2, 2 to 4, 3 to 11), mapOf(0 to 3, 3 to 5), mapOf(0 to 13, 1 to 4, 3 to 6),
                mapOf(0 to 7, 1 to 5), mapOf(0 to 0, 1 to 15, 2 to 6), mapOf(1 to 9, 2 to 15),
                mapOf(0 to 1, 1 to 10, 2 to 17, 3 to 8), mapOf(2 to 11, 3 to 9), mapOf(0 to 10, 1 to 3, 2 to 12, 3 to 19),
                mapOf(0 to 11, 3 to 13), mapOf(0 to 21, 1 to 12, 2 to 5, 3 to 14), mapOf(0 to 15, 1 to 13),
                mapOf(0 to 8, 1 to 23, 2 to 14, 3 to 7), mapOf(1 to 17, 2 to 23), mapOf(0 to 9, 1 to 18, 3 to 16),
                mapOf(2 to 19, 3 to 17), mapOf(0 to 18, 1 to 11, 2 to 20), mapOf(0 to 19, 3 to 21),
                mapOf(1 to 20, 2 to 13, 3 to 22), mapOf(0 to 23, 1 to 21), mapOf(0 to 16, 2 to 22, 3 to 15))
//        24 maps, one for each node, constituting a graph, 0 = UP, 1 = RIGHT, 2 = DOWN, 3 = LEFT

//        const val UP = 0
//        const val RIGHT = 1
//        const val DOWN = 2
//        const val LEFT = 3
//        const val WHITE = 1
//        const val BLACK = -1

        init {
            File("src/main/resources/databasePermanent.txt").forEachLine {
                val line = it.split(",")
                hm.put(line[0].toLong(), line[1].toInt())
            }
        }
    }

    val n = IntArray(24)
//    val allPos = ArrayList<Muehle>()
//    val possibleNodes = ArrayList<IntArray>()
//    val m = Muehle(intArrayOf(2), -1, 1, 18, 1, null)

    init {
        repeat(24) {
            n[it] = node(it)
        }
    }

    override fun turn90(): Muehle {
        val nodesTmp = IntArray(24)
        for (i in 0..23) {
            if ((i % 8) > 1) nodesTmp[i] = n[i - 2]
            else nodesTmp[i] = n[i + 6]
        }
        return Muehle(arrayToBits(nodesTmp), sel, turn, avl, phs, this)
    }

    override fun mirror(): Muehle {
        val nodesTmp = IntArray(24)
        for (i in 0..23) {
            if ((i % 8) == 0 || (i % 8) == 4) nodesTmp[i] = n[i + 2]
            else if ((i % 8) == 2 || (i % 8) == 6) nodesTmp[i] = n[i - 2]
            else if ((i % 8) == 3) nodesTmp[i] = n[i + 4]
            else if ((i % 8) == 7) nodesTmp[i] = n[i - 4]
            else nodesTmp[i] = n[i]
        }
        return Muehle(arrayToBits(nodesTmp), sel, turn, avl, phs, this)
    }

    override fun invert(): Muehle {
        val nodesTmp = IntArray(24)
        for (i in 0..23) {
            if (i <= 7) nodesTmp[i] = n[i + 16]
            else if (i >= 16) nodesTmp[i] = n[i - 16]
            else nodesTmp[i] = n[i]
        }
        return Muehle(arrayToBits(nodesTmp), sel, turn, avl, phs, this)
    }

    private fun getHash(): Long {
        var res = 0L

        //arrange board, so equivalent constellations will have same hash
        // --to be implemented--

        //number of tokens to insert
        res += avl
        res = res.shl(4)

//        //current phase
//        if (phs == -1) res += 2
//        else res += phs
//        res = res.shl(2)

        //tokens of both players, opponent first
        for (t in intArrayOf(-turn, turn)) {
            for (i in 0..23) {
                res = res.shl(1)
                if (n[i] == t) res++
            }
        }
        return res
    }

    private fun getHashes(): List<Long> {
        //generate hashcodes equivalent by symmetry
        val hashes = ArrayList<Long>()
        var m = this
        repeat(2) {
            repeat(2) {
                repeat(4) {
                    hashes.add(m.getHash())
                    m = m.turn90()
                }
                m = m.mirror()
            }
            m = m.invert()
        }
        return hashes
    }

    private fun setHash(value: Int) {
        //puts same entry for 16 different hashcodes due to transformation
        //4 different angles, mirrored and not mirrored, inverted and not inverted
        hm[getHash()] = value
//        var m = this
//        repeat(2) {
//            repeat(2) {
//                repeat(4) {
//                    hm[m.getHash()] = value
//                    m = m.turn90()
//                }
//                m = m.mirror()
//            }
//            m = m.invert()
//        }
    }

    fun randomGame(maxMoves: Int): Int {
        //returns the winner, if game isn't finished maximum amount of moves returns 0
        var m = this
        var moves = 0
        while (!m.gameOver() || moves == maxMoves) {
            moves++
            m = m.play(m.randomMove())
        }
        if (!m.gameOver()) return 0
        return -m.turn
    }

    override fun monteCarlo(): Int {
        var samples = 100
        var sum = 0

        if (gameOver()) return -turn * 100000

        repeat(samples) {
            val winner = randomGame(100)
            sum += winner * 100000
        }
        return sum / samples
    }

    override fun alphabeta(depth: Int, alpha: Int, beta: Int, maximizingPlayer: Boolean): Int {
//        println("alphabeta")
//        val maximizingPlayer = if (node.turn == 1) true else false
        countMoves++    //keep track of calculated possibilities

//        println("Turn: " + turn + " Depth: " + depth)
//        println(stats())
//        if (gameOver()) return turn * -100000

        var checking = 1000000000

        val hashes = getHashes()
        if (depth == 1) {
            //search for hash for this or equivalent position
            hashes.forEach {
                hm[it]?.let { value ->
                    checking = value * turn
                    return value * turn
                }
            }
//            println("noHashAvailable")  //for testing
        }

        //bottom part of function, is only executed if no equivalent position has a saved value yet
        var value: Int
        var alpha = alpha
        var beta = beta

        if (depth == maxDepth || gameOver()) {
            val value = heuristicEval()
//            val value = monteCarlo()
            return value * (1 + maxDepth - depth)
        }

        if (maximizingPlayer) {
            value = -1000000
            for (move in possibleMoves()) {
                value = max(value, play(move).alphabeta(depth + 1, alpha, beta, !maximizingPlayer))
                alpha = max(alpha, value)
                if (alpha >= beta)
                    break
            }
            if (checking != 1000000000 && checking != value)
                println("Hashing Error! Expected: $checking Value: $value")
            if (depth == 1) {
                hm[hashes[0]] = value
            }
            return value
        } else {
            value = 1000000
            for (move in possibleMoves()) {
                value = min(value, play(move).alphabeta(depth + 1, alpha, beta, !maximizingPlayer))
                beta = min(beta, value)
                if (beta <= alpha)
                    break
            }
//            println("min: $value")
            if (checking != 1000000000 && checking != value)
                println("Hashing Error! $checking Value: $value")
            if (depth == 1) {
                hm[hashes[0]] = -value
            }
            return value
        }
    }

//    fun evaluate(turn: Int, depth: Int): Int {
//        countMoves++
//
//        //generate hashcode
//        var hash: Long
//        val depth = if (phs == -1) depth - 1 else depth
//        var m = this
//        repeat(2) {
//            repeat(2) {
//                repeat(4) {
//                    hm[m.getHash()]?.let { value ->
//                        return value * turn
//                    }
//                    m = m.turn90()
//                }
//                m = m.mirror()
//            }
//            m = m.invert()
//        }
//
//        //limit evaluation to prevent overflow
//        if (gameOver() || depth==maxDepth ||
//                (timeLimit!=-1 && System.currentTimeMillis()-start > timeLimit))
//            return turn * heuristicEval()
//
//        val next = play(getBestMove(depth + 1))
//        val hashValue = next.evaluate(next.turn, depth + 1) * turn
//        println("reached setHash")
//        setHash(hashValue)
//        return turn * hashValue
//    }

    override fun heuristicEval(): Int {
        //gameOver:             -100000
        //each token ahead:      +10000
        //each row of 2:          +3000/-2000
        //each row of 1:          +1000/-1000
        //? each double mill:      +30000 ?
//        val hashes = getHashes()
//        hashes.forEach {
//            hm[it]?.let { value ->
//                return value * turn
//            }
//        }
        if (gameOver()) return -100000 * turn

        var res = (countByColor(1) - countByColor(-1)) * 10000

        var numOfRows = countAllRows(1)
        res += numOfRows[0] * 100
        res += numOfRows[1] * 3000
        numOfRows = countAllRows(-1)
        res -= numOfRows[0] * 100
        res -= numOfRows[1] * 2000

        if (avl%2 == 1) res += turn*10000
        if (phs == -1 && avl == 0) res += 10000 * turn
        return res
    }

//    fun getBestMove(): Move {
//
//        return bestMove
//    }

    override fun undo(): Muehle {
        if (last == null) return this
        if (last.last == null) return last
        return last.last
    }

    override fun play(m: Move): Muehle {
        if (m.take == -2) return this     //take = -2 indicates an empty move, nothing is done

        val nodesTmp = nodes.copyOf()
        val player = if (turn == 1) 0 else 1
        val opponent = if (player == 0) 1 else 0

        if (m.from != -1) nodesTmp[if (m.to == -1) opponent else player] -= 1.shl(m.from)
        if (m.to == -1) return Muehle(nodesTmp, -1, -turn, avl, if (avl > 1) 1 else 0, this)
        nodesTmp[player] += 1.shl(m.to)
        //if included in Move, also take out enemy token
        if (m.take != -1) nodesTmp[opponent] -= 1.shl(m.take)

        var res = Muehle(nodesTmp, -1, -turn, avl - if (avl != 0) 1 else 0, if (avl > 1) 1 else 0, this)
        //if row of three is formed, don't change turn and enter phs -1
        if (m.take == -1)
            if (res.rowOfThree(m.to)) res = Muehle(res.nodes, res.sel, -res.turn, res.avl, -1, this)

        return res
    }

    override fun randomMove(): Move {
        val pMoves = possibleMoves()
        if (pMoves.isEmpty()) return Move(-1, -1, -2)
        return pMoves.random()
    }

    override fun bestMove(): Move {
        val pM = possibleMoves()
        if (pM.isEmpty()) return Move(-1, -1, -2)
        var bestMove: Move = pM.random()
        var bestVal = -1000000

        countMoves = 0
        start = System.currentTimeMillis()

        pM.forEach {
            val next = play(it)
            val thisVal = noise((if (turn == 1) 1 else -1) * next.alphabeta(1, -1000000, 1000000, next.turn == 1))
//            val thisVal = noise((if (turn==1) 1 else -1) * next.monteCarlo())
            println("$it / $thisVal");
            if (thisVal > bestVal) {
                bestMove = it
                bestVal = thisVal
            }
        }
        calcDuration = System.currentTimeMillis() - start
//        println("Calculated $countMoves Moves in ${calcDuration}ms")
        return bestMove
    }

    private fun playAndTake(move: Move): List<Move> {
        val moves = ArrayList<Move>()
        for (i in 0..23) {
            if (n[i] == -turn && (!rowOfThree(i) || isAllMills())) moves.add(Move(move.from, move.to, i))
        }
        return moves
    }

    private fun possibleMoves(): List<Move> {
        val pMoves = arrayListOf<Move>()
        if (phs == 1) {
            for (i in 0..23) {
                if (n[i] != 0) continue
                val currentMove = Move(-1, i)
                if (!play(currentMove).rowOfThree(i)) pMoves.add(currentMove)
                else pMoves.addAll(playAndTake(currentMove))
            }
        }
        if (phs == -1) {
            for (i in 0..23) {
                if (n[i] == -turn && !rowOfThree(i)) pMoves.add(Move(i, -1))
            }
            if (pMoves.isEmpty())
                for (i in 0..23) {
                    if (n[i] == -turn) pMoves.add(Move(i, -1))
                }
        }
        if (phs == 0) {
            if (countByColor(turn) < 3) return pMoves
            if (countByColor(turn) > 3)
                for (i in 0..23) {
                    if (n[i] == turn) paths[i].values.forEach {
                        if (n[it] == 0) {
                            val currentMove = Move(i, it)
                            if (!play(currentMove).rowOfThree(it)) pMoves.add(currentMove)
                            else pMoves.addAll(playAndTake(currentMove))
                        }
                    }
                }
            else
                for (i in 0..23) {
                    if (n[i] == turn) for (j in 0..23) {
                        if (n[j] == 0) {
                            val currentMove = Move(i, j)
                            if (!play(currentMove).rowOfThree(j)) pMoves.add(currentMove)
                            else pMoves.addAll(playAndTake(currentMove))
                        }
                    }
                }
        }
        return pMoves
    }

    override fun click(node: Int): Muehle {

        //used when page is refreshed
        if (node == -1 || gameOver()) return this

        //Insert
        if (phs == 1 && n[node] == 0) {
            return this.play(Move(-1, node))
        }

        //Take out
        if (phs == -1 && n[node] == -turn && (!rowOfThree(node) || isAllMills())) {
            return this.play(Move(node, -1))
        }

        //Move selected token
        if (sel != -1 && n[node] == 0 && (paths[sel].containsValue(node) || countByColor(turn) <= 3)) {
            return play(Move(sel, node))
        }

        //Select own Token
        if (phs == 0 && n[node] == turn)
            return Muehle(nodes, node, turn, avl, phs, this)

        //Deselect
        return Muehle(nodes, -1, turn, avl, phs, this)

    }

    private fun rowOfThree(node: Int): Boolean {
        //return true if node is part of a row of three
        if (n[node] == 0) return false
        val turn = n[node]
        for (i in 0..1) {    //0 = vertikal, 1 = horizontal
            if (adj(node, i) == turn) {
                if (adj(paths[node][i]!!, i) == turn) return true
                if (adj(node, i + 2) == turn) return true
            }
            if (adj(node, i + 2) == turn) {
                if (adj(paths[node][i + 2]!!, i + 2) == turn) return true
            }
        }
        return false
    }

    private fun rowOfThreeBit(color: Int): Boolean {
//        val own = if (color==1) white else black
//        if (own.and(own.shr(1)).and(own.shr(2)).and(f1)>0) return true
//        if (own.and(own.shr(1)).and(own.shl(6)).and(f2)>0) return true
//        if (own.and(own.shr(8)).and(own.shr(16)).and(f3)>0) return true
        return false
    }

    private fun countAllRows(color: Int): IntArray {
        val allRows = IntArray(3)
        val countAllRows = IntArray(3)
        val occOpp = occupiedRows(-color)

        //rowsOfThree
        allRows[2] = rowsOfThree(color)

        //rowsOfTwo
        allRows[1] = rowsOfTwo(color, rowsOfZero(color), occOpp)

        //rowsOfOne
        allRows[0] = rowsOfOne(color, rowsOfThree(color), occOpp)

        //count
        repeat(3) {
            countAllRows[it] = countBits(allRows[it])
        }

        return countAllRows
    }

    private fun rowsOfZero(color: Int): Int {
        val own = if (color == 1) nodes[0] else nodes[1]
        return own.or(own.shr(1)).or(own.shr(2)).inv().and(f1) +
                own.or(own.shr(1)).or(own.shl(6)).inv().and(f2) +
                own.or(own.shr(8)).or(own.shr(16)).inv().and(f3)
    }

    private fun rowsOfThree(color: Int): Int {
        val own = if (color == 1) nodes[0] else nodes[1]
        return own.and(own.shr(1)).and(own.shr(2)).and(f1) +
                own.and(own.shr(1)).and(own.shl(6)).and(f2) +
                own.and(own.shr(8)).and(own.shr(16)).and(f3)
    }

    private fun rowsOfTwo(color: Int, rowsOfZero: Int, occupiedByOpponent: Int): Int {
        val own = if (color == 1) nodes[0] else nodes[1]
        val inv = own.inv()
        return rowsOfZero.inv().and(
                (inv.xor(inv.shr(1)).xor(inv.shr(2))).and(f1).and(occupiedByOpponent.inv()) +
                        (inv.xor(inv.shr(1)).xor(inv.shl(6))).and(f2).and(occupiedByOpponent.inv()) +
                        (inv.xor(inv.shr(8)).xor(inv.shr(16))).and(f3).and(occupiedByOpponent.inv()))
    }

    private fun rowsOfOne(color: Int, rowsOfThree: Int, occupiedByOpponent: Int): Int {
        val own = if (color == 1) nodes[0] else nodes[1]
        return rowsOfThree.inv().and(
                own.xor(own.shr(1)).xor(own.shr(2)).and(f1).and(occupiedByOpponent.inv()) +
                        own.xor(own.shr(1)).xor(own.shl(6)).and(f2).and(occupiedByOpponent.inv()) +
                        own.xor(own.shr(8)).xor(own.shr(16)).and(f3).and(occupiedByOpponent.inv()))
    }

    private fun countByColor(color: Int): Int {
        var res = 0
        for (i in 0..23) {
            if (n[i] == color) res++
        }
        return res
    }

    private fun isAllMills(): Boolean {
        for (i in 0..23) {
            if (n[i] == -turn && !rowOfThree(i)) return false
        }
        return true
    }

    private fun occupiedRows(color: Int): Int {
        //returns a Integer representation of rows with 1 or more tokens of the given color
        val own = if (color == 1) nodes[0] else nodes[1]
        val res = own.or(own.shr(1)).or(own.shr(2)).and(f1) + own.or(own.shr(1)).or(own.shl(6)).and(f2) +
                own.or(own.shr(8)).or(own.shr(16)).and(f3)
        return res
    }

    override fun countTokens(player: Int): Int {
        var res = 0
        n.forEach {
            if (it == player) res++
        }
        return res
    }

    override fun gameOver(): Boolean {
        if (phs != 1 && countByColor(turn) < 3) return true
        if (possibleMoves().isEmpty()) return true
        return false
    }

    private fun arrayToBits(values: IntArray): IntArray {
        // phs-turn-23-22-21-20-19-18-17-16-15-14-13-12-11-10-09-08-07-06-05-04-03-02-01-00
        //   0    0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0
        val res = IntArray(2)
        for (i in 0..23) {
            res[0] = res[0].shl(1)
            res[1] = res[1].shl(1)
            if (values[23 - i] == 1) res[0] += 1
            if (values[23 - i] == -1) res[1] += 1
        }
        return res
    }

    override fun toString(): String {
        var res = ""
        repeat(24) {
            res += "${n[it]},"
        }
        val black = countTokens(-1)
        val white = countTokens(1)
        val eval = heuristicEval()
        res += "$sel,$turn," + (if (gameOver()) (if (-turn == 1) 1 else 2) else 0) +
                ",$phs" + ",$white,$black,$eval,$countMoves,$calcDuration,${hm.size},$maxDepth"
        return res
    }

    fun stats(): String {
//        println("node0: ${nodeAsBinary(0, 1)}")
//        println("node1: ${nodeAsBinary(1, -1)}")
//        println("node7: ${nodeAsBinary(7, -1)}")
//        O--------O--------O   turn:
//        |  O-----O-----O  |   phase:
//        |  |  O--O--O  |  |   available:
//        O--O--O     O--O--O   heuristic evaluation:
//        |  |  O--O--O  |  |
//        |  O-----O-----O  |
//        O--------O--------O
        val t = CharArray(24)
        n.forEachIndexed { i, it ->
            if (it == -1) t[i] = 'B'
            if (it == 0) t[i] = 'O'
            if (it == 1) t[i] = 'W'
        }
        val numOfRows = countAllRows(turn)
        return "${t[0]}--------${t[1]}--------${t[2]}   active player: $turn\n" +
                "|  ${t[8]}-----${t[9]}-----${t[10]}  |   phase: $phs\n" +
                "|  |  ${t[16]}--${t[17]}--${t[18]}  |  |   available: $avl\n" +
                "${t[7]}--${t[15]}--${t[23]}     ${t[19]}--${t[11]}--${t[3]}   heuristic evaluation:   ${heuristicEval()}\n" +
                "|  |  ${t[22]}--${t[21]}--${t[20]}  |  |   rows of 3: ${numOfRows[2]}\n" +
                "|  ${t[14]}-----${t[13]}-----${t[12]}  |   rows of 2: ${numOfRows[1]}\n" +
                "${t[6]}--------${t[5]}--------${t[4]}   rows of 1: ${numOfRows[0]}\n"
    }

//    fun takable(node: Int): Boolean {
//        if (n[node] == -turn && (!rowOfThree(node) || isAllMills()))
//            return true
//        return false
//    }

    private fun adj(node: Int, dir: Int): Int {
        return if (paths[node].containsKey(dir)) n[paths[node][dir] ?: error("paths[node][dir] is null!")]
        else 0
    }

    private fun node(i: Int, color: Int = 0): Int {
        if (color == 0) {
            if (nodes[0].and(1.shl(i)) > 0) return 1
            if (nodes[1].and(1.shl(i)) > 0) return -1
        }
        if (color == 1 && nodes[0].and(1.shl(i)) > 0) return 1
        if (color == -1 && nodes[1].and(1.shl(i)) > 0) return 1
        return 0
    }

//    fun nodeAsBinary(i: Int, color: Int = 0): Int {
//        return if (node(i, color) == 1) nodeToBinary(i) else 0
//    }

    private fun nodeToBinary(i: Int): Int {
        return 1.shl(i)
    }

    private fun countBits(bits: Int): Int {
        var count = 0
        var tmp = bits
        while (tmp != 0) {
            count += tmp % 2
            tmp = tmp.shr(1)
        }
        return count
    }

//    fun allPossiblePositions(): List<Muehle> {
//
//        var n = IntArray(2)
//        var avl = 18            //0-18
//        var phs = 1             //-1, 0, 1
//        var turn = 1            //-1, 1
//
//        for (avl in 0..18) {
//            for (turn in intArrayOf(-1, 1)) {
//
//                    for (color in intArrayOf(-1, 0, 1)) {
//                        for (node in 0..23) {
//                        n[node] = color
//                    }
//                }
//            }
//        }
//        return allPositions
//    }

//    fun buildPos() {
//        possibleNodes.forEach { it ->
    //all Pos with avl>0
//            if (it[24] > 0)
//                for (turn in intArrayOf(-1, 1)) {
//                    for (phs in intArrayOf(-1, 1))
//                        allPos.add(Muehle(it.copyOfRange(0, 23), -1, turn, it[24], phs, null))
//                }

    //all Pos with avl=0
//            else {
//                for (turn in intArrayOf(-1, 1)) {
//                    for (phs in intArrayOf(-1, 0))
//                        allPos.add(Muehle(it.copyOfRange(0, 23), -1, turn, 0, phs, null))
//                }
//            }
//        }
//    }

//    fun buildField(n: IntArray, pos: Int, white: Int, black: Int) {
////        var turn = 1
////        var avl = 18
////        var phs = 1
////        if (pos==24) list.add(Muehle(arrayToBits(n),-1,turn,avl,phs,null))
//        if (pos == 24) {
//            possibleNodes.add(intArrayOf(*n, avl, black, white))
//            return
//        }
//
//        for (avl in 0..1) {
//            if (white + avl / 2 < 9) {
//                n[pos] = 1
//                buildField(n, pos + 1, white + 1, black)
//            }
//
//            if (black + avl / 2 < 9) {
//                n[pos] = -1
//                buildField(n, pos + 1, white, black)
//            }
//
//            n[pos] = 0
//            buildField(n, pos + 1, white, black)
//        }
//    }

    override fun evaluateRandom(limit: Int) {
        if (limit == 0) return
        alphabeta(1, -1000000, 1000000, turn == 1)
        val next: Muehle
        if (!gameOver()) {
            next = play(randomMove())
            next.evaluateRandom(limit - 1)
        } else heuristicEval()
    }

    fun buildEndgame(nodes: IntArray, node: Int, remW: Int, remB: Int, list: ArrayList<IntArray>) {
        if (node == 24) {
//            if ( !list.contains( intArrayOf(n[1],n[0]) ) )
            list.add(intArrayOf(nodes[0], nodes[1]))
            return
        }
        if (remW > 0) {
            val white = nodes[0].shl(1) + 1
            val black = nodes[1].shl(1)
            buildEndgame(intArrayOf(white, black), node + 1, remW - 1, remB, list)
        }
        if (remB > 0) {
            val white = nodes[0].shl(1)
            val black = nodes[1].shl(1) + 1
            buildEndgame(intArrayOf(white, black), node + 1, remW, remB - 1, list)
        }
        if (24 - node > remW + remB) {
            val white = nodes[0].shl(1)
            val black = nodes[1].shl(1)
            buildEndgame(intArrayOf(white, black), node + 1, remW, remB, list)
        }
    }

    fun endgame() {
        //get all positions with 3 tokens each player during endgame
        val positions = ArrayList<IntArray>()

        buildEndgame(IntArray(2), 0, 3, 3, positions)
        println(positions.size)

        for (j in 0..26918) {
            for (i in 0..9) {
                for (k in 0..9) {
                    Muehle(positions[j * 100 + i*10 + k], -1, 1, 0, 0).bestMove()
                    print("${i*10 + k + 1}% ")
                    if ((i*10 + k + 1) % 10 == 0) println()
                }
                saveData()
            }
            saveData()
            println("Finished 100 more! Total of ${(j + 1) * 100} positions!")
        }

//        val n = IntArray(24)
//        repeat(6) {
//            while(true) {
//                n[it] = (0..23).random()
//                for(i in (0..(it-1))) {
//                    if (n[it]==n[i]) continue
//                    break
//                }
//            }
//        }
    }

    override fun buildDatabase() {
        val possMoves = possibleMoves()
        possMoves.forEach {
            start = System.currentTimeMillis()
            evaluateRandom(10000)
        }
        saveData()
    }

    override fun saveData() {
        //save HashMap to .txt
        var fileName = "src/main/resources/databasePermanent.txt"
        var database = File(fileName)

        database.printWriter().use { out ->
            for (el in hm) {
                out.println("${el.key},${el.value}")
            }
        }

        //backup
        fileName = "src/main/resources/databasePermanentBackup.txt"
        database = File(fileName)

        database.printWriter().use { out ->
            for (el in hm) {
                out.println("${el.key},${el.value}")
            }
        }
        println("Saved ${hm.keys.size} entries to database")
    }

    override fun emptyHashmap() {
        hm.clear()
    }

    private fun noise(value: Int): Int = value + ((0..200).random() - 100)

}

class Move(val from: Int, val to: Int, val take: Int = -1) {
    override fun toString(): String {
        return "Move: $from -> $to ${if (take != -1) "(" + take + ")" else ""}"
    }
}   //from node to node, -1 is outside game
