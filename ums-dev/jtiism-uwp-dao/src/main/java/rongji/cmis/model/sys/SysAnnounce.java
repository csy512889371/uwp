package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import rongji.framework.base.dao.utils.CustomDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2017/10/24.
 */
@Entity
@Table(name = "SHJT_SAN_INFO")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)

public class SysAnnounce implements Serializable{
    /**
     * ID
     */
    @GenericGenerator(name = "generator", strategy = "rongji.framework.base.dao.generater.UUIDKeyGen")
    @Id
    @GeneratedValue(generator = "generator")
    @JsonProperty
    @Column(name = "SANINF000")
    private String saninf000;

    /**
     * 内容
     */
    @JsonProperty
    @Column(name = "SANINF001")
    private String saninf001;

    /**
     * 状态（1:启用；0：未启用）
     */
    @JsonProperty
    @Column(name = "SANINF002")
    private String saninf002;

    /**
     * 创建时间
     */
    @JsonProperty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "SANINF003")
    private Date saninf003;

    /**
     * 修改时间
     */
    @JsonProperty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "SANINF004")
    private Date saninf004;

    /**
     * 创建人ID
     */
    @JsonProperty
    @Column(name = "SANINF005")
    private String saninf005;

    /**
     * 创建人姓名
     */
    @JsonProperty
    @Column(name = "SANINF006")
    private String saninf006;

    public String getSaninf000() {
        return saninf000;
    }

    public void setSaninf000(String saninf000) {
        this.saninf000 = saninf000;
    }

    public String getSaninf001() {
        return saninf001;
    }

    public void setSaninf001(String saninf001) {
        this.saninf001 = saninf001;
    }

    public String getSaninf002() {
        return saninf002;
    }

    public void setSaninf002(String saninf002) {
        this.saninf002 = saninf002;
    }

    public Date getSaninf003() {
        return saninf003;
    }

    public void setSaninf003(Date saninf003) {
        this.saninf003 = saninf003;
    }

    public Date getSaninf004() {
        return saninf004;
    }

    public void setSaninf004(Date saninf004) {
        this.saninf004 = saninf004;
    }

    public String getSaninf005() {
        return saninf005;
    }

    public void setSaninf005(String saninf005) {
        this.saninf005 = saninf005;
    }

    public String getSaninf006() {
        return saninf006;
    }

    public void setSaninf006(String saninf006) {
        this.saninf006 = saninf006;
    }
}
