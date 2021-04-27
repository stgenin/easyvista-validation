package beans;

public class EVIncidentBean {

    private String id;
    private String title;
    private String subject;
    private String description;
    private String requestor_email;
    private String recipient_email;
    private String ci;
    private String impact_id;
    private String urgency;
    private String assigned_group;
    private String max_resolution_date;
    private String origin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestor_email() {
        return requestor_email;
    }

    public void setRequestor_email(String requestor_email) {
        this.requestor_email = requestor_email;
    }

    public String getRecipient_email() {
        return recipient_email;
    }

    public void setRecipient_email(String recipient_email) {
        this.recipient_email = recipient_email;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getImpact_id() {
        return impact_id;
    }

    public void setImpact_id(String impact_id) {
        this.impact_id = impact_id;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAssigned_group() {
        return assigned_group;
    }

    public void setAssigned_group(String assigned_group) {
        this.assigned_group = assigned_group;
    }

    public String getMax_resolution_date() {
        return max_resolution_date;
    }

    public void setMax_resolution_date(String max_resolution_date) {
        this.max_resolution_date = max_resolution_date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
