package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Repository
public class AirportRepo {
    HashMap<String,Airport> airportMap = new HashMap<>();
    HashMap<Integer,Flight> flightMap = new HashMap<>();
    HashMap<Integer,Passenger> passengerMap = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> fIdVsPassId=new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> pIdVsFlight=new HashMap<>();


    public void  addAirport( Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"

        airportMap.put(airport.getAirportName(),airport);
    }

    public String  getLargestAirportName(){

        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName
        int max=0;
        String LargestName="";
        for(String airport : airportMap.keySet()){
            if(airportMap.get(airport).getNoOfTerminals()>max){
                max=airportMap.get(airport).getNoOfTerminals();
                LargestName=new String(airport);
            }
            else if(airportMap.get(airport).getNoOfTerminals()==max && airportMap.get(airport).getAirportName().compareTo(LargestName)<0){
                LargestName=new String(airport);
            }
        }
        return LargestName;

    }
    public double getShortestDurationOfPossibleBetweenTwoCities( City fromCity, City toCity){
        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.
        double ans=Double.MAX_VALUE;
        for(int flightId :flightMap.keySet()){
            if(flightMap.get(flightId).getFromCity().equals(fromCity) && flightMap.get(flightId).getToCity().equals(toCity)){
                ans=Math.min(ans,flightMap.get(flightId).getDuration());
            }
        }

        return ans==Double.MAX_VALUE?-1:ans;
    }

    public int getNumberOfPeopleOn(Date date,String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
        int ans=0;
        for(int fid : fIdVsPassId.keySet() ){
            if( flightMap.get(fid).getFlightDate().equals(date) && ( (flightMap.get(fid).getFromCity().name().equals(airportName)) || (flightMap.get(fid).getToCity().name().equals(airportName)) ) ) {
                ans+= fIdVsPassId.get(fid).size();
            }
        }
        return ans;
    }

    public int calculateFlightFare(int flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        if(!flightMap.containsKey((flightId)) || !fIdVsPassId.containsKey(flightId))
            return 0;

        int noOfPeopleWhoHaveAlreadyBooked= fIdVsPassId.get(flightId).size()  ;
        return (3000 + noOfPeopleWhoHaveAlreadyBooked*50);

    }

    public String bookATicket(int flightId,int passengerId){
        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"

        if(fIdVsPassId.get(flightId).size()>=flightMap.get(flightId).getMaxCapacity()){
            return "FAILURE";
        }
        if(pIdVsFlight.containsKey(passengerId))
            return "FAILURE";

        ArrayList<Integer> temp= fIdVsPassId.containsKey(flightId) ? fIdVsPassId.get(flightId): new ArrayList<>();
        temp.add(passengerId);
        fIdVsPassId.put(flightId,new ArrayList<>(temp)) ;

        temp=pIdVsFlight.containsKey(passengerId) ? pIdVsFlight.get(passengerId) : new ArrayList<>();
        temp.add(flightId);
        pIdVsFlight.put(passengerId,new ArrayList<>(temp));

        return "SUCCESS";
    }

    public String cancelATicket(int flightId,int passengerId){
        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId

        if( !passengerMap.containsKey(passengerId) || !flightMap.containsKey(flightId) || !fIdVsPassId.containsKey(flightId) || !pIdVsFlight.containsKey(passengerId) )
            return "FAILURE";
        boolean flag=false;

        for(int fid : fIdVsPassId.keySet()){
            if(fid==flightId){
                for(int i=0;i<fIdVsPassId.get(fid).size();i++){
                    if(passengerId==fIdVsPassId.get(fid).get(i)){
                        fIdVsPassId.get(fid).remove(i);
                        if(fIdVsPassId.get(fid).size()==0)
                            fIdVsPassId.remove(fid);
                        flag=true;
                        break;
                    }
                }
            }
        }
        if(!flag)
            return "FAILURE";

        pIdVsFlight.remove(passengerId);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(int passengerId){
        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        if(!pIdVsFlight.containsKey(passengerId))
            return 0;
        return pIdVsFlight.get(passengerId).size();
    }

    public String addFlight(Flight flight){
        //Return a "SUCCESS" message string after adding a flight.
        flightMap.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(int flightId){

        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName
        if(!flightMap.containsKey(flightId))
            return null;
       return flightMap.get(flightId).getFromCity().name();
    }

    public int calculateRevenueOfAFlight(int flightId){

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        int revenue=0;
        if(!fIdVsPassId.containsKey(flightId))
            return 0;
        ArrayList<Integer> passengers=fIdVsPassId.get(flightId);
        int n=passengers.size();
        for(int i=0;i<n;i++){
            revenue+= (3000+i*50);
        }

        return revenue;
    }

    public String addPassenger( Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        passengerMap.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}

