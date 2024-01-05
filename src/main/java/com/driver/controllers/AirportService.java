package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {
    private AirportRepository apR=new AirportRepository();
    /* <---------Post-------------> */
    public String addAirport(Airport ap)
    {
        return apR.addAirport(ap);
    }
    public String  addFlight(Flight fp)
    {
        return apR.addFlight(fp);
    }
    public String  addPassenger(Passenger p)
    {
        return apR.addPassenger(p);
    }
    public String  bookTicket(int fid,int pid)
    {
        return apR.bookedTicket(fid,pid);
    }
    public String  cancelTicket(int fid,int pid)
    {
        return apR.cancelTicket(fid,pid);
    }
    public String getTakeOffAirportNameByFlightId(int  fid)
    {
        return apR.getTakeOffAirportNameByFlightId(fid);
    }
    public int getFare(int  fid)
    {
        return apR.getFare(fid);
    }
    public int getNumberOfPeople(Date date,String  apName)
    {
        try {
            return apR.getNumberOfPeople(date,apName);
        }
        catch (Exception e)
        {
            //throw new RuntimeException();
            return 0;
        }
    }
    public int getBookingCount(int pid)
    {
        return apR.getBookingCount(pid);
    }
    public  int calcRevenue(int fid)
    {
        return apR.calcRevenue(fid);
    }


    /* <--------------GET------------------>*/
    public String getLargestAirport()
    {
        return apR.getLargestAirport();
    }
    public double getShortestTimeTravelBetweenCities(City from,City to)
    {
        return apR.getShortestTimeTravelBetweenCities(from,to);
    }


}