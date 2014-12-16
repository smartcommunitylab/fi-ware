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

package eu.trentorise.smartcampus.parcheggiausiliari.model;


public class Parking extends GeoObject {
	private static final long serialVersionUID = 8076535734041609036L;

	private int slotsTotal;
	private int slotsOccupiedOnTotal;
	private int slotsUnavailable;
	private LastChange lastChange;
	
	public int getSlotsTotal() {
		return slotsTotal;
	}

	public int getSlotsOccupiedOnTotal() {
		return slotsOccupiedOnTotal;
	}

	public void setSlotsOccupiedOnTotal(int mSlotsOccupiedOnTotal) {
		this.slotsOccupiedOnTotal = mSlotsOccupiedOnTotal;
	}

	public int getSlotsUnavailable() {
		return slotsUnavailable;
	}

	public void setSlotsUnavailable(int mSlotsUnavailable) {
		this.slotsUnavailable = mSlotsUnavailable;
	}

	public LastChange getLastChange() {
		return lastChange;
	}

	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}

	public void setLastChange(LastChange lastChange) {
		this.lastChange = lastChange;
	}
}
