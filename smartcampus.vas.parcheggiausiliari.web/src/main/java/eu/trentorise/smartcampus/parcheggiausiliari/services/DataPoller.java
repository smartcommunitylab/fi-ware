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

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Service
@SuppressWarnings("all")
public class DataPoller {

	// - per stazioni:
	// https://tn.smartcampuslab.it/bikesharing/stations/rovereto
	// - per log della stazione:
	// https://tn.smartcampuslab.it/bikesharing/stations/rovereto/{stationId}/reports

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(DataPoller.class);

	@Autowired
	private OrionService orionService;

	private Client client;

	@PostConstruct
	private void init() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		client = Client.create(clientConfig);
	}

	@Scheduled(fixedRate = 2 * 60 * 1000)
	public void retrieveBikes() {
		logger.info("retriving bike data...");
		ClientResponse res = client
				.resource(
						"https://tn.smartcampuslab.it/bikesharing/stations/rovereto")
				.accept("application/json").get(ClientResponse.class);

		if (res.getStatus() == 200) {
			Map<String, Object> e = res.getEntity(Map.class);

			List<Map<String, Object>> d = (List<Map<String, Object>>) e
					.get("data");
			for (Map<String, Object> dd : d) {
				orionService.insertBike(orionService.bykeType, dd);
			}
		}

		logger.info("BIKE orion DONE");
	}

	public void retrieveBikeReports() {

	}

	public void retrieveTnParks() {

	}
}
