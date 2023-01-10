package muehle

interface MuehleInterface {

    //transform board
    fun turn90(): Muehle
    fun mirror(): Muehle
    fun invert(): Muehle

    //actions
    fun click(node: Int): Muehle
    fun undo(): Muehle
    fun play(m: Move): Muehle

    //get a Move
    fun randomMove(): Move
    fun bestMove(): Move

    //evaluation
    fun alphabeta(depth: Int, alpha: Int, beta: Int, maximizingPlayer: Boolean): Int
    fun gameOver(): Boolean
    fun critEval(): Int
    fun monteCarlo(): Int
    fun countTokens(color: Int): Int

    //create Database
    fun evaluateRandom(limit: Int)
    fun buildDatabase()
    fun saveData()
    fun loadData(filename: String)
    fun emptyHashmap()

    //modified toString() function
    override fun toString(): String
}