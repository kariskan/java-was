package codesquad.handler;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;

public interface Handler {

	HttpResponse doService(HttpRequest request);
}
