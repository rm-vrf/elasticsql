package cn.batchfile.elasticsql.exceptions;

public class ExecuteException extends RuntimeException {

	private static final long serialVersionUID = 1176495571370636984L;
	
	private int code;
	private String sqlState;
	
	public int getCode() {
		return code;
	}

	public String getSqlState() {
		return sqlState;
	}

	public ExecuteException(int code, String sqlState) {
		super();
		this.code = code;
		this.sqlState = sqlState;
	}
	
	public ExecuteException(int code, String sqlState, String message) {
		super(message);
		this.code = code;
		this.sqlState = sqlState;
	}

	public ExecuteException(int code, String sqlState, String message, Throwable t) {
		super(message, t);
		this.code = code;
		this.sqlState = sqlState;
	}
}
