package fixtures.exclusionresponsetype;

import java.io.Serializable;

@SuppressWarnings("javadoc")
public class ParentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
