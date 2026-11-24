package org.example.javastreams.dto;


public class Project {

    private String name;
    private String projCode;
    private String client;
    private String buLeadNam;

    public Project() {
    }

    public Project(String name, String projCode, String client , String buLeadNam){
        this.buLeadNam = buLeadNam;
        this.client = client;
        this.name = name;
        this.projCode = projCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjCode() {
        return projCode;
    }

    public void setProjCode(String projCode) {
        this.projCode = projCode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getBuLeadNam() {
        return buLeadNam;
    }

    public void setBuLeadNam(String buLeadNam) {
        this.buLeadNam = buLeadNam;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", projCode='" + projCode + '\'' +
                ", client='" + client + '\'' +
                ", buLeadNam='" + buLeadNam + '\'' +
                '}';
    }
}
