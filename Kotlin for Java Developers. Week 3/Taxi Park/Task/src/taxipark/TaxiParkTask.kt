package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    if (this.trips.isEmpty())
        return this.allDrivers
    val validDrivers = mutableSetOf<Driver>()
    val result = mutableSetOf<Driver>()
    for(driver in this.trips){
        validDrivers.add(driver.driver)
    }
    for(item in this.allDrivers){
        if(validDrivers.contains(item))
            continue
        else{
            result.add(item)
        }
    }
    return result
}


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger>{
    /*Base Cases*/
    if(this.allPassengers.isEmpty())
        return setOf()

    if(minTrips == 0)
        return this.allPassengers

    val table = mutableMapOf<Passenger, Int>()
    for(item in this.trips){
        item.passengers.forEach {
            table.putIfAbsent(it,0)
            table[it] = table[it]!! + 1
        }
    }
    val result = mutableSetOf<Passenger>()
    println(table)
    table.forEach {
        if (it.value >= minTrips){
            result.add(it.key)
        }
    }
    return result
}


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger>{
    val driverPassenger = mutableMapOf<Passenger, Int>()
    val result = mutableSetOf<Passenger>()
    this.trips.forEach { trip ->
        if(trip.driver == driver){
            trip.passengers.forEach {
                driverPassenger.putIfAbsent(it, 0)
                driverPassenger[it] = driverPassenger[it]!! + 1
            }
        }
    }
    println(driverPassenger)
    for((key,value) in driverPassenger){
        if(value > 1)
            result.add(key)
    }
    return result
}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger>{
    val smartPassengerCount = mutableMapOf<Passenger, Int>()
    val result = mutableSetOf<Passenger>()
    this.trips.forEach {trip ->
        trip.passengers.forEach {
            val discount = trip.discount?:0.0
            if(discount != 0.0) {
                smartPassengerCount.putIfAbsent(it, 0)
                smartPassengerCount[it] = smartPassengerCount[it]!! + 1
            } else {
                smartPassengerCount.putIfAbsent(it, 0)
                smartPassengerCount[it] = smartPassengerCount[it]!! - 1
            }
        }
    }

    smartPassengerCount.forEach { (passenger, i) ->
        if(i > 0)
            result.add(passenger)
    }

    return result
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if(this.trips.isEmpty())
        return null
    val counter = mutableMapOf<IntRange, Int>()
    this.trips.forEach {    trip ->
        val temp = trip.duration
        if(temp%10 !=0 || temp%10 !=9){
            counter.putIfAbsent(temp - temp%10..temp - temp%10 + 9, 0)
            counter[temp - temp%10..temp - temp%10 + 9] = counter[temp - temp%10..temp - temp%10 + 9]!! + 1
        } else if(temp%10 == 0){
            counter.putIfAbsent(temp..temp+9, 0)
            counter[temp..temp+9] = counter[temp..temp+9]!! + 1
        } else if(temp%10 == 9){
            counter.putIfAbsent(temp-9..temp, 0)
            counter[temp-9..temp] = counter[temp-9..temp]!! + 1
        }
    }
    val result = counter.maxWith(Comparator { a, b -> a.value.compareTo(b.value)})
    return result?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if(this.trips.isEmpty()) {
        return false
    } else {
        val totalTripsCost = this.trips.map { it.cost }.sum()
        val mapCostByDriverSorted =  trips
                .groupBy { it.driver }
                .mapValues { (_, trips) -> trips.sumByDouble { it.cost }}
                .toList()
                .sortedByDescending { (_, value) -> value}.toMap()

        var currentSum = 0.0
        var numberOfDrivers = 0
        for (value in mapCostByDriverSorted.values){
            numberOfDrivers++
            currentSum += value
            if (currentSum >= (totalTripsCost * 0.8)) break
        }

        return numberOfDrivers <= (allDrivers.size * 0.2)
    }

}