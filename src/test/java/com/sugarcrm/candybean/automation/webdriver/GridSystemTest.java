package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.AutomationInterface;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import sun.management.ThreadInfoCompositeData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by etam on 10/14/14.
 */
public class GridSystemTest {

	//	private RemoteWebDriver iface;
	private WebDriverInterface iface;

	@Before
	public void setUp() throws Exception {
//		DesiredCapabilities cap = new DesiredCapabilities();
//		cap.setBrowserName("firefox");
//		cap.setPlatform(Platform.LINUX);
//
//		try {
//			iface = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
//		} catch(MalformedURLException e) {
//			e.printStackTrace();
//		}
		Candybean candybean = Candybean.getInstance(new Configuration(new File("/Users/etam/sugarcrm/candybean/candybean.config")));
		AutomationInterfaceBuilder builder = candybean.getAIB(GridSystemTest.class);
		builder.setType(AutomationInterface.Type.FIREFOX);
		iface = builder.build();
		iface.start();

//		iface = new GridInterface(AutomationInterface.Type.FIREFOX);
	}

	@Test
	public void test() throws Exception {
		iface.go("http://www.google.ca");
		iface.pause(5000);

	}
}
