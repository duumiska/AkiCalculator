import org.scalatest.FlatSpec

class TestAkiCalculator extends FlatSpec {
	"A AkiCalculator" should "return 14.0 for 5+((1+2)*4)-3" in {
		assert(AkiCalculator.calculate("5+((1+2)*4)-3") == 14.0)
	}

	"A AkiCalculator" should "return 2.3333333333333335 for 7/3" in {
		assert(AkiCalculator.calculate("7/3") == 2.3333333333333335)
	}

	"A AkiCalculator" should "return 5.0 for 1+2*2" in {
		assert(AkiCalculator.calculate("1+2*2") == 5.0)
	}

	"A AkiCalculator" should "return -132.88888888888889 for 2 * (23/(3*3)) - 23 * (2*3)" in {
		assert(AkiCalculator.calculate("2 * (23/(3*3)) - 23 * (2*3)") == -132.88888888888889)
	}

	"A AkiCalculator" should "throw IllegalArgumentException if characters are in equation" in {
		intercept[IllegalArgumentException] {
			AkiCalculator.calculate("5+((kaatuu)*4)-3")
		}
	}

	"A AkiCalculator" should "return 10.0 for (7*1)+1+(5/5)+(0.5+0.5)" in {
		assert(AkiCalculator.calculate("(7*1)+1+(5/5)+(0.5+0.5)") == 10.0)
	}

	"A AkiCalculator" should "throw IllegalArgumentException for empty equation" in {
		intercept[IllegalArgumentException] {
			AkiCalculator.calculate("")
		}
		intercept[IllegalArgumentException] {
			AkiCalculator.calculate(null)
		}
	}

	"A AkiCalculator" should "throw IllegalStateException if equation has too many operators" in {
		intercept[IllegalStateException] {
			AkiCalculator.calculate("2++2")
		}
	}
}
