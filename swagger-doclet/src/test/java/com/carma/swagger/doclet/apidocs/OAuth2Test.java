package com.carma.swagger.doclet.apidocs;

import static com.carma.swagger.doclet.apidocs.FixtureLoader.loadFixture;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.carma.swagger.doclet.DocletOptions;
import com.carma.swagger.doclet.Recorder;
import com.carma.swagger.doclet.model.ApiAuthorizations;
import com.carma.swagger.doclet.model.ApiDeclaration;
import com.carma.swagger.doclet.model.ResourceListing;
import com.carma.swagger.doclet.parser.JaxRsAnnotationParser;
import com.sun.javadoc.RootDoc;

@SuppressWarnings("javadoc")
public class OAuth2Test {

	private Recorder recorderMock;
	private DocletOptions options;

	@Before
	public void setup() throws IOException {
		this.recorderMock = mock(Recorder.class);
		this.options = new DocletOptions().setRecorder(this.recorderMock).setIncludeSwaggerUi(false);

		final ApiAuthorizations apiAuthorizations = loadFixture("/fixtures/oauth2/apiauth.json", ApiAuthorizations.class);
		this.options.setApiAuthorizations(apiAuthorizations);
		this.options.getAuthOperationScopes().add("read:pets");
	}

	@Test
	public void testStart() throws IOException {
		final RootDoc rootDoc = RootDocLoader.fromPath("src/test/resources", "fixtures.oauth2");
		new JaxRsAnnotationParser(this.options, rootDoc).run();

		final ResourceListing expectedListing = loadFixture("/fixtures/oauth2/service.json", ResourceListing.class);
		verify(this.recorderMock).record(any(File.class), eq(expectedListing));

		final ApiDeclaration api = loadFixture("/fixtures/oauth2/oauth2.json", ApiDeclaration.class);
		verify(this.recorderMock).record(any(File.class), eq(api));
	}

}
