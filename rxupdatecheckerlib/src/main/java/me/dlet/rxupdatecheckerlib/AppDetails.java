package me.dlet.rxupdatecheckerlib;

/**
 * Created by darwinlouistoledo on 6/28/16.
 */
class AppDetails{
    private String datePublished;
    private String fileSize;
    private String numDownloads;
    private String softwareVersion;
    private String operatingSystems;

    public String getDatePublished() {
        return datePublished;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getNumDownloads() {
        return numDownloads;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getOperatingSystems() {
        return operatingSystems;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setNumDownloads(String numDownloads) {
        this.numDownloads = numDownloads;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public void setOperatingSystems(String operatingSystems) {
        this.operatingSystems = operatingSystems;
    }

    @Override
    public String toString() {
        return "AppDetails{" +
                "datePublished='" + datePublished + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", numDownloads='" + numDownloads + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", operatingSystems='" + operatingSystems + '\'' +
                '}';
    }
}
