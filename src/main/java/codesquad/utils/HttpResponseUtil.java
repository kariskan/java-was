package codesquad.utils;

import codesquad.domain.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseUtil {

	private HttpResponseUtil() {
	}

	public static void writeResponse(OutputStream bo, HttpResponse response) throws IOException {
		if (response.getStatusLine() == null) {
			response.setStatusLine();
		}
		bo.write(response.getStatusLine().toString().getBytes());
		if (response.getBody() != null) {
			response.addHeader("Content-Length", String.valueOf(response.getBody().length));
		}
		bo.write(response.getHeader().toString().getBytes());
		if (response.getBody() != null) {
			bo.write(response.getBody());
		}
		bo.flush();
	}
}
