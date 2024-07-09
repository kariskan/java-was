package codesquad.utils;

import codesquad.domain.HttpResponse;
import java.io.IOException;
import java.io.OutputStream;

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
