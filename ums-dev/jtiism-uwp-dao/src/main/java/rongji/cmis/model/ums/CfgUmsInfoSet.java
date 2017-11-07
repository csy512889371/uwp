package rongji.cmis.model.ums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class CfgUmsInfoSet implements Serializable {


	private String entinf000;

	public String getEntinf000() {
		return entinf000;
	}

	public void setEntinf000(String entinf000) {
		this.entinf000 = entinf000;
	}
}