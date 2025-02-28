package org.morkato.http;

public class HTTPRoutes {
  /* Routes in API refers of art */
  public static final Route ALL_ARTS = new Route("/arts/{0}");
  public static final Route UNIQUE_ART = new Route("/arts/{0}/{1}");
}
