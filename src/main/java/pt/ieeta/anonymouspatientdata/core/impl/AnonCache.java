/*  Copyright   2016 - Jorge Miguel Ferreira da Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  PersistantDataLucene is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PersistantDataLucene is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.ieeta.anonymouspatientdata.core.impl;

import org.restlet.data.CacheDirective;
import org.restlet.engine.header.CacheDirectiveWriter;
/**
 * @author Jorge Miguel Ferreira das Silva
 *
 */
public class AnonCache {

	CacheDirective cD = new CacheDirective(getPath());
	CacheDirectiveWriter cDW;
	String cacheDirectory= "./cacheDirectory";

	/**
	 * 
	 */
	public AnonCache() {

	}
	
	private String getPath() {
		return this.cacheDirectory;
	}

	
	
}
