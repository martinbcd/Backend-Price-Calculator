import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.serialization.json.*



fun main() {
    embeddedServer(Netty, port = 8000) {
    routing {
        get("/api/v1/delivery-order-price") {
            try {
                val jsonParsing = JsonParsing()
                val calc = Calculations()
                //the client needs the venue slug, the total cart value and the users latitude and longitude as 4 values
                val venueSlug = call.request.queryParameters["venue_slug"]
                    ?: throw IllegalArgumentException("Missing or invalid venue_slug")
                val cartValue = call.request.queryParameters["cart_value"]?.toInt()
                    ?: throw IllegalArgumentException("Missing or invalid cart_value")
                val userLat = call.request.queryParameters["user_lat"]?.toDouble()
                    ?: throw IllegalArgumentException("Missing or invalid user_lat")
                val userLon = call.request.queryParameters["user_lon"]?.toDouble()
                    ?: throw IllegalArgumentException("Missing or invalid user_lon")
                //calling api to fetch Json information
                val staticJsonInfo = jsonParsing.apiToJson( "https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$venueSlug/static")
                val dynamicJsonInfo = jsonParsing.apiToJson("https://consumer-api.development.dev.woltapi.com/home-assignment-api/v1/venues/$venueSlug/dynamic")

                val location = jsonParsing.returnLocation(staticJsonInfo)
                val minimumNoSurcharge = jsonParsing.returnMinimumNoSurcharge(dynamicJsonInfo)
                val basePrice = jsonParsing.returnBasePrice(dynamicJsonInfo)
                val distanceRanges = jsonParsing.parseDistanceRanges(dynamicJsonInfo)

                val distanceDouble: Double
                if (location != null)
                    distanceDouble = calc.calcDistanceBetweenTwoPoints(userLat, userLon, location[1], location[0])
                else throw IllegalArgumentException("Missing or invalid Venue latitude and or longitude")
                val distance = Math.round(distanceDouble).toInt()

                val smallOrderSurcharge = calc.calcSmallOrderSurcharge(
                    cartValue, minimumNoSurcharge ?: throw IllegalArgumentException("Missing or invalid minimumNoSurcharge")
                )


                val deliveryFee = calc.calcDistanceFee(
                    distanceRanges,
                    distance,
                    basePrice ?: throw IllegalArgumentException("Missing or invalid basePrice")
                )

                val totalPrice = calc.calcFinalPrice(cartValue, smallOrderSurcharge, deliveryFee)
                val finalOrder = DataTypes.DeliveryOrder(
                    total_price = totalPrice,
                    small_order_surcharge = smallOrderSurcharge,
                    cart_value = cartValue,
                    delivery = DataTypes.Delivery(
                        fee = deliveryFee,
                        distance = distance
                    )
                )
                //if all information is available and the client is able to parse the order the client responds with a json file of the DeliveryOrder
                call.respond(
                    HttpStatusCode.OK,
                    Json.encodeToString(finalOrder))
            }
            catch (e: Exception){
                val dataTypes = DataTypes()
                val error = DataTypes.ErrorMessage(
                    error = "",
                    response_status_code = 400
                )
                //if the order is not valid or anything fails the client will respond with what error caused the issue
                call.respond(
                    HttpStatusCode.BadRequest,
                    Json.encodeToString(
                        dataTypes.addErrorMessage(error, e.message ?: "An unknown error occurred")))
            }
        }
    }

    }.start(wait = true)
}
