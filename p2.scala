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
      
      def getId() : String = {
        ""
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
        } else 0
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
 }
