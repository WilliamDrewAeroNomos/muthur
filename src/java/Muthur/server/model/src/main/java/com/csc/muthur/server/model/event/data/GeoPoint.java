/**
 * 
 */
package com.csc.muthur.server.model.event.data;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class GeoPoint {

	private Point point; // lat/lon
	private double altitude;

	/**
	 * @param latitude
	 * @param longitude
	 */
	public GeoPoint(final double latitude, final double longitude,
			final double altitude) {
		super();
		point = new Point(latitude, longitude);
		this.altitude = altitude;
	}

	/**
	 * 
	 * @param point
	 * @param altitude
	 */
	public GeoPoint(final Point point, final double altitude) {
		super();
		if (point == null) {
			throw new IllegalArgumentException(
					"Point in GeoPoint constructor was null.");
		}
		this.point = point;
		this.altitude = altitude;
	}

	/**
	 * 
	 */
	public GeoPoint() {
		point = new Point();
	}

	/**
	 * @return the latitude
	 */
	public final double getLatitude() {
		return point.getLatitude();
	}

	/**
	 * @param latitude
	 *          the latitude to set
	 */
	public final void setLatitude(double latitude) {
		this.point.setLatitude(latitude);
	}

	/**
	 * @return the longitude
	 */
	public final double getLongitude() {
		return point.getLongitude();
	}

	/**
	 * @param longitude
	 *          the longitude to set
	 */
	public final void setLongitude(double longitude) {
		this.point.setLongitude(longitude);
	}

	/**
	 * @return the altitude
	 */
	public final double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *          the altitude to set
	 */
	public final void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(altitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((point == null) ? 0 : point.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GeoPoint other = (GeoPoint) obj;
		if (Double.doubleToLongBits(altitude) != Double
				.doubleToLongBits(other.altitude)) {
			return false;
		}
		if (point == null) {
			if (other.point != null) {
				return false;
			}
		} else if (!point.equals(other.point)) {
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
		return "GeoPoint [altitude=" + altitude + ", point=" + point + "]";
	}

}
