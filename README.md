# Backend-Price-Calculator
 Wolt 2025 Backend Engineering Internship submission
 original assignment found here: https://github.com/woltapp/backend-internship-2025


The application was developed using: 
- intellij 2024.3.2
- kotlin jvm 2.1
- sdk 15

It's runnable from the source code if the project is opened in the intellij ide. 
Simply run the Main class otherwise open a terminal, on Windows it would be Windows powershell.

Navigate to the project folder:
- cd ...\DOPC2
(... representing the filepath to the project folder)

- Use the command "./gradlew build"
- Call "java -jar build/libs/DOPC2-all.jar"
the server should now start on the port 8000

In order to communicate with the program, either use a web browser and search for the correct address or use the cmd windows terminal.

An example address to use is below, paste it into the browser searchfield and go:
http://localhost:8000/api/v1/delivery-order-price?venue_slug=home-assignment-venue-stockholm&cart_value=1000&user_lat=59.344591&user_lon=18.043240

This should be returned:
{"total_price":11900,"small_order_surcharge":9000,"cart_value":1000,"delivery":{"fee":1900,"distance":706}}

You will have the same result if you enter the text below into windows cmd:
curl "http://localhost:8000/api/v1/delivery-order-price?venue_slug=home-assignment-venue-stockholm&cart_value=1000&user_lat=59.344591&user_lon=18.043240"

Replace the '' values with the corresponding type to enter another custom value:
http://localhost:8000/api/v1/delivery-order-price?
venue_slug='String'
&cart_value='int'
&user_lat='double'
&user_lon='double'
