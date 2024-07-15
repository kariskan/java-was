package codesquad.utils;

import java.io.IOException;
import java.io.OutputStream;

import codesquad.domain.HttpResponse;

public class HttpResponseUtil {

	private HttpResponseUtil() {
	}

	public static void writeResponse(OutputStream bo, HttpResponse response) throws IOException {
		bo.write(response.getStatusLine().toString().getBytes());
		bo.write(response.getHeader().toString().getBytes());
		if (response.getBody() != null) {
			bo.write(response.getBody());
		}
		bo.flush();
	}
}
