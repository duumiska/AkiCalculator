import scala.collection.mutable.Stack
import scala.util.matching.Regex
import scala.util.control.Breaks._

object AkiCalculator {

	def calculate(s: String) : Double = {
		val stack = new Stack[Char]
		val postfix = new Stack[Char]
		val calc = new Stack[Double]
		// TODO split to Strings with operators and () parenthesis
		// TODO trim all wrong characters
		s.foreach( x =>
			x match {
				case '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9' =>
					print(x)
					postfix.push(x)
				case '+'|'*'|'-'|'/' =>
					if(!stack.isEmpty)
						//TODO o1 is left-associative and its precedence is less than or equal to that of o2, or
						//TODO o1 is right associative, and has precedence less than that of o2,
						if(stack.top == '+' || stack.top == '-' || stack.top == '*' )
							postfix.push(stack.pop)
					stack.push(x)
				case '(' =>
					stack.push(x)
				case ')' =>
					breakable { 
						while(!stack.isEmpty) {
							var popped:Char = stack.pop();
							if (popped != '(') {
								print(popped)
								postfix.push(popped)
							}
						 	else {
						 		break;
						 	}
						}
					}
				case _ =>
					//println(s"invalid char $s")
			}
		)
		while(!stack.isEmpty) {
			var popped:Char = stack.pop();
			print(popped)
			postfix.push(popped)
		}
		println("\nLaskenta alkaa")
		val postfix2 = postfix.reverse
		while(!postfix2.isEmpty) {
			var popped:Char = postfix2.pop();
			popped match {
				case '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9' =>
					calc.push(popped.asDigit)
				case '+' =>
					var tempsum: Double = 0
					var a: Double = calc.pop;
					var b: Double = calc.pop;
					println(s"$b + $a")
					tempsum = b + a;
					calc.push(tempsum)
				case '*' =>
					var tempsum: Double = 0
					var a: Double = calc.pop;
					var b: Double = calc.pop;
					println(s"$b * $a")
					tempsum = b * a;
					calc.push(tempsum)
				case '-' =>
					var tempsum: Double = 0
					var a: Double = calc.pop;
					var b: Double = calc.pop;
					println(s"$b - $a")
					tempsum = b - a;
					calc.push(tempsum)
				case '/' =>
					var tempsum: Double = 0
					var a: Double = calc.pop;
					var b: Double = calc.pop;
					println(s"$b / $a")
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
			println(s"SUM = $sum\n")
			sum = calculate("7/3")
			println(s"SUM = $sum\n")
			sum = calculate("100+200")
			println(s"SUM = $sum\n")
			sum = calculate("1+2*2")
			println(s"SUM = $sum\n")
			sum = calculate("2 * (23/(3*3)) - 23 * (2*3)")
			println(s"SUM = $sum\n")
		}
		else {
			for ( x <- args ) {
         		var sum = calculate(x)
         		println(s"SUM = $sum\n")
      		}
      	}
	}
}