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

package eu.trentorise.smartcampus.parcheggiausiliari.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.ExtendedData;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.SimpleData;
import eu.trentorise.smartcampus.parcheggiausiliari.model.KMLData;

public class KMLHelper {

	public static List<KMLData> readData(InputStream is) {
		List<KMLData> data = new ArrayList<KMLData>();
		
		final Kml kml = Kml.unmarshal(is);
		final Document document = (Document)kml.getFeature();
		List<Feature> t = document.getFeature();
		for(Object o : t){
	        Folder f = (Folder)o;
	        List<Feature> tg = f.getFeature();
	        for(Object ftg : tg){
	            Placemark pm = (Placemark) ftg;
	            ExtendedData ext = pm.getExtendedData();
	            String name = "", id = "";
	            int total = 0;
	            for (SimpleData d: ext.getSchemaData().get(0).getSimpleData()) {
	            	if ("Name".equalsIgnoreCase(d.getName())) {
	            		name = d.getValue();
	            	}
	            	if ("Id".equalsIgnoreCase(d.getName())) {
	            		id = d.getValue();
	            	}
	            	if ("Total".equalsIgnoreCase(d.getName())) {
	            		total = Integer.parseInt(d.getValue());
	            	}
	            }
	            Point point = (Point)pm.getGeometry();
	            
	            KMLData kd = new KMLData();
	            kd.setName(name);
	            kd.setId(id);
	            kd.setLat(point.getCoordinates().get(0).getLatitude());
	            kd.setLon(point.getCoordinates().get(0).getLongitude());
	            kd.setTotal(total);
	            data.add(kd);
	        }
	    }
		return data;
	}
}
