import spray.routing.SimpleRoutingApp
import spray.http.MediaTypes
import akka.actor.ActorSystem
import util.Properties

object CalculatorBay extends App with SimpleRoutingApp{
	implicit val actorSystem = ActorSystem("on-spray-can")
	val myPort = Properties.envOrElse("PORT", "8888").toInt // for Heroku compatibility

	startServer(interface = "0.0.0.0", port = myPort) {
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