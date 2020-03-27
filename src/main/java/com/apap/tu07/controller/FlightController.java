package com.apap.tu07.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apap.tu07.model.FlightModel;
import com.apap.tu07.model.PilotModel;
import com.apap.tu07.service.FlightService;
import com.apap.tu07.service.PilotService;

@RestController
@RequestMapping("/flight")
public class FlightController {
	@Autowired
	private FlightService flightService;

	@Autowired
	private PilotService pilotService;


	@PostMapping(value = "/add/{licenseNumber}")
	public FlightModel addFlightSubmit(@RequestBody FlightModel flight, @PathVariable("licenseNumber") String licenseNumber) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
		flight.setPilot(pilot);
		return flightService.addFlight(flight);
	}

	@GetMapping(value = "/view/{flightNumber}")
	public FlightModel flightView(@PathVariable("flightNumber") String flightNumber) {
		FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
		return flight;

	}

	@DeleteMapping(value = "/delete/{flightId}")
	public String deleteFlight(@PathVariable("flightId") long flightId) {
		FlightModel flight = flightService.getFlightById(flightId).get();
		flightService.deleteFlight(flight);
		return "flight has been deleted";
	}

	@PutMapping(value = "/update/{flightId}")
	public String updatePilotSubmit(@PathVariable("flightId") long flightId,
			@RequestParam(value = "destination") String destination,
			@RequestParam(value = "origin") String origin,
			@RequestParam(value = "time") Date time) {
		FlightModel flight = flightService.getFlightById(flightId).get();
		if (flight.equals(null)) {
			return "Couldn't find your flight";
		}
		flight.setDestination(destination);
		flight.setOrigin(origin);
		flight.setTime(time);
		flightService.updateFlight(flightId, flight);
		return "flight update success";
	}

	@GetMapping(value = "/all")
	public List<FlightModel> viewAll() {
		List<FlightModel> flight = flightService.getAllFlight();
		return flight;
	}
}