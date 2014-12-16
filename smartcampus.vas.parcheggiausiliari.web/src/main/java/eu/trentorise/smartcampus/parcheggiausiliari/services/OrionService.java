/**
 *    Copyright 2012-2013 Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.smartcampus.parcheggiausiliari.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import eu.trentorise.smartcampus.parcheggiausiliari.model.Parking;
import eu.trentorise.smartcampus.parcheggiausiliari.model.Street;

@Service
public class OrionService {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(OrionService.class);

	private Client client;

	@Value("${orion.parking.type}")
	@Autowired
	public String parkingType;

	@Value("${orion.bike.type}")
	@Autowired
	public String bykeType;

	@Value("${orion.bikerepo.type}")
	@Autowired
	public String bikerepoType;

	@Value("${orion.bike.simulate}")
	@Autowired
	public boolean bikeSimulate;

	private static final int T_BIKE = 1;
	private static final int T_BIKEREPO = 2;

	@PostConstruct
	@SuppressWarnings("unused")
	private void init() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
	}

	private boolean updateFailed(ClientResponse resp) {
		Map r = resp.getEntity(Map.class);
		return "404".equalsIgnoreCase((String) ((Map) ((Map) ((List) r
				.get("contextResponses")).get(0)).get("statusCode"))
				.get("code"));
	}

	public void insert(String entityType, Parking o) {
		ClientResponse res = client
				.resource("http://orion:1026/NGSI10/updateContext")
				.type("application/json").accept("application/json")
				.post(ClientResponse.class, convert(entityType, o));

		if (updateFailed(res)) {
			OrionInsert ins = convert(entityType, o);
			ins.updateAction = "APPEND";
			client.asyncResource("http://orion:1026/NGSI10/updateContext")
					.type("application/json").accept("application/json")
					.post(ins);
			logger.info("orion insert DONE");
		} else {
			logger.info("orion update DONE");
		}

	}

	public void insert(String entityType, Street o) {
		ClientResponse res = client
				.resource("http://orion:1026/NGSI10/updateContext")
				.type("application/json").accept("application/json")
				.post(ClientResponse.class, convert(entityType, o));

		if (updateFailed(res)) {
			OrionInsert ins = convert(entityType, o);
			ins.updateAction = "APPEND";
			client.asyncResource("http://orion:1026/NGSI10/updateContext")
					.type("application/json").accept("application/json")
					.post(ins);
			logger.info("orion insert DONE");
		} else {
			logger.info("orion update DONE");
		}
	}

	public void insertBike(String entityType, Map<String, Object> o) {

		ClientResponse res = client
				.resource("http://orion:1026/NGSI10/updateContext")
				.type("application/json").accept("application/json")
				.post(ClientResponse.class, convert(entityType, o, T_BIKE));

		if (updateFailed(res)) {
			OrionInsert ins = convert(entityType, o, T_BIKE);
			ins.updateAction = "APPEND";
			client.asyncResource("http://orion:1026/NGSI10/updateContext")
					.type("application/json").accept("application/json")
					.post(ins);
			logger.info("orion insert DONE");
		} else {
			logger.info("orion update DONE");
		}
	}

	private ContextElement randomizeBikes(String entityType,
			Map<String, Object> o) {
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int min = 0, max = (Integer) o.get("maxSlots");
		int rBikes = rand.nextInt((max - min) + 1) + min;

		int rBroken = rand.nextInt((max - min) + 1) + min;
		min = 0;
		max = 10;
		int rReport = rand.nextInt((max - min) + 1) + min;

		return new ContextElement(entityType, String.valueOf(o.get("id"))
				.replaceAll(" ", "_"), Arrays.asList(
				new Tuple("name", String.valueOf(o.get("name"))), new Tuple(
						"nBikes", String.valueOf(rBikes)), new Tuple(
						"maxSlots", String.valueOf(o.get("maxSlots"))),
				new Tuple("nBrokenBikes", String.valueOf(rBroken)), new Tuple(
						"street", String.valueOf(o.get("street"))), new Tuple(
						"position", String.valueOf(o.get("latitude")) + ","
								+ String.valueOf(o.get("longitude"))),
				new Tuple("reportsNumber", String.valueOf(rReport))));
	}

	// {"name":"Noriglio - Rovereto","street":"Noriglio - Rovereto","id":"Noriglio - Rovereto","nBikes":3,"maxSlots":6,"nBrokenBikes":4,"latitude":45.88365364364294,"longitude":11.070399481792492,"reportsNumber":1}
	private OrionInsert convert(String entityType, Map<String, Object> o,
			int type) {
		OrionInsert payload = new OrionInsert();

		List<ContextElement> entities = new ArrayList<ContextElement>();

		switch (type) {
		case T_BIKE:
			entities.add(bikeSimulate ? randomizeBikes(entityType, o)
					: new ContextElement(
							entityType,
							String.valueOf(o.get("id")).replaceAll(" ", "_"),
							Arrays.asList(
									new Tuple("name", String.valueOf(o
											.get("name"))),
									new Tuple("nBikes", String.valueOf(o
											.get("nBikes"))),
									new Tuple("maxSlots", String.valueOf(o
											.get("maxSlots"))),
									new Tuple("nBrokenBikes", String.valueOf(o
											.get("nBrokenBikes"))),
									new Tuple("street", String.valueOf(o
											.get("street"))),
									new Tuple("position",
											String.valueOf(o.get("latitude"))
													+ ","
													+ String.valueOf(o
															.get("longitude"))),
									new Tuple("reportsNumber", String.valueOf(o
											.get("reportsNumber"))))));
			logger.info("bike converted");
			break;

		case T_BIKEREPO:

			break;

		default:
			break;
		}

		payload.setContextElements(entities);
		return payload;
	}

	private OrionInsert convert(String entityType, Street o) {
		OrionInsert payload = new OrionInsert();

		List<ContextElement> entities = new ArrayList<ContextElement>();
		entities.add(new ContextElement(entityType, o.getId(), Arrays.asList(
				new Tuple("name", o.getName()),
				new Tuple("slotOccupied", String.valueOf(o
						.getSlotsOccupiedOnPaying())),
				new Tuple("position", o.getPosition()[0] + ","
						+ o.getPosition()[1]),
				new Tuple("agency", o.getAgency()))));
		payload.setContextElements(entities);
		logger.info("street converted");
		return payload;
	}

	private OrionInsert convert(String entityType, Parking o) {
		OrionInsert payload = new OrionInsert();

		List<ContextElement> entities = new ArrayList<ContextElement>();
		entities.add(new ContextElement(entityType, o.getId(), Arrays.asList(
				new Tuple("name", o.getName()),
				new Tuple("slotOccupied", String.valueOf(o
						.getSlotsOccupiedOnTotal())),
				new Tuple("position", o.getPosition()[0] + ","
						+ o.getPosition()[1]),
				new Tuple("agency", o.getAgency()))));
		payload.setContextElements(entities);
		logger.info("parking converted");
		return payload;
	}

	private class OrionInsert {
		private List<ContextElement> contextElements;
		public String updateAction = "UPDATE";

		public List<ContextElement> getContextElements() {
			return contextElements;
		}

		public void setContextElements(List<ContextElement> contextElements) {
			this.contextElements = contextElements;
		}

		public String getUpdateAction() {
			return updateAction;
		}

	}

	private class Tuple {
		private String e1;
		private String e2;

		public Tuple(String e1, String e2) {
			this.e1 = e1;
			this.e2 = e2;
		}

		public String getE1() {
			return e1;
		}

		public String getE2() {
			return e2;
		}

	}

	private class ContextElement {
		private String type;
		private String isPattern = "false";
		private String id;
		private List<OrionAttribute> attributes;

		public ContextElement(String type, String id, List<Tuple> attrs) {
			this.type = type;
			this.id = id;
			attributes = new ArrayList<OrionAttribute>();
			for (Tuple t : attrs) {
				attributes.add(new OrionAttribute(t.getE1(), "", t.getE2()));
			}
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getIsPattern() {
			return isPattern;
		}

		public void setIsPattern(String isPattern) {
			this.isPattern = isPattern;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<OrionAttribute> getAttributes() {
			return attributes;
		}

		public void setAttributes(List<OrionAttribute> attributes) {
			this.attributes = attributes;
		}

	}

	private class OrionAttribute {
		private String name;
		private String type;
		private String value;

		public OrionAttribute(String name, String type, String value) {
			this.name = name;
			this.type = type;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
