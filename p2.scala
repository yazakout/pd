  import scala.collection.mutable.{Map, SynchronizedMap, HashMap}

class pd {
      
  object pd {
    val symTable : Map[String, Int] = Map()
    var currName : String = ""
    var index : Int = 0
    var programText : String = ""
    var numNames : Int = 0
    

      def get(id:String)  : Int = {
          return symTable.getOrElse(id, -1)
      }
    
      def set(id:String, value:Int) {
          symTable + (id -> value)
      }
      
      def consume(i:Int) {
        while(isSpace())
          index += 1 /**The lack of "++" is disappointing*/
      }
      
      def error() {
        println(programText.substring(index))
        
      }
      
      
      
      def getInt() : Int = {
        var currentNum : Int = -1
        var index : Int = 0
        while (programText.charAt(index).isDigit || programText.charAt(index).equals("_")) {
          if (programText.charAt(index).isDigit) {
            if (currentNum < 0)
              currentNum = programText.charAt(index) - '0'
            else
              currentNum = currentNum * 10 + (programText.charAt(index) - '0')
          }
          index += 1
        }
        currentNum
      }
      
      def isId() : Boolean = {
        var i : Int = 0;
        while (programText.charAt(i).isLetterOrDigit && (index != 0)) 
          i += 1
        !((i == 0) && programText.substring(index, i).equals("else")
            && programText.substring(index, i).equals("while")
            && programText.substring(index, i).equals("if"))
      }
      
      def getId() : String = {
        var i : Int = index;
        while (programText.charAt(i).isLetterOrDigit) {
          
        }
        var n : String = programText.substring(index, i)
        if (n.equals("else") || n.equals("if") || n.equals("while")) 
          0
        n
      }
      
      def isLeftBlock() : Boolean = {
        true
      }
      
      def isInt() : Boolean = {
        programText.charAt(index).isDigit
      }
      
      def expression() : Int = {
        1
      }
      
      def e3() : Int = {
        var value : Int = e2()
        while (isPlus()) {
          consume(1)
          value = value + e2()
        }
        value
      }
      
      def e4 () : Int = {
        var value : Int = e3()
        while (isEqEq()) {
          consume(2)
          if (value == e3())
            value = 1
          else 
            value = 0
        }
        return value
      }
      
      def statement(value:Int) : Int = {
        if(isId()) {
          val id : String = getId()
          consume(id.length())
          if (!isEq())
            error()
          consume(1)
          val v : Int = expression()
          if (value == 1)
            set(id, v)
          if (isSemi())
            consume(1)
          1
        } else if (isLeftBlock()) {
            consume(1)
            seq(value)
            if (!isRightBlock())
              error()
            1
        } else if (isIf()) {
          consume(2)
          var c : Int = expression()
          if ((c != 0) && (value != 0))
             statement(1)
          else
             statement(0)
          if (isElse()) {
            consume(4)
            if ((c == 0) && (value == 0)) 
                statement(1)
            else
                statement(0)
            0
          }
          1
        } else if (isWhile()) {
          consume(5)
          var progTextCopy : String = programText.substring(index)
          var c : Int = 0
          do {
            programText = progTextCopy
            c = expression()
            if (c != 0)
              statement(1)
            else 
              statement (0)
          } while ((c != 0) && (value != 0))
          1
         } else if (isSemi()) {
           consume(1)
           1
         } else 
           0
      }
      
      def seq(value:Int) {
        while(statement(value) == 1) {}
      }
      
      def program() {
        seq(1)
        if (!isEnd())
          error()
      }
      
      def interpret(prog:String) {
        program()
        numNames = 20;
      }
      
      def main (args: String){
        programText = args.replace(" ", "");
        interpret(args)
      }
    }
 }