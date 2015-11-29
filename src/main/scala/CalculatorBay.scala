import spray.routing.SimpleRoutingApp
import spray.http.MediaTypes
import akka.actor.ActorSystem

object CalculatorBay extends App with SimpleRoutingApp{
	implicit val actorSystem = ActorSystem("on-spray-can")

	startServer(interface = "localhost", port = 8888) {
		get {
			path("calculus" / "example") {
				respondWithMediaType(MediaTypes.`application/json`) {
					complete {
						AkiCalculator.calculateJson("NSsoKDErMikqNCktMw==")
					}
				}
			}
		} ~
		get {
			path("calculus") {
				parameters("query") { (equation) =>
					respondWithMediaType(MediaTypes.`application/json`) {
						complete {
							AkiCalculator.calculateJson(equation)
						}
					}
				}
			}
		}
	}
}