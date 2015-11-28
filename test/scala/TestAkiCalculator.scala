import org.scalatest.FlatSpec

class TestAkiCalculator extends FlatSpec {
    "A AkiCalculator" should "return 1 for all calculations" in {
    assert(AkiCalculator.calculate("blaa") === 1)
  }
}