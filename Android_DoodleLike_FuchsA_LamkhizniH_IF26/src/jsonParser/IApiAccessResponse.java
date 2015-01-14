package jsonParser;

import org.apache.http.HttpResponse;
//permet aux activites de recuperer les reponses des classes Async
public interface IApiAccessResponse {
  void postResult(HttpResponse asyncresult);
}