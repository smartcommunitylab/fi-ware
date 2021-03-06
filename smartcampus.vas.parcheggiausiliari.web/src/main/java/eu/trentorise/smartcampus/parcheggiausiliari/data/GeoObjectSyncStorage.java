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

package eu.trentorise.smartcampus.parcheggiausiliari.data;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.geo.Circle;

import eu.trentorise.smartcampus.parcheggiausiliari.model.GeoObject;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.common.exception.NotFoundException;

public interface GeoObjectSyncStorage {//extends BasicObjectSyncStorage {
	
	public <T extends GeoObject> List<T> searchObjects(Class<T> cls, Circle circle, Map<String, Object> criteria) throws DataException; 
	public <T extends GeoObject> List<T> searchObjects(Class<T> cls, Circle circle, Map<String, Object> criteria, int limit, int skip) throws DataException; 

	public <T extends GeoObject> T getObjectByIdAndAgency(Class<T> cls, String id, String agency) throws DataException, NotFoundException; 
}
