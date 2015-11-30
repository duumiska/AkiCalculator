/* No package, simple practice project, never to be used anywhere else */

import scala.collection.mutable.Stack
import scala.util.matching.Regex
import scala.util.control.Breaks._
import spray.json._
import org.apache.commons.codec.binary.{ Base64 => ApacheBase64 }

object AkiCalculator {

	def decode(encoded: String) = new String(ApacheBase64.decodeBase64(encoded.getBytes))

	/** Splits calculation string to numbers, operators and parenthesis */
	def parseEquationString(s: String) : Array[String] = {
		//This could be nicer with kickass regex, good for now
		val clean = s.replaceAll(" ", "").replaceAll("\\+", "|\\+|").replaceAll("\\-", "|\\-|").replaceAll("\\*", "|\\*|").replaceAll("\\/", "|\\/|").replaceAll("\\(", "|\\(|").replaceAll("\\)", "|\\)|")
		return clean.split("\\|")
	}

	/** Parses and makes calculation for string, returns sum in Double
  	*
  	* Takes String with calculation, parses it and checks if it really is calculation
  	* Uses following algorithms: 
  	*   for parsing https://en.wikipedia.org/wiki/Shunting-yard_algorithm 
  	*   for calculation https://en.wikipedia.org/wiki/Reverse_Polish_notation
  	*
  	* Throws IllegalArgumentException on bad String and
  	*		 IllegalStateException on bad calculation
  	*
  	* Supports integer and decimal values and
  	*  +, -, *, / operators and ( ) parenthesis
  	*/
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


	/** Decodes base64 calculation, calculates it and returns json
  	*
  	* Takes base64 encoded String containing calculation
  	* Return JSON
  	* - success
  	*	{ "error": "false", "result": "$sum" }
  	* 
  	* - error
  	*   { "error": "true", "message": "Bad equation: $message" }
	*/
	def calculateJson(data: String): String = {
		var decoded = decode(data)
		
		try {
			var sum = calculate(decoded)					
			val source = s"""{ "error": "false", "result": "$sum" }"""
			val jsonAst = source.parseJson
			return jsonAst.prettyPrint
		} catch {
			case e: Exception =>
				var message = e.getMessage;
				if(message == null)
					message = "Unkown error"
				val source = s"""{ "error": "true", "message": "Bad equation: $message" }"""
				val jsonAst = source.parseJson
				return jsonAst.prettyPrint
		}
	}
}
