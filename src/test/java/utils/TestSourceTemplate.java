package utils;

@SuppressWarnings("ALL")
public class TestSourceTemplate {

    private String historyID;
    private String testCase;
    private Scenario scenario;
    private String queryParams;
    private String pathParams;
    private String expected;
    private String description;

    public TestSourceTemplate() {
    }

    public TestSourceTemplate(String historyID, String testCase, String scenario, String queryParams, String pathParams, String expected, String description) {
        this.historyID = historyID;
        this.testCase = testCase;
        setScenario(scenario);
        this.queryParams = queryParams;
        this.pathParams = pathParams;
        this.expected = expected;
        this.description = description;
    }

    private void setScenario(String scenario){
        if(scenario.equalsIgnoreCase("fail")) setScenario(Scenario.FAIL); else setScenario(Scenario.SUCCESS);
    }

    public String getHistoryID() {
        return historyID;
    }

    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public Scenario getScenario() {
        return scenario;
    }

    @SuppressWarnings("WeakerAccess")
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathParams() { return pathParams; }

    public void setPathParams(String pathParams) { this.pathParams = pathParams; }

    @Override
    public String toString() {
        return "TestSourceTemplate{" +
                "historyID='" + historyID + '\'' +
                ", testCase='" + testCase + '\'' +
                ", scenario=" + scenario +
                ", queryParams='" + queryParams + '\'' +
                ", pathParams='" + pathParams + '\'' +
                ", expected='" + expected + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
