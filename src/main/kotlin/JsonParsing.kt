import kotlinx.serialization.json.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI

class JsonParsing {
    //returns a string from an HTTP API
    fun apiToJson(apiString : String?) : String{
        val url = URI.create(apiString).toURL()
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        val responseCode: Int = connection.responseCode
        println("Response Code: $responseCode")

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader  = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?
            val response = StringBuilder()
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            connection.disconnect()
            return response.toString()
        }
        else {
            connection.disconnect()
            throw IllegalArgumentException("Unable to fetch data from the API")
        }
    }
    //returns the longitude and latitude from the static Wolt api json file
    fun returnLocation(jsonString: String): List<Double>? {
        try {
            val jsonElement = Json.parseToJsonElement(jsonString)
            val coordinates = jsonElement.jsonObject["venue_raw"]
                ?.jsonObject?.get("location")
                ?.jsonObject?.get("coordinates")
                ?.jsonArray

            return coordinates?.map { it.jsonPrimitive.double }
        } catch (e: Exception) {
            throw IllegalArgumentException ("Invalid location type or format")
        }
    }
    //returns the minimum value to not pay a surcharge from the dynamic Wolt api json file
    fun returnMinimumNoSurcharge(jsonString: String): Int?{
        try {
            val jsonElement = Json.parseToJsonElement(jsonString)
            return jsonElement.jsonObject["venue_raw"]
                ?.jsonObject?.get("delivery_specs")
                ?.jsonObject?.get("order_minimum_no_surcharge")
                ?.jsonPrimitive?.int
        } catch (e: Exception) {
            throw IllegalArgumentException ("Invalid minimum_no_surcharge type or format")
        }
    }
    //returns the base price of the delivery fee from the dynamic Wolt api json file
    fun returnBasePrice(jsonString: String): Int? {
        try {
            val jsonElement = Json.parseToJsonElement(jsonString)
            return jsonElement.jsonObject["venue_raw"]
                ?.jsonObject?.get("delivery_specs")
                ?.jsonObject?.get("delivery_pricing")
                ?.jsonObject?.get("base_price")
                ?.jsonPrimitive?.int
        } catch (e: Exception) {
            throw IllegalArgumentException ("Invalid base_price type or format")
        }
    }
    //returns list of values relating to the amount for the distance fee converting it into a list of DistanceRange dataType
    //from the dynamic Wolt api json file
    fun parseDistanceRanges(jsonString: String): List<DataTypes.DistanceRange> {
        try {
            val jsonObject = Json.parseToJsonElement(jsonString).jsonObject
            val venueRaw = jsonObject["venue_raw"]?.jsonObject
            val deliverySpecs = venueRaw?.get("delivery_specs")?.jsonObject
            val distanceRanges = deliverySpecs?.get("delivery_pricing")?.jsonObject
                ?.get("distance_ranges")?.jsonArray
            val distanceRangeList = distanceRanges?.map { range ->
                val rangeObject = range.jsonObject
                DataTypes.DistanceRange(
                    min = rangeObject["min"]?.jsonPrimitive?.int ?: 0,
                    max = rangeObject["max"]?.jsonPrimitive?.int ?: 0,
                    a = rangeObject["a"]?.jsonPrimitive?.int ?: 0,
                    b = rangeObject["b"]?.jsonPrimitive?.double ?: 0.0,
                )
            } ?: emptyList()

            return distanceRangeList
        }
        catch (e: Exception){
            throw IllegalArgumentException ("Invalid distance_ranges type or format")
        }
    }
}