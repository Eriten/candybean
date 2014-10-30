package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by etam on 10/14/14.
 */
public class GridInterface extends WebDriverInterface {
	protected ThreadLocal<RemoteWebDriver> threadDriver = null;
	private DesiredCapabilities dc = new DesiredCapabilities();

	public GridInterface(Type iType) throws CandybeanException {
		super(iType);
	}

	@Override
	public void start() throws CandybeanException {

		dc.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
//		dc.setCapability("version", "12");
//		capabilities.setBrowserName(candybean.config.getValue("grid.browser"));
//		capabilities.setCapability("version",candybean.config.getValue("grid.version"));
//		capabilities.setCapability("platform",candybean.config.getValue("grid.platform"));
		String ip = candybean.config.getValue("grid.ip");
		String port = candybean.config.getValue("grid.port");
		logger.info("Starting interface with ip " + ip + " and port " + port);
		try {
			URL url = new URL("http://" + ip + ":" + port +  "/wd/hub");
			System.out.println(url.toString());
			wd = new RemoteWebDriver(url, dc);
		} catch (MalformedURLException e) {
			throw new CandybeanException(e);
		}
		super.start(); // requires wd to be instantiated first
	}

	@Override
	public void stop() throws CandybeanException {
		logger.info("Stopping automation interface with type: " + super.iType);
		super.stop();
	}

	@Override
	public void restart() throws CandybeanException {
		logger.info("Restarting automation interface with type: " + super.iType);
		this.stop();
		this.start();
	}

	public DesiredCapabilities getCapabilities() {
		return dc;
	}

}
