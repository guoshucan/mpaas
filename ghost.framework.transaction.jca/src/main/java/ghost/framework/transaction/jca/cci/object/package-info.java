/**
 * The classes in this package represent EIS operations as threadsafe,
 * reusable objects. This higher level of CCI abstraction depends on the
 * lower-level abstraction in the {@code ghost.framework.jca.cci.core} package.
 * Exceptions thrown are as in the {@code ghost.framework.dao} package,
 * meaning that code using this package does not need to worry about error handling.
 */
@NoScanPackage
package ghost.framework.transaction.jca.cci.object;

import ghost.framework.beans.annotation.scan.NoScanPackage;