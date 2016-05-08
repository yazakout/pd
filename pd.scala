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
          symTable put (id, value)
      }
 
      def consume(i:Int) {
        while(isSpace())
          index += 1 /**The lack of "++" is disappointing*/
        index += i
	while(isSpace())
	  index+=1
      }
      
      def error() {
        println("Error at " + programText.substring(index));
	System.exit(1)
      }
      
      def getInt() : Int = {
        var currentNum : Int = -1
        var i : Int = 0
        while ( ((index + i) < programText.length()) && 
		programText.charAt(index + i).isDigit 
		|| ( ((index + i) < programText.length()) && 
			(programText.charAt(index + i) == '_'))) {
          if (programText.charAt(index + i).isDigit) {
            if (currentNum < 0) {
              currentNum = programText.charAt(index + i) - '0'
           } else {
              currentNum = currentNum * 10 + (programText.charAt(index + i) - '0')
		}
          }
          i += 1
        }
        return currentNum
      }
      
      def isId() : Boolean = {
	if (isEnd())
		return false
        var i : Int = 0;
        while (programText.charAt(index + i).isLetter || (programText.charAt(index + i).isDigit && (index != 0)))  
          i += 1
	return (!((i == 0) ||  programText.substring(index, index + i).equals("else")
            || programText.substring(index, index + i).equals("while")
            || programText.substring(index, index + i).equals("if") || programText.substring(index, index + i).equals("")))
      }
      
      def getId() : String = {
        var i : Int = index;
        while (programText.charAt(i).isLetterOrDigit && i <= (programText.length() - 1)) {
         i+=1 
        }
        var n : String = programText.substring(index, i)
        if (n.equals("else") || n.equals("if") || n.equals("while")) 
          return ""
        return n
      }
      
      def isLeftBlock() : Boolean = {
        (!isEnd() && programText.charAt(index) == '{')
      }

      def isRightBlock() : Boolean = {
        (!isEnd() && programText.charAt(index) == '}')
      }
      
      def isMul() : Boolean = {
        (!isEnd() && programText.charAt(index) == '*')
      }
      
      def isPlus() : Boolean = {
        (!isEnd() && programText.charAt(index) == '+')
      }
      
      def isSpace() : Boolean = {
        (!isEnd() && programText.charAt(index) == ' ')
      }
      
      def isEnd() : Boolean = {
	index == programText.length()
      }

      def isWhile() : Boolean = {
        (!isEnd() && !isId() && !(index + 5 >= programText.length()) && programText.substring(index, index + 5).equals("while"))
      }

      def isIf() : Boolean = {
        (!isEnd() &&  !isId() && !(index + 2 >= programText.length()) && programText.substring(index, index + 2).equals("if"))
      }

      def isElse() : Boolean = {
        (!isEnd() && !isId() && !(index + 4 >= programText.length()) && programText.substring(index, index + 4).equals("else"))
      }

      def isSemi() : Boolean = {
        (!isEnd() && programText.charAt(index) == ';')
      }

      def isEq() : Boolean = {
        (!isEnd() &&  !isEqEq() && programText.charAt(index) == '=')
      }

      def isEqEq() : Boolean = {
        (!isEnd() &&  !(index + 2 >= programText.length()) && programText.charAt(index) == '=' 
			&& programText.charAt(index + 1) == '=')
      }

      def isLeft() : Boolean = {
        (!isEnd() && programText.charAt(index) == '(')
      }

      def isRight() : Boolean = {
        (!isEnd() && programText.charAt(index) == ')')
      }
      
      def isInt() : Boolean = {
        (!isEnd() && programText.charAt(index).isDigit)
      }
      
      def getIntLength() : Int = {
	var spaces : Int = 0
	var i : Int = 0
        while ( (index + i) < programText.length() && (programText.charAt(index + i).isDigit 
			|| programText.charAt(index + i) == '_')) {
		spaces += 1
		i += 1
	}
	return spaces
      }
      def expression() : Int = {
	return e4()
      }
      
      def e1() : Int = {
        if (isLeft()) {
          consume(1)
          var v :Int = expression()
          if (!isRight()) { 
		println("fuck this rightParen")
            error()
	}
          consume(1)
          return v
        } else if (isInt()) {
          var v : Int = getInt()
	  var spaces : Int = getIntLength()
	  consume(spaces)
          return v
        } else if (isId()) {
          var id : String = getId()
          consume(id.length()) 
          return get(id)
        } else {
	
		println("fuck this e1")
	error
	}
        return 0
      }
      
      def e2() : Int = {
        var value : Int = e1()
        while (isMul()) {
          consume(1)
          value = value * e1()
        }
        return value
      }
      
      def e3() : Int = {
        var value : Int = e2()
        while (isPlus()) {
	  
          consume(1)
          value = value + e2()
        }
        return value
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
          if (!isEq()) {
            error()
	}
          consume(1)
          val v : Int = expression()
          if (value != 0)
            set(id, v)
          if (isSemi())
            consume(1)
          return 1
        } else if (isLeftBlock()) {
            consume(1)
            seq(value)
            if (!isRightBlock()) {
		println("fuck this rightBlock")
             error()
            }
	    consume(1)
            return 1
        } else if (isIf()) {
          consume(2)
          var c : Int = expression()
          if ((c != 0) && (value != 0))
             statement(1)
          else
             statement(0)
          if (isElse()) {
            consume(4)
            if ((c == 0) && (value != 0)) 
                statement(1)
            else
                statement(0)
            return 0
          }
          return 1
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
          return 1
         } else if (isSemi()) {
           consume(1)
           return 1
	} else if (isSpace()) {
		consume(1)
		return 1
        } else {
           return 0
	}
      }
      
      def seq(value:Int) {
        while(statement(value) == 1) {}
      }
      
      def program() {
        seq(1)
        if (!isEnd()) {
	  println("fuck you")
          error()
	}
      }
      
      def main (args: Array[String]) : Unit = {
       /*programText = args(0).replace(" ", "")*/
	programText = args(0)
        program()
      }
    }


