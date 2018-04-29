/**
 * 
 */
package fr.n7.stl.util;

/**
 * @author Marc Pantel
 *
 */
public class BlockSemanticsError extends RuntimeException {

	private static final long serialVersionUID = 2853370029801143645L;

	/**
	 * 
	 */
	public BlockSemanticsError() {

	}

	/**
	 * @param message
	 */
	public BlockSemanticsError(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public BlockSemanticsError(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public BlockSemanticsError(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BlockSemanticsError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
