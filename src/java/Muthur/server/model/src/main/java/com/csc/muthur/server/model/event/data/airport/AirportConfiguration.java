/**
 * 
 */
package com.csc.muthur.server.model.event.data.airport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Element;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.GeoPoint;
import com.csc.muthur.server.model.event.data.Runway;
import com.csc.muthur.server.model.event.data.RunwayFlow;
import com.csc.muthur.server.model.event.data.RunwayName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AirportConfiguration extends AiportDataObject {

	private Map<RunwayName, Runway> runways =
			new ConcurrentHashMap<RunwayName, Runway>();

	private Runway currentRunway;
	private GeoPoint currentGeoPoint;

	/**
	 * 
	 * @param airportCode
	 * @throws MuthurException
	 */
	public AirportConfiguration(final String airportCode) throws MuthurException {
		super(airportCode);
	}

	/**
	 * 
	 */
	public AirportConfiguration() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getEventName()
	 */
	@Override
	public String getEventName() {
		return DataTypeEnum.AirportConfiguration.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.AirportConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	@Override
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// runways element

			Element runwaysElement =
					new Element(EventAttributeName.runways.toString());

			for (Runway runway : runways.values()) {

				Element runwayElement =
						new Element(EventAttributeName.runway.toString());

				Element nextEle =
						new Element(EventAttributeName.runwayName.toString())
								.setText(runway.getName().toString());
				runwayElement.addContent(nextEle);

				nextEle =
						new Element(EventAttributeName.runwayFlow.toString())
								.setText(runway.getRunwayFlow().toString());
				runwayElement.addContent(nextEle);

				// upper left

				Element geoPointElement = null;

				if (runway.getUpperLeft() != null) {

					geoPointElement =
							new Element(EventAttributeName.runwayUpperLeft.toString());

					nextEle =
							new Element(EventAttributeName.latitudeDegrees.toString())
									.setText(Double.toString(runway.getUpperLeft().getLatitude()));
					geoPointElement.addContent(nextEle);

					nextEle =
							new Element(EventAttributeName.longitudeDegrees.toString())
									.setText(Double
											.toString(runway.getUpperLeft().getLongitude()));
					geoPointElement.addContent(nextEle);
					runwayElement.addContent(geoPointElement);
				}

				// lower right

				if (runway.getLowerRight() != null) {

					geoPointElement =
							new Element(EventAttributeName.runwayLowerRight.toString());

					nextEle =
							new Element(EventAttributeName.latitudeDegrees.toString())
									.setText(Double
											.toString(runway.getLowerRight().getLatitude()));
					geoPointElement.addContent(nextEle);

					nextEle =
							new Element(EventAttributeName.longitudeDegrees.toString())
									.setText(Double.toString(runway.getLowerRight()
											.getLongitude()));
					geoPointElement.addContent(nextEle);

					runwayElement.addContent(geoPointElement);
				}

				// add the runway to the runways element

				runwaysElement.addContent(runwayElement);

			}

			// add the runways to the datablock element

			dataBlockElement.addContent(runwaysElement);

		}
	}

	@Override
	public void startOfElement(String currentElementName) {

		if ((currentElementName != null) && (currentElementName.length() > 0)) {

			if (currentElementName.equalsIgnoreCase(EventAttributeName.runway
					.toString())) {
				currentRunway = new Runway();
			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.runwayLowerRight.toString())) {
				currentGeoPoint = new GeoPoint();
				if (currentRunway != null) {
					currentRunway.setLowerRight(currentGeoPoint);
				}
			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.runwayUpperLeft.toString())) {
				currentGeoPoint = new GeoPoint();
				if (currentRunway != null) {
					currentRunway.setUpperLeft(currentGeoPoint);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.data.AbstractDataObject#newElement(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName.equalsIgnoreCase(EventAttributeName.airportCode
					.toString())) {
				setAirportCode(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.runwayFlow
					.toString())) {
				if (currentRunway != null) {
					currentRunway.setRunwayFlow(RunwayFlow.valueOf(elementValue));
				}
			} else if (elementName.equalsIgnoreCase(EventAttributeName.runwayName
					.toString())) {
				if (currentRunway != null) {
					currentRunway.setName(elementValue);
				}
			} else if (elementName.equalsIgnoreCase(EventAttributeName.altitudeFt
					.toString())) {
				if (currentGeoPoint != null) {
					currentGeoPoint.setAltitude(Double.valueOf(elementValue));
				}
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.longitudeDegrees.toString())) {
				if (currentGeoPoint != null) {
					currentGeoPoint.setLongitude(Double.valueOf(elementValue));
				}
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.latitudeDegrees.toString())) {
				if (currentGeoPoint != null) {
					currentGeoPoint.setLatitude(Double.valueOf(elementValue));
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.data.AbstractDataObject#endOfElement(java.lang
	 * .String)
	 */
	@Override
	public void endOfElement(String currentElementName) {

		if ((currentElementName != null)
				&& (!"".equalsIgnoreCase(currentElementName))) {

			if (currentElementName.equalsIgnoreCase(EventAttributeName.runway
					.toString())) {
				if (currentRunway != null) {
					runways.put(currentRunway.getName(), currentRunway);
				}
			}
		}
	}


	/**
	 * Returns the {@link Runway} with the matching {@link RunwayName} constructed
	 * from name.
	 * 
	 * @param name
	 *          Name of the runway to retrieve.
	 * @return
	 */
	public Runway getRunway(final String name) {
		Runway runway = null;

		if (name != null) {

			RunwayName runwayName = new RunwayName(name);

			if (runwayName != null) {
				runway = runways.get(runwayName);
			}
		}
		return runway;
	}

	/**
	 * @return the runways
	 */
	public final Map<RunwayName, Runway> getRunways() {
		return runways;
	}

	/**
	 * @param runways
	 *          the runways to set
	 */
	public final void setRunways(Map<RunwayName, Runway> runways) {
		this.runways = runways;
	}

	/**
	 * 
	 * @param runway
	 */
	public final void addRunway(final Runway runway) {
		if (runway != null) {
			runways.put(runway.getName(), runway);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((runways == null) ? 0 : runways.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AirportConfiguration [runways=" + runways + ", toString()="
				+ super.toString() + "]";
	}
}
