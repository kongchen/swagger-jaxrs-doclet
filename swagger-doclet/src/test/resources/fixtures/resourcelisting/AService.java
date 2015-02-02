package fixtures.resourcelisting;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @resource a1
 * @resourcePriority 2
 * @resourceDescription A
 */
@Path("/a")
public class AService {

	@SuppressWarnings("javadoc")
	@GET
	public void getX() {
		// noop
	}

}
