/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.element.Hook;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sugarcrm.candybean.exceptions.CandybeanException;

import static java.lang.System.currentTimeMillis;

/**
 * Utility class that provides several methods for an element to pause until a
 * condition is satisfied
 * @author Eric Tam
 */
public class WebDriverPause<T> {
	private WebDriver wd;
	private static final long WD_POLLING_INTERVAL = 5000;

	public WebDriverPause(WebDriver wd) {
		this.wd = wd;
	}

	/**
	 * Takes any ExpectedCondition and poll this condition within timeout
	 * @param timeoutMs	Timeout in Milliseconds
	 * @param condition	The condition to poll
	 * @return			Returning the object that is returned from ExpectedCondition when the condition is met
	 * @throws CandybeanException
	 */
	public Object waitUntil(ExpectedCondition condition, long timeoutMs) throws CandybeanException {
		long wdPollingInterval = WD_POLLING_INTERVAL;
		if(timeoutMs <= WD_POLLING_INTERVAL) {
			wdPollingInterval = timeoutMs;
		}

		// This is done by Double-polling. WebDriverWait waits for wdPollingInterval amount of time.
		// This is done repetitively as long as the time is still within timeoutMs.
		final long startTime = currentTimeMillis();
		CandybeanException toThrow = null;
		Object toReturn = null;
		do {
			try {
				toReturn = (new WebDriverWait(this.wd, wdPollingInterval / 1000)).until(condition);
				toThrow = null;
				break;
			} catch (WebDriverException wdException) {
				toThrow = new CandybeanException(wdException.toString());
			}
		} while((currentTimeMillis() - startTime) <= timeoutMs);

		if(toThrow != null) {
			throw toThrow;
		}

		return toReturn;
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param hook
	 * @param timeoutMs
	 * @throws CandybeanException
	 */
	public void waitForVisible(Hook hook, long timeoutMs) throws CandybeanException {
		this.waitUntil(WaitConditions.visible(hook), timeoutMs);
	}

	/**
	 * Provides a simple method to wait for visible as it is often used
	 * @param wde
	 * @param timeoutMs
	 * @throws CandybeanException
	 */
	public void waitForVisible(WebDriverElement wde, long timeoutMs) throws CandybeanException {
		this.waitUntil(WaitConditions.visible(wde), timeoutMs);
	}
}
