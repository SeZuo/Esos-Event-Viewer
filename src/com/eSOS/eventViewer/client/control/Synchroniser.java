/*
 * Copyright 2010 Sebastien Zurfluh
 * 
 * This file is part of "ESOS Event Viewer".
 * 
 * "ESOS Event Viewer" is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * "ESOS Event Viewer" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with "ESOS Event Viewer".  If not, see <http://www.gnu.org/licenses/>.
 */

package com.eSOS.eventViewer.client.control;

import java.util.LinkedList;

import org.gwttime.time.Interval;


import com.eSOS.eventViewer.client.control.LocationTuple;
import com.eSOS.eventViewer.client.jUnit.EventFactory;
import com.eSOS.eventViewer.client.model.EventModel;
import com.eSOS.eventViewer.client.model.Location;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.json.client.JSONParser;

/**
 * Synchronise the local model with the JSON data
 * @author Sebastien Zurfluh
 *
 */
public class Synchroniser {


//	private static String getData(String jsonString) {
//		JSONValue jsonValue = JSONParser.parseStrict(jsonString);
//		// The json should contain an object per location
//		JSONObject jsonData = jsonValue.isObject();
//
//		while (Location.hasNext()) {
//			
//			Location loc = Location.next();
//			
//			JSONArray jsResByLocation;
//			JSONString jsSymbol;
//			JSONNumber jsPrice, jsChange;
//			
//			//get the content of the given location
//			if ((jsResByLocation = jsonData.get(loc.getServId()).isArray()) == null) continue;
//			
//			
//			if ((jsonValue = jsStock.get("symbol")) == null) continue;
//			if ((jsSymbol = jsonValue.isString()) == null) continue;
//
//			if ((jsonValue = jsStock.get("price")) == null) continue;
//			if ((jsPrice = jsonValue.isNumber()) == null) continue;
//
//			if ((jsonValue = jsStock.get("change")) == null) continue;
//			if ((jsChange = jsonValue.isNumber()) == null) continue;
//
//			stockPrices.add(new StockPrice(jsSymbol.stringValue(),
//					jsPrice.getValue(),
//					jsChange.getValue()));
//		}
//		return "";
//		updateTable(stockPrices.toArray(new StockPrice[0]));
//	}

	//TODO use this to ask for an update
	//	private void refreshWatchList() {
	//		  // add watch list stock symbols to URL
	//		  String url = "url from sven's page";
	//		  
	//		  Iterator<String> iter = stocks.iterator();
	//		  while (iter.hasNext()) {
	//		    url += iter.next();
	//		      if (iter.hasNext()) {
	//		        url += "+";
	//		      }
	//		  }
	//		  
	//		  url = URL.encode(url);
	//		  
	//		  RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
	//		  
	//		  try {
	//		    Request request = builder.sendRequest(null, new RequestCallback() {
	//		      public void onError(Request request, Throwable exception) {
	//		         displayError("Couldn't retrieve JSON");         
	//		      }
	//
	//		      public void onResponseReceived(Request request, Response response) {
	//		        if (200 == response.getStatusCode()) {
	//		          try {
	//		            // parse the response text into JSON
	//		            JSONValue jsonValue = JSONParser.parse(response.getText());
	//		            JSONArray jsonArray = jsonValue.isArray();
	//		            
	//		            if (jsonArray != null) {
	//		              updateTable(jsonArray);
	//		            } else {
	//		              throw new JSONException(); 
	//		            }
	//		          } catch (JSONException e) {
	//		            displayError("Could not parse JSON");
	//		          }
	//		        } else {
	//		          displayError("Couldn't retrieve JSON (" + response.getStatusText() + ")");
	//		        }
	//		      }       
	//		    });
	//		  } catch (RequestException e) {
	//		    displayError("Couldn't retrieve JSON");         
	//		  }
	//		}
	
