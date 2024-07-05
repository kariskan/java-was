package codesquad.handler;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;

public interface Handler {

	void doService(HttpRequest request, HttpResponse response);
}
