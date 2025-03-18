import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith


class Tests {

    private var validStaticJson: String? = null
    private var validDynamicJson: String? = null
    private var distanceValues: List<DataTypes.DistanceRange> = listOf()
    val jsonParsing = JsonParsing()
    val calc = Calculations()
    val dataTypes = DataTypes()
    private val basePrice = 500

    @Test
    fun testReturnLocation() {
        val location = jsonParsing.returnLocation(validStaticJson!!)
        assertEquals(18.0314984, location!![0])
        assertEquals(59.3466978, location[1])
    }
    @Test
    fun testMinimumNoSurcharge() {
        val minimumNoSurcharge = jsonParsing.returnMinimumNoSurcharge(validDynamicJson!!)
        assertEquals(10000, minimumNoSurcharge!!)
    }
    @Test
    fun testReturnBasePrice(){
        val basePrice = jsonParsing.returnBasePrice(validDynamicJson!!)
        assertEquals(900, basePrice!!)
    }
    @Test
    fun testParseDistanceRanges(){
        val distanceRanges = jsonParsing.parseDistanceRanges(validDynamicJson!!)
        assertEquals(distanceValues, distanceRanges)
    }
    @Test
    fun testAddErrorMessage(){
        val errorValue = DataTypes.ErrorMessage(error = "test", response_status_code = 200)
        val errorTest = DataTypes.ErrorMessage(error = "", response_status_code = 200)
        dataTypes.addErrorMessage(errorTest,"test")
        assertEquals(errorValue,errorTest)
    }
    @Test
    fun testCalcFinalPrice(){
        val intAnswer = 6673
        val testNum = calc.calcFinalPrice(184,5677,812)
        assertEquals(intAnswer,testNum)
    }
    @Test
    fun testCalcDistanceFeeSmallRange() {
        val distance = 200
        val expectedFee = 500 // basePrice + 0 (from range)
        val actualFee = calc.calcDistanceFee(distanceValues, distance, basePrice)
        assertEquals(expectedFee, actualFee)
    }


    @Test
    fun testCalcDistanceFeeLongRange() {
        val distance = 1800
        val expectedFee = 4300 // (1800 * 10 / 10) + 2000 + 500
        val actualFee = calc.calcDistanceFee(distanceValues, distance, basePrice)
        assertEquals(expectedFee, actualFee)
    }

    @Test
    fun testCalcDistanceNegativeRange() {
        val distance = -100
        assertFailsWith<IllegalArgumentException> {
            calc.calcDistanceFee(distanceValues, distance, basePrice)
        }
    }

    @Test
    fun testCalcDistanceOutOfRange() {
        val distance = 2500
        assertFailsWith<IllegalArgumentException> {
            calc.calcDistanceFee(distanceValues, distance, basePrice)
        }
    }
    @Test
    fun testCalcSmallOrderSurchargeUpCharge(){
        val minimumNoSurcharge = 1000
        val cartValue = 800
        val testResult = 200
        val calcResult = calc.calcSmallOrderSurcharge(cartValue, minimumNoSurcharge)
        assertEquals(testResult, calcResult)
    }
    @Test
    fun testCalcSmallOrderSurchargeNoChange(){
        val minimumNoSurcharge = 800
        val cartValue = 1000
        val testResult = 0
        val calcResult = calc.calcSmallOrderSurcharge(cartValue, minimumNoSurcharge)
        assertEquals(testResult, calcResult)
    }
    @Test
    fun testCalcDistanceBetweenTwoPointsZeroDistance() {
        val lat1 = 0.0
        val lon1 = 0.0
        val lat2 = 0.0
        val lon2 = 0.0

        val distance = calc.calcDistanceBetweenTwoPoints(lat1, lon1, lat2, lon2)
        assertEquals(0.0, distance)
    }
    @Test
    fun testCalcDistanceBetweenTwoPoints() {
        val lat1 = 52.5200
        val lon1 = 13.4050
        val lat2 = 48.8566
        val lon2 = 2.3522

        val distance = calc.calcDistanceBetweenTwoPoints(lat1, lon1, lat2, lon2)
        val expectedDistance = 877463.325917543
        assertEquals(expectedDistance, distance, 1000.0)
    }


