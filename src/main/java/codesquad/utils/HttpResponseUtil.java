package codesquad.utils;

import java.io.IOException;
import java.io.OutputStream;

import codesquad.domain.HttpResponse;

public class HttpResponseUtil {

	private HttpResponseUtil() {
	}

	public static void writeResponse(OutputStream bo, HttpResponse httpResponse) throws IOException {
		bo.write(httpResponse.getBytes());
		bo.flush();
	}
}