	/**
	 * range
	 * @return empty LinkedList means there was an error with the retrieved data OR there is no data.
	 */
	public static LinkedList<EventModel> getEventsInRange(Interval range) {
		LinkedList<EventModel> eventList = new LinkedList<EventModel>();
		
//		// TODO this should be obtained from a call to the server.
//		String jsonString = "{\"sc14b390e8b9d302\":[{\"resid\":\"sc14b719ab7ecb7e\",\"machid\":\"sc14b390e8b9d302\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1265702400\",\"end_date\":\"1265702400\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1265736375\",\"modified\":\"1265742471\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"2\",\"person_id\":\"2\",\"nopersonnes\":\"23\",\"genre\":\"2\",\"status\":\"2\",\"tva\":\"1\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"},{\"resid\":\"sc14b71b426c24c7\",\"machid\":\"sc14b390e8b9d302\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1265788800\",\"end_date\":\"1265788800\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1265742886\",\"modified\":\"1265743089\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"2\",\"person_id\":\"1\",\"nopersonnes\":\"12\",\"genre\":\"3\",\"status\":\"3\",\"tva\":\"1\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"},{\"resid\":\"sc14b71d8b0750ee\",\"machid\":\"sc14b390e8b9d302\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1265875200\",\"end_date\":\"1265875200\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1265752240\",\"modified\":\"\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"0\",\"person_id\":\"0\",\"nopersonnes\":\"0\",\"genre\":\"1\",\"status\":\"3\",\"tva\":\"0\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"},{\"resid\":\"sc14b732de7752fa\",\"machid\":\"sc14b390e8b9d302\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1266307200\",\"end_date\":\"1266307200\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1265839591\",\"modified\":\"1265841012\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"8\",\"person_id\":\"1\",\"nopersonnes\":\"34\",\"genre\":\"3\",\"status\":\"3\",\"tva\":\"1\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"}],\"sc14a8051c495e25\":[{\"resid\":\"sc14b7333ed0d9af\",\"machid\":\"sc14a8051c495e25\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1266393600\",\"end_date\":\"1266393600\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1265841133\",\"modified\":\"\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"1\",\"person_id\":\"1\",\"nopersonnes\":\"256\",\"genre\":\"1\",\"status\":\"1\",\"tva\":\"0\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"},{\"resid\":\"sc14b857141486da\",\"machid\":\"sc14a8051c495e25\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1267776000\",\"end_date\":\"1267776000\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1267036481\",\"modified\":\"1267037285\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"15\",\"person_id\":\"8\",\"nopersonnes\":\"10\",\"genre\":\"3\",\"status\":\"1\",\"tva\":\"1\",\"memberid\":\"sc14b84f172aedf6\",\"owner\":\"\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sandro\",\"lname\":\"Haroutunian\",\"participantid\":\"\"},{\"resid\":\"sc14b855ef4d23b1\",\"machid\":\"sc14a8051c495e25\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1267862400\",\"end_date\":\"1267948800\",\"starttime\":\"480\",\"endtime\":\"1020\",\"created\":\"1267031796\",\"modified\":\"1267035500\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"bonjour sven merci pour tout ton travail\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"1\",\"person_id\":\"6\",\"nopersonnes\":\"200\",\"genre\":\"3\",\"status\":\"3\",\"tva\":\"1\",\"memberid\":\"sc14b84f172aedf6\",\"owner\":\"\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sandro\",\"lname\":\"Haroutunian\",\"participantid\":\"\"}],\"sc14b71cc4d0612e\":[{\"resid\":\"sc14b85129db9c86\",\"machid\":\"sc14b71cc4d0612e\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1266825600\",\"end_date\":\"1266825600\",\"starttime\":\"480\",\"endtime\":\"540\",\"created\":\"1267012253\",\"modified\":\"1267012283\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"Auqune\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"1\",\"client_id\":\"14\",\"person_id\":\"6\",\"nopersonnes\":\"23\",\"genre\":\"2\",\"status\":\"1\",\"tva\":\"1\",\"memberid\":\"sc14b84f172aedf6\",\"owner\":\"\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sandro\",\"lname\":\"Haroutunian\",\"participantid\":\"\"}],\"sc14a8051d697177\":[{\"resid\":\"sc14b819b3d0a9ef\",\"machid\":\"sc14a8051d697177\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1266912000\",\"end_date\":\"1266912000\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1266785085\",\"modified\":\"1267039419\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"3\",\"person_id\":\"1\",\"nopersonnes\":\"90\",\"genre\":\"1\",\"status\":\"3\",\"tva\":\"1\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"}],\"sc14b71cc072cc76\":[{\"resid\":\"sc14b8447b7948be\",\"machid\":\"sc14b71cc072cc76\",\"scheduleid\":\"sc1423642970aa9f\",\"start_date\":\"1266998400\",\"end_date\":\"1266998400\",\"starttime\":\"540\",\"endtime\":\"1020\",\"created\":\"1266960311\",\"modified\":\"\",\"parentid\":\"\",\"is_blackout\":\"0\",\"is_pending\":\"0\",\"summary\":\"\",\"allow_participation\":\"0\",\"allow_anon_participation\":\"0\",\"has_warning\":\"0\",\"client_id\":\"1\",\"person_id\":\"1\",\"nopersonnes\":\"589\",\"genre\":\"1\",\"status\":\"1\",\"tva\":\"0\",\"memberid\":\"sc148ba7c56eebba\",\"owner\":\"1\",\"invited\":\"0\",\"perm_modify\":\"1\",\"perm_delete\":\"1\",\"accept_code\":\"\",\"fname\":\"Sven\",\"lname\":\"Godo\",\"participantid\":\"sc148ba7c56eebba\"}]}";
//		
//		
//		JSONValue jsonValue;
//		if ((jsonValue = JSONParser.parseStrict(jsonString))==null) return eventList;
//		// The JSON should contain an object
//		JSONObject jsLocationObject;
//		if ((jsLocationObject = jsonValue.isObject())==null) return eventList;
//
//		// Retrieve the desired data. jsonData contains an object per location. 
//		// Inside location objects lie arrays of reservation objects
//		while (Location.hasNext()) {
//			JSONArray jsReservationArray;
//			JSONString jsResId, jsMachId, jsName, jsSummary;
//			JSONNumber jsStartDate, jsStartTime, jsEndDate, jsEndTime;
//			
//			try {
//				if ((jsReservationArray = jsLocationObject.get(Location.next().getServId()).isArray()) == null) continue;
//				for (int i = 0; i < jsReservationArray.size(); i++) {
//					if ((jsResId = jsLocationObject.get("resid").isString()) == null) continue;
//					if ((jsMachId = jsLocationObject.get("machid").isString()) == null) continue;
//					if ((jsName = jsLocationObject.get("name").isString()) == null) continue;
//					if ((jsSummary = jsLocationObject.get("summary").isString()) == null) continue;
//					if ((jsStartDate = jsLocationObject.get("start_date").isNumber()) == null) continue;
//					if ((jsStartTime = jsLocationObject.get("starttime").isNumber()) == null) continue;
//					if ((jsEndDate = jsLocationObject.get("end_date").isNumber()) == null) continue;
//					if ((jsEndTime = jsLocationObject.get("endtime").isNumber()) == null) continue;
//
//
//
//					eventList.add(EventFactory.createEventModel(jsResId.stringValue(),jsMachId.stringValue(),
//							jsName.stringValue(),jsSummary.stringValue(),
//							((Double)jsStartDate.doubleValue()).longValue(),
//							((Double)jsStartTime.doubleValue()).longValue(),
//							((Double)jsEndDate.doubleValue()).longValue(),
//							((Double)jsEndTime.doubleValue()).longValue() ));
//				}
//			} catch (Exception e) {
//				// The requested room's got no data
//				System.out.println("error in json parsing");
//				continue;
//			}
//		}
//		
//		
//		return eventList;
		// Use this line for testing purposes only
		return EventFactory.createRandomInTimeRange(50, range);
	}
	