    @BeforeEach
    fun setUp() {
        distanceValues = listOf(
            DataTypes.DistanceRange(min = 0, max = 500, a = 0, b = 0.0),
            DataTypes.DistanceRange(min = 500, max = 1000, a = 1000, b = 0.0),
            DataTypes.DistanceRange(min = 1000, max = 1500, a = 2000, b = 0.0),
            DataTypes.DistanceRange(min = 1500, max = 2000, a = 2000, b = 10.0),
            DataTypes.DistanceRange(min = 2000, max = 0, a = 0, b = 0.0)
        )
        //below is the wall of Json where no man ventures
        validStaticJson = """
            {
  "venue": {
    "id": "66ff83329ee25f2fb7e962a6",
    "image_url": "https://image-resizer-proxy.development.dev.woltapi.com/assets/677fb0cb2e9a6361713418cf",
    "image_blurhash": "j2jQRa00hGTt;;gPmXKGcPTsPbpl",
    "brand_logo_image_url": null,
    "brand_logo_image_blurhash": null,
    "brand_slug": null,
    "brand_name": null,
    "name": "Home Assignment Venue Stockholm",
    "description": "This is a venue that's used in engineering home assignment. Please don't modify!",
    "rating": null,
    "opening_times_schedule": [
      { "day": "Monday", "formatted_times": "All day" },
      { "day": "Tuesday", "formatted_times": "All day" },
      { "day": "Wednesday", "formatted_times": "All day" },
      { "day": "Thursday", "formatted_times": "All day" },
      { "day": "Friday", "formatted_times": "All day" },
      { "day": "Saturday", "formatted_times": "All day" },
      { "day": "Sunday", "formatted_times": "All day" }
    ],
    "delivery_times_schedule": [
      { "day": "Monday", "formatted_times": "All day" },
      { "day": "Tuesday", "formatted_times": "All day" },
      { "day": "Wednesday", "formatted_times": "All day" },
      { "day": "Thursday", "formatted_times": "All day" },
      { "day": "Friday", "formatted_times": "All day" },
      { "day": "Saturday", "formatted_times": "All day" },
      { "day": "Sunday", "formatted_times": "All day" }
    ],
    "group_order_enabled": true,
    "share_url": "https://wolt-com.development.dev.woltapi.com/sv/swe/stockholm/restaurant/home-assignment-venue-stockholm",
    "delivery_methods": ["takeaway", "homedelivery"],
    "currency": "SEK",
    "active_menu": null,
    "address": "Solnavägen 3H",
    "city": "Stockholm",
    "country": "SWE",
    "type": "purchase",
    "website": null,
    "substitutions_enabled": null,
    "post_code": "113 63",
    "product_line": "restaurant",
    "secondary_product_line": "none",
    "phone": null,
    "self_delivery": false,
    "timezone": "Europe/Stockholm",
    "delivery_base_price": 900,
    "delivery_geo_range": {
      "bbox": null,
      "type": "Polygon",
      "coordinates": [
        [
          [18.039073461128798, 59.366102491137696],
          [18.046356923720776, 59.36497598152632],
          [18.053068470437285, 59.363146708856554],
          [18.058949901303233, 59.360685077624844],
          [18.06377509867718, 59.35768581260352],
          [18.067358731604223, 59.354264299463054],
          [18.0695633628232, 59.35055213143022],
          [18.070304687662667, 59.34669203603941],
          [18.069554710056632, 59.34283237940375],
          [18.067342743374958, 59.339121460839976],
          [18.063754209046238, 59.33570181766239],
          [18.058927290526192, 59.33270475841308],
          [18.053047580799955, 59.33024533295381],
          [18.04634093548248, 59.32841793024796],
          [18.03906480835582, 59.327292670106694],
          [18.03149840000001, 59.32691272466978],
          [18.023931991644172, 59.327292670106694],
          [18.01665586451754, 59.32841793024796],
          [18.00994921920004, 59.33024533295381],
          [18.0040695094738, 59.33270475841308],
          [17.99924259095378, 59.33570181766239],
          [17.99635476322395, 59.338453732368976],
          [17.9986953735352, 59.3410684613923],
          [18.0127666471829, 59.3454766863041],
          [18.0187625486702, 59.3444140904369],
          [18.0220463055186, 59.3439764203359],
          [18.0259016745788, 59.3441116382853],
          [18.0307102203369, 59.3471078171971],
          [18.0365522488894, 59.349473277975],
          [18.0416492611209, 59.3519594138417],
          [18.03778376546953, 59.366167253630515],
          [18.039073461128798, 59.366102491137696]
        ]
      ]
    },
    "service_fee_short_description": null,
    "service_fee_estimate": null,
    "feature_croatia_currency_selection_enabled": false,
    "show_eco_packaging": false,
    "is_pickup_friendly": false,
    "age_verification_method": "age_verification",
    "trader_information": null,
    "show_delivery_preestimate_by_time": false,
    "merchant": {
      "id": "66ff823b54a09a5a8d60c939",
      "name": "Home Assignment Merchant Sweden",
      "business_id": "home-assingment-id-234",
      "street_address": "Solnavägen 3H",
      "city": "Stockholm",
      "post_code": "113 63",
      "country": "Sweden"
    },
    "digital_services_act_information": {
      "name": "Home Assignment Merchant Sweden",
      "business_id": "home-assingment-id-234",
      "address": "Solnavägen 3H, 113 63, Stockholm, Sweden",
      "self_certification": "The Partner is committed to only offering products and/or services that comply with the applicable laws.",
      "report_item_url": "https://www.surveymonkey.com/r/VHFTCJX"
    },
    "slug": "home-assignment-venue-stockholm",
    "venue_supports_wolt_pay": false,
    "group_order_id": null,
    "info": {
      "venue_info_service_fee_description": null,
      "venue_info_order_minimum": "SEK100.00",
      "venue_info_base_delivery_price": "SEK9.00"
    },
    "zero_distance_fees": {
      "delivery_price": 900
    }
  },
  "venue_raw": {
    "id": "66ff83329ee25f2fb7e962a6",
    "name": "Home Assignment Venue Stockholm",
    "image_url": "https://image-resizer-proxy.development.dev.woltapi.com/assets/677fb0cb2e9a6361713418cf",
    "image_blurhash": "j2jQRa00hGTt;;gPmXKGcPTsPbpl",
    "brand_logo_image_url": null,
    "brand_logo_image_blurhash": null,
    "description": "This is a venue that's used in engineering home assignment. Please don't modify!",
    "group_order_enabled": true,
    "share_url": "https://wolt-com.development.dev.woltapi.com/sv/swe/stockholm/restaurant/home-assignment-venue-stockholm",
    "delivery_methods": ["takeaway", "homedelivery"],
    "currency": "SEK",
    "opening_times": {
      "monday": [{ "type": "open", "value": 0 }],
      "tuesday": [],
      "wednesday": [],
      "thursday": [],
      "friday": [],
      "saturday": [],
      "sunday": [{ "type": "close", "value": 86400 }]
    },
    "preorder_specs": {
      "preorder_only": false,
      "time_step": 5,
      "homedelivery_spec": {
        "earliest_timedelta": 60,
        "latest_timedelta": 8,
        "preorder_times": {
          "monday": [{ "type": "open", "value": 0 }],
          "tuesday": null,
          "wednesday": null,
          "thursday": null,
          "friday": null,
          "saturday": null,
          "sunday": [{ "type": "close", "value": 86400 }]
        }
      },
      "takeaway_spec": {
        "earliest_timedelta": 45,
        "latest_timedelta": 8,
        "preorder_times": {
          "monday": [{ "type": "open", "value": 0 }],
          "tuesday": null,
          "wednesday": null,
          "thursday": null,
          "friday": null,
          "saturday": null,
          "sunday": [{ "type": "close", "value": 86400 }]
        }
      },
      "eatin_spec": null
    },
    "ncd_allowed": true,
    "tipping": {
      "currency": "SEK",
      "tip_amounts": [1000, 2000, 3000],
      "max_amount": 20000,
      "min_amount": 100,
      "type": "pre_tipping_amount"
    },
    "delivery_note": null,
    "public_visible": false,
    "bag_fee": null,
    "comment_disabled": false,
    "short_description": null,
    "city_id": "58da4cdea3284104e8fd9ee0",
    "merchant": "66ff823b54a09a5a8d60c939",
    "show_allergy_disclaimer_on_menu": true,
    "price_range": 0,
    "item_cards_enabled": false,
    "service_fee_description": "Helps us improve our delivery service further by bringing you new features to enjoy and providing exceptional customer support. The service fee may vary based on order value, selected venue, or delivery method.",
    "food_tags": null,
    "allowed_payment_methods": ["card", "card_raw"],
    "food_safety_reports": null,
    "is_wolt_plus": false,
    "categories": null,
    "rating": null,
    "location": {
      "bbox": null,
      "type": "Point",
      "coordinates": [18.0314984, 59.3466978]
    },
    "string_overrides": {
      "weighted_items_popup_disclaimer": null,
      "restricted_item_bottom_sheet_title": "ID check upon delivery",
      "restricted_item_bottom_sheet_info": "Your order contains items with an age limit that require checking your ID upon delivery. The name on your ID must match the name in your Wolt account. If you do not meet the age limit, the item or items will be returned to the store.\nAre you allowed to purchase these items?",
      "restricted_item_bottom_sheet_confirm": "Yes, I am allowed"
    }
  },
  "order_minimum": 10000
}

        
        """.trimIndent()

        validDynamicJson = """
            {
  "venue": {
    "delivery_open_status": {
      "value": "Open all day",
      "style": {
        "icon": null,
        "type": "OPEN"
      },
      "is_open": true,
      "now": "2025-01-26T03:21:30.600485+01:00",
      "previous_open": null,
      "next_open": null,
      "next_close": null,
      "next_close_time_localized": null
    },
    "id": "66ff83329ee25f2fb7e962a6",
    "open_status": {
      "value": "Open all day",
      "style": {
        "icon": null,
        "type": "OPEN"
      },
      "is_open": true,
      "now": "2025-01-26T03:21:30.600485+01:00",
      "previous_open": null,
      "next_open": null,
      "next_close": null,
      "next_close_time_localized": null
    },
    "online": true,
    "time_slots_schedule": null,
    "banners": [],
    "header": {
      "delivery_method_default": "UNAVAILABLE",
      "delivery_method_statuses": [
        {
          "delivery_method": "UNAVAILABLE",
          "metadata": [
            {
              "type": "STRING",
              "link": null,
              "icon": null,
              "style": "NORMAL",
              "value": "Open all day"
            },
            {
              "type": "STRING",
              "link": null,
              "icon": null,
              "style": "NORMAL",
              "value": "Min. order SEK100.00"
            },
            {
              "type": "LINK",
              "link": "ALLERGEN_INFO",
              "icon": null,
              "style": "NORMAL",
              "value": "Allergens"
            }
          ],
          "call_to_action": {
            "icon": "LOCATION",
            "style": "NO_FILL",
            "enabled": true,
            "configurable": false,
            "value": "Choose location"
          },
          "group_order_button": {
            "enabled": false,
            "visible": false
          },
          "notification": null,
          "disclaimer": null
        }
      ]
    },
    "delivery_configs": []
  },
  "venue_raw": {
    "id": "66ff83329ee25f2fb7e962a6",
    "favourite": false,
    "preestimate_total": {
      "min": 35,
      "max": 45,
      "mean": 40
    },
    "preestimate_preparation": {
      "min": 10,
      "max": 30,
      "mean": 20
    },
    "alive": true,
    "applepay_callback_flow_enabled": false,
    "googlepay_callback_flow_enabled": false,
    "discounts": [],
    "surcharges": [],
    "delivery_specs": {
      "delivery_enabled": true,
      "order_minimum_no_surcharge": 10000,
      "order_minimum_possible": 10000,
      "delivery_times": {
        "monday": [
          {
            "type": "open",
            "value": 0
          }
        ],
        "tuesday": null,
        "wednesday": null,
        "thursday": null,
        "friday": null,
        "saturday": null,
        "sunday": [
          {
            "type": "close",
            "value": 86400
          }
        ]
      },
      "original_delivery_price": 900,
      "delivery_pricing": {
        "base_price": 900,
        "price_ranges": [
          {
            "min": 0,
            "max": 10000,
            "a": 10000,
            "b": -1.0,
            "flag": null,
            "custom_distance_ranges": null
          },
          {
            "min": 10000,
            "max": 0,
            "a": 0,
            "b": 0.0,
            "flag": null,
            "custom_distance_ranges": null
          }
        ],
        "distance_ranges": [
          {
            "min": 0,
            "max": 500,
            "a": 0,
            "b": 0.0,
            "flag": null
          },
          {
            "min": 500,
            "max": 1000,
            "a": 1000,
            "b": 0.0,
            "flag": null
          },
          {
            "min": 1000,
            "max": 1500,
            "a": 2000,
            "b": 0.0,
            "flag": null
          },
          {
            "min": 1500,
            "max": 2000,
            "a": 2000,
            "b": 10.0,
            "flag": null
          },
          {
            "min": 2000,
            "max": 0,
            "a": 0,
            "b": 0.0,
            "flag": null
          }
        ],
        "meta": {
          "subscription_minimum_basket": null,
          "subscription_max_distance": null,
          "subscription_plan_id": null
        }
      },
      "delivery_pricing_with_subscription": null,
      "delivery_pricing_without_subscription": null,
      "geo_range": {
        "bbox": null,
        "type": "Polygon",
        "coordinates": [
          [
            [18.039073461128798, 59.366102491137696],
            [18.046356923720776, 59.36497598152632],
            [18.053068470437285, 59.363146708856554],
            [18.058949901303233, 59.360685077624844],
            [18.06377509867718, 59.35768581260352],
            [18.067358731604223, 59.354264299463054],
            [18.0695633628232, 59.35055213143022],
            [18.070304687662667, 59.34669203603941],
            [18.069554710056632, 59.34283237940375],
            [18.067342743374958, 59.339121460839976],
            [18.063754209046238, 59.33570181766239],
            [18.058927290526192, 59.33270475841308],
            [18.053047580799955, 59.33024533295381],
            [18.04634093548248, 59.32841793024796],
            [18.03906480835582, 59.327292670106694],
            [18.03149840000001, 59.32691272466978],
            [18.023931991644172, 59.327292670106694],
            [18.01665586451754, 59.32841793024796],
            [18.00994921920004, 59.33024533295381],
            [18.0040695094738, 59.33270475841308],
            [17.99924259095378, 59.33570181766239],
            [17.99635476322395, 59.338453732368976],
            [17.9986953735352, 59.3410684613923],
            [18.0127666471829, 59.3454766863041],
            [18.0187625486702, 59.3444140904369],
            [18.0220463055186, 59.3439764203359],
            [18.0259016745788, 59.3441116382853],
            [18.0307102203369, 59.3471078171971],
            [18.0365522488894, 59.349473277975],
            [18.0416492611209, 59.3519594138417],
            [18.03778376546953, 59.366167253630515],
            [18.039073461128798, 59.366102491137696]
          ]
        ]
      }
    },
    "self_delivery": false,
    "payment_method_restrictions": null,
    "discounts_restrictions_text": "The following categories are excluded from offers: beer, wine, alcohol, tobacco, nicotine products, smokers items and accessories, baby formula and lottery scratchcards",
    "preorder_specs": {
      "preorder_only": false,
      "time_step": 5,
      "homedelivery_spec": {
        "earliest_timedelta": 60,
        "latest_timedelta": 8,
        "preorder_times": {
          "monday": [
            {
              "type": "open",
              "value": 0
            }
          ],
          "tuesday": null,
          "wednesday": null,
          "thursday": null,
          "friday": null,
          "saturday": null,
          "sunday": [
            {
              "type": "close",
              "value": 86400
            }
          ]
        }
      },
      "takeaway_spec": {
        "earliest_timedelta": 45,
        "latest_timedelta": 8,
        "preorder_times": {
          "monday": [
            {
              "type": "open",
              "value": 0
            }
          ],
          "tuesday": null,
          "wednesday": null,
          "thursday": null,
          "friday": null,
          "saturday": null,
          "sunday": [
            {
              "type": "close",
              "value": 86400
            }
          ]
        }
      },
      "eatin_spec": null
    },
    "group_order_id": "",
    "is_venue_favourite": false,
    "order_status": {
      "value": ""
    },
    "order_minimum": 10000,
    "do_use_backend_pricing": "no"
  }
}

        
        """.trimIndent()
    }

}