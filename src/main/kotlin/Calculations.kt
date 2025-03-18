import kotlin.math.*

class Calculations {
    //calculates the final prize of the order with simple addition
    fun calcFinalPrice(cartValue : Int, smallOrderSurcharge : Int, distanceFee : Int) : Int{
        return cartValue + smallOrderSurcharge + distanceFee
    }
    //calculates the distance fee for the order with the formula (b * distance/10) + a + baseprice
    //a and b are from wolt api venue_raw -> delivery_specs -> delivery_pricing -> distance_ranges
    fun calcDistanceFee(distanceRanges : List<DataTypes.DistanceRange>, distance : Int, basePrice : Int) : Int{
        for (range in distanceRanges){
            if (range.max == 0){
                throw IllegalArgumentException("Distance out of range")
            }
            else if (distance >= range.min && distance < range.max){
                return Math.round(range.b * (distance / 10)  + range.a + basePrice).toInt()
            }

        }
        throw IllegalArgumentException("uncategorized distance error")
    }
    //calculates the surcharge if an order is below the minimum value, it cannot be negative
    fun calcSmallOrderSurcharge(cartValue: Int, minimumNoSurcharge: Int): Int{
        return 0.coerceAtLeast(minimumNoSurcharge - cartValue)
    }

    //uses the haversine formula to calculate the distance between two longitude, latitude points
    fun calcDistanceBetweenTwoPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double{
        val earthRadius = 6371000.0 // Radius of the Earth in meters

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}