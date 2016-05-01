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
      
      def isId() : Boolean = {
        true
      }
      
      def isEq() : Boolean = {
        true
      }
      
      def isEnd() : Boolean = {
        true
      }
      
      def isSemi() : Boolean = {
        true
      }
      
      def isSpace() : Boolean = {
        true
      }
      
      def isIf() : Boolean = {
        true
      }
      
      def isWhile() : Boolean = {
        true
      }
      
      def isRightBlock() : Boolean = {
        true
      }
      
      def isElse() : Boolean = {
       true 
      }
      
      def getId() : String = {
        ""
      }
      
      def isLeftBlock() : Boolean = {
        true
      }
      
      def expression() : Int = {
        1
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
        programText = args;
        interpret(args)
      }
    }
 
