package org.javaboy.vhr.model;

import java.io.Serializable;

public class EmployeeSalary implements Serializable {

    private static final long serialVersionUID = 6744951306898361300L;

    private Integer id;

    private Integer eid;

    private Integer sid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }
}