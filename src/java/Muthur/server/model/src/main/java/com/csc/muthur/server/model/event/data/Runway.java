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
public class Runway {

	private RunwayName name;
	private GeoPoint upperLeft;
	private GeoPoint lowerRight;
	private RunwayFlow runwayFlow; // departure, arrival or both

	/**
	 * 
	 */
	public Runway() {
		super();
		this.name = new RunwayName();
	}

	public Runway(final String name) {
		this.name = new RunwayName(name);
	}

	/**
	 * @param name
	 * @param upperLeft
	 * @param lowerRight
	 */
	public Runway(final String name, final GeoPoint upperLeft,
			final GeoPoint lowerRight, final RunwayFlow runwayFlow) {
		super();
		this.name = new RunwayName(name);
		this.upperLeft = upperLeft;
		this.lowerRight = lowerRight;
		this.runwayFlow = runwayFlow;
	}

	/**
	 * @return the name
	 */
	public final RunwayName getName() {
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	public final void setName(final String name) {
		this.name.setName(name);
	}

	/**
	 * @return the upperLeft
	 */
	public final GeoPoint getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @param upperLeft
	 *          the upperLeft to set
	 */
	public final void setUpperLeft(final GeoPoint upperLeft) {
		this.upperLeft = upperLeft;
	}

	/**
	 * @return the lowerRight
	 */
	public final GeoPoint getLowerRight() {
		return lowerRight;
	}

	/**
	 * @param lowerRight
	 *          the lowerRight to set
	 */
	public final void setLowerRight(final GeoPoint lowerRight) {
		this.lowerRight = lowerRight;
	}

	/**
	 * @return the runwayFlow
	 */
	public final RunwayFlow getRunwayFlow() {
		return runwayFlow;
	}

	/**
	 * @param runwayFlow
	 *          the runwayFlow to set
	 */
	public final void setRunwayFlow(final RunwayFlow runwayFlow) {
		this.runwayFlow = runwayFlow;
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
		result =
				prime * result + ((lowerRight == null) ? 0 : lowerRight.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result =
				prime * result + ((runwayFlow == null) ? 0 : runwayFlow.hashCode());
		result = prime * result + ((upperLeft == null) ? 0 : upperLeft.hashCode());
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
		Runway other = (Runway) obj;
		if (lowerRight == null) {
			if (other.lowerRight != null) {
				return false;
			}
		} else if (!lowerRight.equals(other.lowerRight)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (runwayFlow == null) {
			if (other.runwayFlow != null) {
				return false;
			}
		} else if (!runwayFlow.equals(other.runwayFlow)) {
			return false;
		}
		if (upperLeft == null) {
			if (other.upperLeft != null) {
				return false;
			}
		} else if (!upperLeft.equals(other.upperLeft)) {
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
		return "Runway [lowerRight=" + lowerRight + ", name=" + name
				+ ", runwayFlow=" + runwayFlow + ", upperLeft=" + upperLeft + "]";
	}

}
