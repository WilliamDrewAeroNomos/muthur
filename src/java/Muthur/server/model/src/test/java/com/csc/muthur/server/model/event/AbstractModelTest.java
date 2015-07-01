/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.model.event.data.IBaseDataObject;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractModelTest extends TestCase {

	/**
	 * 
	 */
	protected void writeToFile(IEvent event, boolean append) {

		String fileName = System.getProperty("user.dir") + "/target/"
				+ event.getEventName() + ".xml";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
			out.write(event.serialize());
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 
	 */
	protected void writeToFile(IBaseDataObject data, boolean append) {

		String fileName = System.getProperty("user.dir") + "/target/"
				+ data.getEventName() + ".xml";

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
			out.write(data.serialize());
			out.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 
	 */
	protected void writeToFile(DataPublicationEvent dp, boolean append) {

		String fileName = System.getProperty("user.dir") + "/target/"
				+ dp.getEventName() + "." + dp.getDataType().toString() + ".xml";

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
			out.write(dp.serialize());
			out.close();
		} catch (IOException e) {
		}
	}

}
