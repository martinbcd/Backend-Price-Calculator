The application was developed using intellij 2024.3.2
kotlin jvm 2.1
sdk 15
It's runnable from the source code if the project is opened in the intellij ide. Simply run the Main class 

otherwise open a terminal, on windows it'd Windows powershell

navigate to the project folder,
cd ...\DOPC2
(... representing the filepath to the project folder)

use the command "./gradlew build"

then call "java -jar build/libs/DOPC2-all.jar"

after which the server should start on the port 8000

in order to communicate with the program 

either use a web browser and search for the correct adress or use the cmd windows terminal

an example address to use is below, paste it into the browser searchfield and go:
http://localhost:8000/api/v1/delivery-order-price?venue_slug=home-assignment-venue-stockholm&cart_value=1000&user_lat=59.344591&user_lon=18.043240
should return this
{"total_price":11900,"small_order_surcharge":9000,"cart_value":1000,"delivery":{"fee":1900,"distance":706}}

same result if you enter the text below into windows cmd
curl "http://localhost:8000/api/v1/delivery-order-price?venue_slug=home-assignment-venue-stockholm&cart_value=1000&user_lat=59.344591&user_lon=18.043240"

replace the '' values with the corresponding type to enter another custom value
http://localhost:8000/api/v1/delivery-order-price?
venue_slug='String'
&cart_value='int'
&user_lat='double'
&user_lon='double'