package lagrange.impl.readers;

import java.io.IOException;

import lagrange.utils.IndexLookup_Nearest;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * Retrieves values from a 3D NetCDF File (x,y and time)
 * 
 * @author Johnathan Kool
 */

public class Reader_NetCDF_3D {

	private NetcdfFile netcdfFile;
	private Variable bndVar;
	private String varName = "mld";
	private String latName = "Latitude";
	private String lonName = "Longitude";
	private String timeName = "Time";
	private boolean neglon = false;
	private IndexLookup_Nearest lats, lons, time;

	/**
	 * Constructor accepting a String containing the path of the resource.
	 * 
	 * @param filename
	 */

	public Reader_NetCDF_3D(String filename) {

		try {
			netcdfFile = NetcdfFile.open(filename);
			bndVar = netcdfFile.findVariable(varName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor accepting a String containing the path of the resource, as
	 * well as Strings containing the variable names for latitude, longitude,
	 * time, and the data content.
	 * 
	 * @param filename
	 * @param varname
	 * @param timeName
	 * @param latName
	 * @param lonName
	 * @throws IOException
	 */

	public Reader_NetCDF_3D(String filename, String varname, String timeName,
			String latName, String lonName) throws IOException {

		netcdfFile = NetcdfFile.open(filename);
		bndVar = netcdfFile.findVariable(varName);
		this.latName = latName;
		this.lonName = lonName;
		this.timeName = timeName;
		initialize();
	}

	/**
	 * Returns a clone of the class instance
	 */
	
	@Override
	public Reader_NetCDF_3D clone() {
		Reader_NetCDF_3D ncb;
		ncb = new Reader_NetCDF_3D(netcdfFile.getLocation());
		// TODO TIDY THIS UP!!!!
		ncb.neglon = neglon;
		return ncb;
	}

	/**
	 * 
	 * @return
	 */
	
	public String getLatName() {
		return latName;
	}

	public String getLonName() {
		return lonName;
	}

	public double getValue(double t, double x, double y) {

		if (neglon) {
			x = (x + 180) % 360 - 180;
		}

		int tm = time.lookup(t);
		int i = lats.lookup(y);
		int j = lons.lookup(x);

		if (time.isIn_Bounds() != 0 || lats.isIn_Bounds() != 0
				|| lons.isIn_Bounds() != 0) {
			return Double.NaN;
		}

		Array bnd = null;
		try {
			bnd = bndVar.read(new int[] { tm, i, j }, new int[] { 1, 1, 1 });
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidRangeException e) {
			e.printStackTrace();
		}
		return bnd.getDouble(0);
	}

	public String getVariableName() {
		return varName;
	}

	public void initialize() throws IOException {
		lats = new IndexLookup_Nearest(netcdfFile.findVariable(latName));
		lons = new IndexLookup_Nearest(netcdfFile.findVariable(lonName));
		time = new IndexLookup_Nearest(netcdfFile.findVariable(timeName));
	}

	public boolean isNeglon() {
		return neglon;
	}

	public void setLatName(String latName) {
		this.latName = latName;
	}

	public void setLonName(String lonName) {
		this.lonName = lonName;
	}

	public void setNeglon(boolean neglon) {
		this.neglon = neglon;
	}

	public void setVariableName(String variableName) {
		this.varName = variableName;
	}
}
