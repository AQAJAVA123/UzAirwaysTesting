## UzAirways Web Automation Test

This repository contains test automation for https://uzairways.com/en using Selenium WebDriver with TestNG and Page Object Model (POM) design pattern.

#### Core Logic and Design

Each page is encapsulated into its own Java class. This separates the logic for each page, making the code easier to reuse, manage, and maintain.

- **MainPage**: Manages the acceptance of cookies through the cookie consent banner.

- **FlightSearchPage**: Encapsulates the logic for interacting with the flight booking form, including selecting departure and arrival cities, travel dates, passenger counts, and preferred currency.

- **LoginPage and RegistrationPage**: Handle form interactions for user authentication and registration flows, abstracting input and submission logic. Test data uses placeholder (non-functional) email addresses.

### Tests

The test classes simulate user interactions with various parts of the UzAirways website, such as flight searches, login, and registration. Each test verifies that the expected behaviors (e.g. redirection, input handling, and form submission) function correctly using real-time UI automation. These tests use mock data and are designed to validate user flows without relying on actual user accounts or live bookings.

### How to run tests

#### Prerequisites

You should have the following prerequisites installed and configured on your system before running the tests:
- Java (version 11 or higher)
- Maven
- Google Chrome browser
- ChromeDriver matching your Chrome version

To run the tests, use the command: **mvn clean test** or by right-clicking a test class → **Run**
