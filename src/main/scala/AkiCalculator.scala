import scala.collection.mutable.Stack
import scala.util.matching.Regex
import scala.util.control.Breaks._

object AkiCalculator {

	def parseEquationString(s: String) : Array[String] = {
// For some reason this regexp didn't work with longer numbers :( 
//		val finder = """([\\+-\\*/\\(\\)])|([0-9][0-9]*)""".r
//		splitted.foreach(println)
//		return finder.findAllIn(clean).toArray
		
		//More straight forward way to parse equation
		val clean = s.replaceAll(" ", "")
		val clean1 = clean.replaceAll("\\+", "|\\+|")
		val clean2 = clean1.replaceAll("\\-", "|\\-|")
		val clean3 = clean2.replaceAll("\\*", "|\\*|")
		val clean4 = clean3.replaceAll("\\/", "|\\/|")
		val clean5 = clean4.replaceAll("\\(", "|\\(|")
		val clean6 = clean5.replaceAll("\\)", "|\\)|")
		return clean6.split("\\|")
	}

	def calculate(s: String) : Double = {
		val digit = new Regex("[0-9][0-9]*")
		val operator = new Regex("[\\+\\-\\*/]")
		val stack = new Stack[String]
		val postfix = new Stack[String]
		val calc = new Stack[Double]

		val parsed = parseEquationString(s)

		parsed.foreach( x =>
			x match {
				case digit() =>
					postfix.push(x)
				case operator() =>
					if(!stack.isEmpty)
						//TODO o1 is left-associative and its precedence is less than or equal to that of o2, or
						//TODO o1 is right associative, and has precedence less than that of o2,
						if(stack.top == "+" || stack.top == "-" || stack.top == "*" || stack.top == "/")
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
					//Just skipit, no harm done
			}
		)
		
		while(!stack.isEmpty) {
			var popped:String = stack.pop();
			postfix.push(popped)
		}
		val postfix2 = postfix.reverse
		while(!postfix2.isEmpty) {
			var popped:String = postfix2.pop();
			popped match {
				case digit() =>
					calc.push(popped.toDouble)
				case operator() =>
					var tempsum: Double = 0
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
					//TODO expections
					println("grande error, should explode")
			}
		}

		return calc.pop
	}

	def main(args: Array[String]) {
		if(args.length < 1) {
			println("No parameter. Using example calculate \"5+((1+2)*4)-3\"")
			var sum = calculate("5+((1+2)*4)-3")
			println(s"5+((1+2)*4)-3 = $sum\n")
			sum = calculate("7/3")
			println(s"7/3 = $sum\n")
			sum = calculate("100+200")
			println(s"100+200 = $sum\n")
			sum = calculate("1+2*2")
			println(s"1+2*2 = $sum\n")
			sum = calculate("2 * (23/(3*3)) - 23 * (2*3)")
			println(s"2 * (23/(3*3)) - 23 * (2*3) = $sum\n")
		}
		else {
			for ( x <- args ) {
         		var sum = calculate(x)
         		println(s"SUM = $sum\n")
      		}
      	}
	}
}