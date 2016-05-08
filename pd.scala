  import scala.collection.mutable.{Map, SynchronizedMap, HashMap}

      
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
          println(id + ":" + value)
          symTable + (id -> value)
      }
 
      def consume(i:Int) {
        while(isSpace())
          index += 1 /**The lack of "++" is disappointing*/
        index += i
      }
      
      def error() {
        println("Error at " + programText.substring(index));
	System.exit(1)
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
	println("getId")
        var i : Int = index;
        while (programText.charAt(i).isLetterOrDigit && i <= (programText.length() - 1)) {
         i+=1 
        }
        var n : String = programText.substring(index, i)
        if (n.equals("else") || n.equals("if") || n.equals("while")) 
          0
        n
      }
      
      def isLeftBlock() : Boolean = {
        programText.charAt(index) == '{'
      }

      def isRightBlock() : Boolean = {
        programText.charAt(index) == '}'
      }
      
      def isMul() : Boolean = {
        programText.charAt(index).equals('*')
      }
      
      def isPlus() : Boolean = {
        programText.charAt(index) == '+'
      }
      
      def isSpace() : Boolean = {
        programText.charAt(index) == ' '
      }
      
      def isEnd() : Boolean = {
	index == programText.length() - 1
      }

      def isWhile() : Boolean = {
        programText.substring(index, index + 5).equals("while")
      }

      def isIf() : Boolean = {
        programText.substring(index, index + 2).equals("if")
      }

      def isElse() : Boolean = {
        programText.substring(index, index + 4).equals("else")
      }

      def isSemi() : Boolean = {
        programText.charAt(index) == ';'
      }

      def isEq() : Boolean = {
        programText.charAt(index) == '='
      }

      def isEqEq() : Boolean = {
        programText.charAt(index) == '=' && programText.charAt(index) == '='
      }

      def isLeft() : Boolean = {
        programText.charAt(index) == '('
      }

      def isRight() : Boolean = {
        programText.charAt(index) == ')'
      }
      
      def isInt() : Boolean = {
        programText.charAt(index).isDigit
      }
      
      def getIntLength() : Int = {
	var spaces : Int = 0
	var i : Int = 0
        while (programText.charAt(index + i).isDigit || programText.charAt(index + i) == '_') {
		spaces += 1
		i += 1
	}
	spaces
      }
      def expression() : Int = {
	println("expression")
	e4()
      }
      
      def e1() : Int = {
        if (isLeft()) {
          consume(1)
          var v :Int = expression()
          if (!isRight()) 
            error()
          consume(1)
          v
        } else if (isInt()) {
          var v : Int = getInt()
	  var spaces : Int = getIntLength()
	  consume(spaces)
          v
        } else if (isId()) {
          var id : String = getId()
          consume(id.length()) 
          return get(id)
        } else error
        0
      }
      
      def e2() : Int = {
        var value : Int = e1()
        while (isMul()) {
          consume(1)
	println(value)
          value = value * e1()
        }
        value
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
		println("isId")
          val id : String = getId()
	println("returned")
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
		println("isLeft")
            consume(1)
            seq(value)
            if (!isRightBlock())
              error()
            1
        } else if (isIf()) {
		println("isIf")
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
		println("isWhile")
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
	println("isSemi")	
           consume(1)
           1
         } else 
           0
      }
      
      def seq(value:Int) {
	println("seq")
        while(statement(value) == 1) {}
      }
      
      def program() {
	println("program")
        seq(1)
        if (!isEnd())
          error()
      }
      
      def interpret(prog:String) {
	println("interpret")
        program()
        numNames = 20;
      }
      
      def main (args: Array[String]) : Unit = {
        programText = args(0).replace(" ", "")
	println(programText)
        interpret(args(0))
      }
    }

