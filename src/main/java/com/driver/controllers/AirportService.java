package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class AirportService {

    @Autowired
    AirportRepo arobj;
    public void  addAirport( Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"

        arobj.addAirport(airport);
    }

    public String  getLargestAirportName(){

        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName
        return arobj.getLargestAirportName();

    }
    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.
        return arobj.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);
    }

    public int getNumberOfPeopleOn(Date date, String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
        return arobj.getNumberOfPeopleOn(date,airportName);
    }

    public int calculateFlightFare(int flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        return arobj.calculateFlightFare(flightId);

    }

    public String bookATicket(int flightId,int passengerId){
        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"

        return arobj.bookATicket(flightId,passengerId);
    }

    public String cancelATicket(int flightId,int passengerId){
        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        return arobj.cancelATicket(flightId,passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        return arobj.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public String addFlight(Flight flight){
        //Return a "SUCCESS" message string after adding a flight.
        return arobj.addFlight(flight);
    }

    public String getAirportNameFromFlightId(int flightId){

        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName
        return arobj.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfAFlight(int flightId){

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

       return arobj.calculateRevenueOfAFlight(flightId);
    }

    public String addPassenger( Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        return arobj.addPassenger(passenger);
    }


}
