package rest;

import beans.EVIncidentBean;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class RestTests {

    private String url;
    private EVIncidentBean evIncidentBean;
    private int statusCode;
    private String issueKey;
    private String username;
    private String password;
    private JSONObject issueDetails;

    @Given("the site URL is (.*)")
    public void the_site_URL_is_http_localhost_jira(String string) {
        this.url=string;
    }

    @Given("an incident is created in EasyVista")
    public void an_incident_is_created_in_EasyVista() {
        this.evIncidentBean=new EVIncidentBean();
        evIncidentBean.setId("S0236263");
        evIncidentBean.setTitle("Incident Title");
        evIncidentBean.setDescription("Incident Description");
        evIncidentBean.setRequestor_email("requestor@admin.com");
        evIncidentBean.setRecipient_email("recipient@admin.com");
        evIncidentBean.setCi("REXJIR-APP12PPR");
        evIncidentBean.setUrgency("1");
        evIncidentBean.setAssigned_group("Group Name");
        evIncidentBean.setMax_resolution_date("2021-06-01 18:30:23");
        evIncidentBean.setOrigin("12");
        evIncidentBean.setImpact_id("3");
    }

    @Given("the incident priority in EasyVista is {string}")
    public void the_incident_priority_in_EasyVista_is(String string) {
        evIncidentBean.setImpact_id(string);
    }

    @Given("the incident summary in EasyVista is {string}")
    public void the_incident_summary_in_EasyVista_is(String string) {
        evIncidentBean.setTitle(string);
    }

    @When("the incident is sent to Jira by (.*) authenticated by (.*)")
    public void theIncidentIsSentToJiraByUserAuthenticatedByPassword(String username,String password) {
        HttpClient httpClientDetails = HttpClientBuilder.create().build();
        HttpPost httpPostDetails = new HttpPost(this.url+"/rest/easyvista/1.0/issue");
        httpPostDetails.setHeader("Authorization", "Basic "+new String(Base64.encodeBase64((username + ":" + password).getBytes())));
        httpPostDetails.setHeader("Content-Type", "application/json");

        JSONObject data = new JSONObject();
        JSONObject fields=new JSONObject();
        fields.put("id",evIncidentBean.getId());
        fields.put("title",evIncidentBean.getTitle());
        fields.put("subject",evIncidentBean.getSubject());
        fields.put("description",evIncidentBean.getDescription());
        fields.put("requestor_email",evIncidentBean.getRequestor_email());
        fields.put("recipient_email",evIncidentBean.getRecipient_email());
        fields.put("ci",evIncidentBean.getCi());
        fields.put("impact_id",evIncidentBean.getImpact_id());
        fields.put("urgency",evIncidentBean.getUrgency());
        fields.put("assigned_group",evIncidentBean.getAssigned_group());
        fields.put("max_resolution_date",evIncidentBean.getMax_resolution_date());
        fields.put("String origin",evIncidentBean.getOrigin());
        data.put("fields",fields);

        HttpEntity httpEntity = null;
        try {
            httpEntity = new ByteArrayEntity(data.toString().getBytes("UTF-8"));
            httpPostDetails.setEntity(httpEntity);
            HttpResponse response = httpClientDetails.execute(httpPostDetails);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201){
                String responseString=EntityUtils.toString(response.getEntity());
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(responseString);
                issueKey= (String) json.get("key");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @When("(.*) is connected to Jira with password (.*)")
    public void admin_is_connected_to_Jira_with_password_admin(String username,String password) {
        // Write code here that turns the phrase above into concrete actions
        this.username=username;
        this.password=password;
    }

    @Then("the incident is created in Jira")
    public void the_incident_is_created_in_Jira() {
        HttpClient httpClientDetails = HttpClientBuilder.create().build();
        HttpGet httpGetDetails = new HttpGet(this.url+"/rest/api/2/issue/"+this.issueKey);
        httpGetDetails.setHeader("Authorization", "Basic "+new String(Base64.encodeBase64((this.username + ":" + this.password).getBytes())));
        HttpResponse response = null;
        try {
            response = httpClientDetails.execute(httpGetDetails);
            statusCode = response.getStatusLine().getStatusCode();
            assertEquals(200,statusCode);
            String responseString=EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            this.issueDetails = (JSONObject) parser.parse(responseString);
            issueKey= (String) this.issueDetails.get("key");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Then("the incident priority in Jira is (.*)")
    public void the_incident_priority_in_Jira_is(String string) {
        assertEquals(string, ((JSONObject)(((JSONObject) this.issueDetails.get("fields")).get("customfield_10700"))).get("value"));
    }

    @Then("the incident summary in Jira is (.*)")
    public void the_incident_summary_in_Jira_is(String string) {
        assertEquals(string, ((JSONObject) this.issueDetails.get("fields")).get("summary"));
    }

    @Given("the incident CI in EasyVista is (.*)")
    public void the_incident_CI_in_EasyVista_is(String string) {
        evIncidentBean.setCi(string);
    }

    @Then("the incident CI parent in Jira is (.*)")
    public void the_incident_CI_parent_in_Jira_is(String string) {
        // Write code here that turns the phrase above into concrete actions
        String parentValue=(String)((JSONObject)(((JSONObject) this.issueDetails.get("fields")).get("customfield_10501"))).get("value");
        assertEquals(string,parentValue);
    }

    @Then("the incident CI child in Jira is (.*)")
    public void the_incident_CI_child_in_Jira_is(String string) {
        String childValue=(String)((JSONObject)(((JSONObject)(((JSONObject) this.issueDetails.get("fields")).get("customfield_10501"))).get("child"))).get("value");
        assertEquals(string,childValue);
    }

}
