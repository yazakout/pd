  import scala.collection.mutable.{Map, SynchronizedMap, HashMap}

class pd {
      
  object pd {
    val symTable : Map[String, Int] = Map()
    val currName : Int = 0;

    val index : Int = 0;
    

      def get(id:String)  : Int = {
          return symTable.getOrElse(id, -1)
      }
    
      def set(id:String, value:Int) {
        /**if (symTable.contains(id))*/ 
          symTable + (id -> value)
      }
      
      def consume(i:Int) {
        
      }
      
      def error() {
        println("error at ", programText.substring(index))
        
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
        def programText : String = prog
        program()
      }
      
  }
 }

