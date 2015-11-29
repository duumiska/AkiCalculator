import scala.collection.mutable.Stack
import scala.util.matching.Regex
import scala.util.control.Breaks._
import spray.json._
import org.apache.commons.codec.binary.{ Base64 => ApacheBase64 }

object AkiCalculator {

	def decode(encoded: String) = new String(ApacheBase64.decodeBase64(encoded.getBytes))
  	def encode(decoded: String) = new String(ApacheBase64.encodeBase64(decoded.getBytes))

	def parseEquationString(s: String) : Array[String] = {
// For some reason this regexp didn't work with longer numbers :( 
//		val finder = """([\\+-\\*/\\(\\)])|([0-9][0-9]*)""".r
//		splitted.foreach(println)
//		return finder.findAllIn(clean).toArray
		
		//More straight forward way to parse equation
		val clean = s.replaceAll(" ", "").replaceAll("\\+", "|\\+|").replaceAll("\\-", "|\\-|").replaceAll("\\*", "|\\*|").replaceAll("\\/", "|\\/|").replaceAll("\\(", "|\\(|").replaceAll("\\)", "|\\)|")
		return clean.split("\\|")
	}

	def calculate(s: String) : Double = {
		val digit = new Regex("[0-9][0-9]*")
		val decimal = new Regex("[0-9]*\\.[0-9]*")
		val operator = new Regex("[\\+\\-\\*/]")
		val stack = new Stack[String]
		val postfix = new Stack[String]
		val calc = new Stack[Double]

		if(s == null) {
			throw new IllegalArgumentException("Null equation")
		}

		if(s.length < 1) {
			throw new IllegalArgumentException("Empty Equation")
		}
		
		if(s.matches("(.*[a-z].*)|(.*[A-Z].*)")) {
			throw new IllegalArgumentException(s"Equation ($s) has illegal characters")
		}

		val parsed = parseEquationString(s)

		// https://en.wikipedia.org/wiki/Shunting-yard_algorithm
		parsed.foreach( x =>
			x match {
				case digit() =>
					postfix.push(x)
				case decimal() =>
					postfix.push(x)
				case operator() =>
					if(!stack.isEmpty)
						if( (x == "*" || x == "/") && (stack.top == "*" || stack.top == "/") )
							postfix.push(stack.pop)
						else if( (x == "+" || x == "-") && (stack.top == "*" || stack.top == "/") )
							postfix.push(stack.pop)
					stack.push(x)
				case "(" =>
					stack.push(x)
				case ")" =>
					breakable { 
						while(!stack.isEmpty) {
							var popped:String = stack.pop();
							if (popped != "(") {
								postfix.push(popped)
							}
						 	else {
						 		break;
						 	}
						}
					}
				case _ =>
					//Just skipit, no harm done here
			}
		)

		while(!stack.isEmpty) {
			var popped:String = stack.pop();
			postfix.push(popped)
		}

		// https://en.wikipedia.org/wiki/Reverse_Polish_notation
		val postfix2 = postfix.reverse
		while(!postfix2.isEmpty) {
			var popped:String = postfix2.pop();
			popped match {
				case digit() =>
					calc.push(popped.toDouble)
				case decimal() =>
					calc.push(popped.toDouble)
				case operator() =>
					var tempsum: Double = 0
					if(calc.length < 2)
						 throw new IllegalStateException(s"not enough numbers ($s)");
					var a: Double = calc.pop;
					var b: Double = calc.pop;
//					println(s"$b $popped $a")
					if(popped == "+")
						tempsum = b + a;
					else if(popped == "-")
						tempsum = b - a;
					else if(popped == "*")						
						tempsum = b * a;
					else if(popped == "/")
						tempsum = b / a;
					calc.push(tempsum)
				case _ => 
					 throw new IllegalStateException(s"Illegal component ($popped)");
			}
		}

		return calc.pop
	}

	def calculateJson(data: String): String = {
		var decoded = decode(data)
		
		try {
			var sum = calculate(decoded)					
			val source = s"""{ "error": "false", "result": "$sum" }"""
			val jsonAst = source.parseJson // or JsonParser(source)
			return jsonAst.prettyPrint
		} catch {
			case e: Exception =>
				var message = e.getMessage;
				if(message == null)
					message = "Unkown error"
				val source = s"""{ "error": "true", "message": "Bad equation: $message" }"""
				val jsonAst = source.parseJson // or JsonParser(source)
				return jsonAst.prettyPrint
		}
	}
/*
	def main(args: Array[String]) {
		if(args.length < 1) {
			try {
				println("No parameter. Using example calculate \"5+((1+2)*4)-3\"")
				var sum = calculate("5+((1+2)*4)-3")
				println(s"5+((1+2)*4)-3 = $sum\n")
			} catch {
				case e: Exception => println("exception caught, bad equation: " + e);
			}
		}
		else {
			for ( x <- args ) {
				try {
					var sum = calculate(x)
					println(s"$x = $sum\n")
				} catch {
					case e: Exception => println("exception caught, bad equation: " + e);
				}
      		}
      	}
	}
*/
}