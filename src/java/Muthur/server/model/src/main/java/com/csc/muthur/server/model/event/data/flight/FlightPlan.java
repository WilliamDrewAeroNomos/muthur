/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.DataTypeEnum;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FlightPlan extends FlightDataObject {

	private static final Logger LOG = LoggerFactory.getLogger(FlightPlan.class
			.getName());

	/**
	 * flight plan data
	 */
	private String source;
	private String aircraftType;

	private double cruiseSpeedKts;
	private double cruiseAltitudeFt;
	private String routePlan;

	private String departureAirportCode;
	private String arrivalAirportCode;

	private long plannedDepartureTimeMSecs;
	private String plannedDepartureRunway;
	private String plannedTaxiOutGate;
	private String departureCenter;
	private String departureFix;

	private long plannedArrivalTimeMSecs;
	private String plannedArrivalRunway;
	private String plannedTaxiInGate;
	private String arrivalCenter;
	private String arrivalFix;

	private String physicalAircraftClass;
	private String weightAircraftClass;

	private String userAircraftClass;
	private int numOfAircraft;
	private String airborneEquipmentQualifier;

	private FlightPlanStatus flightPlanStatus = FlightPlanStatus.ACTIVE;

	private String acLivery;

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public FlightPlan(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public FlightPlan() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getEventName()
	 */
	@Override
	public String getEventName() {
		return DataTypeEnum.FlightPlan.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.FlightPlan;
	}

	/**
	 * @return the source
	 */
	public final String getSource() {
		return source;
	}

	/**
	 * @param source
	 *          the source to set
	 */
	public final void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the aircraftType
	 */
	public final String getAircraftType() {
		return aircraftType;
	}

	/**
	 * @param aircraftType
	 *          the aircraftType to set
	 */
	public final void setAircraftType(String aircraftType) {
		this.aircraftType = aircraftType;
	}

	/**
	 * @return the plannedDepartureTimeMSecs
	 */
	public final long getPlannedDepartureTimeMSecs() {
		return plannedDepartureTimeMSecs;
	}

	/**
	 * @param plannedDepartureTimeMSecs
	 *          the plannedDepartureTimeMSecs to set
	 */
	public final void
			setPlannedDepartureTimeMSecs(long plannedDepartureTimeMSecs) {
		this.plannedDepartureTimeMSecs = plannedDepartureTimeMSecs;
	}

	/**
	 * @return the plannedDepartureRunway
	 */
	public final String getPlannedDepartureRunway() {
		return plannedDepartureRunway;
	}

	/**
	 * @param plannedDepartureRunway
	 *          the plannedDepartureRunway to set
	 */
	public final void setPlannedDepartureRunway(String plannedDepartureRunway) {
		this.plannedDepartureRunway = plannedDepartureRunway;
	}

	/**
	 * @return the plannedTaxiOutGate
	 */
	public final String getPlannedTaxiOutGate() {
		return plannedTaxiOutGate;
	}

	/**
	 * @param plannedTaxiOutGate
	 *          the plannedTaxiOutGate to set
	 */
	public final void setPlannedTaxiOutGate(String plannedTaxiOutGate) {
		this.plannedTaxiOutGate = plannedTaxiOutGate;
	}

	/**
	 * @return the plannedArrivalTimeMSecs
	 */
	public final long getPlannedArrivalTimeMSecs() {
		return plannedArrivalTimeMSecs;
	}

	/**
	 * @param plannedArrivalTimeMSecs
	 *          the plannedArrivalTimeMSecs to set
	 */
	public final void setPlannedArrivalTimeMSecs(long plannedArrivalTimeMSecs) {
		this.plannedArrivalTimeMSecs = plannedArrivalTimeMSecs;
	}

	/**
	 * @return the plannedArrivalRunway
	 */
	public final String getPlannedArrivalRunway() {
		return plannedArrivalRunway;
	}

	/**
	 * @param plannedArrivalRunway
	 *          the plannedArrivalRunway to set
	 */
	public final void setPlannedArrivalRunway(String plannedArrivalRunway) {
		this.plannedArrivalRunway = plannedArrivalRunway;
	}

	/**
	 * @return the plannedTaxiInGate
	 */
	public final String getPlannedTaxiInGate() {
		return plannedTaxiInGate;
	}

	/**
	 * @param plannedTaxiInGate
	 *          the plannedTaxiInGate to set
	 */
	public final void setPlannedTaxiInGate(String plannedTaxiInGate) {
		this.plannedTaxiInGate = plannedTaxiInGate;
	}

	/**
	 * @return the cruiseSpeedKts
	 */
	public final double getCruiseSpeedKts() {
		return cruiseSpeedKts;
	}

	/**
	 * @param cruiseSpeedKts
	 *          the cruiseSpeedKts to set
	 */
	public final void setCruiseSpeedKts(double cruiseSpeedKts) {
		this.cruiseSpeedKts = cruiseSpeedKts;
	}

	/**
	 * @return the cruiseAltitudeFt
	 */
	public final double getCruiseAltitudeFt() {
		return cruiseAltitudeFt;
	}

	/**
	 * @param cruiseAltitudeFt
	 *          the cruiseAltitudeFt to set
	 */
	public final void setCruiseAltitudeFt(double cruiseAltitudeFt) {
		this.cruiseAltitudeFt = cruiseAltitudeFt;
	}

	/**
	 * @return the routePlan
	 */
	public final String getRoutePlan() {
		return routePlan;
	}

	/**
	 * @param routePlan
	 *          the routePlan to set
	 */
	public final void setRoutePlan(String routePlan) {
		this.routePlan = routePlan;
	}

	/**
	 * @return the departureAirportCode
	 */
	public String getDepartureAirportCode() {
		return departureAirportCode;
	}

	/**
	 * @param departureAirportCode
	 *          the departureAirportCode to set
	 */
	public void setDepartureAirportCode(String departureAirportCode) {
		this.departureAirportCode = departureAirportCode;
	}

	/**
	 * @return the arrivalAirportCode
	 */
	public String getArrivalAirportCode() {
		return arrivalAirportCode;
	}

	/**
	 * @param arrivalAirportCode
	 *          the arrivalAirportCode to set
	 */
	public void setArrivalAirportCode(String arrivalAirportCode) {
		this.arrivalAirportCode = arrivalAirportCode;
	}

	/**
	 * @return the departureCenter
	 */
	public final String getDepartureCenter() {
		return departureCenter;
	}

	/**
	 * @param departureCenter
	 *          the departureCenter to set
	 */
	public final void setDepartureCenter(String departureCenter) {
		this.departureCenter = departureCenter;
	}

	/**
	 * @return the arrivalCenter
	 */
	public final String getArrivalCenter() {
		return arrivalCenter;
	}

	/**
	 * @param arrivalCenter
	 *          the arrivalCenter to set
	 */
	public final void setArrivalCenter(String arrivalCenter) {
		this.arrivalCenter = arrivalCenter;
	}

	/**
	 * @return the departureFix
	 */
	public final String getDepartureFix() {
		return departureFix;
	}

	/**
	 * @param departureFix
	 *          the departureFix to set
	 */
	public final void setDepartureFix(String departureFix) {
		this.departureFix = departureFix;
	}

	/**
	 * @return the arrivalFix
	 */
	public final String getArrivalFix() {
		return arrivalFix;
	}

	/**
	 * @param arrivalFix
	 *          the arrivalFix to set
	 */
	public final void setArrivalFix(String arrivalFix) {
		this.arrivalFix = arrivalFix;
	}

	/**
	 * @return the physicalAircraftClass
	 */
	public final String getPhysicalAircraftClass() {
		return physicalAircraftClass;
	}

	/**
	 * @param physicalAircraftClass
	 *          the physicalAircraftClass to set
	 */
	public final void setPhysicalAircraftClass(String physicalAircraftClass) {
		this.physicalAircraftClass = physicalAircraftClass;
	}

	/**
	 * @return the weightAircraftClass
	 */
	public final String getWeightAircraftClass() {
		return weightAircraftClass;
	}

	/**
	 * @param weightAircraftClass
	 *          the weightAircraftClass to set
	 */
	public final void setWeightAircraftClass(String weightClass) {
		this.weightAircraftClass = weightClass;
	}

	/**
	 * @return the userAircraftClass
	 */
	public final String getUserAircraftClass() {
		return userAircraftClass;
	}

	/**
	 * @param userAircraftClass
	 *          the userAircraftClass to set
	 */
	public final void setUserAircraftClass(String userAircraftClass) {
		this.userAircraftClass = userAircraftClass;
	}

	/**
	 * @return the numOfAircraft
	 */
	public final int getNumOfAircraft() {
		return numOfAircraft;
	}

	/**
	 * @param numOfAircraft
	 *          the numOfAircraft to set
	 */
	public final void setNumOfAircraft(int numOfAircraft) {
		this.numOfAircraft = numOfAircraft;
	}

	/**
	 * @return the airborneEquipmentQualifier
	 */
	public final String getAirborneEquipmentQualifier() {
		return airborneEquipmentQualifier;
	}

	/**
	 * @param airborneEquipmentQualifier
	 *          the airborneEquipmentQualifier to set
	 */
	public final void setAirborneEquipmentQualifier(
			String airborneEquipmentQualifier) {
		this.airborneEquipmentQualifier = airborneEquipmentQualifier;
	}

	/**
	 * @return the flightPlanStatus
	 */
	public FlightPlanStatus getFlightPlanStatus() {
		return flightPlanStatus;
	}

	/**
	 * @param flightPlanStatus
	 *          the flightPlanStatus to set
	 */
	public void setFlightPlanStatus(FlightPlanStatus flightPlanStatus) {
		this.flightPlanStatus = flightPlanStatus;
	}

	/**
	 * @return the acLivery
	 */
	public String getAcLivery() {
		return acLivery;
	}

	/**
	 * @param acLivery
	 *          the acLivery to set
	 */
	public void setAcLivery(String acLivery) {
		this.acLivery = acLivery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	@Override
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// flightPlanData section

			Element flightPlanDataElement = new Element(
					EventAttributeName.flightPlanData.toString());

			Element nextEle = new Element(EventAttributeName.source.toString())
					.setText(getSource());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.aircraftType.toString())
					.setText(getAircraftType());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.plannedArrivalTimeMSecs.toString()).setText(Long
					.toString(getPlannedArrivalTimeMSecs()));
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.plannedArrivalRunway.toString())
					.setText(getPlannedArrivalRunway());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.plannedTaxiInGate.toString())
					.setText(getPlannedTaxiInGate());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.plannedDepartureTimeMSecs.toString()).setText(Long
					.toString(getPlannedDepartureTimeMSecs()));
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.plannedDepartureRunway.toString())
					.setText(getPlannedDepartureRunway());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.plannedTaxiOutGate.toString())
					.setText(getPlannedTaxiOutGate());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.cruiseSpeedKts.toString())
					.setText(Double.toString(getCruiseSpeedKts()));
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.cruiseAltitudeFt.toString())
					.setText(Double.toString(getCruiseAltitudeFt()));
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.routePlan.toString())
					.setText(getRoutePlan());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.arrivalAirportCode.toString())
					.setText(getArrivalAirportCode());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.departureAirportCode.toString())
					.setText(getDepartureAirportCode());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.departureCenter.toString())
					.setText(getDepartureCenter());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.arrivalCenter.toString())
					.setText(getArrivalCenter());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.departureFix.toString())
					.setText(getDepartureFix());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.arrivalFix.toString())
					.setText(getArrivalFix());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.physicalAircraftClass.toString())
					.setText(getPhysicalAircraftClass());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.weightAircraftClass.toString())
					.setText(getWeightAircraftClass());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.userAircraftClass.toString())
					.setText(getUserAircraftClass());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.numOfAircraft.toString())
					.setText(Integer.toString(getNumOfAircraft()));
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.airborneEquipmentQualifier.toString())
					.setText(getAirborneEquipmentQualifier());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.flightPlanStatus.toString())
					.setText(getFlightPlanStatus().name());
			flightPlanDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.acLivery.toString())
					.setText(getAcLivery());
			flightPlanDataElement.addContent(nextEle);

			// add the flight plan data element to the parent datablock element

			dataBlockElement.addContent(flightPlanDataElement);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.data.AbstractDataObject#newElement(java
	 * .lang .String, java.lang.String)
	 */
	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// flight plan data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign.toString())) {
				callSign = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.tailNumber
					.toString())) {
				tailNumber = elementValue;

			} else if (elementName.equalsIgnoreCase(EventAttributeName.source
					.toString())) {
				source = elementValue;

			} else if (elementName.equalsIgnoreCase(EventAttributeName.aircraftType
					.toString())) {
				aircraftType = elementValue;

			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedDepartureTimeMSecs
							.toString())) {
				plannedDepartureTimeMSecs = Long.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedDepartureRunway
							.toString())) {
				plannedDepartureRunway = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedTaxiOutGate.toString())) {
				plannedTaxiOutGate = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedArrivalTimeMSecs
							.toString())) {
				plannedArrivalTimeMSecs = Long.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedArrivalRunway.toString())) {
				plannedArrivalRunway = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.plannedTaxiInGate.toString())) {
				plannedTaxiInGate = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.cruiseSpeedKts
					.toString())) {
				cruiseSpeedKts = Double.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.cruiseAltitudeFt.toString())) {
				cruiseAltitudeFt = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.routePlan
					.toString())) {
				routePlan = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.departureCenter.toString())) {
				departureCenter = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.arrivalCenter
					.toString())) {
				arrivalCenter = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.departureFix
					.toString())) {
				departureFix = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.arrivalFix
					.toString())) {
				arrivalFix = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.physicalAircraftClass.toString())) {
				physicalAircraftClass = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.weightAircraftClass.toString())) {
				weightAircraftClass = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.userAircraftClass.toString())) {
				userAircraftClass = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.numOfAircraft
					.toString())) {
				numOfAircraft = Integer.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.airborneEquipmentQualifier
							.toString())) {
				airborneEquipmentQualifier = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.flightPlanStatus.toString())) {
				flightPlanStatus = FlightPlanStatus.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.arrivalAirportCode.toString())) {
				arrivalAirportCode = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.departureAirportCode.toString())) {
				setDepartureAirportCode(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.acLivery
					.toString())) {
				setAcLivery(elementValue);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validation()
	 */
	@Override
	public boolean validate() {

		if (!super.validate()) {
			return false;
		}

		// departure airport code
		if (departureAirportCode == null) {
			LOG.warn("Validation failed: Departure airport code was null for ["
					+ getCallSign() + "]");
			return false;
		}
		if (departureAirportCode.length() == 0) {
			LOG.warn("Validation failed: Departure airport code was empty for ["
					+ getCallSign() + "]");
			return false;
		}

		// arrival airport code
		if (arrivalAirportCode == null) {
			LOG.warn("Validation failed: Arrival airport code was null for ["
					+ getCallSign() + "]");
			return false;
		}
		if (arrivalAirportCode.length() == 0) {
			LOG.warn("Validation failed: Arrival airport code was empty for ["
					+ getCallSign() + "]");
			return false;
		}

		// planned arrival runway

		if (plannedArrivalRunway == null) {
			LOG.warn("Validation failed: Planned arrival runway was null for ["
					+ getCallSign() + "]");
			plannedArrivalRunway = "1R";
			// return false;
		}
		if (plannedArrivalRunway.length() == 0) {
			LOG.warn("Validation failed: Planned arrival runway was empty for ["
					+ getCallSign() + "]");
			plannedArrivalRunway = "1R";
			// return false;
		}

		// planned departure runway

		if (plannedDepartureRunway == null) {
			LOG.warn("Validation failed: Planned departure runway was null for ["
					+ getCallSign() + "]");
			plannedDepartureRunway = "1R";
			// return false;
		}

		if (plannedDepartureRunway.length() == 0) {
			LOG.warn("Validation failed: Planned departure runway was empty for ["
					+ getCallSign() + "]");
			plannedDepartureRunway = "1R";
			// return false;
		}
		return true;
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
		result = prime * result + ((acLivery == null) ? 0 : acLivery.hashCode());
		result = prime
				* result
				+ ((airborneEquipmentQualifier == null) ? 0
						: airborneEquipmentQualifier.hashCode());
		result = prime * result
				+ ((aircraftType == null) ? 0 : aircraftType.hashCode());
		result = prime * result
				+ ((arrivalAirportCode == null) ? 0 : arrivalAirportCode.hashCode());
		result = prime * result
				+ ((arrivalCenter == null) ? 0 : arrivalCenter.hashCode());
		result = prime * result
				+ ((arrivalFix == null) ? 0 : arrivalFix.hashCode());
		long temp;
		temp = Double.doubleToLongBits(cruiseAltitudeFt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(cruiseSpeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((departureAirportCode == null) ? 0 : departureAirportCode.hashCode());
		result = prime * result
				+ ((departureCenter == null) ? 0 : departureCenter.hashCode());
		result = prime * result
				+ ((departureFix == null) ? 0 : departureFix.hashCode());
		result = prime * result
				+ ((flightPlanStatus == null) ? 0 : flightPlanStatus.hashCode());
		result = prime * result + numOfAircraft;
		result = prime
				* result
				+ ((physicalAircraftClass == null) ? 0 : physicalAircraftClass
						.hashCode());
		result = prime
				* result
				+ ((plannedArrivalRunway == null) ? 0 : plannedArrivalRunway.hashCode());
		result = prime * result
				+ (int) (plannedArrivalTimeMSecs ^ (plannedArrivalTimeMSecs >>> 32));
		result = prime
				* result
				+ ((plannedDepartureRunway == null) ? 0 : plannedDepartureRunway
						.hashCode());
		result = prime
				* result
				+ (int) (plannedDepartureTimeMSecs ^ (plannedDepartureTimeMSecs >>> 32));
		result = prime * result
				+ ((plannedTaxiInGate == null) ? 0 : plannedTaxiInGate.hashCode());
		result = prime * result
				+ ((plannedTaxiOutGate == null) ? 0 : plannedTaxiOutGate.hashCode());
		result = prime * result + ((routePlan == null) ? 0 : routePlan.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result
				+ ((userAircraftClass == null) ? 0 : userAircraftClass.hashCode());
		result = prime * result
				+ ((weightAircraftClass == null) ? 0 : weightAircraftClass.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlightPlan other = (FlightPlan) obj;
		if (acLivery == null) {
			if (other.acLivery != null) {
				return false;
			}
		} else if (!acLivery.equals(other.acLivery)) {
			return false;
		}
		if (airborneEquipmentQualifier == null) {
			if (other.airborneEquipmentQualifier != null) {
				return false;
			}
		} else if (!airborneEquipmentQualifier
				.equals(other.airborneEquipmentQualifier)) {
			return false;
		}
		if (aircraftType == null) {
			if (other.aircraftType != null) {
				return false;
			}
		} else if (!aircraftType.equals(other.aircraftType)) {
			return false;
		}
		if (arrivalAirportCode == null) {
			if (other.arrivalAirportCode != null) {
				return false;
			}
		} else if (!arrivalAirportCode.equals(other.arrivalAirportCode)) {
			return false;
		}
		if (arrivalCenter == null) {
			if (other.arrivalCenter != null) {
				return false;
			}
		} else if (!arrivalCenter.equals(other.arrivalCenter)) {
			return false;
		}
		if (arrivalFix == null) {
			if (other.arrivalFix != null) {
				return false;
			}
		} else if (!arrivalFix.equals(other.arrivalFix)) {
			return false;
		}
		if (Double.doubleToLongBits(cruiseAltitudeFt) != Double
				.doubleToLongBits(other.cruiseAltitudeFt)) {
			return false;
		}
		if (Double.doubleToLongBits(cruiseSpeedKts) != Double
				.doubleToLongBits(other.cruiseSpeedKts)) {
			return false;
		}
		if (departureAirportCode == null) {
			if (other.departureAirportCode != null) {
				return false;
			}
		} else if (!departureAirportCode.equals(other.departureAirportCode)) {
			return false;
		}
		if (departureCenter == null) {
			if (other.departureCenter != null) {
				return false;
			}
		} else if (!departureCenter.equals(other.departureCenter)) {
			return false;
		}
		if (departureFix == null) {
			if (other.departureFix != null) {
				return false;
			}
		} else if (!departureFix.equals(other.departureFix)) {
			return false;
		}
		if (flightPlanStatus != other.flightPlanStatus) {
			return false;
		}
		if (numOfAircraft != other.numOfAircraft) {
			return false;
		}
		if (physicalAircraftClass == null) {
			if (other.physicalAircraftClass != null) {
				return false;
			}
		} else if (!physicalAircraftClass.equals(other.physicalAircraftClass)) {
			return false;
		}
		if (plannedArrivalRunway == null) {
			if (other.plannedArrivalRunway != null) {
				return false;
			}
		} else if (!plannedArrivalRunway.equals(other.plannedArrivalRunway)) {
			return false;
		}
		if (plannedArrivalTimeMSecs != other.plannedArrivalTimeMSecs) {
			return false;
		}
		if (plannedDepartureRunway == null) {
			if (other.plannedDepartureRunway != null) {
				return false;
			}
		} else if (!plannedDepartureRunway.equals(other.plannedDepartureRunway)) {
			return false;
		}
		if (plannedDepartureTimeMSecs != other.plannedDepartureTimeMSecs) {
			return false;
		}
		if (plannedTaxiInGate == null) {
			if (other.plannedTaxiInGate != null) {
				return false;
			}
		} else if (!plannedTaxiInGate.equals(other.plannedTaxiInGate)) {
			return false;
		}
		if (plannedTaxiOutGate == null) {
			if (other.plannedTaxiOutGate != null) {
				return false;
			}
		} else if (!plannedTaxiOutGate.equals(other.plannedTaxiOutGate)) {
			return false;
		}
		if (routePlan == null) {
			if (other.routePlan != null) {
				return false;
			}
		} else if (!routePlan.equals(other.routePlan)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (userAircraftClass == null) {
			if (other.userAircraftClass != null) {
				return false;
			}
		} else if (!userAircraftClass.equals(other.userAircraftClass)) {
			return false;
		}
		if (weightAircraftClass == null) {
			if (other.weightAircraftClass != null) {
				return false;
			}
		} else if (!weightAircraftClass.equals(other.weightAircraftClass)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlightPlan [source=").append(source)
				.append(", aircraftType=").append(aircraftType)
				.append(", cruiseSpeedKts=").append(cruiseSpeedKts)
				.append(", cruiseAltitudeFt=").append(cruiseAltitudeFt)
				.append(", routePlan=").append(routePlan)
				.append(", departureAirportCode=").append(departureAirportCode)
				.append(", arrivalAirportCode=").append(arrivalAirportCode)
				.append(", plannedDepartureTimeMSecs=")
				.append(plannedDepartureTimeMSecs).append(", plannedDepartureRunway=")
				.append(plannedDepartureRunway).append(", plannedTaxiOutGate=")
				.append(plannedTaxiOutGate).append(", departureCenter=")
				.append(departureCenter).append(", departureFix=").append(departureFix)
				.append(", plannedArrivalTimeMSecs=").append(plannedArrivalTimeMSecs)
				.append(", plannedArrivalRunway=").append(plannedArrivalRunway)
				.append(", plannedTaxiInGate=").append(plannedTaxiInGate)
				.append(", arrivalCenter=").append(arrivalCenter)
				.append(", arrivalFix=").append(arrivalFix)
				.append(", physicalAircraftClass=").append(physicalAircraftClass)
				.append(", weightAircraftClass=").append(weightAircraftClass)
				.append(", userAircraftClass=").append(userAircraftClass)
				.append(", numOfAircraft=").append(numOfAircraft)
				.append(", airborneEquipmentQualifier=")
				.append(airborneEquipmentQualifier).append(", flightPlanStatus=")
				.append(flightPlanStatus).append(", acLivery=").append(acLivery)
				.append("]");
		return builder.toString();
	}

}
