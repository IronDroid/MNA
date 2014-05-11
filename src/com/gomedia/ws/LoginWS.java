package com.gomedia.ws;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginWS {

    //private static final String URL = "http://192.168.2.101:8081/AudioGuiasWS/videoguia?WSDL";
    private static final String URL = "http://200.87.225.157:1111/AudioGuiasWS/videoguia?WSDL";
    private static final String NAMESPACE = "http://ws.videoguia.mna/";
    private static final String METHOD_LOGIN = "login";
    private static final String SOAP_ACTION_LOGIN = "http://ws.videoguia.mna/videoguia/loginRequest";
    private ObjRespuesta resp;
    private SoapObject request = null;
    private SoapSerializationEnvelope envelope = null;
    private SoapObject resultsRequestSOAP = null;

    public ObjRespuesta logIn(String user, String pass) throws Exception {
        resp = new ObjRespuesta();
        request = new SoapObject(NAMESPACE, METHOD_LOGIN);
        request.addProperty("usuario", user);
        request.addProperty("clave", pass);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);
        transporte.call(SOAP_ACTION_LOGIN, envelope);
        resultsRequestSOAP = (SoapObject) envelope.getResponse();
        resp.setCodigo(Integer.parseInt(resultsRequestSOAP.getProperty("codigo").toString()));
        resp.setMensaje(resultsRequestSOAP.getProperty("mensaje").toString());

        return resp;
    }
}
