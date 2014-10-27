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

import com.google.common.base.Function;
import com.sugarcrm.candybean.automation.element.Hook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sugarcrm.candybean.exceptions.CandybeanException;

import static java.lang.System.currentTimeMillis;

/**
 * Utility class that provides several methods for an element to pause until an
 * action occurs.
 * @author Eric Tam
 */
public class WebDriverPause<T> {
	private WebDriver wd;
	private static final long WD_POLLING_INTERVAL = 5000;

	public WebDriverPause(WebDriver wd) {
		this.wd = wd;
	}

	/**
	 * A helper method that takes any ExpectedCondition and poll this condition within timeout
	 * @param timeoutMs	Timeout in Milliseconds
	 * @param condition	The condition to poll
	 * @return			Returning the object that is returned from ExpectedCondition when the condition is met
	 * @throws CandybeanException
	 */
	private Object wait(ExpectedCondition condition, long timeoutMs) throws CandybeanException {
		long wdPollingInterval = WD_POLLING_INTERVAL;
		if(timeoutMs <= WD_POLLING_INTERVAL) {
			wdPollingInterval = timeoutMs;
		}

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
	 * A helper method to convert Hook to By
	 * @param hook	The hook that specifies a web element
	 * @return		The converted By
	 * @throws CandybeanException
	 */
	private By getBy(Hook hook) throws CandybeanException{
		return WebDriverElement.By(hook);
	}

	/**
	 * A helper method to convert a WebElement to WebDriverElement with information from Hook
	 * @param hook
	 * @param we
	 * @return
	 * @throws CandybeanException
	 */
	private WebDriverElement convertToWebDriverElement(Hook hook, WebElement we, String tag) throws CandybeanException{
		if(tag.equals("select")) {
			return new WebDriverSelector(hook, this.wd);
		}
		return new WebDriverElement(hook, 0, this.wd, we);
	}

	/**
	 * Wait until the element is present on the DOM AND visible
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement visible(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.visibilityOfElementLocated(getBy(hook));
		return convertToWebDriverElement(hook, (WebElement) this.wait(condition, timeoutMs), tag);
	}

	/**
	 * Wait until the element is present on the DOM AND visible
	 * @param timeoutMs
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement visible(long timeoutMs, WebDriverElement wde) throws CandybeanException {
		this.wait(ExpectedConditions.visibilityOf(wde.we), timeoutMs);
		return wde;
	}

	/**
	 * Wait until the element is not present on the DOM OR invisible
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public boolean invisible(long timeoutMs, Hook hook) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.invisibilityOfElementLocated(getBy(hook));
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the element is not present on the DOM OR invisible
	 * @param timeoutMs
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public boolean invisibleWithText(long timeoutMs, Hook hook, String text) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.invisibilityOfElementWithText(getBy(hook), text);
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the element is present on the DOM
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement present(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.presenceOfElementLocated(getBy(hook));
		return convertToWebDriverElement(hook, (WebElement) this.wait(condition, timeoutMs), tag);
	}

	/**
	 * Wait until the element is clickable state (visible AND enabled)
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement clickable(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.elementToBeClickable(getBy(hook));
		return convertToWebDriverElement(hook, (WebElement) this.wait(condition, timeoutMs), tag);
	}

	/**
	 * Wait until the element is clickable state (visible AND enabled)
	 * @param timeoutMs
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement clickable(long timeoutMs, WebDriverElement wde) throws CandybeanException {
		this.wait(ExpectedConditions.elementToBeClickable(wde.we), timeoutMs);
		return wde;
	}

	/**
	 * Wait until the element is selected
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement selected(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.elementToBeSelected(getBy(hook));
		return convertToWebDriverElement(hook, (WebElement) this.wait(condition, timeoutMs), tag);
	}

	/**
	 * Wait until the element is selected
	 * @param timeoutMs
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement selected(long timeoutMs, WebDriverElement wde) throws CandybeanException {
		this.wait(ExpectedConditions.elementToBeSelected(wde.we), timeoutMs);
		return wde;
	}

	/**
	 * Wait until the element is unselected
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement unselected(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.elementSelectionStateToBe(getBy(hook), false);
		return convertToWebDriverElement(hook, (WebElement) this.wait(condition, timeoutMs), tag);
	}

	/**
	 * Wait until the element is unselected
	 * @param timeoutMs
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriverElement unselected(long timeoutMs, WebDriverElement wde) throws CandybeanException {
		this.wait(ExpectedConditions.elementSelectionStateToBe(wde.we, false), timeoutMs);
		return wde;
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 * @param timeoutMs
	 * @param hook
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriver frameAvailableAndSwitchToIt(long timeoutMs, Hook hook, String tag) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.frameToBeAvailableAndSwitchToIt(getBy(hook));
		return (WebDriver) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the frame is available to switch (Polling on switchTo().frame(...) until No NoSuchFrameException) and
	 * switch to this frame if no exception has occurred
	 * @param timeoutMs
	 * @param name
	 * @return
	 * @throws CandybeanException
	 */
	public WebDriver frameAvailableAndSwitchToIt(long timeoutMs, String name) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.frameToBeAvailableAndSwitchToIt(name);
		return (WebDriver) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the element is not present on the DOM
 	 * @param timeoutMs
	 * @param wde
	 * @return
	 * @throws CandybeanException
	 */
	public Boolean staleness(long timeoutMs, WebDriverElement wde) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.stalenessOf(wde.we);
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the expected text presents on the element
	 * @param timeoutMs
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public Boolean textIsPresent(long timeoutMs, Hook hook, String text) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.textToBePresentInElementLocated(getBy(hook), text);
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the expected text presents on the element
	 * @param timeoutMs
	 * @param wde
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public Boolean textIsPresent(long timeoutMs, WebDriverElement wde, String text) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.textToBePresentInElement(wde.we, text);
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the expected text presents on the element's value attribute
	 * @param timeoutMs
	 * @param hook
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public Boolean textIsPresentInValue(long timeoutMs, Hook hook, String text) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.textToBePresentInElementValue(getBy(hook), text);
		return (Boolean) this.wait(condition, timeoutMs);
	}

	/**
	 * Wait until the expected text presents on the element's value attribute
	 * @param timeoutMs
	 * @param wde
	 * @param text
	 * @return
	 * @throws CandybeanException
	 */
	public Boolean textIsPresentInValue(long timeoutMs, WebDriverElement wde, String text) throws CandybeanException {
		ExpectedCondition condition = ExpectedConditions.textToBePresentInElementValue(wde.we, text);
		return (Boolean) this.wait(condition, timeoutMs);
	}
}
