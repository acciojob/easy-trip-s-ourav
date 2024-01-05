package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.*;
@Repository
public class AirportRepository {
    HashMap<String, Airport>airportMap=new HashMap<>();
    HashMap<Integer, Flight>flightMap=new HashMap<>();
    HashMap<Integer, Passenger>passengerMap=new HashMap<>();
    HashMap<Integer,List<Integer>>flightData=new HashMap<>();
    HashMap<Integer,List<Integer>>passengerData=new HashMap<>();

    AirportRepository()
    {
    }
    /* <------------Post-------------> */
    public String addAirport(Airport ap)
    {
        airportMap.put(ap.getAirportName(),ap);
        return "SUCCESS";
    }
    public String addFlight(Flight fp)
    {
        if(flightMap.containsKey(fp.getFlightId())==false)
        {
            flightMap.put(fp.getFlightId(),fp);
            flightData.put(fp.getFlightId(),new ArrayList<>());
            return "SUCCESS";
        }
        else
        {
            return null;
        }
    }
    public String addPassenger(Passenger p)
    {
        if(passengerMap.containsKey(p.getPassengerId())==false)
        {
            passengerMap.put(p.getPassengerId(),p);
            passengerData.put(p.getPassengerId(), new ArrayList<>());
            return "SUCCESS";
        }
        else
        {
            return null;
        }
    }

    /* <------------------GET-------------------> */
    public String getLargestAirport()
    {
        if(airportMap.size()>0)
        {
            List<String> l=new ArrayList<>();
            int largest=Integer.MIN_VALUE;
            for(String key:airportMap.keySet())
            {
                largest=Math.max(largest,airportMap.get(key).getNoOfTerminals());
            }
            for(String key:airportMap.keySet())
            {
                if(largest==airportMap.get(key).getNoOfTerminals())
                {
                    l.add(key);
                }
            }
            Collections.sort(l);
            return  l.get(0);
        }
        else
        {
            return "";
        }
    }

    public double getShortestTimeTravelBetweenCities(City fromCity, City toCity)
    {
        if(flightMap.size()==0)return -1;
        double time=Double.MAX_VALUE;
        for(Flight flight:flightMap.values()){
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity)){
                if(flight.getDuration()<time)time=flight.getDuration();
            }
        }
        if(time==Double.MAX_VALUE)return -1;
        else return time;
    }

    /* <----------------------PUT---------------> */
    public String bookedTicket(int fid,int pid)
    {
        if(flightMap.containsKey(fid)==false || passengerMap.containsKey(pid)==false)
        {
            return "FAILURE";
        }
        if(flightData.get(fid).size()==flightMap.get(fid).getMaxCapacity())
        {
            return "FAILURE";
        }
        if(flightData.get(fid).contains(pid)==true) // change in 2nd  attempt false to true
        {
            return "FAILURE";
        }
        passengerData.get(pid).add(fid);
        flightData.get(fid).add(pid);
        return "SUCCESS";
    }
    public String cancelTicket(int fid,int pid)
    {
        if(flightData.containsKey(fid)==false||passengerData.containsKey(pid)==false){ //checking for a valid flightId and passengerId
            return "FAILURE";
        }else if(flightData.get(fid).contains(pid)==false){ //checking for space availability
            return "FAILURE";
        }else{
            int p1=passengerData.get(pid).indexOf(fid);
            passengerData.get(pid).remove(p1);
            int f1=flightData.get(fid).indexOf(pid);
            flightData.get(pid).remove(f1);
            return "SUCCESS";
        }

    }
    public String getTakeOffAirportNameByFlightId(int fid)
    {
        if(flightMap.containsKey(fid)==false)
        {
            return  null;
        }
        Flight f1=flightMap.get(fid);
        for(Airport ap:airportMap.values())
        {
            if(f1.getFromCity().equals(ap.getCity())) return ap.getAirportName();
        }
        return null;
    }
    public int  getFare(int fid)
    {
        if(flightMap.containsKey(fid)==false)
        {
            return  0;
        }
        return 3000+(flightData.get(fid).size()*50);
    }
    public int getNumberOfPeople(Date date,String apName) throws Exception
    {
        if (flightData.size() == 0 || flightMap.size() == 0 || airportMap.size() == 0) return 0;
        int numberOfPeople = 0;

        for (Flight flight : flightMap.values()) {
            if ((date.equals(flight.getFlightDate()) == true) &&
                    (flight.getFromCity().equals(airportMap.get(apName).getCity()) || flight.getToCity().equals(airportMap.get(apName).getCity()))) {
                numberOfPeople += flightData.get(flight.getFlightId()).size();
            }
        }
        return numberOfPeople;

    }
    public int getBookingCount(int pid)
    {
        if(passengerData.containsKey(pid)==false)
        {
            return 0;
        }
        return passengerData.get(pid).size();
    }
    public int calcRevenue(int fid)
    {
        int totalRevenue=0;
        if(flightData.containsKey(fid)==true) {
            for (int i = 0; i < flightData.get(fid).size(); i++) {
                totalRevenue += 3000 + (i * 50);
            }
        }
        return totalRevenue;
    }
}