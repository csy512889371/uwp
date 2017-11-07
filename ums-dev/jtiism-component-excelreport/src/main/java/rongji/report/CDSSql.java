package rongji.report;

public interface CDSSql {
	CDSBaseImpl with(String withStr);
	
	CDSBaseImpl select(String selectStr);

	CDSBaseImpl from(String fromStr);

	CDSBaseImpl join(String joinStr);

	CDSBaseImpl whereAnd(String whereAndStr);
}