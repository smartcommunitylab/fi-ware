/**
 *    Copyright 2014 FBK
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

package eu.trentorise.smartcampus.parcheggiausiliari;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.trentorise.smartcampus.parcheggiausiliari.data.GeoStorage;

@Configuration
public class AppConfig {

	@Value("${smartcampus.vas.web.mongo.host}")
	private String mongohost;
	@Value("${smartcampus.vas.web.mongo.port}")
	private String mongoport;
	@Value("${smartcampus.vas.web.mongo.db}")
	private String mongoDatabaseName;

	@Bean
	public MongoTemplate getMongoTemplate() throws UnknownHostException, MongoException {
		return new MongoTemplate(new Mongo(mongohost, Integer.parseInt(mongoport)), mongoDatabaseName);
	}
	
//	@Bean 
//	public GeoStorage getStorage() throws UnknownHostException, MongoException {
//		return new GeoStorage(getMongoTemplate());
//	}
}
