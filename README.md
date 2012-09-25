# Arquillian Extension for the iOS Platform

This extensions allows you to bring Arquillian Drone WebDriver based testing to iOS simulators.

## Usage


You have to do following steps, expecting your project was already set up to use Drone.

### Add iOS extension to dependencies

        <dependency>
            <groupId>org.jboss.arquillian.extension</groupId>
            <artifactId>arquillian-ios-depchain</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>test</scope>
        </dependency>

### Set up WebDriver in `arquillian.xml`

        <extension qualifier="webdriver">
            <!-- this is optional if you set -->
            <property name="browserCapabilities">iphone</property>
            <!-- this makes WebDriver connect hub on iOS simulator -->
            <property name="remoteAddress">http://localhost:3001/wd/hub</property>
        </extension>

### Set up iOS in `arquillian.xml`

You should be aware that following might change in the future. You've been warned!

    <extension qualifier="ios">
        <!--
            this is optional - the extension can do `git clone` and build
            the project automatically, but it can take a long time
        -->
        <property name="waxsimBinary">path to your WaxSim binary</property>
    </extension>

Properties explained, required in **bold**:

* `family` - (iphone) determines whether iPhone or iPad is used, available values: `ipad`, `iphone`
* `sdk` - SDK version
* `skip` - (false) skip execution
* `verbose` - (false) enables printing of additional information
* `waxsimBinary` - path to WaxSim binary used to start iOS simulator
* `waxsimGitRepository` - (git://github.com/jonathanpenn/WaxSim.git) WaxSim GIT repository

### Set up iOS Drone in `arquillian.xml`

You should be aware that following might change in the future. You've been warned!

    <extension qualifier="ios-drone">
        <!--
            this is optional - the extension can do svn checkout automatically,
            but it can take a long time
        -->
        <property name="localSeleniumCopy">path to your local copy of Selenium repository</property>
    </extension>

Properties explained, required in **bold**:

* `localSeleniumCopy` - path to your local copy of Selenium SVN repository
* `seleniumSvnRepository` - (http://selenium.googlecode.com/svn/trunk/) URL of remote SVN repository used to 'svn checkout'
* `skip` - (false) skip execution
* `timeoutInSeconds` - (10) maximal time to get Selenium support on iOS simulator started
* `verbose` - (false) enables printing of additional information