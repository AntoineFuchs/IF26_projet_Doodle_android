package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import org.apache.http.HttpResponse;
//permet aux activites de recuperer les reponses des classes Async
public interface IApiAccessResponse {
  void postResult(HttpResponse asyncresult);
}