	public static LinkedList<LocationTuple> getLocations() {
		LinkedList<LocationTuple> locTupleList = new LinkedList<LocationTuple>();
		
		
		// TODO this should be obtained from a call to the server.
		String jsonString = "[{\"machid\":\"sc14b390eb072102\",\"name\":\"Erlach\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14a8051c495e25\",\"name\":\"Espace Piccard\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14b71cc072cc76\",\"name\":\"Fontana 1 Matin\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14b390e8b9d302\",\"name\":\"Fontana 2 Aprem\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14b71cc4d0612e\",\"name\":\"Fontana 2 Soir\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14b390e7372931\",\"name\":\"Soldati\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14a8051d697177\",\"name\":\"Tenete Gallatin\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14a8051b25c07c\",\"name\":\"Terrase Cent-Suisse\",\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"},{\"machid\":\"sc14a805195a7d2c\",\"name\":null,\"status\":\"a\",\"approval\":\"0\",\"min_notice_time\":\"0\",\"max_notice_time\":\"0\",\"scheduleid\":\"sc1423642970aa9f\"}]";
		
		
		
		
		JSONValue jsonValue = JSONParser.parseStrict(jsonString);
		// The json should contain an array
		JSONArray jsonData = jsonValue.isArray();

		for (int i=0; i<jsonData.size(); i++) {
			JSONObject jsLocation;
			JSONString jsName, jsMachId;
			
			//get the data object by object. Each object represents a location
			if ((jsLocation = jsonData.get(i).isObject()) == null) continue;
			
			// retrieve the desired data
			if ((jsName = jsLocation.get("name").isString()) == null) continue;
			if ((jsMachId = jsLocation.get("machid").isString()) == null) continue;

			locTupleList.add(new LocationTuple(jsName.stringValue(),jsMachId.stringValue()));
		}
		return locTupleList;
	}
}
