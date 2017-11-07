package rongji.framework.base;

public class CommonEnum {
	/**
	 * 
	 * 公用枚举
	 *
	 **/

	public enum Status {

		USABLE(Short.valueOf("1"), "可用"), UNUSABLE(Short.valueOf("2"), "禁用");

		private Short code;
		private String name;

		Status(final Short status, final String name) {
			this.code = status;
			this.name = name;
		}

		public Short getCode() {
			return code;
		}

		public void setCode(Short status) {
			this.code = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 系统状态码
	 * 
	 * @author zhangzhiyi
	 * 
	 */
	public enum StatusCode {
		SUCCESS(200, "成功"), CHECK_ERROR(300, "校验错误"), LOGIC_ERROR(400, "逻辑错误"), ACL_REFUSE(401, "无权限,拒绝访问"), ERROR(500, "异常错误"), UNLOGIN(600, "未登录");
		private final Integer code;
		private final String name;

		StatusCode(final Integer code, final String name) {
			this.code = code;
			this.name = name;
		}

		public Integer getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * 数据查询范围
	 * 
	 * @author zhangzhiyi
	 *
	 */
	public enum DataRangeLevel {
		PERMISSION_DENIED(Short.valueOf("0"), "权限拒绝"), DEFAULT(Short.valueOf("1"), "按用户信息"), SELF_PERSON(Short.valueOf("2"), "本人"), SELF_DEPARTMENT(Short.valueOf("3"), "本部门"), CHARGE_DEPARTMENT(
				Short.valueOf("4"), "分管部门"), SELF_COURT(Short.valueOf("5"), "本院"), SELF_AREA(Short.valueOf("6"), "本地区"), SELF_PROVINCE(Short.valueOf("7"), "本省");

		private Short code;
		private String name;

		DataRangeLevel(final Short status, final String name) {
			this.code = status;
			this.name = name;
		}

		public Short getCode() {
			return code;
		}

		public void setCode(Short status) {
			this.code = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static final DataRangeLevel getInstance(short value) {
			DataRangeLevel[] levels = DataRangeLevel.values();
			for (DataRangeLevel level : levels) {
				if (level.getCode().shortValue() == value) {
					return level;
				}
			}
			return null;
		}
	}

	/**
	 * 用户等级
	 * 
	 * @author zhangzhiyi
	 *
	 */
	public enum UserLevel {
		NORMAL(Short.valueOf("1"), "普通用户"), DEPART_LEADER(Short.valueOf("2"), "部门领导"), CHARGE_LEADER(Short.valueOf("3"), "分管领导"), COURT_LEADER(Short.valueOf("4"), "院领导");
		private Short code;
		private String name;

		UserLevel(final Short status, final String name) {
			this.code = status;
			this.name = name;
		}

		public Short getCode() {
			return code;
		}

		public void setCode(Short status) {
			this.code = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
