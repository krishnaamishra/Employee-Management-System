package org.example.employee;

public class Leave {

    private int eid;
    private int lid;
    private String status;
    private String leaveReason;

    private String from;

    private String to;

    private String ltype;

    public Leave(int eid,int lid,String status,String leaveReason,String from,String to,String ltype) {
        this.eid=eid;
        this.lid=lid;
        this.status=status;
        this.leaveReason=leaveReason;
        this.from=from;
        this.to=to;
        this.ltype=ltype;

    }
    // Getters and Setters
    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLtype() {
        return ltype;
    }

    public void setLtype(String ltype) {
        this.ltype = ltype;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "eid=" + eid +
                ", lid=" + lid +
                ", status='" + status + '\'' +
                ", leaveReason='" + leaveReason + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", ltype='" + ltype + '\'' +
                '}';
    }
}

