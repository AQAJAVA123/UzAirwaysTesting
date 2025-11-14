mvn clean test
allure generate target/allure-results --clean -o target/allure-report
start chrome target/allure-report/index.html