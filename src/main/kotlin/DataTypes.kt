import kotlinx.serialization.Serializable

class DataTypes {
    //stores the finalized delivery order
    @Serializable
    data class DeliveryOrder(
        val total_price: Int,
        val small_order_surcharge: Int,
        val cart_value: Int,
        val delivery: Delivery
    )
    //used in the finalized delivery order for formatting
    @Serializable
    data class Delivery(
        val fee: Int,
        val distance: Int
    )
    //type for handling the distance ranges from the api
    @Serializable
    data class DistanceRange(
        val min: Int,
        val max: Int,
        val a: Int,
        val b: Double
    )
    //errorMessage format used to send out as a response if the application fails
    @Serializable
    data class ErrorMessage(
        var error: String,
        val response_status_code: Int,
    )
    //used to simplify adding error messages in the main application
    fun addErrorMessage(errorMessage: ErrorMessage,string : String) : ErrorMessage{
        errorMessage.error = string
        return errorMessage
    }

}