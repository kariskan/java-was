package codesquad.handler;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;

public abstract class DynamicHandler implements Handler {

	@Override
	public void doService(HttpRequest request, HttpResponse response) {
		if (request.isGet()) {
			doGet(request, response);
		}
		if (request.isPost()) {
			doPost(request, response);
		}
	}

	public void doPost(HttpRequest request, HttpResponse response) {
	}

	public void doGet(HttpRequest request, HttpResponse response) {
	}
}
